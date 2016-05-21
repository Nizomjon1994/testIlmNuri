package com.ilmnuri.com.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 3/5/2016.
 */
public class AlbumModel implements Serializable {
    @SerializedName("album")
    String album;
    @SerializedName("category")
    String category;
    @SerializedName("items")
    List<Audio> mAudios;

    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Audio> getAudios() {
        return mAudios;
    }

    public void setAudios(List<Audio> audios) {
        mAudios = audios;
    }


}
