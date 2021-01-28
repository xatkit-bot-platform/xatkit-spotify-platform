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
public class GetPlaylists extends RuntimeAction < SpotifyPlatform > {
    /**
     * Constructs a new {@link GetGif} with the provided {@code platform}, {@code context}.
     * <p>
     * This constructor requires a valid Spotify API token in order to build the REST query used to retrieve GIF urls.
     *
     * @param platform     the {@link SpotifyPlatform} containing this action
     * @param context      the {@link StateContext} associated to this action
     * @throws NullPointerException if the provided {@code runtimePlatform}, {@code session}
     *                              is {@code null}
     * @see SpotifyPlatform#getSpotifyToken()
     */
    public GetPlaylists(@NonNull SpotifyPlatform platform, @NonNull StateContext context) {
        super(platform, context);
    }

    @Override
    protected Object compute() throws Exception {
        try {
            String token = this.runtimePlatform.getSpotifyToken();

            HttpRequest request = Unirest.get("https://api.spotify.com/v1/me/playlists")
                .header("accept", "application/json")
                .header("content-Type", "application/json")
                .header("Authorization", "Bearer " + token);
            JsonNode jsonNode = request.asJson().getBody();

            String playlistNames = "";
            for (int i = 0; i < jsonNode.getObject().getJSONArray("items").length(); i++) {
                JSONObject obj = jsonNode.getObject().getJSONArray("items").getJSONObject(i);
                playlistNames = playlistNames + obj.getString("name") + ", ";
            }

            if (playlistNames != "") {
                playlistNames = playlistNames.substring(0, playlistNames.length() - 2);
            }

            return playlistNames;
        } catch (Exception e) {
            throw new XatkitException("Cannot get playlists, see attached exception", e);
        }
    }
}