package com.ilmnuri.com.utility;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilmnuri.com.model.AlbumModel;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by User on 19.05.2016.
 */
public class CacheUtils {
    private static final String AUDIOS_JSON = "audios.json";
    private Context mContext;
    private Gson mGson;

    private ArrayList<AlbumModel> mAlbumModels;

    public CacheUtils(Context context, Gson gson) {
        mContext = context;
        mGson = gson;
    }

    public void saveToCache(ArrayList<AlbumModel> albumModels) {
        mAlbumModels = albumModels;
        try {
            FileUtils.stringToFile(mContext, mGson.toJson(mAlbumModels), AUDIOS_JSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<AlbumModel> getAlbums() {
        if (mAlbumModels == null) {
            mAlbumModels = getAlbumModels(AUDIOS_JSON);
        }
        return mAlbumModels;
    }

    private ArrayList<AlbumModel> getAlbumModels(String name) {
        try {
            String json = FileUtils.fileToString(mContext, name);
            if (!TextUtils.isEmpty(json)) {
                Type type = new TypeToken<ArrayList<AlbumModel>>() {
                }.getType();
                return mGson.fromJson(json, type);
            }
        } catch (Throwable e) {
            //
        }
        return null;
    }
}
