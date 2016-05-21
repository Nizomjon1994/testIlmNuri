package com.ilmnuri.com.api;

import com.ilmnuri.com.model.ListAlbumResult;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by User on 18.05.2016.
 */
public interface IlmApi {


    @GET("/albums/")
    void getAlbums(Callback<ListAlbumResult> albumResult);

    @GET("/")
    void getAboutUs(Callback<String> response);


}
