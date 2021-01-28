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
public class PlayerActions extends RuntimeAction < SpotifyPlatform > {
    /**
     * The {@link String} used to search.
     */
    private String playerAction;
    private SpotifyPlatform platform;

    /**
     * Constructs a new {@link GetGif} with the provided {@code platform}, {@code context}, and {@code playerAction}.
     * <p>
     * This constructor requires a valid Spotify API token in order to build the REST query used to retrieve GIF urls.
     *
     * @param platform     the {@link SpotifyPlatform} containing this playerAction
     * @param context      the {@link StateContext} associated to this playerAction
     * @param playerAction the {@link String} used to specify action
     * @throws NullPointerException if the provided {@code runtimePlatform}, {@code session}, or {@code playerAction}
     *                              is {@code null}
     * @see SpotifyPlatform#getSpotifyToken()
     */
    public PlayerActions(@NonNull SpotifyPlatform platform, @NonNull StateContext context, @NonNull String playerAction) {
        super(platform, context);
        this.playerAction = playerAction;
        this.platform = platform;
    }

    @Override
    protected Object compute() throws Exception {
        try {
            // String device_id = getDeviceID(context, this.runtimePlatform);
            String token = this.runtimePlatform.getSpotifyToken();

            HttpRequestWithBody request;
            if (playerAction == "pause") {
                request = Unirest.put("https://api.spotify.com/v1/me/player/" + playerAction);
            } else {
                request = Unirest.post("https://api.spotify.com/v1/me/player/" + playerAction);
            }

            request.header("accept", "application/json")
                .header("content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .asJson()
                .getBody();

            return "";
        } catch (Exception e) {
            throw new XatkitException("Cannot play music, see attached exception", e);
        }
    }
}