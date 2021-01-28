package com.xatkit.plugins.spotify.platform.action;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
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
public class GetDevicesNames extends RuntimeAction < SpotifyPlatform > {
    /**
     * The {@link String} used to search.
     */
    private String searchString;

    /**
     * Constructs a new {@link GetGif} with the provided {@code platform}, {@code context}, and {@code searchString}.
     * <p>
     * This constructor requires a valid Spotify API token in order to build the REST query used to retrieve GIF urls.
     *
     * @param platform     the {@link SpotifyPlatform} containing this action
     * @param context      the {@link StateContext} associated to this action
     * @param searchString the {@link String} used to search available devices names
     * @throws NullPointerException if the provided {@code runtimePlatform}, {@code session}, or {@code searchString}
     *                              is {@code null}
     * @see SpotifyPlatform#getSpotifyToken()
     */
    public GetDevicesNames(@NonNull SpotifyPlatform platform, @NonNull StateContext context, @NonNull String searchString) {
        super(platform, context);
        this.searchString = searchString;
    }

    @Override
    protected Object compute() throws Exception {
        try {
            String token = this.runtimePlatform.getSpotifyToken();

            HttpRequest request = Unirest.get("https://api.spotify.com/v1/me/player/devices")
                .header("accept", "application/json")
                .header("content-Type", "application/json")
                .header("Authorization", "Bearer " + token);
            JsonNode jsonNode = request.asJson().getBody();

            String deviceNames = "";
            for (int i = 0; i < jsonNode.getObject().getJSONArray("devices").length(); i++) {
                JSONObject obj = jsonNode.getObject().getJSONArray("devices").getJSONObject(i);
                deviceNames = deviceNames + obj.getString("name") + ", ";
            }

            if (deviceNames != "") {
                deviceNames = deviceNames.substring(0, deviceNames.length() - 2);
                return deviceNames;
            }

            return deviceNames;
        } catch (Exception e) {
            throw new XatkitException("Cannot get device's names, see attached exception", e);
        }
    }
}