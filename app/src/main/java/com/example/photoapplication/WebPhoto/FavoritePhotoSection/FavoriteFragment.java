package com.example.photoapplication.WebPhoto.FavoritePhotoSection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photoapplication.R;

public class FavoriteFragment extends Fragment {
    RecyclerView recyclerView;
    FavoritePhotoAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorite_web_photo,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        adapter = new FavoritePhotoAdapter(getContext());
        recyclerView=view.findViewById(R.id.favorite_web_photo_recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    public void addFavoriteItem(String jsonString) {
        adapter.addFavoriteItem(jsonString);
    }
}
