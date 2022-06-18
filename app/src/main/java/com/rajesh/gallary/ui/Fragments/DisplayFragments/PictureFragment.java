package com.rajesh.gallary.ui.Fragments.DisplayFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.rajesh.gallary.databinding.FragmentPictureBinding;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.ui.viewModels.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class PictureFragment extends Fragment {
    private static final String TAG = "PictureFragment";
    private mediaModel model;
    private FragmentPictureBinding binding;
    private MainViewModel viewModel;
    private boolean isClicked = false;

    public PictureFragment() {
    }

    public static PictureFragment newInstance(mediaModel model) {
        Bundle args = new Bundle();
        args.putSerializable("Imagepath", model);
        PictureFragment f = new PictureFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPictureBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        model = (mediaModel) getArguments().getSerializable("Imagepath");
        binding.pic.setZoomEnabled(true);
        binding.pic.setMaxScale(10f);
        binding.pic.setImage(ImageSource.uri(model.getMediaPath()));

        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding.pic.setOnClickListener(view1 -> {
            Log.d(TAG, "onClick: Image clicked: " + isClicked);
            viewModel.clickedItem(true);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.selectItem(model);
        Log.d(TAG, "onStart: ImageFragment: is resume");

    }


}