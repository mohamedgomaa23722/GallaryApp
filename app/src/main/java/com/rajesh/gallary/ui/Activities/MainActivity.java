package com.rajesh.gallary.ui.Activities;


import static com.rajesh.gallary.common.Constant.ALBUM_GRID_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.ALBUM_INCREASE_COLOUMN;
import static com.rajesh.gallary.common.Constant.ALBUM_LIST_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.ALBUM_NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.ALBUM_OLD_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.ALBUM_REDUCE_COLOUMN;
import static com.rajesh.gallary.common.Constant.ALBUM_SIZE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.ALL_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.EXTERNAL_IMAGE;
import static com.rajesh.gallary.common.Constant.EXTERNAL_VIDEO;
import static com.rajesh.gallary.common.Constant.GRID_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.IMAGE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.IMAGE_PROJECTION;
import static com.rajesh.gallary.common.Constant.INCREASE_COLOUMN;
import static com.rajesh.gallary.common.Constant.LIST_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.NAME_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.OLD_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.REDUCE_COLOUMN;
import static com.rajesh.gallary.common.Constant.SHARED_P_NAME;
import static com.rajesh.gallary.common.Constant.SIZE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.THEME;
import static com.rajesh.gallary.common.Constant.VIDEO_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.VIDEO_PROJECTION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivityMainBinding;
import com.rajesh.gallary.homeSliderAdapter;
import com.rajesh.gallary.model.Albums;
import com.rajesh.gallary.model.DateOfMedia;
import com.rajesh.gallary.model.mediaModel;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.SavedData;


import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private final String[] tabNames = {"Photos", "Albums", "Fav"};
    private MainViewModel viewModel;

    @Inject
    SavedData savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        homeSliderAdapter adapter = new homeSliderAdapter(this);
        binding.mainNav.setAdapter(adapter);
        new TabLayoutMediator(binding.TabLayout, binding.mainNav, (tab, position) -> tab.setText(tabNames[position])).attach();

        binding.topAppBar.setOnMenuItemClickListener(this::onMenuItemClick);
        binding.topAppBar.setTitle("Photos");

        binding.TabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Menu menu = binding.topAppBar.getMenu();
                binding.topAppBar.setTitle(tab.getText());

                if (tab.getText().equals("Albums")) {
                    menu.findItem(R.id.filterByType).setVisible(false);
                    menu.findItem(R.id.name).setVisible(true);
                    menu.findItem(R.id.size).setVisible(true);
                } else {
                    menu.findItem(R.id.filterByType).setVisible(true);
                    menu.findItem(R.id.name).setVisible(false);
                    menu.findItem(R.id.size).setVisible(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Menu menu = binding.topAppBar.getMenu();
        boolean isFilterVisible = menu.findItem(R.id.filterByType).isVisible();
        Toast.makeText(getApplicationContext(), "" + isFilterVisible, Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case R.id.listView:
                // change view type grid or list of items
                if (isFilterVisible)
                    viewModel.setMenuOperation(LIST_VIEW_TYPE);
                else
                    viewModel.setMenuOperation(ALBUM_LIST_VIEW_TYPE);
                //make increase and decrease items Invisible
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(false);
                return true;
            case R.id.GridView:
                // change view type grid or list of items
                if (isFilterVisible)
                    viewModel.setMenuOperation(GRID_VIEW_TYPE);
                else
                    viewModel.setMenuOperation(ALBUM_GRID_VIEW_TYPE);

                menu.getItem(1).setVisible(true);
                menu.getItem(2).setVisible(true);
                return true;
            case R.id.reduceColoumn:
                //Reduce coloumn size
                if (isFilterVisible)
                    viewModel.setMenuOperation(REDUCE_COLOUMN);
                else
                    viewModel.setMenuOperation(ALBUM_REDUCE_COLOUMN);
                return true;
            case R.id.increaseColoumn:
                //increase coloumn size
                if (isFilterVisible)
                    viewModel.setMenuOperation(INCREASE_COLOUMN);
                else
                    viewModel.setMenuOperation(ALBUM_INCREASE_COLOUMN);
                return true;
            case R.id.share_app:
                //share app with friends
                return true;
            case R.id.rate_us:
                //rate us on google play
                return true;
            case R.id.settings:
                //go to settings
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.newDate:
                //Filter by newest date
                if (isFilterVisible) {
                    viewModel.setMenuOperation(NEW_FILTER_DATE);
                    Toast.makeText(getApplicationContext(), "new date all media", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.setMenuOperation(ALBUM_NEW_FILTER_DATE);
                    Toast.makeText(getApplicationContext(), "new date all media", Toast.LENGTH_SHORT).show();
                }
                item.setChecked(true);
                return true;
            case R.id.oldDate:
                //Filter by oldest date
                if (isFilterVisible)
                    viewModel.setMenuOperation(OLD_FILTER_DATE);
                else
                    viewModel.setMenuOperation(ALBUM_OLD_FILTER_DATE);
                item.setChecked(true);
                return true;
            case R.id.allmedia:
                //Filter by get all media files
                viewModel.setMenuOperation(ALL_MEDIA_FILTER);
                item.setChecked(true);
                return true;
            case R.id.imageOnly:
                //Filter by image only
                viewModel.setMenuOperation(IMAGE_MEDIA_FILTER);
                item.setChecked(true);
                return true;
            case R.id.videoOnly:
                //filter by video only
                viewModel.setMenuOperation(VIDEO_MEDIA_FILTER);
                item.setChecked(true);
                return true;
            case R.id.size:
                //filter by size
                viewModel.setMenuOperation(ALBUM_SIZE_MEDIA_FILTER);
                item.setChecked(true);
                return true;

            case R.id.name:
                //sort by name
                viewModel.setMenuOperation(NAME_MEDIA_FILTER);
                item.setChecked(true);
                return true;
        }
        return true;
    }
}

