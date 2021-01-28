package com.xatkit.plugins.spotify.platform;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.platform.RuntimePlatform;
import com.xatkit.core.platform.action.RuntimeActionResult;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.spotify.platform.action.GetPlaylists;
import com.xatkit.plugins.spotify.platform.action.PlaylistData;
import com.xatkit.plugins.spotify.platform.action.CreatePlaylist;
import com.xatkit.plugins.spotify.platform.action.GetPlaylist;
import com.xatkit.plugins.spotify.platform.action.GetPlaylistNameIDs;
import com.xatkit.plugins.spotify.platform.action.SearchTrack;
import com.xatkit.plugins.spotify.platform.action.SearchArtist;
import com.xatkit.plugins.spotify.platform.action.AddTrack;
import com.xatkit.plugins.spotify.platform.action.GetDevicesNames;
import com.xatkit.plugins.spotify.platform.action.GetDevicesNameIDs;
import com.xatkit.plugins.spotify.platform.action.PlayMusic;
import com.xatkit.plugins.spotify.platform.action.PlayerActions;
import lombok.NonNull;
import org.apache.commons.configuration2.Configuration;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.xatkit.plugins.spotify.platform.action.PlaylistData;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;

import java.util.List;

/**
 * A {@link RuntimePlatform} class that connects and interacts with the Spotify API.
 */
public class SpotifyPlatform extends RuntimePlatform {

    /**
     * The {@link Configuration} key to store the Spotify API token.
     */
    public static String SPOTIFY_TOKEN = "xatkit.spotify.token";
    public static String SPOTIFY_REFRESH_TOKEN = "xatkit.spotify.refresh_token";
    public static String SPOTIFY_USERNAME = "xatkit.spotify.username";

    /**
     * The Spotify API token used to initialize this class.
     */
    private String spotifyToken;

    private String spotifyRefreshToken;

    private String spotifyUsername;

    /**
     * {@inheritDoc}
     * <p>
     * This method checks that the provided {@code configuration} contains a Spotify API token.
     *
     * @throws IllegalArgumentException if the provided {@code configuration} does not contain a Spotify API token
     */
    @Override
    public void start(XatkitBot xatkitBot, Configuration configuration) {
        super.start(xatkitBot, configuration);
        checkArgument(configuration.containsKey(SPOTIFY_TOKEN), "Cannot construct a %s, please ensure that the " +
            "configuration contains a valid Spotify API token (configuration key: %s)",
            this.getClass().getSimpleName(), SPOTIFY_TOKEN);
        spotifyToken = configuration.getString(SPOTIFY_TOKEN);
        spotifyRefreshToken = configuration.getString(SPOTIFY_REFRESH_TOKEN);
        spotifyUsername = configuration.getString(SPOTIFY_USERNAME);
    }

    /**
     * Returns a list of playlists associated to the Spotify account.
     *
     * @param context      the current {@link StateContext}
     * @return List of playlist's names separated by comma
     */
    public @NonNull String getPlaylists(@NonNull StateContext context) {
        GetPlaylists action = new GetPlaylists(this, context);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    /**
     * Creates a new playlist to the associated Spotify account with name {@code searchString}.
     *
     * @param context      the current {@link StateContext}
     * @param searchString the query to send to the Spotify API
     * @return Result of the operation, wether it was successful or not
     */
    public @NonNull String createPlaylist(@NonNull StateContext context, @NonNull String searchString) {
        CreatePlaylist action = new CreatePlaylist(this, context, searchString);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    /**
     * Returns the URL of the track with name (or name and artist) {@code searchString}.
     *
     * @param context      the current {@link StateContext}
     * @param searchString the query to send to the Spotify API
     * @return the URL of the track
     */
    public @NonNull String searchTrack(@NonNull StateContext context, @NonNull String searchString) {
        SearchTrack action = new SearchTrack(this, context, searchString);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    public @NonNull String searchArtist(@NonNull StateContext context, @NonNull String searchString) {
        SearchArtist action = new SearchArtist(this, context, searchString);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    public @NonNull String addTrack(@NonNull StateContext context, @NonNull String searchString) {
        AddTrack action = new AddTrack(this, context, searchString);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    public @NonNull String getDevicesNames(@NonNull StateContext context, @NonNull String searchString) {
        GetDevicesNames action = new GetDevicesNames(this, context, searchString);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    public @NonNull String getDevicesNameIDs(@NonNull StateContext context) {
        GetDevicesNameIDs action = new GetDevicesNameIDs(this, context);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    public @NonNull String playMusic(@NonNull StateContext context, @NonNull String searchString) {
        PlayMusic action = new PlayMusic(this, context, searchString);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    public @NonNull String getPlaylist(@NonNull StateContext context, @NonNull String searchString) {
        GetPlaylist action = new GetPlaylist(this, context, searchString);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    public @NonNull String playerActions(@NonNull StateContext context, @NonNull String playerAction) {
        PlayerActions action = new PlayerActions(this, context, playerAction);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    public @NonNull String getPlaylistsNameIDs(@NonNull StateContext context, @NonNull String searchString) {
        GetPlaylistNameIDs action = new GetPlaylistNameIDs(this, context, searchString);
        RuntimeActionResult result = action.call();
        return (String) result.getResult();
    }

    /**
     * Returns the Spotify API token.
     *
     * @return the Spotify API token
     */
    public String getSpotifyToken() {
        try {
            return Unirest.post("https://accounts.spotify.com/api/token")
                .header("Authorization", "Basic " + this.spotifyToken)
                .field("grant_type", "refresh_token")
                .field("refresh_token", this.spotifyRefreshToken)
                .asJson()
                .getBody()
                .getObject()
                .getString("access_token");
        } catch (UnirestException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public String getSpotifyUsername() {
        return this.spotifyUsername;
    }
}