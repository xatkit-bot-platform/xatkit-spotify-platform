package com.xatkit.plugins.spotify.platform.action;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
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
public class CreatePlaylist extends RuntimeAction < SpotifyPlatform > {
    /**
     * The {@link String} used to search.
     */
    private String searchString;

    /**
     * Constructs a new {@link CreatePlaylist} with the provided {@code platform}, {@code context}, and {@code searchString}.
     * <p>
     * This constructor requires a valid Spotify API token in order to build the REST query used to retrieve GIF urls.
     *
     * @param platform     the {@link SpotifyPlatform} containing this action
     * @param context      the {@link StateContext} associated to this action
     * @param searchString the {@link String} used to create a Spotify playlist
     * @throws NullPointerException if the provided {@code runtimePlatform}, {@code session}, or {@code searchString}
     *                              is {@code null}
     * @see SpotifyPlatform#getSpotifyToken()
     */
    public CreatePlaylist(@NonNull SpotifyPlatform platform, @NonNull StateContext context, @NonNull String searchString) {
        super(platform, context);
        this.searchString = searchString;
    }

    @Override
    protected Object compute() throws Exception {
        try {
            String token = this.runtimePlatform.getSpotifyToken();
            String endpoint = "https://api.spotify.com/v1/users/" + this.runtimePlatform.getSpotifyUsername() + "/playlists";

            JSONObject obj = new JSONObject();
            obj.put("name", this.searchString);
            obj.put("public", false);

            JSONObject responseObj = Unirest.post(endpoint)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(obj)
                .asJson()
                .getBody()
                .getObject();

            String id = responseObj.getString("id");
            String uri = responseObj.getString("uri");
            return id + ";" + uri;
        } catch (Exception e) {
            throw new XatkitException("Cannot create playlist, see attached exception", e);
        }
    }
}