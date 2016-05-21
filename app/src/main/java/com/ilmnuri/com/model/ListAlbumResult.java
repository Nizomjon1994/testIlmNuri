package com.ilmnuri.com.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by User on 18.05.2016.
 */
public class ListAlbumResult  {

    @SerializedName("albums")
    ArrayList<AlbumModel> mAlbumModels;





    public ArrayList<AlbumModel> getAlbumModels() {
        return mAlbumModels;
    }

    public void setAlbumModels(ArrayList<AlbumModel> albumModels) {
        mAlbumModels = albumModels;
    }


}
