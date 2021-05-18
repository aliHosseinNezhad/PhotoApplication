package com.example.photoapplication.Gallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.example.photoapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class Gallery extends AppCompatActivity {
    private static final int WRITE_EXTERNAL_STORAGE = 1253;

    RecyclerView recyclerView;


    @BindView(R.id.selected_item_count_text)
    TextView selectedItemCountTxt;
    @BindView(R.id.floating_delete_button)
    FloatingActionButton floatingDeleteBtn;

    boolean[] menuGroupVisibility = {true, false};
    PhotoAdapter adapter;
//    @BindView(R.id.fast_scroller)
//    VerticalRecyclerViewFastScroller scroller;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.gallery);
        }
        setSupportActionBar(toolbar);


        recyclerView=findViewById(R.id.gallery_recycler_view);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startFunction();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startFunction();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        menu.setGroupVisible(R.id.gallery_photo_adapter_default_group, menuGroupVisibility[0]);
        menu.setGroupVisible(R.id.gallery_photo_adapter_selection_group, menuGroupVisibility[1]);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.gallery_select_all) {
            adapter.selectAll();
        } else if (item.getItemId() == R.id.gallery_photo_adapter_start_selection) {
            adapter.startSelectionDesign();
        } else if (item.getItemId() == R.id.gallery_delete) {
            adapter.deleteSelectedItems();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFunction();
            }
        }
    }

    private void startFunction(){
        visibleDefaultMenuItems();
        getImageFiles();
        showImageList();
    }

    private void showImageList() {
        adapter = new PhotoAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
    }


    private void getImageFiles() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            PhotoData.photoList.clear();
            PhotoData.photoList=new ArrayList<>();
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;
//Stores all the images from the gallery in Cursor
            Cursor cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                    null, orderBy);
//Total number of images
            int count = cursor.getCount();

//Create an array to store path to all the images
//            String[] arrPath = new String[count];
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                //Store the path of the image


                PhotoData photoData = new PhotoData();
                photoData.path = cursor.getString(dataColumnIndex);
                PhotoData.photoList.add(photoData);
            }
// The cursor should be freed up after use with close()
            cursor.close();
            Collections.reverse(PhotoData.photoList);
        } else {
            Toast.makeText(this, "sd card not mounted", Toast.LENGTH_SHORT).show();
        }
    }


    public void visibleDefaultMenuItems() {
        menuGroupVisibility[0] = true;
        menuGroupVisibility[1] = false;
        invalidateOptionsMenu();
    }

    public void visibleSelectionMenuItems() {
        menuGroupVisibility[0] = false;
        menuGroupVisibility[1] = true;
        invalidateOptionsMenu();
    }
}