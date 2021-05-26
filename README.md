Xatkit Spotify Platform 🎵 🎸  
======
_Created with ❤️ by [María Noel Bassagoda](https://github.com/noebassagoda), [Maria Victoria Armand](https://gitlab.fing.edu.uy/maria.armand.ugon) and [Cecilia Guayta](https://gitlab.fing.edu.uy/cecilia.guayta)_
_Supervised by [Daniel Calegari](https://gitlab.fing.edu.uy/dcalegar)_

⚠ This platform is outdated and not actively supported anymore.

Create Xatkit bots to manipulate Spotify playlists

## Integrate Spotify
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

## Actions

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
