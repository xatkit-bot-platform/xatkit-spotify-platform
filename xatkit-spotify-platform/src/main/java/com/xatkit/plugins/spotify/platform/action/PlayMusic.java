package com.xatkit.plugins.spotify.platform.action;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.spotify.platform.SpotifyPlatform;
import com.xatkit.core.XatkitException;
import lombok.NonNull;
import org.json.JSONObject;




/**
 * A {@link RuntimeAction} that retrieves a GIF from a given search string.
 * <p>
 * This class relies on the {@link SpotifyPlatform} to access the Spotify API using the token stored in the
 * {@link org.apache.commons.configuration2.Configuration}.
 */
public class PlayMusic extends RuntimeAction < SpotifyPlatform > {
    /**
     * The {@link String} used to search.
     */
    private String searchString;
    private SpotifyPlatform platform;

    /**
     * Constructs a new {@link GetGif} with the provided {@code platform}, {@code context}, and {@code searchString}.
     * <p>
     * This constructor requires a valid Spotify API token in order to build the REST query used to retrieve GIF urls.
     *
     * @param platform     the {@link SpotifyPlatform} containing this action
     * @param context      the {@link StateContext} associated to this action
     * @param searchString the {@link String} used to specify device
     * @throws NullPointerException if the provided {@code runtimePlatform}, {@code session}, or {@code searchString}
     *                              is {@code null}
     * @see SpotifyPlatform#getSpotifyToken()
     */
    public PlayMusic(@NonNull SpotifyPlatform platform, @NonNull StateContext context, @NonNull String searchString) {
        super(platform, context);
        this.searchString = searchString;
        this.platform = platform;
    }

    private static String getDeviceID(StateContext context, SpotifyPlatform platform, String deviceName) {
        Object device = context.getSession().get("device-id");
        String device_id = "";

        if (deviceName != "") {
            String[] devices = platform.getDevicesNameIDs(context).split(";");
            for (int i = 0; i < devices.length; i++) {
                String[] deviceObj = devices[i].split(":");
                if (deviceObj[0].contains(deviceName)) {
                    device_id = deviceObj[1];
                }
            }
        } else if (device != null) {
            device_id = device.toString();
        } else {
            String[] devices = platform.getDevicesNameIDs(context).split(";");
            device_id = (devices[0].split(":"))[1];
        }

        return device_id;
    }

    private static String getPlaylistUri(StateContext context) {
        Object playlist = context.getSession().get("playlist-uri");
        String playlist_param = "";

        if (playlist != null) {
            playlist_param = playlist.toString();
        }

        return playlist_param;
    }

    @Override
    protected Object compute() throws Exception {
        try {
            String device_id = getDeviceID(context, this.runtimePlatform, this.searchString);
            String token = this.runtimePlatform.getSpotifyToken();

            JSONObject obj = null;
            String playlist_uri = getPlaylistUri(context);
            if (playlist_uri != "") {
                obj = new JSONObject();
                obj.put("context_uri", playlist_uri);
            }

            if (device_id != "") {
                HttpRequestWithBody request = Unirest.put("https://api.spotify.com/v1/me/player/play?device_id=" + device_id)
                    .header("accept", "application/json")
                    .header("content-Type", "application/json")
                    .header("Authorization", "Bearer " + token);

                if (obj != null) {
                    request.body(obj).asJson();
                } else {
                    request.asJson();
                }
            }

            return device_id;
        } catch (Exception e) {
            throw new XatkitException("Cannot play music, see attached exception", e);
        }
    }
}