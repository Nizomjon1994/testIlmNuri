package com.ilmnuri.com.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/7/2016.
 */
public class Global {

    private static Global instance;
    ArrayList<AlbumModel> arrayList;

    private int current_position;
    Audio mAudio;

    public Audio getAudio() {
        return mAudio;
    }

    public void setAudio(Audio audio) {
        mAudio = audio;
    }

    public ArrayList<AlbumModel> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<AlbumModel> arrayList) {
        this.arrayList = arrayList;
    }

    public Global() {
        instance = this;
        arrayList = new ArrayList<>();
    }

    public static Global getInstance() {
        if (instance == null) {
            return new Global();
        }
        return instance;
    }

    public ArrayList<AlbumModel> getAlbums(String categoryName) {
        ArrayList<AlbumModel> arrAlbums = new ArrayList<>();
        for (AlbumModel albumModel : arrayList) {
            if (albumModel.getCategory().equals(categoryName)) {
                arrAlbums.add(albumModel);
            }
        }
        return arrAlbums;
    }

    public boolean checkAudio(Audio audio) {
        if (getAudio() != null) {

            if (getAudio().equals(audio)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public int getCurrent_position() {
        return current_position;
    }

    public void setCurrent_position(int current_position) {
        this.current_position = current_position;
    }
}
