package com.example.photoapplication.WebPhoto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.example.photoapplication.R;
import com.example.photoapplication.Utils.StaticData;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebPhoto extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    WebPagerAdapter webPagerAdapter;
    AddFavoriteBroadCast addFavoriteBroadCast = new AddFavoriteBroadCast();
    IntentFilter filter = new IntentFilter(StaticData.AddToFavorite);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_photo);
        ButterKnife.bind(this);

        registerReceiver(addFavoriteBroadCast, filter);
        webPagerAdapter = new WebPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(webPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(addFavoriteBroadCast);
    }

    private class AddFavoriteBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(webPagerAdapter!=null){
                webPagerAdapter.addFavoriteItem(intent.getStringExtra(StaticData.ImageDataKey));
            }
        }
    }
}