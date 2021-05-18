package com.example.photoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.photoapplication.Gallery.Gallery;
import com.example.photoapplication.WebPhoto.WebPhoto;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.gallery_web_layout)
    ConstraintLayout galleryWebLayout;

    @BindView(R.id.main_back_image_view)
    ImageView backImageView;

    @BindView(R.id.gallery_btn)
    ImageView galleryBtn;

    @BindView(R.id.web_btn)
    ImageView webBtn;

    @BindView(R.id.gallery_text_view)
    TextView galleryTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        StartAnimation();
    }

    private void StartAnimation() {
        double time=1500;
        CountDownTimer countDownTimer=new CountDownTimer((int)time,1) {
            ColorDrawable colorDrawable=new ColorDrawable();
            long stp=0;
            @Override
            public void onTick(long l) {
                stp= (long) (time-l);
                Log.i("SayLong", "onTick: "+stp);

                colorDrawable.setColor(Color.argb((int)
                                        (255*graphicFunc((Math.PI*0.95f)*(stp/(time))))
                                ,0,0,0));
                backImageView.setForeground(colorDrawable);
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
        galleryWebLayout.setScaleX(3f);
        galleryWebLayout.setScaleY(3f);
        galleryWebLayout.setAlpha(0);
        galleryWebLayout.animate().scaleY(1).scaleX(1).alpha(1).setDuration(900).start();
    }

    private double graphicFunc(double x) {
        return 1-(Math.sin(x) * Math.exp(x)) /
                (Math.sin(3 * Math.PI / 4) * Math.exp(3 * Math.PI / 4));
    }

    public void webPhoto(View view) {
        Intent intent = new Intent(MainActivity.this, WebPhoto.class);
        startActivity(intent);
    }

    public void gallery(View view) {
        Intent intent = new Intent(MainActivity.this, Gallery.class);
        startActivity(intent);

    }

}