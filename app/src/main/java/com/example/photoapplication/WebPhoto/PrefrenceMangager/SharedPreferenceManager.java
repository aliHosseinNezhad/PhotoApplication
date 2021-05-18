package com.example.photoapplication.WebPhoto.PrefrenceMangager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.photoapplication.WebPhoto.ImageData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreferenceManager {
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    private String SharedName = "GallerySharedPreference";
    private String favoriteKey = "favorite";
    private static SharedPreferenceManager sharedPreferenceManager;

    private SharedPreferenceManager(Context context) {
        sharedPreference = context.getSharedPreferences(SharedName, Context.MODE_PRIVATE);
        editor = sharedPreference.edit();
    }

    public static SharedPreferenceManager getInstance(Context context) {
        if (sharedPreferenceManager == null) {
            sharedPreferenceManager = new SharedPreferenceManager(context);
        }
        return sharedPreferenceManager;
    }

    public boolean haveItems() {
        if (sharedPreference.getString(favoriteKey, null) == null) {
            return false;
        }
        return true;
    }

    public ArrayList<ImageData> getFavoriteItems() {
        if (haveItems()) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Type type = new TypeToken<ArrayList<ImageData>>() {
            }.getType();
            return gson.fromJson(sharedPreference.getString(favoriteKey, null), type);
        }
        return new ArrayList<>();
    }

    public void addFavoriteItem(ImageData imageData) {
        ArrayList<ImageData> imageList = getFavoriteItems();
        imageList.add(imageData);
        setFavoriteList(imageList);
    }

    public void setFavoriteList(ArrayList<ImageData> imageList) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Type type=new TypeToken<ArrayList<ImageData>>(){}.getType();
        editor.putString(favoriteKey, gson.toJson(imageList,type));
        editor.apply();
    }

    public void deleteItem(int position) {
        ArrayList<ImageData> imageList = getFavoriteItems();
        imageList.remove(position);
        setFavoriteList(imageList);
    }

    public void deleteAllItems() {
        ArrayList<ImageData> imageList = getFavoriteItems();
        imageList.clear();
        imageList = new ArrayList<>();
        setFavoriteList(imageList);
    }

    public void deleteItem(String url, String largeUrl) {
        int whichUrl = (url == null) ? 1 : 0;
        String sUrl = (whichUrl == 0) ? url : largeUrl;
        int position = searchForItem(whichUrl, sUrl);
        if(position!=-1){
            deleteItem(position);
        }
    }

    private int searchForItem(int whichUrl, String sUrl) {
        ArrayList<ImageData> imageList = getFavoriteItems();
        if (whichUrl == 0)
            for (int i = 0; i < imageList.size(); i++) {
                if (imageList.get(i).getUrl().compareTo(sUrl) == 0) {
                    return i;
                }
            }
        else
            for (int i = 0; i < imageList.size(); i++) {
                if (imageList.get(i).getLargeUrl().compareTo(sUrl) == 0) {
                    return i;
                }
            }
        return -1;
    }

}
