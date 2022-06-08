package com.rajesh.gallary.ui.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rajesh.gallary.Adapter.AlbumsAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentAlbumBinding;
import com.rajesh.gallary.ui.viewModels.AlbumViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AlbumFragment extends Fragment {

    private FragmentAlbumBinding binding;
    private AlbumViewModel viewModel;

    @Inject
    AlbumsAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAlbumBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AlbumViewModel.class);
        setupView();
        SetupData();
    }

    private void setupView() {
        binding.albumRecycler.setHasFixedSize(true);
        binding.albumRecycler.setAdapter(adapter);
    }

    private void SetupData() {
        viewModel.initializeAlbumsData();
        viewModel.getAlbums().observe(getViewLifecycleOwner(), albumResponse -> {
            adapter.setAlbums(albumResponse);
        });
    }
}