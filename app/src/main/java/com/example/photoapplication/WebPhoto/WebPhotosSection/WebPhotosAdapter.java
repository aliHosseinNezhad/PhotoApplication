package com.example.photoapplication.WebPhoto.WebPhotosSection;

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

public class WebPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int IMAGE_VIEW_TYPE = 1254;
    private static final int LOADING_VIEW_TYPE = 314;
    private final RecyclerView recyclerView;
    int addItemCount;
    Context context;
    ArrayList<ImageData> photoList;
    WebPhotosDownloadManager webPhotosDownloadManager;
    public class WebPhotosHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton favoriteBtn;
        RotateLoading loadingBar;
        TextView webSiteTitle;
        TextView errorMessageTxt;

        public WebPhotosHolder(@NonNull View itemView) {
            super(itemView);
            favoriteBtn = itemView.findViewById(R.id.favorite_btn);
            imageView = itemView.findViewById(R.id.photo_imageView);
            loadingBar = itemView.findViewById(R.id.progressBar);
            webSiteTitle = itemView.findViewById(R.id.web_site_name);
            loadingBar.start();
            errorMessageTxt = itemView.findViewById(R.id.error_message);

            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webPhotosDownloadManager.addToFavorite(getAdapterPosition());
                }
            });
        }
    }

    public class LoadingPhotosHolder extends RecyclerView.ViewHolder {
        public LoadingPhotosHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    public WebPhotosAdapter(int addItemCount, Context context,RecyclerView recyclerView) {
        this.addItemCount = addItemCount;
        this.context = context;
        this.recyclerView=recyclerView;
        photoList = new ArrayList<>();
        webPhotosDownloadManager =new WebPhotosDownloadManager(context,photoList,this);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == IMAGE_VIEW_TYPE) {
            return new WebPhotosHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.web_photo_holder, parent, false));
        } else if (viewType == LOADING_VIEW_TYPE) {
            return new LoadingPhotosHolder(LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.loading_photo_holder, parent, false));
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position < photoList.size()) {
            return IMAGE_VIEW_TYPE;
        } else {
            return LOADING_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == IMAGE_VIEW_TYPE) {
            showImage((WebPhotosHolder) holder, position);
        } else if (getItemViewType(position) == LOADING_VIEW_TYPE) {
            android.os.Handler handler =new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    webPhotosDownloadManager.requestAddItem();
                }
            },300);
        }
    }


    private void showImage(WebPhotosHolder holder, int position) {
        holder.imageView.setImageBitmap(photoList.get(position).getImage());
        holder.webSiteTitle.setText(photoList.get(position).getWebName());
        holder.loadingBar.setVisibility(photoList.get(position).loadingBarVisibility);
        holder.errorMessageTxt.setVisibility(photoList.get(position).errorMessageVisibility);
        holder.errorMessageTxt.setText(photoList.get(position).errorMessageText);
        if (photoList.get(position).isLiked())
            holder.favoriteBtn.setImageDrawable(context.getDrawable(R.drawable.favorite_pink));
        else
            holder.favoriteBtn.setImageDrawable(context.getDrawable(R.drawable.favorite_white));
    }

    @Override
    public int getItemCount() {
        return photoList.size() + 1;
    }
}
