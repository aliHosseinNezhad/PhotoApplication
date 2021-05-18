package com.example.photoapplication.Gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;

import com.example.photoapplication.Utils.OnResultCallBack.JobFinished;

import java.io.File;
import java.io.FileOutputStream;

public class RotationTask extends AsyncTask<Void, Void, Boolean> {
    Bitmap bitmap = null;
    String path;
    JobFinished jobFinished;
    Context context;

    public RotationTask(String path,Context context,JobFinished jobFinished) {
        this.path = path;
        this.context=context;
        this.jobFinished=jobFinished;
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        bitmap = BitmapFactory.decodeFile(path);
        bitmap = rotateBitmap(bitmap, 90);
        String format = path.substring(path.lastIndexOf(".") + 1);

        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //refreshMediaStore(path);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean finish) {
        jobFinished.jobFinished(finish,null);
    }
    private void refreshMediaStore(String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
    }
}
