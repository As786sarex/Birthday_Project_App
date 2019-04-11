package com.wildcardenter.myfab.for_jahan.models;

public class SwipeImageModel {
    private String postId;
    private String ImageUrl;
    private String imageTitle;
    private String imageMessage;
    private String imageName;

    public SwipeImageModel(String postId, String imageUrl, String imageTitle, String imageMessage, String imageName) {
        this.postId = postId;
        ImageUrl = imageUrl;
        this.imageTitle = imageTitle;
        this.imageMessage = imageMessage;
        this.imageName = imageName;
    }

    public SwipeImageModel() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImageMessage() {
        return imageMessage;
    }

    public void setImageMessage(String imageMessage) {
        this.imageMessage = imageMessage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
