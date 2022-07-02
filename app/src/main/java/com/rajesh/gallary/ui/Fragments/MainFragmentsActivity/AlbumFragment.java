package com.rajesh.gallary.ui.Fragments.MainFragmentsActivity;

import static com.rajesh.gallary.common.Constant.ADD_FAV_SELECTED;
import static com.rajesh.gallary.common.Constant.ALBUM_DATA;
import static com.rajesh.gallary.common.Constant.ALBUM_GRID_COUNT;

import static com.rajesh.gallary.common.Constant.ALBUM_SIZE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.ALBUM_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.DELETE_SELECTED;
import static com.rajesh.gallary.common.Constant.GRID_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.INCREASE_COLOUMN;
import static com.rajesh.gallary.common.Constant.LIST_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.MOVE_TO_VAULT;
import static com.rajesh.gallary.common.Constant.NAME_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.REDUCE_COLOUMN;
import static com.rajesh.gallary.common.Constant.SELECT_ALL;
import static com.rajesh.gallary.common.Constant.SHARE_SELECTED;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rajesh.gallary.ui.Adapter.AlbumsAdapter;
import com.rajesh.gallary.databinding.FragmentAlbumBinding;

import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onAlbumClicked;
import com.rajesh.gallary.network.onLongSelected;
import com.rajesh.gallary.ui.Activities.AlbumDisplayActivity;
import com.rajesh.gallary.ui.BottomSheetss.DeleteBottomSheet;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.AdmobHelper;
import com.rajesh.gallary.utils.DataFilterHelper;
import com.rajesh.gallary.utils.SavedData;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AlbumFragment extends Fragment implements onAlbumClicked<String>, onLongSelected {

    private List<AlbumsAndMedia> albumsAndMedia = new ArrayList<>();

    private FragmentAlbumBinding binding;
    private MainViewModel viewModel;

    private AlbumsAdapter adapter;
    @Inject
    DataFilterHelper dataFilterHelper;

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
        adapter = new AlbumsAdapter(getActivity());
        binding.albumRecycler.setHasFixedSize(true);
        binding.albumRecycler.setAdapter(adapter);
        adapter.setAlbumClicked(this);
        adapter.setOnLongSelected(this);


        if (savedData.getViewType(ALBUM_VIEW_TYPE) == 0) {
            binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), savedData.getGridCount(ALBUM_GRID_COUNT)));
            adapter.setGridCount(savedData.getGridCount(ALBUM_GRID_COUNT));
        } else {
            binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            adapter.setGridCount(savedData.getGridCount(ALBUM_GRID_COUNT));
        }
    }

    private void SetupData() {
        viewModel.initializeAlbumsData();
        viewModel.getAlbums().observe(getViewLifecycleOwner(), albumResponse -> {
            albumsAndMedia.clear();
            adapter.setAlbums(albumResponse);
            albumsAndMedia.addAll(albumResponse);
        });

        //Menu Operations Handler
        viewModel.getAlbumCommunicatorData().observe(getViewLifecycleOwner(), s -> {
            switch (s) {
                case LIST_VIEW_TYPE:
                    //show toast and let User choose between list or grid
                    binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                    savedData.setViewType(1, ALBUM_VIEW_TYPE);
                    break;
                case GRID_VIEW_TYPE:
                    binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), savedData.getGridCount(ALBUM_GRID_COUNT)));
                    savedData.setViewType(0, ALBUM_VIEW_TYPE);
                    break;
                case REDUCE_COLOUMN:
                    // Get the saved preference data of Coloumn size and reduce it by one
                    if (savedData.getGridCount(ALBUM_GRID_COUNT) != 1) {
                        savedData.SetGridCount(savedData.getGridCount(ALBUM_GRID_COUNT) - 1, ALBUM_GRID_COUNT);
                        adapter.setGridCount(savedData.getGridCount(ALBUM_GRID_COUNT));
                        binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), savedData.getGridCount(ALBUM_GRID_COUNT)));
                    }
                    break;
                case INCREASE_COLOUMN:
                    // Get the saved preference data of Coloumn size and increase it by one
                    savedData.SetGridCount(savedData.getGridCount(ALBUM_GRID_COUNT) + 1, ALBUM_GRID_COUNT);
                    adapter.setGridCount(savedData.getGridCount(ALBUM_GRID_COUNT));
                    binding.albumRecycler.setLayoutManager(new GridLayoutManager(getActivity(), savedData.getGridCount(ALBUM_GRID_COUNT)));
                    break;
                case ALBUM_SIZE_MEDIA_FILTER:
                    //filter data with album name
                    adapter.setAlbums(dataFilterHelper.QuickSortAlbumsBySize(albumsAndMedia));
                    break;
                case NAME_MEDIA_FILTER:
                    adapter.sortByName();
                    break;
            }
        });
    }

    @Inject
    AdmobHelper admobHelper;

    @Override
    public void onAlbumClickedListener(String id) {
        Intent intent = new Intent(getActivity(), AlbumDisplayActivity.class);
        intent.putExtra(ALBUM_DATA, id);
        startActivity(intent);
        if (admobHelper.initializeFullScreenAds() != null) {
            admobHelper.initializeFullScreenAds().show(getActivity());
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    @Override
    public void longClicked(boolean isEnable, boolean isSelected, int Operation) {
        if (Operation == SELECT_ALL) {
            SelectAllItems(isSelected);
        } else if (Operation == DELETE_SELECTED) {
            DisplayDeleteBottomSheet();
        } else if (Operation == SHARE_SELECTED) {
            ShareMedia();
        } else if (Operation == ADD_FAV_SELECTED) {
            AddToFavourites();
        } else if (Operation == MOVE_TO_VAULT) {
            viewModel.AddToVault(GetSelectedItems());
            Toast.makeText(getContext(), "Added To Vault", Toast.LENGTH_SHORT).show();
        } else {
            // remove selection from all items
            SelectAllItems(false);
        }
    }

    private void SelectAllItems(boolean isSelected) {
        List<AlbumsAndMedia> result = new ArrayList<>();
        for (AlbumsAndMedia albumsAndMedia : albumsAndMedia) {
            albumsAndMedia.albums.setSelected(isSelected);
            result.add(albumsAndMedia);
        }
        adapter.setAlbums(result);
    }

    @Override
    public void OnChildSelected(List<mediaModel> data) {

    }

    private void ShareMedia() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.setType("*/*");
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, SelectedUri(shareIntent));
        startActivity(Intent.createChooser(shareIntent, null));
    }

    private ArrayList<Uri> SelectedUri(Intent intent) {
        ArrayList<Uri> uris = new ArrayList<>();
        for (mediaModel data : GetSelectedItems()) {
            Uri uri = FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider", new File(data.getMediaPath()));
            uris.add(uri);
            @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> resInfoList = getActivity().getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                getActivity().grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        return uris;
    }

    public void AddToFavourites() {
        for (mediaModel mediaModel : GetSelectedItems()) {
            viewModel.AddToFav(mediaModel.getMediaPath(), true);
        }
    }

    /**
     * Display Delete Bottom Sheet
     */
    public void DisplayDeleteBottomSheet() {
        // show the bottom sheet
        DeleteBottomSheet frag = new DeleteBottomSheet(adapter.getAlbums(), true);
        frag.show(getActivity().getSupportFragmentManager(), "Delete");


    }


    private List<mediaModel> GetSelectedItems() {
        List<mediaModel> result = new ArrayList<>();
        for (AlbumsAndMedia albums : adapter.getAlbums()) {
            for (mediaModel mediaModel : albums.mediaModelList) {
                if (albums.albums.isSelected())
                    result.add(mediaModel);
            }
        }
        return result;
    }
}