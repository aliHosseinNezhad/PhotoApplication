package com.example.photoapplication.WebPhoto.WebPhotosSection;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.photoapplication.Utils.StaticData;
import com.example.photoapplication.WebPhoto.Download.ApiManager;
import com.example.photoapplication.WebPhoto.Download.DATA_MODEL;
import com.example.photoapplication.WebPhoto.Download.DownloadTask;
import com.example.photoapplication.WebPhoto.ImageData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class WebPhotosDownloadManager {
    Context context;
    ArrayList<ImageData> photoList;
    WebPhotosAdapter webPhotosAdapter;
    int addItemCount = 5;
    private boolean addingRequested = false;
    DownloadTask downloadTask;
    ApiManager apiManager;

    public WebPhotosDownloadManager(Context context, ArrayList<ImageData> photoList,
                                    WebPhotosAdapter webPhotosAdapter) {
        this.photoList = photoList;
        this.webPhotosAdapter = webPhotosAdapter;
        this.context = context;
        addSomeItems();
        start();
    }

    private void start() {
        for (int i = 0; i < photoList.size(); i++) {
            if (photoList.get(i).getStatus().compareTo(ImageData.NOT_STARTED) == 0) {
                start(i);
                return;
            }
        }
        if (addingRequested) {
            addingRequested = false;
            addSomeItems();
        }
    }

    private void start(int position) {
        if (position < photoList.size()) {
            ImageData imageData = photoList.get(position);
            imageData.setStatus(ImageData.STARTED);
            webPhotosAdapter.notifyItemChanged(position);
            apiManager = new ApiManager(new ApiManager.ResponseCallBack() {
                @Override
                public void onResponse(Call<DATA_MODEL> call, Response<DATA_MODEL> response) {
                    if (response.isSuccessful()) {
                        String url = null;
                        imageData.url=response.body().url;
                        imageData.largeUrl=response.body().largeUrl;
                        imageData.webName=response.body().site;
                        if (response.body().url != null) {
                            url = response.body().url;
                        } else if (response.body().largeUrl != null) {
                            url = response.body().largeUrl;
                        }
                        downloadTask = new DownloadTask(result -> {
                            imageData.setImage(result);
                            if (imageData.getStatus().compareTo(ImageData.ERROR) != 0) {
                                start();
                            }
                            imageData.setStatus(ImageData.FINISHED);
                            webPhotosAdapter.notifyItemChanged(position);
                        }, () -> {
                            imageData.setStatus(ImageData.ERROR);
                            start();
                        });
                        if (url != null)
                            downloadTask.execute(url);
                        else
                            Toast.makeText(context, "url = null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DATA_MODEL> call, Throwable t) {
                    imageData.setStatus(ImageData.ERROR);
                    start();
                }
            });
            apiManager.start();
        }
    }

    public void addSomeItems() {
        for (int i = 0; i < addItemCount; i++) {
            ImageData imageData = new ImageData();
            photoList.add(imageData);
        }
        webPhotosAdapter.notifyDataSetChanged();
    }

    public void requestAddItem() {
        if (photoList.get(photoList.size() - 1).getStatus()
                .compareTo(ImageData.FINISHED) == 0 ||
                photoList.get(photoList.size() - 1).getStatus()
                        .compareTo(ImageData.ERROR) == 0) {
            addSomeItems();
            start();
        } else {
            addingRequested = true;
        }
    }

    public void addToFavorite(int position) {
        if (photoList.get(position).url != null || photoList.get(position).largeUrl != null) {
            photoList.get(position).setLiked(true);
            webPhotosAdapter.notifyItemChanged(position);
            sendBroadCastNotify(position);
        } else {
            Toast.makeText(context,
                    "Please wait while the photo is loading"
                    , Toast.LENGTH_SHORT).show();
        }
    }

    private void sendBroadCastNotify(int position) {
        Intent intent = new Intent(StaticData.AddToFavorite);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        ImageData imageData = photoList.get(position);
        String jsonImageData = gson.toJson(imageData, ImageData.class);
        intent.putExtra(StaticData.ImageDataKey,jsonImageData);
        context.sendBroadcast(intent);
    }
}
