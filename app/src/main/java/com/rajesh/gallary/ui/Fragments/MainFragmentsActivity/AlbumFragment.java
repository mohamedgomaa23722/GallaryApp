package com.rajesh.gallary.ui.Fragments.MainFragmentsActivity;

import static com.rajesh.gallary.common.Constant.ALBUM_DATA;
import static com.rajesh.gallary.common.Constant.ALBUM_GRID_COUNT;
import static com.rajesh.gallary.common.Constant.ALBUM_GRID_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.ALBUM_INCREASE_COLOUMN;
import static com.rajesh.gallary.common.Constant.ALBUM_LIST_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.ALBUM_REDUCE_COLOUMN;
import static com.rajesh.gallary.common.Constant.ALBUM_SIZE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.ALBUM_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.NAME_MEDIA_FILTER;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rajesh.gallary.Adapter.AlbumsAdapter;
import com.rajesh.gallary.databinding.FragmentAlbumBinding;

import com.rajesh.gallary.network.onAlbumClicked;
import com.rajesh.gallary.ui.Activities.AlbumDisplayActivity;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.SavedData;



import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AlbumFragment extends Fragment implements onAlbumClicked<String> {

    private FragmentAlbumBinding binding;
    private MainViewModel viewModel;

    @Inject
    AlbumsAdapter adapter;

    @Inject
    SavedData savedData;

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
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        setupView();
        SetupData();
    }

    private void setupView() {
        binding.albumRecycler.setHasFixedSize(true);
        binding.albumRecycler.setAdapter(adapter);
        adapter.setAlbumClicked(this);



        if (savedData.getViewType(ALBUM_VIEW_TYPE) == 0){
            binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), savedData.getGridCount(ALBUM_GRID_COUNT)));
            adapter.setGridCount(savedData.getGridCount(ALBUM_GRID_COUNT));
        }else{
            binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(),1));
            adapter.setGridCount(savedData.getGridCount(ALBUM_GRID_COUNT));
        }
    }

    private void SetupData() {
        viewModel.initializeAlbumsData();
        viewModel.getAlbums().observe(getViewLifecycleOwner(), albumResponse -> {
            adapter.setAlbums(albumResponse);
        });

        //Menu Operations Handler
        viewModel.getMenuOperation().observe(getViewLifecycleOwner(), s -> {
            switch (s) {
                case ALBUM_LIST_VIEW_TYPE:
                    //show toast and let User choose between list or grid
                    binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(),1));
                    savedData.setViewType(1,ALBUM_VIEW_TYPE);
                    Toast.makeText(getActivity(), "ALBUM_LIST_VIEW_TYPE", Toast.LENGTH_SHORT).show();
                    break;
                case ALBUM_GRID_VIEW_TYPE:
                    binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), savedData.getGridCount(ALBUM_GRID_COUNT)));
                    savedData.setViewType(0,ALBUM_VIEW_TYPE);
                    Toast.makeText(getActivity(), "ALBUM_GRID_VIEW_TYPE", Toast.LENGTH_SHORT).show();
                    break;
                case ALBUM_REDUCE_COLOUMN:
                    // Get the saved preference data of Coloumn size and reduce it by one
                    if (savedData.getGridCount(ALBUM_GRID_COUNT) != 1) {
                        savedData.SetGridCount(savedData.getGridCount(ALBUM_GRID_COUNT) - 1,ALBUM_GRID_COUNT);
                        adapter.setGridCount(savedData.getGridCount(ALBUM_GRID_COUNT));
                        binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), savedData.getGridCount(ALBUM_GRID_COUNT)));
                    }

                    break;
                case ALBUM_INCREASE_COLOUMN:
                    // Get the saved preference data of Coloumn size and increase it by one
                    savedData.SetGridCount(savedData.getGridCount(ALBUM_GRID_COUNT) + 1,ALBUM_GRID_COUNT);
                    adapter.setGridCount(savedData.getGridCount(ALBUM_GRID_COUNT));
                    binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), savedData.getGridCount(ALBUM_GRID_COUNT)));
                    break;
                case ALBUM_SIZE_MEDIA_FILTER:
                    //filter data with album name
                     adapter.SortBySize();
                    break;
                case NAME_MEDIA_FILTER:
                    adapter.sortByName();
                    break;
            }
        });
    }


    @Override
    public void onAlbumClickedListener(String id) {
        Intent intent = new Intent(getActivity(), AlbumDisplayActivity.class);
        intent.putExtra(ALBUM_DATA, id);
        startActivity(intent);
    }
}