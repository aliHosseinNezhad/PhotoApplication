package com.example.photoapplication.Gallery;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.photoapplication.Gallery.ShowImage.GalleryShowImage;
import com.example.photoapplication.R;
import com.example.photoapplication.Utils.OnResultCallBack.ConfigureCallBack;
import com.example.photoapplication.Utils.WarningData;
import com.example.photoapplication.Utils.WarningDialog;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.io.File;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
    private final OnBackPressedCallback selectionDesignBackPressCallBack;
    Context context;
    ViewGroup parent;
    private boolean onSelection = false;
    Gallery gallery;

    public PhotoAdapter(Context context) {
        this.context = context;
        gallery = (Gallery) context;
        selectionDesignBackPressCallBack = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                exitSelectionDesign();
            }
        };
        gallery.getOnBackPressedDispatcher().addCallback(gallery, selectionDesignBackPressCallBack);

        gallery.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onSelection)
                    if (dy > 0 && gallery.floatingDeleteBtn.getVisibility() == View.VISIBLE) {
                        gallery.floatingDeleteBtn.hide();
                    } else if (dy < 0 && gallery.floatingDeleteBtn.getVisibility() != View.VISIBLE) {
                        gallery.floatingDeleteBtn.show();
                    }
            }
        });
        gallery.floatingDeleteBtn.setOnClickListener(view -> {
            deleteSelectedItems();
        });
        startDefaultDesign();
    }


    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_photo_holder, parent, false);
        return new PhotoHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        File imgFile = new File(PhotoData.photoList.get(position).path);
        if (imgFile.exists()) {
            File file = new File(PhotoData.photoList.get(position).path);
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.my_drawable);
            Glide.with(context)
                    .load(file)
                    .centerCrop()
                    .apply(requestOptions)
                    .into(holder.photo);
        }
        holder.selectImageView.setVisibility(PhotoData.photoList.get(position).SelectVisibility);
        holder.checkBox.setVisibility(PhotoData.photoList.get(position).checkBoxVisibility);
        holder.checkBox.setChecked(PhotoData.photoList.get(position).isChecked);
    }

    @Override
    public int getItemCount() {
        return (PhotoData.photoList == null) ? 0 : PhotoData.photoList.size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {
        ImageView selectImageView;
        ImageView photo;
        MaterialCheckBox checkBox;

        public PhotoHolder(@NonNull View view) {
            super(view);
            setViewsById(view);
            photo.setOnClickListener(view12 -> {
                if (onSelection) {
                    if (PhotoData.photoList.get(getAdapterPosition()).SelectVisibility == View.GONE)
                        setPhotoVisibility(getAdapterPosition(), View.VISIBLE);
                    else
                        setPhotoVisibility(getAdapterPosition(), View.GONE);

                } else {
                    Intent intent = new Intent(context, GalleryShowImage.class);
                    String[] photosList = new String[PhotoData.photoList.size()];
                    for (int i = 0; i < PhotoData.photoList.size(); i++) {
                        photosList[i] = PhotoData.photoList.get(i).path;
                    }
                    intent.putExtra(context.getString(R.string.photosList), photosList);
                    intent.putExtra(context.getString(R.string.currentItem), getAdapterPosition());
                    context.startActivity(intent);
                }

            });
            photo.setOnLongClickListener(view1 -> {
                startSelectionDesign(getAdapterPosition());
                return true;
            });
        }
        private void setViewsById(View view) {
            photo = view.findViewById(R.id.photo_imageView);
            selectImageView = view.findViewById(R.id.select_image_view);
            checkBox = view.findViewById(R.id.gallery_holder_check_box);
        }
    }

    private void startSelectionDesign(int position) {
        setPhotoVisibility(position, View.VISIBLE);
        notifyItemChanged(position);
        startSelectionDesign();
    }

    public void startSelectionDesign() {
        exitDefaultDesign();
        for (int i = 0; i < PhotoData.photoList.size(); i++) {
            PhotoData.photoList.get(i).checkBoxVisibility = View.VISIBLE;
        }
        gallery.selectedItemCountTxt.setVisibility(View.VISIBLE);
        gallery.selectedItemCountTxt.setText(context.getString(R.string.select_count, getCountOfSelectedItems()));
        selectionDesignBackPressCallBack.setEnabled(true);
        gallery.floatingDeleteBtn.show();
        gallery.visibleSelectionMenuItems();
        onSelection = true;
        notifyDataSetChanged();
    }

    public void exitSelectionDesign() {
        gallery.selectedItemCountTxt.setVisibility(View.GONE);
        selectionDesignBackPressCallBack.setEnabled(false);
        gallery.floatingDeleteBtn.hide();
        for (int i = 0; i < PhotoData.photoList.size(); i++) {
            setPhotoVisibility(i, View.GONE);
            PhotoData.photoList.get(i).checkBoxVisibility = View.GONE;
        }
        onSelection = false;
        notifyDataSetChanged();
        startDefaultDesign();
    }

    public void startDefaultDesign() {
        gallery.visibleDefaultMenuItems();
        onSelection = false;
        notifyDataSetChanged();
    }

    public void exitDefaultDesign() {

    }

    private void setPhotoVisibility(int i, int visibility) {
        PhotoData.photoList.get(i).SelectVisibility = visibility;
        if (visibility == View.VISIBLE) {
            PhotoData.photoList.get(i).isChecked = true;
        } else {
            PhotoData.photoList.get(i).isChecked = false;
        }
        gallery.selectedItemCountTxt.setText(context.getString(R.string.select_count, getCountOfSelectedItems()));
        notifyItemChanged(i);
    }

    private int getCountOfSelectedItems() {
        int temp = 0;
        for (int i = 0; i < PhotoData.photoList.size(); i++) {
            if (PhotoData.photoList.get(i).SelectVisibility == View.VISIBLE) {
                temp++;
            }
        }
        return temp;
    }

    public void deleteSelectedItems() {
        int itemCount=getCountOfSelectedItems();
        if(itemCount==0){
            Toast.makeText(context, "there is nothing selected! \n please select item and delete", Toast.LENGTH_SHORT).show();
            return;
        }
        WarningData warningData = new WarningData();
        warningData.cancelButtonText = "cancel";
        warningData.okayButtonText = "delete";
        warningData.warningMessage = "are you sure to delete (" + itemCount + ") selected items?";
        warningData.warningTitle = "delete warning";
        WarningDialog warningDialog = new WarningDialog(context, warningData, new ConfigureCallBack() {
            @Override
            public void requestConfigure(boolean accept) {
                if(accept){
                    for(int i=PhotoData.photoList.size()-1;i>=0;i--){
                        if(PhotoData.photoList.get(i).isChecked){
                            File file=new File(PhotoData.photoList.get(i).path);
                            if (file.exists()) {
                                file.delete();
                                refreshMediaStore(PhotoData.photoList.get(i).path);
                            }
                            PhotoData.photoList.remove(i);
                            notifyItemRemoved(i);
                        }
                    }
                    startSelectionDesign();
                }
            }
        });
        warningDialog.show();
    }

    private void refreshMediaStore(String path) {
        if (Build.VERSION.SDK_INT >= 14) {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
            MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    public void selectAll() {
        startSelectionDesign();
        for (int i = 0; i < PhotoData.photoList.size(); i++) {
            setPhotoVisibility(i, View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    public void DeselectAll() {
        for (int i = 0; i < PhotoData.photoList.size(); i++) {
            setPhotoVisibility(i, View.GONE);
        }
        notifyDataSetChanged();
    }
}
