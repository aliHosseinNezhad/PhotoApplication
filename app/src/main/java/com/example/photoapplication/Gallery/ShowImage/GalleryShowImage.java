package com.example.photoapplication.Gallery.ShowImage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.photoapplication.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryShowImage extends AppCompatActivity {
    @BindView(R.id.image_display_options_container)
    ConstraintLayout optionsContainer;

    @BindView(R.id.gallery_show_image_photo_pager)
    ViewPager2 viewPager;

    @BindView(R.id.image_share)
    public ImageView share_img;

    @BindView(R.id.image_delete)
    public ImageView delete_img;


    @BindView(R.id.image_rotation)
    public ImageView rotation_image;

    private GalleryShowImagePager pagerAdapter;
    ArrayList<String> photoPaths;
    int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.gallery_show_image);
        ButterKnife.bind(this);
        setData();
        startViewPager();
    }

    private void startViewPager() {
        pagerAdapter = new GalleryShowImagePager(this, viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentItem, false);
    }

    private void setData() {
        photoPaths = new ArrayList<>();
        String[] array = getIntent().getStringArrayExtra(getResources().getString(R.string.photosList));
        for (int i = 0; i < array.length; i++)
            photoPaths.add(array[i]);
        currentItem = getIntent().getIntExtra(getResources().getString(R.string.currentItem), 0);
    }

    public void pager_click(View view) {
        if (optionsContainer.getVisibility() == View.VISIBLE) {
            hideOptions();
        } else {
            showOptions();
        }
    }

    private void showOptions() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(optionsContainer, "alpha", 1);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                optionsContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    private void hideOptions() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(optionsContainer, "alpha", 0);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                optionsContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }
}