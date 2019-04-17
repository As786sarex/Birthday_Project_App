package com.wildcardenter.myfab.for_jahan.models;

public class SecretItemModel {
    private String secret_img_url;
    private String secret_title;
    private String secret_desc;
    private int secret_type;

    public SecretItemModel() {
    }



    public SecretItemModel(String secret_img_url, String secret_title, String secret_desc, int secret_type) {
        this.secret_img_url = secret_img_url;
        this.secret_title = secret_title;
        this.secret_desc = secret_desc;
        this.secret_type=secret_type;
    }

    public String getSecret_img_url() {
        return secret_img_url;
    }

    public void setSecret_img_url(String secret_img_url) {
        this.secret_img_url = secret_img_url;
    }

    public String getSecret_title() {
        return secret_title;
    }

    public void setSecret_title(String secret_title) {
        this.secret_title = secret_title;
    }

    public String getSecret_desc() {
        return secret_desc;
    }

    public void setSecret_desc(String secret_desc) {
        this.secret_desc = secret_desc;
    }

    public int getSecret_type() {
        return secret_type;
    }

    public void setSecret_type(int secret_type) {
        this.secret_type = secret_type;
    }
}
