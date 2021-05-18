package com.example.photoapplication.WebPhoto.WebPhotosSection;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebPhotosFragment extends Fragment {
    @BindView(R.id.web_recycler_view)
    RecyclerView recyclerView;

    LinearLayoutManager manager;
    WebPhotosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_photo_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = new WebPhotosAdapter(5, getActivity(),recyclerView);
        recyclerView.setAdapter(adapter);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }
}
