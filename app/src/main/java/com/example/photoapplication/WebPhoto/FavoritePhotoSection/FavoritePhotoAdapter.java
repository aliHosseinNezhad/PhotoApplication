package com.example.photoapplication.WebPhoto.FavoritePhotoSection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photoapplication.R;
import com.example.photoapplication.WebPhoto.ImageData;
import com.example.photoapplication.WebPhoto.PrefrenceMangager.SharedPreferenceManager;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;

public class FavoritePhotoAdapter extends RecyclerView.Adapter<FavoritePhotoAdapter.FavoriteHolder> {
    Context context;
    ArrayList<ImageData> favoriteList;
    FavoriteDownloadManager favoriteDownloadManager;

    public FavoritePhotoAdapter(Context context) {
        this.context = context;
        favoriteList = SharedPreferenceManager.getInstance(context).getFavoriteItems();
        favoriteDownloadManager =new FavoriteDownloadManager(this,favoriteList,context);
    }

    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.web_photo_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
        showImage(holder, position);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public void addFavoriteItem(String jsonString) {
        favoriteDownloadManager.addFavorite(jsonString);
    }

    public class FavoriteHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton favoriteBtn;
        RotateLoading loadingBar;
        TextView webSiteTitle;

        public FavoriteHolder(@NonNull View itemView) {
            super(itemView);
            favoriteBtn = itemView.findViewById(R.id.favorite_btn);
            imageView = itemView.findViewById(R.id.photo_imageView);
            loadingBar = itemView.findViewById(R.id.progressBar);
            webSiteTitle = itemView.findViewById(R.id.web_site_name);
            loadingBar.start();
        }
    }


    private void showImage(FavoriteHolder holder, int position) {
        ImageData imageData=favoriteList.get(position);
        holder.imageView.setImageBitmap(imageData.getImage());
        holder.webSiteTitle.setText(imageData.getWebName());
        holder.loadingBar.setVisibility(imageData.loadingBarVisibility);
    }



}
