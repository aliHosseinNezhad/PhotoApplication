package com.example.photoapplication.WebPhoto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.photoapplication.WebPhoto.FavoritePhotoSection.FavoriteFragment;
import com.example.photoapplication.WebPhoto.WebPhotosSection.WebPhotosFragment;

public class WebPagerAdapter extends FragmentPagerAdapter {
    FavoriteFragment favoriteFragment;
    WebPhotosFragment webPhotosFragment;

    public WebPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        favoriteFragment = new FavoriteFragment();
        webPhotosFragment = new WebPhotosFragment();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return favoriteFragment;
        } else if (position == 0) {
            return webPhotosFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1) {
            return "Favorite";
        } else {
            return "Web Photos";
        }
    }

    public void addFavoriteItem(String jsonString) {
        favoriteFragment.addFavoriteItem(jsonString);
    }
}
