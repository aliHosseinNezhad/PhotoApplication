package com.example.photoapplication.WebPhoto.Download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.CountDownTimer;

import com.example.photoapplication.Utils.OnResultCallBack.OnErrorCallBack;
import com.example.photoapplication.Utils.OnResultCallBack.OnFinishCallBack;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, Bitmap> {
    OnFinishCallBack onFinishCallBack;
    OnErrorCallBack onErrorCallBack;
    CountDownTimer countDownTimer;
    public DownloadTask(OnFinishCallBack onFinishCallBack, OnErrorCallBack onErrorCallBack){
        this.onFinishCallBack=onFinishCallBack;
        this.onErrorCallBack = onErrorCallBack;
        countDownTimer=new CountDownTimer(20000,10000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                onErrorCallBack.onTimeOut();
            }
        };
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // When all async task done
    protected void onPostExecute(Bitmap result) {
        onFinishCallBack.onFinish(result);
    }
}
