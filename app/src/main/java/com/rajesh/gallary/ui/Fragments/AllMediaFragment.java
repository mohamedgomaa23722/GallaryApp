package com.rajesh.gallary.ui.Fragments;

import static com.rajesh.gallary.common.Constant.ALL_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.DATA;
import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.GRID_COUNT;
import static com.rajesh.gallary.common.Constant.GRID_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.IMAGE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.INCREASE_COLOUMN;
import static com.rajesh.gallary.common.Constant.LIST_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.OLD_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.REDUCE_COLOUMN;
import static com.rajesh.gallary.common.Constant.SIZE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.VIDEO_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;
import static com.rajesh.gallary.common.Constant.VIEW_TYPE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rajesh.gallary.Adapter.allPicsAndVideos.DateAndMediaAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentAllMediaBinding;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.ui.Activities.DisplayActivity;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.DataFilterHelper;
import com.rajesh.gallary.utils.SavedData;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AllMediaFragment extends Fragment implements onItemClickListener<mediaModel>, SwipeRefreshLayout.OnRefreshListener {
    private FragmentAllMediaBinding binding;
    private MainViewModel viewModel;
    @Inject
    DateAndMediaAdapter adapter;
    @Inject
    SavedData savedData;
    @Inject
    DataFilterHelper dataFilterHelper;
    private RecyclerView.RecycledViewPool viewPool;
    private long LastDate;
    private List<DateAndMedia> dateAndMediaList = new ArrayList<>();

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

        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        SetupView();
        ObserveData();

        Toast.makeText(getActivity(), ""+savedData.getLastDate(), Toast.LENGTH_SHORT).show();
    }

    private void SetupView() {
        binding.mediaList.setHasFixedSize(true);
        viewPool = new RecyclerView.RecycledViewPool();
        binding.mediaList.setAdapter(adapter);
        binding.mediaList.setVisibility(View.INVISIBLE);
        adapter.setOnItemClickListener(this);
        binding.swiperefresh.setOnRefreshListener(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void ObserveData() {
        viewModel.getAllMediaItems().observe(getViewLifecycleOwner(), MediaResponse -> {
            dateAndMediaList.addAll(MediaResponse);
            //Initialize Save ui data preference
            adapter.setGridCount(savedData.getGridCount(GRID_COUNT));
            adapter.setViewType(savedData.getViewType(VIEW_TYPE));
            //Create View pool
            viewPool.setMaxRecycledViews(R.layout.parent_media_item, MediaResponse.size());
            binding.mediaList.setRecycledViewPool(viewPool);
            binding.mediaList.setItemViewCacheSize(MediaResponse.size());
            adapter.setMediaAndDateList(MediaResponse);
            //Display the Views
            binding.progress.setVisibility(View.INVISIBLE);
            binding.mediaList.setVisibility(View.VISIBLE);
        });


        //Menu Operations Handler
        viewModel.getMenuOperation().observe(getViewLifecycleOwner(), s -> {
            switch (s) {
                case LIST_VIEW_TYPE:
                    //show toast and let User choose between list or grid
                    adapter.setViewType(1);
                    savedData.setViewType(1,VIEW_TYPE);
                    break;
                case GRID_VIEW_TYPE:
                    adapter.setViewType(0);
                    savedData.setViewType(0,VIEW_TYPE);
                    break;
                case REDUCE_COLOUMN:
                    // Get the saved preference data of Coloumn size and reduce it by one
                    if (savedData.getGridCount(GRID_COUNT) != 1) {
                        savedData.SetGridCount(savedData.getGridCount(GRID_COUNT) - 1,GRID_COUNT);
                        adapter.setGridCount(savedData.getGridCount(GRID_COUNT));
                    }

                    break;
                case INCREASE_COLOUMN:
                    // Get the saved preference data of Coloumn size and increase it by one
                    savedData.SetGridCount(savedData.getGridCount(GRID_COUNT) + 1,GRID_COUNT);
                    adapter.setGridCount(savedData.getGridCount(GRID_COUNT));

                    break;
                case NEW_FILTER_DATE:
                    // Get the array and pass it to adapter to filter it via comparable by new date
                    adapter.setMediaAndDateList(dataFilterHelper.SortDataByDate(dateAndMediaList, NEW_FILTER_DATE));
                    break;
                case OLD_FILTER_DATE:
                    // Get the array and pass it to adapter to filter it via comparable by old date
                    adapter.setMediaAndDateList(dataFilterHelper.SortDataByDate(dateAndMediaList, OLD_FILTER_DATE));
                    break;
                case ALL_MEDIA_FILTER:
                    // Set original data to adapter contains all medias
                    adapter.setMediaAndDateList(dataFilterHelper.FilterMediaAndDate(dateAndMediaList, ALL_MEDIA_FILTER));
                    break;
                case IMAGE_MEDIA_FILTER:
                    //filter data with getting only image resources
                    adapter.setMediaAndDateList(dataFilterHelper.FilterMediaAndDate(dateAndMediaList, IMAGE_MEDIA_FILTER));
                    break;
                case VIDEO_MEDIA_FILTER:
                    //filter data with getting only video resources
                    adapter.setMediaAndDateList(dataFilterHelper.FilterMediaAndDate(dateAndMediaList, VIDEO_MEDIA_FILTER));
                    break;
                case SIZE_MEDIA_FILTER:
                    //filter data with media size
                    break;
            }
        });

    }

    @Override
    public void onClickedItem(mediaModel itemData) {
        Intent intent = new Intent(getContext(), DisplayActivity.class);
        intent.putExtra(DATA, itemData);
        startActivity(intent);
    }

    @Override
    public void onLongTouch(mediaModel itemData) {

    }

    @Override
    public void onRefresh() {
        viewModel.RefreshData();
        binding.swiperefresh.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.RefreshData();
    }
}

