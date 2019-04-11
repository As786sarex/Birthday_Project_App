package com.wildcardenter.myfab.for_jahan.models;

public class MusicListModel {
    private int musicAdd;
    private int musicPhotoTumb;
    private String musicTitle;
    private String musicDesc;

    public MusicListModel(int musicAdd, int musicPhotoTumb, String musicTitle, String musicDesc) {
        this.musicAdd = musicAdd;
        this.musicPhotoTumb = musicPhotoTumb;
        this.musicTitle = musicTitle;
        this.musicDesc = musicDesc;
    }

    public int getMusicAdd() {
        return musicAdd;
    }

    public void setMusicAdd(int musicAdd) {
        this.musicAdd = musicAdd;
    }

    public int getMusicPhotoTumb() {
        return musicPhotoTumb;
    }

    public void setMusicPhotoTumb(int musicPhotoTumb) {
        this.musicPhotoTumb = musicPhotoTumb;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getMusicDesc() {
        return musicDesc;
    }

    public void setMusicDesc(String musicDesc) {
        this.musicDesc = musicDesc;
    }
}
