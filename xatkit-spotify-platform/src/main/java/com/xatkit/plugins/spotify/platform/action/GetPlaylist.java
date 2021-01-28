package com.xatkit.plugins.spotify.platform.action;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.spotify.platform.SpotifyPlatform;
import lombok.NonNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;


/**
 * A {@link RuntimeAction} that retrieves a GIF from a given search string.
 * <p>
 * This class relies on the {@link SpotifyPlatform} to access the Spotify API using the token stored in the
 * {@link org.apache.commons.configuration2.Configuration}.
 */
public class GetPlaylist extends RuntimeAction < SpotifyPlatform > {
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
     * @param searchString the {@link String} used to search a specific playlist
     * @throws NullPointerException if the provided {@code runtimePlatform}, {@code session}, or {@code searchString}
     *                              is {@code null}
     * @see SpotifyPlatform#getSpotifyToken()
     */
    public GetPlaylist(@NonNull SpotifyPlatform platform, @NonNull StateContext context, @NonNull String searchString) {
        super(platform, context);
        this.searchString = searchString;
    }

    private static String getPlaylistID(StateContext context, SpotifyPlatform platform, String plName) {
        Object playlist = context.getSession().get("playlist-id");
        String playlist_id = "";

        if (plName != "") {
            String[] playlists = platform.getPlaylistsNameIDs(context, "").split(";");
            for (int i = 0; i < playlists.length; i++) {
                String[] plObj = playlists[i].split(":");
                if (plObj != null && plObj[0] != null && plObj[0].contains(plName)) {
                    playlist_id = plObj[1];
                }
            }
        } else if (playlist != null) {
            playlist_id = playlist.toString();
        } else {
            String[] devices = platform.getPlaylistsNameIDs(context, "").split(";");
            playlist_id = (devices[0].split(":"))[1];
        }

        return playlist_id;
    }
    @Override
    protected Object compute() throws Exception {

        String pl_id = getPlaylistID(context, this.runtimePlatform, this.searchString);

        return pl_id;

    }
}