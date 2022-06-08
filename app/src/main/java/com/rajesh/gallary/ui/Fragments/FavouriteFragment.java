package com.rajesh.gallary.ui.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajesh.gallary.Adapter.allPicsAndVideos.MediaAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentFavouriteBinding;
import com.rajesh.gallary.ui.viewModels.FavViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteFragment extends Fragment {

    private FragmentFavouriteBinding binding;
    private FavViewModel viewModel;
    private MediaAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FavViewModel.class);
        SetupView();
        SetupData();
    }

    private void SetupView() {
        adapter = new MediaAdapter(new ArrayList<>(), getContext());
        binding.FavRecycler.setAdapter(adapter);
        binding.FavRecycler.setHasFixedSize(true);

    }

    private void SetupData() {
        viewModel.InitializeFavMedia();
        viewModel.getFavMedia().observe(getViewLifecycleOwner(), FavResponse -> {
           if(!CheckData(FavResponse.isEmpty()))
            adapter.setMediaModelList(FavResponse);
        });
    }

    public boolean CheckData(boolean isEmpty) {
        if (isEmpty) {
            binding.FavRecycler.setVisibility(View.INVISIBLE);
            binding.emptyText.setVisibility(View.VISIBLE);
        } else {
            binding.FavRecycler.setVisibility(View.VISIBLE);
            binding.emptyText.setVisibility(View.INVISIBLE);
        }
        return isEmpty;
    }
}