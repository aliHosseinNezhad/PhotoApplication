package com.example.photoapplication.WebPhoto;

import android.graphics.Bitmap;
import android.view.View;

import com.google.gson.annotations.Expose;

public class ImageData {
    public static final String NOT_STARTED = "not_started";
    public static final String STARTED = "STARTED";
    public static final String FINISHED = "FINISHED";
    public static final String CANCELED = "CANCELED";
    public static final String ERROR = "TIME_OUT";

    public int errorMessageVisibility;
    public String errorMessageText = "";
    public int loadingBarVisibility;


    private String status;

    public String getStatus() {
        return status;
    }

    @Expose(serialize = true,deserialize = true)
    public String url;

    public String getUrl() {
        return url;
    }

    @Expose(serialize = true,deserialize = true)
    public String largeUrl;

    public String getLargeUrl() {
        return largeUrl;
    }

    @Expose(serialize = true,deserialize = true)
    public String webName;

    public String getWebName() {
        return webName;
    }

    @Expose(serialize = true,deserialize = true)
    public boolean liked;

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }


    private Bitmap bitmap;

    public Bitmap getImage() {
        return bitmap;
    }

    public void setImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public ImageData(String url, String largeUrl) {
        this.url = url;
        this.largeUrl = largeUrl;
        init();
    }

    public ImageData() {
        init();
    }

    public void init() {
        errorMessageVisibility = View.VISIBLE;
        errorMessageText = "suspend";
        loadingBarVisibility = View.GONE;
        liked = false;
        webName = "";
        status = NOT_STARTED;
    }

    public void setStatus(String status) {
        this.status = status;
        if (status.compareTo(STARTED) == 0) {
            loadingBarVisibility = View.VISIBLE;
        } else if (status.compareTo(FINISHED) == 0) {
            loadingBarVisibility = View.GONE;
        }
    }
}
