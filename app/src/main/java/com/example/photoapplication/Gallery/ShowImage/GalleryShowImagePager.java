package com.example.photoapplication.Gallery.ShowImage;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.photoapplication.Gallery.PhotoData;
import com.example.photoapplication.Gallery.RotationTask;
import com.example.photoapplication.R;
import com.example.photoapplication.Utils.OnResultCallBack.ConfigureCallBack;
import com.example.photoapplication.Utils.OnResultCallBack.JobFinished;
import com.example.photoapplication.Utils.WarningData;
import com.example.photoapplication.Utils.WarningDialog;

import java.io.File;

public class GalleryShowImagePager extends RecyclerView.Adapter<GalleryShowImagePager.GalleryPagerHolder> {
    Context context;
    ViewPager2 viewPager;
    GalleryShowImage galleryShowImage;


    public GalleryShowImagePager(Context context, ViewPager2 viewPager) {
        this.context = context;
        this.viewPager = viewPager;
        galleryShowImage = (GalleryShowImage) context;
        this.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                galleryShowImage.share_img.setAlpha(1 - positionOffset);
                galleryShowImage.delete_img.setAlpha(1 - positionOffset);
                galleryShowImage.rotation_image.setAlpha(1 - positionOffset);
            }
        });
        setGalleryShowImageListeners();
    }
    private void setGalleryShowImageListeners() {
        galleryShowImage.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WarningData warningData = new WarningData();
                warningData.cancelButtonText = "cancel";
                warningData.okayButtonText = "delete";
                warningData.warningMessage = "are you sure to delete" + viewPager.getCurrentItem() + " item?";
                warningData.warningTitle = "delete warning";

                WarningDialog warningDialog = new WarningDialog(context, warningData, new ConfigureCallBack() {
                    @Override
                    public void requestConfigure(boolean accept) {
                        if (accept) {
                            File file = new File(PhotoData.photoList.get(viewPager.getCurrentItem()).path);
                            if (file.exists()) {
                                file.delete();
                                callBroadCast(PhotoData.photoList.get(viewPager.getCurrentItem()).path);
                            }
                            PhotoData.photoList.remove(viewPager.getCurrentItem());
                            notifyItemRemoved(viewPager.getCurrentItem());
                        }
                    }
                });
                warningDialog.show();

            }

            private void callBroadCast(String path) {
                if (Build.VERSION.SDK_INT >= 14) {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString();
                    MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
                } else {
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                }
            }
        });
        galleryShowImage.share_img.setOnClickListener(view -> {
            Log.i("share_rotation", "onClick: ");
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri screenshotUri = Uri.parse(PhotoData.photoList.get(viewPager.getCurrentItem()).path);
            String Type = PhotoData.photoList.get(viewPager.getCurrentItem()).
                    path.substring(PhotoData.photoList.get(viewPager.getCurrentItem()).
                    path.lastIndexOf(".") + 1);
            sharingIntent.setType("image/" + Type);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            context.startActivity(Intent.createChooser(sharingIntent, "Share image using"));
        });
        galleryShowImage.rotation_image.setOnClickListener(view -> {
            new RotationTask(PhotoData.photoList.get(viewPager.getCurrentItem()).path,context, new JobFinished() {
                @Override
                public void jobFinished(boolean finished,String...strings) {
                    notifyItemChanged(viewPager.getCurrentItem());
                }
            }).execute();
        });
    }

    @NonNull
    @Override
    public GalleryPagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_show_image_pager_holder, parent, false);
        return new GalleryPagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPagerHolder holder, int position) {
        setImage(holder, position);
    }

    private void setImage(GalleryPagerHolder holder, int position) {
        holder.imageView.setDoubleTapZoomDpi(500);
        holder.imageView.setImage(ImageSource.uri(PhotoData.photoList.get(position).path));
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position > 0) {
                    notifyItemChanged(position - 1);
                }
                if (position < PhotoData.photoList.size() - 1) {
                    notifyItemChanged(position + 1);
                }
            }
        });
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    || (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public int getItemCount() {
        if (PhotoData.photoList == null || PhotoData.photoList.size() == 0) {
            galleryShowImage.finish();
        }
        return PhotoData.photoList == null ? 0 : PhotoData.photoList.size();
    }

    public class GalleryPagerHolder extends RecyclerView.ViewHolder {
        SubsamplingScaleImageView imageView;

        public GalleryPagerHolder(@NonNull View itemView) {
            super(itemView);
            setViewsById(itemView);
        }

        private void setViewsById(View itemView) {
            imageView = itemView.findViewById(R.id.gallery_pager_image_view);
        }
    }
}
