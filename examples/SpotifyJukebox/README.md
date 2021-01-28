Xatkit - Chatbot based on MDE to manipulate Spotify 🎵 🎸  
======
_Created with ❤️ by [María Noel Bassagoda](https://github.com/noebassagoda), [Maria Victoria Armand](https://gitlab.fing.edu.uy/maria.armand.ugon) and [Cecilia Guayta](https://gitlab.fing.edu.uy/cecilia.guayta)_

In this project, we extended the platform Xatkit to create a Slack chatbot integrated with Spotify and Wikipedia. In particular, we implemented two platforms, one to integrate Xatkit with Spotify's API and the other one with Wikipedia. Finally, we integrated these two platforms within a Slack workspace to manage our Spotify playlists and player from there.


## Table of Contents

- [Xatkit](#xatkit-)
- [Getting Started](#getting-started-)
- [Available Intents](#available-intents-)
- [Demo](#demo-)

## Xatkit 🤖

[![Wiki Badge](https://img.shields.io/badge/doc-wiki-blue)](https://github.com/xatkit-bot-platform/xatkit/wiki)
[![Twitter](https://img.shields.io/twitter/follow/xatkit?label=Follow&style=social)](https://twitter.com/xatkit)


*Get your own smart chatbot. Design your bot once, deploy it everywhere*

Xatkit has been created to reduce boilerplate code, complex API understanding, and technical details to facilitate the definition and deployment of your bots. Xatkit helps you focus on what really matters: **the conversation logic you want to embed in your chatbot**. 

To do so, we have baked a **chatbot-specific definition language** to specify user intentions, receive events (your bots can also be proactive!), and bind them to computable actions following powerful [state machine semantics](https://xatkit.com/chatbot-dsl-state-machines-xatkit-language/). Our chatbot language is implemented as a [Java Fluent Interface](https://xatkit.com/fluent-interface-building-chatbots-bots/) combining the low-code benefits of using a dedicated chatbot Internal DSL with the full power of Java when you need to write complex bot behaviours. 

This chatbot *specification* is then handled by the **Xatkit Runtime Engine**, which automatically manages its deployment and execution. See some [examples](https://xatkit.com/chatbot-examples/) of what you can do with Xatkit!. Reuse the existing platforms (or add your own) to make your bot *talk* with external services (Slack, GitHub, websites,...).


## Getting Started 🚀

#### Integrate DialogFlow
By default, this example relies on Xatkit's RegExp intent provider, that performs exact matching of user inputs to extract intents. If you want to use a more powerful intent provider such as DialogFlow you can take a look at [this article](https://github.com/xatkit-bot-platform/xatkit-releases/wiki/Integrating-DialogFlow). Once you follow the instructions on how to set up your account you should set the following keys in the configurations section at the end of the definition of the **SpotifyBot**:

```
botConfiguration.addProperty("xatkit.dialogflow.projectId", <project-ID>);
botConfiguration.addProperty("xatkit.dialogflow.credentials.path", "agent_properties.json");
botConfiguration.addProperty("xatkit.dialogflow.language", "en-US");
botConfiguration.addProperty("xatkit.dialogflow.clean_on_startup", "true");
```


#### Integrate Slack

Our bot needs to access the Slack API to receive user messages and post answers. Slack provides a way to do it through Slack Apps, which are third-party applications that can be added to Slack workspaces.

[Create a Classic Slack App here](https://api.slack.com/apps?new_classic_app=1). You can check [this article](https://github.com/xatkit-bot-platform/xatkit-releases/wiki/Deploying-on-Slack) to create a Slack app for Xatkit. When selecting the **Permission Scopes**, make sure to allow **ALL** of the following scopes: `bot`, `channels:read`, `chat:write:bot`, `chat:write:user`, `groups:read`, `im:read`, `mpim:read`, `users:read`. Once you followed the instructions on how to set up your account you should set the following key like you did for the DialogFlow keys:

```
botConfiguration.addProperty("xatkit.slack.token", <slack-token>);
```

> **NOTE:** Slack has improved how they handle permissions for apps, but it's very important to create [create a Classic Slack App](https://api.slack.com/apps?new_classic_app=1) since Xatkit uses the RTM API which is not supported in newer Slack Apps.

#### Integrate Spotify
* Head to [Spotify Developer](https://developer.spotify.com/dashboard/login) and register (or login to your account in case you already have one)

* Create a new app in the My Applications section. Under the newly created app config, add the following Redirect URI - https://www.getpostman.com/oauth2/callback

* We'll use the [Authorization Code Flow](https://developer.spotify.com/documentation/general/guides/authorization-guide/)to obtain the Refresh Token.
    * Open Postman, under a new request, click on the Authorization tab, select OAuth 2.0 and fill in these values:
        * Token Name: can be anything
        * Auth URL: https://accounts.spotify.com/authorize
        * Access Token URL: https://accounts.spotify.com/api/token
        * Client ID: <client-id>
        * Client Secret: <client-secret>
        * Scope: `playlist-read-private` `playlist-modify-private` `user-read-playback-state` `user-read-currently-playing` `user-read-playback-state` `user-modify-playback-state`
        * Grant Type: Authorization Code
        * Request access token locally: Checked
    * Click on Request Token, go through the OAuth flow, and add the following keys to the bot configuration:

```
botConfiguration.addProperty("xatkit.spotify.token", <base64 encoded client_id:client_secret>);
botConfiguration.addProperty("xatkit.spotify.refresh_token", <refresh-token>);
botConfiguration.addProperty("xatkit.spotify.username", <spotify-username>);
```

#### Build the latest version of Xatkit
In order to build the latest version of Xatkit navigate to the root folder of the project and run the following commands:

```bash
mvn clean install -DskipTests
cd xatkit-spotify-platform
clean install -DskipTests
cd ..
cd xatkit-wikipedia-platform
mvn clean install -DskipTests
```

#### Start the server
Navigate to `xatkit-examples/Spotify/SpotifyJukebox` and start the example:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.xatkit.example.SpotifyBot"
```

The console will log some initialization information, and after a few seconds you should see the following message:

```bash
Xatkit bot started
```

#### **You're all set!** 💃 🥳 🚀

You can now open your browser and navigate to your slack working environment to test your deployed slack-based bot! 

## Available Intents ✨

#### Wikipedia

| Intent               | Training Sentences | Parameter | Constraint    |
| -------------------- | ------------------| --------- | ------------- |
|Search| Search **this**  <br> Can you give me some info about **this**? <br> Do you know **this**? <br> What can you tell me about **this**?| **this** | Mandatory Parameter|

#### Spotify

| Intent               | Training Sentences | Parameter | Constraint    |
| -------------------- | ------------------| --------- | ------------- |
| List Playlists | Can you list me my playlists?<br>List me my playlists<br>Wich are my playlists?|||
|Search Artist| Search artist **name** <br> Can you give me some info about the artist **name**? <br> Who is the artist **name**? | **name**| **Mandatory Parameter** |
|Create Playlist |Create playlist **name** <br> Can you create the playlist **name**? <br> Please create the playlist **name**|**name**| **Mandatory Parameter**. The playlist <br> will be stored in the **context**|
|Select Playlist| Select **playlistName** <br> Select playlist **playlistName** <br> Can you select the playlist **playlistName**? <br> Can you select **playlistName**? |**playlistName**|**Mandatory Parameter** The playlist <br> will be stored in the **context**|
|Show Playlist| Can you show me the playlist **playlistName**? <br> Can you show me **playlistName**? | **playlistName** | **Mandatory Parameter**|
|Search Track|Search track **name** <br> Can you search the track **name**? <br> Can you list me the track **name**?| **name** | **Mandatory Parameter** |
|Add Track By Name| Add track **trackName** <br> Add **trackName** <br> Can you add **trackName**?|**trackName**| **Mandatory Parameter**.<br> You must have **created/selected**<br> a playlist in order for this to work|
|Add Track (after making a search)|Add it! <br> Add it please <br> Can you add it please?|| **No Parameter Required**. You <br> should have previously searched  a  <br> song which will be stored in the **context** |
|List Devices| List me my devices <br> List devices <br> Can you list me my devices please?| | |
|Play Music| Play music <br> Can you turn on the music? <br> Turn on the music <br> Play <br> Turn on the music on **device** <br> Play music on **device**| **device** |**Optional Parameter**. If no device is <br> specified it will play on the first one <br> available. If theres a playlist stored  <br>in the **context** it will play that one, <br> if not it will turn on the currently Playing Track|
|Pause Music| Turn off <br> Turn off the music <br> Pause music <br> Pause <br> Can you pause the music? | | |
|Next Track|Next<br> Next track <br> Following track <br> Next song <br>Can you change to the following track?| | |
|Previous Track|Previous<br> Previous track <br> Previous song <br>Can you change to the previous track? | | |


## Demo 🤩

<p align="center">
  <img src="https://user-images.githubusercontent.com/54251435/101290963-163b7a80-37e4-11eb-9678-56ddd39f3e2d.gif" width="400" height="552" />
  <img src="https://user-images.githubusercontent.com/54251435/101290726-e049c680-37e2-11eb-83e4-e6775986fb6f.gif" width="400" height="552"/> 
</p>

