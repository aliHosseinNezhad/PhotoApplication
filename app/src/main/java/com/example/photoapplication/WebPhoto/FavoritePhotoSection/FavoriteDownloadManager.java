package com.example.photoapplication.WebPhoto.FavoritePhotoSection;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.photoapplication.WebPhoto.Download.DownloadTask;
import com.example.photoapplication.WebPhoto.ImageData;
import com.example.photoapplication.WebPhoto.PrefrenceMangager.SharedPreferenceManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FavoriteDownloadManager {
    private final Context context;
    FavoritePhotoAdapter favoritePhotoAdapter;
    ArrayList<ImageData> photoList;
    DownloadTask downloadTask;
    private boolean moreActive=false;

    public FavoriteDownloadManager(FavoritePhotoAdapter favoritePhotoAdapter,
                                   ArrayList<ImageData> photoList, Context context) {
        this.favoritePhotoAdapter = favoritePhotoAdapter;
        this.photoList = photoList;
        this.context=context;
        for (int i = 0; i < photoList.size(); i++) {
            photoList.get(i).init();
        }
        start();
    }

    private void start() {
        moreActive=false;
        for (int i = 0; i < photoList.size(); i++) {
            if (photoList.get(i).getStatus().compareTo(ImageData.NOT_STARTED) == 0) {
                start(i);
                return;
            }
        }
        moreActive=true;
    }

    private void start(int position) {
        if (photoList != null && position < photoList.size()) {
            ImageData imageData = photoList.get(position);
            imageData.setStatus(ImageData.STARTED);
            favoritePhotoAdapter.notifyItemChanged(position);
            downloadTask = new DownloadTask(bitmap -> {
                imageData.setImage(bitmap);
                if (imageData.getStatus().compareTo(ImageData.ERROR) != 0) {
                    imageData.setStatus(ImageData.FINISHED);
                    start();
                }
                imageData.setStatus(ImageData.FINISHED);
                favoritePhotoAdapter.notifyDataSetChanged();
            }, () -> {
                imageData.setStatus(ImageData.ERROR);
                start();
            });
            if (imageData.getUrl() != null)
                downloadTask.execute(photoList.get(position).getUrl());
            else
                downloadTask.execute(photoList.get(position).getLargeUrl());
        }
    }

    public void addFavorite(String jsonString) {
        photoList.add(new Gson().fromJson(jsonString,ImageData.class));
        favoritePhotoAdapter.notifyDataSetChanged();
        SharedPreferenceManager.getInstance(context).setFavoriteList(photoList);
        if(moreActive){
            start();
        }
    }

    public void removeFavorite(int position) {

    }

    public void refresh() {

    }
}
