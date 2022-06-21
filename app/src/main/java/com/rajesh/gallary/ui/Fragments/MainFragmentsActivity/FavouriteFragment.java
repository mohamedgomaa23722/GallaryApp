package com.rajesh.gallary.ui.Fragments.MainFragmentsActivity;

import static com.rajesh.gallary.common.Constant.ADD_FAV_SELECTED;
import static com.rajesh.gallary.common.Constant.ALBUM_DATA;
import static com.rajesh.gallary.common.Constant.ALL_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.COPY_SELECTED;
import static com.rajesh.gallary.common.Constant.DATA;
import static com.rajesh.gallary.common.Constant.DELETE_SELECTED;
import static com.rajesh.gallary.common.Constant.FAV_GRID_COUNT;
import static com.rajesh.gallary.common.Constant.FAV_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.GRID_COUNT;
import static com.rajesh.gallary.common.Constant.GRID_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.IMAGE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.INCREASE_COLOUMN;
import static com.rajesh.gallary.common.Constant.LIST_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.MOVE_TO_VAULT;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.OLD_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.REDUCE_COLOUMN;
import static com.rajesh.gallary.common.Constant.SELECT_ALL;
import static com.rajesh.gallary.common.Constant.SHARE_SELECTED;
import static com.rajesh.gallary.common.Constant.VIDEO_MEDIA_FILTER;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rajesh.gallary.Adapter.allPicsAndVideos.MediaAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.FragmentFavouriteBinding;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.network.onLongSelected;
import com.rajesh.gallary.ui.Activities.AlbumDisplayActivity;
import com.rajesh.gallary.ui.Activities.DisplayActivity;
import com.rajesh.gallary.ui.BottomSheetss.CopyBottomSheet;
import com.rajesh.gallary.ui.BottomSheetss.DeleteBottomSheet;
import com.rajesh.gallary.ui.viewModels.FavViewModel;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.AdmobHelper;
import com.rajesh.gallary.utils.DataFilterHelper;
import com.rajesh.gallary.utils.SavedData;
import com.rajesh.gallary.utils.ShareAndRateHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteFragment extends Fragment implements onItemClickListener<mediaModel>, onLongSelected {

    private FragmentFavouriteBinding binding;
    private MainViewModel viewModel;
    private MediaAdapter adapter;
    private String ViewType = GRID_VIEW_TYPE;

    @Inject
    SavedData savedData;
    @Inject
    DataFilterHelper dataFilterHelper;
    List<mediaModel> mediaModelList = new ArrayList<>();

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
        SetUpViews();
        SetupData();
    }

    private void SetUpViews() {
        //Recycler view Tool
        binding.FavRecycler.setHasFixedSize(true);
        adapter = new MediaAdapter(getActivity());
        binding.FavRecycler.setAdapter(adapter);
        binding.FavRecycler.setLayoutManager(new GridLayoutManager(getContext(), savedData.getGridCount(FAV_GRID_COUNT)));
        adapter.setFAv(true);
    }


    private void SetupData() {
        viewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        viewModel.InitializeFavMedia();
        adapter.setOnLongSelected(this);
        viewModel.getFavMedia().observe(getViewLifecycleOwner(), FavResponse -> {
            if (!CheckData(FavResponse.isEmpty())) {
                mediaModelList.clear();
                adapter.setMediaModelList(FavResponse);
                adapter.setGridCount(savedData.getGridCount(FAV_GRID_COUNT));
                adapter.setViewType(savedData.getViewType(FAV_VIEW_TYPE));
                mediaModelList.addAll(FavResponse);
            }
        });
        adapter.setOnItemClickListener(this);

        //Menu Operations Handler
        viewModel.getFavouriteCommunicatorData().observe(getViewLifecycleOwner(), s -> {
            switch (s) {
                case LIST_VIEW_TYPE:
                    // change view type grid or list of items
                    binding.FavRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    //make increase and decrease items Invisible
                    adapter.setViewType(1);
                    savedData.setViewType(1, FAV_VIEW_TYPE);
                    break;
                case GRID_VIEW_TYPE:
                    // change view type grid or list of items
                    adapter.setViewType(0);
                    binding.FavRecycler.setLayoutManager(new GridLayoutManager(getContext(), savedData.getGridCount(FAV_GRID_COUNT)));
                    savedData.setViewType(0, FAV_VIEW_TYPE);
                    break;
                case REDUCE_COLOUMN:
                    //Reduce coloumn size
                    if (savedData.getGridCount(FAV_GRID_COUNT) != 1) {
                        savedData.SetGridCount(savedData.getGridCount(FAV_GRID_COUNT) - 1, FAV_GRID_COUNT);
                        binding.FavRecycler.setLayoutManager(new GridLayoutManager(getContext(), savedData.getGridCount(FAV_GRID_COUNT)));
                        adapter.setGridCount(savedData.getGridCount(FAV_GRID_COUNT));
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case INCREASE_COLOUMN:
                    //increase coloumn size
                    savedData.SetGridCount(savedData.getGridCount(FAV_GRID_COUNT) + 1, FAV_GRID_COUNT);
                    binding.FavRecycler.setLayoutManager(new GridLayoutManager(getContext(), savedData.getGridCount(FAV_GRID_COUNT)));
                    adapter.setGridCount(savedData.getGridCount(FAV_GRID_COUNT));
                    adapter.notifyDataSetChanged();

                    break;
                case NEW_FILTER_DATE:
                    // Get the array and pass it to adapter to filter it via comparable by new date
                    adapter.FilterMediaModelList(dataFilterHelper.SortMediaByDate(mediaModelList, NEW_FILTER_DATE));
                    break;
                case OLD_FILTER_DATE:
                    // Get the array and pass it to adapter to filter it via comparable by old date
                    adapter.FilterMediaModelList(dataFilterHelper.SortMediaByDate(mediaModelList, OLD_FILTER_DATE));
                    break;
                case ALL_MEDIA_FILTER:
                    // Set original data to adapter contains all medias
                    adapter.FilterMediaModelList(dataFilterHelper.FilterMediaData(mediaModelList, ALL_MEDIA_FILTER));
                    break;
                case IMAGE_MEDIA_FILTER:
                    //filter data with getting only image resources
                    adapter.FilterMediaModelList(dataFilterHelper.FilterMediaData(mediaModelList, IMAGE_MEDIA_FILTER));
                    break;
                case VIDEO_MEDIA_FILTER:
                    //filter data with getting only video resources
                    adapter.FilterMediaModelList(dataFilterHelper.FilterMediaData(mediaModelList, VIDEO_MEDIA_FILTER));
                    break;
            }
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

    @Inject
    AdmobHelper admobHelper;

    @Override
    public void onClickedItem(mediaModel itemData) {
        Intent intent = new Intent(getContext(), DisplayActivity.class);
        intent.putExtra(DATA, itemData);
        intent.putExtra("FAV", true);
        startActivity(intent);
        if (admobHelper.initializeFullScreenAds() != null) {
            admobHelper.initializeFullScreenAds().show(getActivity());
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    @Override
    public void onLongTouch(mediaModel itemData) {

    }

    @Override
    public void longClicked(boolean isEnable, boolean isSelected, int Operation) {
        if (Operation == SELECT_ALL) {
            SelectAllItems(isSelected);
        } else if (Operation == DELETE_SELECTED) {
            DisplayDeleteBottomSheet();
        } else if (Operation == SHARE_SELECTED) {
            ShareMedia();
        } else if (Operation == COPY_SELECTED) {
            DisplayCopyBottomSheet();
        } else if (Operation == ADD_FAV_SELECTED) {
            mediaModelList.clear();
            RemoveFromFav();
        } else if (Operation == MOVE_TO_VAULT) {
            mediaModelList.clear();
            viewModel.AddToVault(GetSelectedItems());
            Toast.makeText(getContext(), "Added To Vault", Toast.LENGTH_SHORT).show();
        } else {
            // remove selection from all items
            SelectAllItems(false);
        }
    }

    private void SelectAllItems(boolean isSelected) {
        List<mediaModel> result = new ArrayList<>();
        for (mediaModel mediaModel : mediaModelList) {
            mediaModel.setSelected(isSelected);
            result.add(mediaModel);
        }
        adapter.setMediaModelList(result);
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

    public void RemoveFromFav() {
        for (mediaModel mediaModel : GetSelectedItems()) {
            viewModel.AddToFav(mediaModel.getMediaPath(), false);
        }
    }

    /**
     * Display Delete Bottom Sheet
     */
    public void DisplayDeleteBottomSheet() {
        // show the bottom sheet
        DeleteBottomSheet frag = new DeleteBottomSheet(GetSelectedItems());
        frag.show(getActivity().getSupportFragmentManager(), "Delete");
    }

    /**
     * Display Copy Bottom Sheet
     */
    public void DisplayCopyBottomSheet() {
        //Initialize Bottom Sheet
        CopyBottomSheet frag = new CopyBottomSheet(GetSelectedItems());
        frag.show(getActivity().getSupportFragmentManager(), "Copy");
    }

    private List<mediaModel> GetSelectedItems() {
        List<mediaModel> result = new ArrayList<>();
        for (mediaModel data : adapter.getMediaModelList()) {
            if (data.getSelected())
                result.add(data);
        }
        return result;
    }
}