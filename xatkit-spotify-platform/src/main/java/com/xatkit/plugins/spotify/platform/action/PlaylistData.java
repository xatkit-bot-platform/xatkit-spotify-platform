package com.xatkit.plugins.spotify.platform.action;

import org.json.JSONObject;

public class PlaylistData {

    private String id;
    private String name;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public PlaylistData(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

}
