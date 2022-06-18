package com.rajesh.gallary.ui.Activities;

import static com.rajesh.gallary.common.Constant.ALBUM_DATA;
import static com.rajesh.gallary.common.Constant.ALL_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.DATA;
import static com.rajesh.gallary.common.Constant.GRID_COUNT;
import static com.rajesh.gallary.common.Constant.GRID_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.IMAGE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.INCREASE_COLOUMN;
import static com.rajesh.gallary.common.Constant.LIST_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.OLD_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.REDUCE_COLOUMN;
import static com.rajesh.gallary.common.Constant.SHARED_P_NAME;
import static com.rajesh.gallary.common.Constant.SIZE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.THEME;
import static com.rajesh.gallary.common.Constant.VIDEO_MEDIA_FILTER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.DataFilterHelper;
import com.rajesh.gallary.utils.SavedData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlbumDisplayActivity extends AppCompatActivity implements onItemClickListener<mediaModel>, MenuItem.OnMenuItemClickListener {
    private ActivityAlbumDisplayBinding binding;
    private MediaAdapter adapter;
    private MainViewModel viewModel;
    private String albumId;
    @Inject
    SavedData savedData;

    @Inject
    DataFilterHelper dataFilterHelper;
    private String ViewType = GRID_VIEW_TYPE;
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
        adapter = new MediaAdapter(this);
        binding.albumRecycler.setAdapter(adapter);
        binding.albumRecycler.setLayoutManager(new GridLayoutManager(this, savedData.getGridCount(GRID_COUNT)));

    }

    private void SetUpData() {
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        albumId = getIntent().getStringExtra(ALBUM_DATA);
        viewModel.InitializeMediaByAlbumData(albumId);
        viewModel.getMediaByAlbum().observe(this, mediaByAlbumResponse -> {
            adapter.setMediaModelList(mediaByAlbumResponse);
            adapter.setGridCount(savedData.getGridCount(GRID_COUNT));
            adapter.setViewType(savedData.getViewType(GRID_COUNT));
            mediaModelList.addAll(mediaByAlbumResponse);
        });
        adapter.setOnItemClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClickedItem(mediaModel itemData) {
        Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
        intent.putExtra(ALBUM_DATA, itemData.getAlbumID());
        intent.putExtra(DATA, itemData);
        Log.d("TAG", "SetUpData: " + itemData.getAlbumID());
        startActivity(intent);
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
                ViewType = LIST_VIEW_TYPE;
                SetMenuItemVisible(menu, false);

                return true;
            case R.id.GridView:
                // change view type grid or list of items
                adapter.setViewType(0);
                binding.albumRecycler.setLayoutManager(new GridLayoutManager(this, savedData.getGridCount(GRID_COUNT)));
                ViewType = GRID_VIEW_TYPE;
                SetMenuItemVisible(menu, true);
                return true;
            case R.id.reduceColoumn:
                //Reduce coloumn size
                viewModel.setMenuOperation(REDUCE_COLOUMN);
                if (savedData.getGridCount(GRID_COUNT) != 1) {
                    savedData.SetGridCount(savedData.getGridCount(GRID_COUNT) - 1, GRID_COUNT);
                    binding.albumRecycler.setLayoutManager(new GridLayoutManager(this, savedData.getGridCount(GRID_COUNT)));
                    adapter.setGridCount(savedData.getGridCount(GRID_COUNT));
                    adapter.notifyDataSetChanged();

                }
                return true;
            case R.id.increaseColoumn:
                //increase coloumn size
                viewModel.setMenuOperation(INCREASE_COLOUMN);
                savedData.SetGridCount(savedData.getGridCount(GRID_COUNT) + 1, GRID_COUNT);
                binding.albumRecycler.setLayoutManager(new GridLayoutManager(this, savedData.getGridCount(GRID_COUNT)));
                adapter.setGridCount(savedData.getGridCount(GRID_COUNT));
                adapter.notifyDataSetChanged();
                return true;
            case R.id.share_app:
                //share app with friends
                return true;
            case R.id.rate_us:
                //rate us on google play
                return true;
            case R.id.settings:
                //go to settings
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
}