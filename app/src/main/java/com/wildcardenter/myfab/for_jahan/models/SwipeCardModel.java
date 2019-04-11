package com.wildcardenter.myfab.for_jahan.models;

import java.io.Serializable;

public class SwipeCardModel implements Serializable {
    private int photoAdd;
    private String photoAddrs;
    private String title;
    private String desc;


    public SwipeCardModel(int photoAdd, String title, String desc) {
        this.photoAdd = photoAdd;
        this.title = title;
        this.desc = desc;
    }

    public SwipeCardModel(String photoAddrs, String title, String desc) {
        this.photoAddrs = photoAddrs;
        this.title = title;
        this.desc = desc;
    }

    public SwipeCardModel() {
    }

    public String getPhotoAddrs() {
        return photoAddrs;
    }

    public void setPhotoAddrs(String photoAddrs) {
        this.photoAddrs = photoAddrs;
    }

    public int getPhotoAdd() {
        return photoAdd;
    }

    public void setPhotoAdd(int photoAdd) {
        this.photoAdd = photoAdd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
