package com.example.photoapplication.Gallery;

import android.view.View;

import java.util.ArrayList;

public class PhotoData {
    public String path;
    public int SelectVisibility;
    public boolean isChecked;
    public int checkBoxVisibility;

    public PhotoData() {
        SelectVisibility = View.GONE;
        checkBoxVisibility = View.GONE;
        isChecked = false;
    }

    public static ArrayList<PhotoData> photoList = new ArrayList<>();
}
