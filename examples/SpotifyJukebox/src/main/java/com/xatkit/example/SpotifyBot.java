package com.xatkit.example;

import com.xatkit.core.XatkitBot;
import com.xatkit.library.core.CoreLibrary;
import com.xatkit.plugins.spotify.platform.SpotifyPlatform;
import com.xatkit.plugins.spotify.platform.action.PlaylistData;
import com.xatkit.plugins.wikipedia.platform.WikipediaPlatform;
import com.xatkit.plugins.slack.platform.SlackPlatform;
import com.xatkit.plugins.slack.platform.io.SlackIntentProvider;
import lombok.val;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.json.JSONObject;

import static com.xatkit.dsl.DSL.any;
import static com.xatkit.dsl.DSL.eventIs;
import static com.xatkit.dsl.DSL.fallbackState;
import static com.xatkit.dsl.DSL.intent;
import static com.xatkit.dsl.DSL.intentIs;
import static com.xatkit.dsl.DSL.model;
import static com.xatkit.dsl.DSL.state;

import java.util.List;

public class SpotifyBot {

    private static String getPlaylistNames(List < PlaylistData > pl) {
        String playlistNames = "";
        for (PlaylistData p: pl) {
            playlistNames = playlistNames.substring(0, playlistNames.length() - 2) + ".";
            return playlistNames;
        }
        playlistNames = playlistNames.substring(0, playlistNames.length() - 2) + ".";
        return playlistNames;
    }


    /*
     * Your bot is a plain Java application: you need to define a main method to make the created jar executable.
     */
    public static void main(String[] args) {

        /*
         * Define the intents our bot will slack to.
         */
        val listPlaylists = intent("ListPlaylists")
            .trainingSentence("Can you list me my playlists?")
            .trainingSentence("List me my playlists")
            .trainingSentence("Wich are my playlists?");


        val search = intent("Search")
            .trainingSentence("Can you give me some info about this?")
            .trainingSentence("Do you know this?")
            .trainingSentence("Search this")
            .trainingSentence("What can you tell me about this?")
            .parameter("request")
            .fromFragment("this")
            .entity(any());

        val searchArtist = intent("SearchArtist")
            .trainingSentence("Search artist name")
            .trainingSentence("Can you give me some info about the artist name?")
            .trainingSentence("Who is the artist name?")
            .parameter("request")
            .fromFragment("name")
            .entity(any());

        val createPlaylist = intent("CreatePlaylist")
            .trainingSentence("Create playlist name")
            .trainingSentence("Can you create the playlist name")
            .trainingSentence("Please create the playlist name")
            .parameter("request")
            .fromFragment("name")
            .entity(any());

        val searchTrack = intent("SearchTrack")
            .trainingSentence("Search track name")
            .trainingSentence("Can you search the track name?")
            .trainingSentence("Can you list me the track name?")
            .parameter("request")
            .fromFragment("name")
            .entity(any());

        val addTrack = intent("AddTrack")
            .trainingSentence("Add it!")
            .trainingSentence("Add it please")
            .trainingSentence("Can you add it please?");

        val addTrackByName = intent("AddTrackByName")
            .trainingSentence("Add track trackName")
            .trainingSentence("Can you add trackName?")
            .trainingSentence("Add trackName")
            .parameter("request")
            .fromFragment("trackName")
            .entity(any());

        val listDevices = intent("ListDevices")
            .trainingSentence("List me my devices")
            .trainingSentence("List devices")
            .trainingSentence("Can you list me my devices please?");

        val playMusic = intent("PlayMusic")
            .trainingSentence("Turn on the music on device")
            .trainingSentence("Play music on device")
            .trainingSentence("Play music")
            .trainingSentence("Can you turn on the music?")
            .trainingSentence("Turn on the music")
            .trainingSentence("Play")
            .parameter("request")
            .fromFragment("device")
            .entity(any());

        val pauseMusic = intent("PauseMusic")
            .trainingSentence("Turn off")
            .trainingSentence("Turn off the music")
            .trainingSentence("Pause music")
            .trainingSentence("Pause")
            .trainingSentence("Can you pause the music?");

        val next = intent("Next")
            .trainingSentence("Next track")
            .trainingSentence("Following track")
            .trainingSentence("Next")
            .trainingSentence("Next song")
            .trainingSentence("Can you change to the following track?");

        val previous = intent("Previous")
            .trainingSentence("Previous track")
            .trainingSentence("Previous")
            .trainingSentence("Previous song")
            .trainingSentence("Can you change to the previous track?");

        val selectPlaylist = intent("SelectPlayList")
            .trainingSentence("Select playlistName")
            .trainingSentence("Select playlist playlistName")
            .trainingSentence("Can you select the playlist playlistName?")
            .trainingSentence("Can you select playlistName?")
            .trainingSentence("Can you show me the playlist playlistName?")
            .trainingSentence("Can you show me playlistName?")
            .parameter("request")
            .fromFragment("playlistName")
            .entity(any());


        /*
         * Instantiate the platforms we will use in the bot definition.
         */
        SlackPlatform slackPlatform = new SlackPlatform();
        SpotifyPlatform spotifyPlatform = new SpotifyPlatform();
        WikipediaPlatform wikipediaPlatform = new WikipediaPlatform();
        /*
         * Similarly, instantiate the intent/event providers we want to use.
         */
        SlackIntentProvider slackIntentProvider = new SlackIntentProvider(slackPlatform);

        /*
         * Create the states we want to use in our bot.
         */
        val awaitingInput = state("AwaitingInput");
        val handleGreetings = state("HandleGreetings");
        val handleListPlaylists = state("HandleListPlaylists");
        val handleSearch = state("HandleSearch");
        val handleCreatePlaylist = state("HandleCreatePlaylist");
        val handleSearchTrack = state("HandleSearchTrack");
        val handleSearchArtist = state("HandleSearchArtist");
        val handleAddTrack = state("HandleAddTrack");
        val handleAddTrackByName = state("HandleAddTrackByName");
        val handleHelp = state("HandleHelp");
        val handleListDevices = state("HandleListDevices");
        val handlePlayMusic = state("HandlePlayMusic");
        val handlePauseMusic = state("HandlePauseMusic");
        val handleNext = state("HandleNext");
        val handlePrevious = state("HandlePrevious");
        val handleSelectPlaylist = state("HandleSelectPlaylist");
        /*
         * Specify the content of the bot states (i.e. the behavior of the bot).
         */

        awaitingInput
            .next()
            /*
             * The Xatkit DSL offers dedicated predicates (intentIs(IntentDefinition) and eventIs
             * (EventDefinition) to check received intents/events.
             * <p>
             * You can also check a condition over the underlying bot state using the following syntax:
             * <pre>
             * {@code
             * .when(context -> [condition manipulating the context]).moveTo(state);
             * }
             * </pre>
             */
            .when(intentIs(CoreLibrary.Greetings)).moveTo(handleGreetings)
            .when(intentIs(listPlaylists)).moveTo(handleListPlaylists)
            .when(intentIs(CoreLibrary.Help)).moveTo(handleHelp)
            .when(intentIs(search)).moveTo(handleSearch)
            .when(intentIs(createPlaylist)).moveTo(handleCreatePlaylist)
            .when(intentIs(searchTrack)).moveTo(handleSearchTrack)
            .when(intentIs(searchArtist)).moveTo(handleSearchArtist)
            .when(intentIs(addTrack)).moveTo(handleAddTrack)
            .when(intentIs(addTrackByName)).moveTo(handleAddTrackByName)
            .when(intentIs(listDevices)).moveTo(handleListDevices)
            .when(intentIs(playMusic)).moveTo(handlePlayMusic)
            .when(intentIs(pauseMusic)).moveTo(handlePauseMusic)
            .when(intentIs(next)).moveTo(handleNext)
            .when(intentIs(previous)).moveTo(handlePrevious)
            .when(intentIs(selectPlaylist)).moveTo(handleSelectPlaylist);


        handleGreetings
            .body(context -> slackPlatform.reply(context, "Hi, I can do many things, challenge me!  \nYou can " +
                "start with something like `Can you <whatever you want>?`  \nThis bot is inspired by [this " +
                "article](https://uxdesign.cc/wanna-build-a-superbot-that-can-do-anything-heres-how-d8eeeeef1882)"))
            .next()
            .moveTo(awaitingInput);

        handleListPlaylists
            .body(context -> {
                String playlistsNames = spotifyPlatform.getPlaylists(context);
                if (playlistsNames == "") {
                    slackPlatform.reply(context, String.join("", "Sorry, no playlists available"));
                } else {
                    String[] resp = playlistsNames.split(",");
                    for (int i = 0; i < resp.length; i++) {
                        resp[i] = "\n• " + resp[i];
                    }
                    slackPlatform.reply(context, String.join("", resp));
                }
            })
            .next()
            .moveTo(awaitingInput);


        handleSearch
            .body(context -> {
                String wikipediaSearch = wikipediaPlatform.getInfo(context, (String) context.getIntent().getValue("request"));
                slackPlatform.reply(context, "Sure! Your you can find some info about it here: " + wikipediaSearch);
            })
            .next()
            .moveTo(awaitingInput);

        handleSearchArtist
            .body(context -> {
                String response = spotifyPlatform.searchArtist(context, (String) context.getIntent().getValue("request"));
                String[] respArr = response.split(";");
                slackPlatform.reply(context, respArr[0]);
                context.getSession().put("artist-name", respArr[1]);
            })
            .next()
            .moveTo(awaitingInput);

        handleCreatePlaylist
            .body(context -> {
                String searchString = (String) context.getIntent().getValue("request");
                String[] respArr = spotifyPlatform.createPlaylist(context, searchString).split(";");
                slackPlatform.reply(context, "Sure! I just created the following playlist: " + searchString);
                context.getSession().put("playlist-id", respArr[0]);
                context.getSession().put("playlist-uri", respArr[1]);
            })
            .next()
            .moveTo(awaitingInput);
        handleSearchTrack
            .body(context -> {
                String response = spotifyPlatform.searchTrack(context, (String) context.getIntent().getValue("request"));
                String[] respArr = response.split(";");
                slackPlatform.reply(context, respArr[1]);
                context.getSession().put("track-uri", respArr[0]);
            })
            .next()
            .moveTo(awaitingInput);

        handleAddTrack
            .body(context -> {
                Object playlistId = context.getSession().get("playlist-id");
                if (playlistId != null) {
                    Object trackUri = context.getSession().get("track-uri");
                    if (trackUri != null) {
                        String resp = spotifyPlatform.addTrack(context, trackUri.toString());
                        slackPlatform.reply(context, resp);
                    } else {
                        slackPlatform.reply(context, "Sorry, I didn't find any song. Please indicate me what song you want me to search.");
                    }
                } else {
                    slackPlatform.reply(context, "Sorry, no playlist available. Please select one or create it before adding tracks.");
                }
            })
            .next()
            .moveTo(awaitingInput);

        handleAddTrackByName
            .body(context -> {
                Object playlistId = context.getSession().get("playlist-id");
                if (playlistId != null) {
                    String response = spotifyPlatform.searchTrack(context, (String) context.getIntent().getValue("request"));
                    String[] respArr = response.split(";");
                    if (respArr.length != 0) {
                        String resp = spotifyPlatform.addTrack(context, respArr[0]);
                        slackPlatform.reply(context, resp);
                    } else {
                        slackPlatform.reply(context, "Sorry, I didn't find any song with that name");
                    }
                } else {
                    slackPlatform.reply(context, "Sorry, no playlist available. Please select one or create one before attempting to add tracks.");
                }
            })
            .next()
            .moveTo(awaitingInput);

        handleHelp
            .body(context -> slackPlatform.reply(context, "Ask me if I can do something and I'll tell you!  \nYou" +
                " can start with something like `Can you <whatever you want>?`"))
            .next()
            .moveTo(awaitingInput);

        handleListDevices
            .body(context -> {
                String deviceNames = spotifyPlatform.getDevicesNames(context, "");
                if (deviceNames == "") {
                    slackPlatform.reply(context, "Sorry, no devices available");
                } else {
                    String[] resp = deviceNames.split(",");
                    for (int i = 0; i < resp.length; i++) {
                        resp[i] = "\n• " + resp[i];
                    }
                    slackPlatform.reply(context, String.join("", resp));
                }
            })
            .next()
            .moveTo(awaitingInput);

        handlePlayMusic
            .body(context -> {
                String resp = spotifyPlatform.playMusic(context, (String) context.getIntent().getValue("request"));
                if (resp != "") {
                    slackPlatform.reply(context, "Enjoy");
                } else {
                    slackPlatform.reply(context, "Sorry, I didn't find the device");
                }
            })
            .next()
            .moveTo(awaitingInput);

        handlePauseMusic
            .body(context -> {
                String resp = spotifyPlatform.playerActions(context, "pause");
                if (resp == "") {
                    slackPlatform.reply(context, "Music Paused");
                } else {
                    slackPlatform.reply(context, "Sorry, I didn't find the device");
                }
            })
            .next()
            .moveTo(awaitingInput);

        handleNext
            .body(context -> {
                String resp = spotifyPlatform.playerActions(context, "next");
                if (resp == "") {
                    slackPlatform.reply(context, "Track Changed");
                } else {
                    slackPlatform.reply(context, "Sorry, I didn't find the device");
                }
            })
            .next()
            .moveTo(awaitingInput);

        handlePrevious
            .body(context -> {
                String resp = spotifyPlatform.playerActions(context, "previous");
                if (resp == "") {
                    slackPlatform.reply(context, "Track Changed");
                } else {
                    slackPlatform.reply(context, "Track Changed");
                }
            })
            .next()
            .moveTo(awaitingInput);

        handleSelectPlaylist
            .body(context -> {
                String searchString = (String) context.getIntent().getValue("request");

                String plId = spotifyPlatform.getPlaylist(context, searchString);
                if (plId != "") {
                    context.getSession().put("playlist-id", plId);
                    slackPlatform.reply(context, "https://open.spotify.com/playlist/" + plId);
                } else
                    slackPlatform.reply(context, "Playlist " + searchString + " does not exist");
                //+		"\n" + tracksPlaylist);
            })
            .next()
            .moveTo(awaitingInput);

        /*
         * The state that is executed if the engine doesn't find any navigable transition in a state and the state
         * doesn't contain a fallback.
         */
        val defaultFallback = fallbackState()
            .body(context -> slackPlatform.reply(context, "Sorry, I didn't, get it"));

        /*
         * Creates the bot model that will be executed by the Xatkit engine.
         * <p>
         * A bot model contains:
         * - A list of intents/events (or libraries) used by the bot. This allows to register the events/intents to the NLP
         * service.
         * - A list of platforms used by the bot. Xatkit will take care of starting and initializing the platforms
         * when starting the bot.
         * - A list of providers the bot should listen to for events/intents. As for the platforms Xatkit will take
         * care of initializing the provider when starting the bot.
         * - The list of states the compose the bot (this list can contain the init/fallback state, but it is optional)
         * - The entry point of the bot (a.k.a init state)
         * - The default fallback state: the state that is executed if the engine doesn't find any navigable
         * transition in a state and the state doesn't contain a fallback.
         */
        val botModel = model()
            .useIntent(CoreLibrary.Greetings)
            .useIntent(CoreLibrary.Help)
            .usePlatform(slackPlatform)
            .usePlatform(spotifyPlatform)
            .listenTo(slackIntentProvider)
            .useState(awaitingInput)
            .useState(handleGreetings)
            .useState(handleSearch)
            .useState(handleSearchArtist)
            .useState(handleListPlaylists)
            .useState(handleHelp)
            .useState(handleSearchTrack)
            .useState(handleAddTrack)
            .useState(handleAddTrackByName)
            .useState(handleCreatePlaylist)
            .useState(handleListDevices)
            .useState(handlePlayMusic)
            .useState(handlePauseMusic)
            .useState(handleNext)
            .useState(handlePrevious)
            .useState(handleSelectPlaylist)
            .initState(awaitingInput)
            .defaultFallbackState(defaultFallback);

        Configuration botConfiguration = new BaseConfiguration();

        /*
         * Add your configuration properties (e.g. slack: token; spotify: base64 encoding of client_id:client_secret, refresh token, username).
         */

        // Credencials Slack
        // botConfiguration.addProperty("xatkit.slack.token", "<slack-token>");

        // Credentials Spotify
        // botConfiguration.addProperty("xatkit.spotify.token", "<base64 encoded client_id:client_secret>");
        // botConfiguration.addProperty("xatkit.spotify.refresh_token", "<spotify-refresh-token>");
        // botConfiguration.addProperty("xatkit.spotify.username", "<spotify-username>");

        // Credentials DialogFlow
        // botConfiguration.addProperty("xatkit.dialogflow.projectId", "<project-id>");
        // botConfiguration.addProperty("xatkit.dialogflow.credentials.path", "agent_properties.json");
        // botConfiguration.addProperty("xatkit.dialogflow.language", "en-US");
        // botConfiguration.addProperty("xatkit.dialogflow.clean_on_startup", "true");

        botConfiguration.addProperty("xatkit.wikipedia.token", "wikipedia-token");

        XatkitBot xatkitBot = new XatkitBot(botModel, botConfiguration);
        xatkitBot.run();
        /*
         * The bot is now started, you can check http://localhost:5000/admin to test it.
         * The logs of the bot are stored in the logs folder at the root of this project.
         */
    }
}
