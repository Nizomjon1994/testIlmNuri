package com.ilmnuri.com.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 19.05.2016.
 */
public class Audio {

    @SerializedName("song_atitle")
    private String trackName;
    @SerializedName("song_id")
    private int trackId;
    @SerializedName("song_size")
    private String trackSize;

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getTrackSize() {
        return trackSize;
    }

    public void setTrackSize(String trackSize) {
        this.trackSize = trackSize;
    }
}
