package com.rajesh.gallary.ui.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajesh.gallary.Adapter.allPicsAndVideos.DateAndMediaAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentAllMediaBinding;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.ui.Activities.DisplayActivity;
import com.rajesh.gallary.ui.viewModels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AllMediaFragment extends Fragment implements onItemClickListener<mediaModel> {
    private FragmentAllMediaBinding binding;
    private MainViewModel viewModel;
    @Inject
    DateAndMediaAdapter adapter;
    private RecyclerView.RecycledViewPool viewPool;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllMediaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel=new ViewModelProvider(this).get(MainViewModel.class);
        SetupView();
        ObserveData();
    }

    private void SetupView() {
        binding.mediaList.setHasFixedSize(true);
        viewPool = new RecyclerView.RecycledViewPool();
        binding.mediaList.setAdapter(adapter);
        binding.mediaList.setVisibility(View.INVISIBLE);
        adapter.setOnItemClickListener(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void ObserveData() {
        viewModel.getAllMediaItems().observe(getViewLifecycleOwner(), MediaResponse -> {
            viewPool.setMaxRecycledViews(R.layout.parent_media_item,MediaResponse.size());
            binding.mediaList.setRecycledViewPool(viewPool);
            binding.mediaList.setItemViewCacheSize(MediaResponse.size());
            adapter.setMediaAndDateList(MediaResponse);
            Log.d("gogo", "ObserveData: " + MediaResponse.size());
            binding.progress.setVisibility(View.INVISIBLE);
            binding.mediaList.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onClickedItem(mediaModel itemData) {
     if(itemData.isImage()){

     }else{
         Intent intent = new Intent(getContext(), DisplayActivity.class);
         intent.putExtra("path",itemData.getMediaPath());
         startActivity(intent);
     }
    }
}