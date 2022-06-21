package com.rajesh.gallary.ui.Activities;

import static com.rajesh.gallary.common.Constant.ADD_FAV_SELECTED;
import static com.rajesh.gallary.common.Constant.ALBUM_DATA;
import static com.rajesh.gallary.common.Constant.ALL_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.COPY_SELECTED;
import static com.rajesh.gallary.common.Constant.DATA;
import static com.rajesh.gallary.common.Constant.DELETE_SELECTED;
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
import static com.rajesh.gallary.common.Constant.SHARED_P_NAME;
import static com.rajesh.gallary.common.Constant.SHARE_SELECTED;
import static com.rajesh.gallary.common.Constant.SIZE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.THEME;
import static com.rajesh.gallary.common.Constant.VIDEO_MEDIA_FILTER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.rajesh.gallary.Adapter.allPicsAndVideos.MediaAdapter;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivityAlbumDisplayBinding;
import com.rajesh.gallary.databinding.ActivityDisplayBinding;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.network.onItemClickListener;
import com.rajesh.gallary.network.onLongSelected;
import com.rajesh.gallary.ui.BottomSheetss.CopyBottomSheet;
import com.rajesh.gallary.ui.BottomSheetss.DeleteBottomSheet;
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
public class AlbumDisplayActivity extends AppCompatActivity implements onItemClickListener<mediaModel>, MenuItem.OnMenuItemClickListener, onLongSelected {
    private ActivityAlbumDisplayBinding binding;
    private MediaAdapter adapter;
    private MainViewModel viewModel;
    private String albumId;
    @Inject
    SavedData savedData;
    ShareAndRateHelper shareAndRateHelper;
    @Inject
    DataFilterHelper dataFilterHelper;
    private List<mediaModel> mediaModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SetUpViews();
        SetUpData();

    }

    private void SetUpViews() {
        //Setup tool bar
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        binding.topAppBar.setOnMenuItemClickListener(this::onMenuItemClick);
        //Recycler view Tool
        binding.albumRecycler.setHasFixedSize(true);
        adapter = new MediaAdapter(AlbumDisplayActivity.this);
        binding.albumRecycler.setAdapter(adapter);
        binding.albumRecycler.setLayoutManager(new GridLayoutManager(this, savedData.getGridCount(GRID_COUNT)));
    }

    private void SetUpData() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        albumId = getIntent().getStringExtra(ALBUM_DATA);
        viewModel.InitializeMediaByAlbumData(albumId);
        adapter.setOnLongSelected(this);
        viewModel.getMediaByAlbum().observe(this, mediaByAlbumResponse -> {
            mediaModelList.clear();
            adapter.setMediaModelList(mediaByAlbumResponse);
            adapter.setGridCount(savedData.getGridCount(GRID_COUNT));
            adapter.setViewType(savedData.getViewType(GRID_COUNT));
            mediaModelList.addAll(mediaByAlbumResponse);
            if (mediaByAlbumResponse.isEmpty()){
                //Then Delete Album
                viewModel.deleteAlbum(albumId);
                finish();
            }
        });
        adapter.setOnItemClickListener(this);

        shareAndRateHelper = new ShareAndRateHelper(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Inject
    AdmobHelper admobHelper;

    @Override
    public void onClickedItem(mediaModel itemData) {
        Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
        intent.putExtra(ALBUM_DATA, itemData.getAlbumID());
        intent.putExtra(DATA, itemData);
        startActivity(intent);
        if (admobHelper.initializeFullScreenAds() != null) {
            admobHelper.initializeFullScreenAds().show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }

    private void SetMenuItemVisible(Menu menu, boolean visible) {
        menu.findItem(R.id.reduceColoumn).setVisible(visible);
        menu.findItem(R.id.increaseColoumn).setVisible(visible);
    }

    @Override
    public void onLongTouch(mediaModel itemData) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Menu menu = binding.topAppBar.getMenu();
        switch (item.getItemId()) {
            case R.id.listView:
                // change view type grid or list of items
                binding.albumRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                //make increase and decrease items Invisible
                adapter.setViewType(1);
                SetMenuItemVisible(menu, false);

                return true;
            case R.id.GridView:
                // change view type grid or list of items
                adapter.setViewType(0);
                binding.albumRecycler.setLayoutManager(new GridLayoutManager(this, savedData.getGridCount(GRID_COUNT)));
                SetMenuItemVisible(menu, true);
                return true;
            case R.id.reduceColoumn:
                //Reduce coloumn size
                if (savedData.getGridCount(GRID_COUNT) != 1) {
                    savedData.SetGridCount(savedData.getGridCount(GRID_COUNT) - 1, GRID_COUNT);
                    binding.albumRecycler.setLayoutManager(new GridLayoutManager(this, savedData.getGridCount(GRID_COUNT)));
                    adapter.setGridCount(savedData.getGridCount(GRID_COUNT));
                    adapter.notifyDataSetChanged();

                }
                return true;
            case R.id.increaseColoumn:
                //increase coloumn size
                savedData.SetGridCount(savedData.getGridCount(GRID_COUNT) + 1, GRID_COUNT);
                binding.albumRecycler.setLayoutManager(new GridLayoutManager(this, savedData.getGridCount(GRID_COUNT)));
                adapter.setGridCount(savedData.getGridCount(GRID_COUNT));
                adapter.notifyDataSetChanged();
                return true;
            case R.id.share_app:
                //share app with friends
                shareAndRateHelper.ShareApp();
                return true;
            case R.id.rate_us:
                //rate us on google play
                shareAndRateHelper.RateUS();
                return true;
            case R.id.settings:
                //go to settings
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                return true;
            case R.id.newDate:
                //Filter by newest date
                adapter.SortMediaByDate(NEW_FILTER_DATE);
                item.setChecked(true);
                return true;
            case R.id.oldDate:
                //Filter by oldest date
                adapter.SortMediaByDate(OLD_FILTER_DATE);
                item.setChecked(true);
                return true;
            case R.id.allmedia:
                //Filter by get all media files
                viewModel.InitializeMediaByAlbumData(albumId);
                viewModel.getMediaByAlbum().observe(this, mediaByAlbumResponse -> {
                    adapter.setMediaModelList(mediaByAlbumResponse);
                });
                item.setChecked(true);
                return true;
            case R.id.imageOnly:
                //Filter by image only
                viewModel.GetAlbumMediaByType(albumId, true).observe(this, mediaModelList -> {
                    adapter.setMediaModelList(mediaModelList);
                });
                item.setChecked(true);
                return true;
            case R.id.videoOnly:
                //filter by video only
                viewModel.GetAlbumMediaByType(albumId, false).observe(this, mediaModelList -> {
                    adapter.setMediaModelList(mediaModelList);
                });
                item.setChecked(true);
                return true;
            case R.id.size:
                //filter by size
                item.setChecked(true);
                return true;

        }
        return true;
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
            AddToFav();
        } else if (Operation == MOVE_TO_VAULT) {
            viewModel.AddToVault(GetSelectedItems());
            Toast.makeText(this, "Added To Vault", Toast.LENGTH_SHORT).show();
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
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(data.getMediaPath()));
            uris.add(uri);
            @SuppressLint("QueryPermissionsNeeded") List<ResolveInfo> resInfoList = getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        return uris;
    }

    public void AddToFav() {
        for (mediaModel mediaModel : GetSelectedItems()) {
            viewModel.AddToFav(mediaModel.getMediaPath(), true);
        }
    }

    /**
     * Display Delete Bottom Sheet
     */
    public void DisplayDeleteBottomSheet() {
        // show the bottom sheet
        DeleteBottomSheet frag = new DeleteBottomSheet(GetSelectedItems());
        frag.show(getSupportFragmentManager(), "Delete");
    }

    /**
     * Display Copy Bottom Sheet
     */
    public void DisplayCopyBottomSheet() {
        //Initialize Bottom Sheet
        CopyBottomSheet frag = new CopyBottomSheet(GetSelectedItems());
        frag.show(getSupportFragmentManager(), "Copy");
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