package com.rajesh.gallary.ui.Activities;


import static com.rajesh.gallary.common.Constant.ALBUM_SIZE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.ALL_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.GRID_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.IMAGE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.INCREASE_COLOUMN;
import static com.rajesh.gallary.common.Constant.LIST_VIEW_TYPE;
import static com.rajesh.gallary.common.Constant.NAME_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.OLD_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.REDUCE_COLOUMN;
import static com.rajesh.gallary.common.Constant.VIDEO_MEDIA_FILTER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivityMainBinding;
import com.rajesh.gallary.homeSliderAdapter;
import com.rajesh.gallary.ui.viewModels.MainViewModel;
import com.rajesh.gallary.utils.AdmobHelper;
import com.rajesh.gallary.utils.SavedData;
import com.rajesh.gallary.utils.ShareAndRateHelper;


import java.util.Objects;

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
    @Inject
    AdmobHelper admobHelper;
    ShareAndRateHelper shareAndRateHelper;
    private String CommunicatorType = "Photos";

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
                CommunicatorType = Objects.requireNonNull(tab.getText()).toString();

                if (tab.getText().equals("Albums")) {
                    menu.findItem(R.id.filterByType).setVisible(false);
                    menu.findItem(R.id.name).setVisible(true);
                    menu.findItem(R.id.size).setVisible(true);
                    menu.findItem(R.id.oldDate).setVisible(false);
                    menu.findItem(R.id.newDate).setVisible(false);
                } else {
                    menu.findItem(R.id.filterByType).setVisible(true);
                    menu.findItem(R.id.name).setVisible(false);
                    menu.findItem(R.id.size).setVisible(false);
                    menu.findItem(R.id.oldDate).setVisible(true);
                    menu.findItem(R.id.newDate).setVisible(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        shareAndRateHelper = new ShareAndRateHelper(this);

        binding.adView.loadAd(admobHelper.InitializeBannerAds());

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
        switch (item.getItemId()) {
            case R.id.listView:
                // change view type grid or list of items
                initializeCommunicatorData(CommunicatorType, LIST_VIEW_TYPE);
                //make increase and decrease items Invisible
                menu.findItem(R.id.increaseColoumn).setVisible(false);
                menu.findItem(R.id.reduceColoumn).setVisible(false);
                return true;
            case R.id.GridView:
                // change view type grid or list of items
                initializeCommunicatorData(CommunicatorType, GRID_VIEW_TYPE);
                menu.findItem(R.id.increaseColoumn).setVisible(true);
                menu.findItem(R.id.reduceColoumn).setVisible(true);
                return true;
            case R.id.reduceColoumn:
                //Reduce coloumn size
                initializeCommunicatorData(CommunicatorType, REDUCE_COLOUMN);
                return true;
            case R.id.increaseColoumn:
                //increase coloumn size
                initializeCommunicatorData(CommunicatorType, INCREASE_COLOUMN);
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
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.newDate:
                //Filter by newest date
                initializeCommunicatorData(CommunicatorType, NEW_FILTER_DATE);
                item.setChecked(true);
                return true;
            case R.id.oldDate:
                //Filter by oldest date
                initializeCommunicatorData(CommunicatorType, OLD_FILTER_DATE);
                item.setChecked(true);
                return true;
            case R.id.allmedia:
                //Filter by get all media files
                initializeCommunicatorData(CommunicatorType, ALL_MEDIA_FILTER);
                item.setChecked(true);
                return true;
            case R.id.imageOnly:
                //Filter by image only
                initializeCommunicatorData(CommunicatorType, IMAGE_MEDIA_FILTER);
                item.setChecked(true);
                return true;
            case R.id.videoOnly:
                //filter by video only
                initializeCommunicatorData(CommunicatorType, VIDEO_MEDIA_FILTER);
                item.setChecked(true);
                return true;
            case R.id.size:
                //filter by size
                initializeCommunicatorData(CommunicatorType, ALBUM_SIZE_MEDIA_FILTER);
                item.setChecked(true);
                return true;

            case R.id.name:
                //sort by name
                initializeCommunicatorData(CommunicatorType, NAME_MEDIA_FILTER);
                item.setChecked(true);
                return true;
        }
        return true;
    }

    /**
     * This Method used to send some data from activity -> viewmodel -> fragment
     * depend on communicator type
     *
     * @param CommunicatorType
     */
    private void initializeCommunicatorData(String CommunicatorType, String CommunicatorData) {
        switch (CommunicatorType) {
            case "Photos":
                viewModel.setAllMediaCommunicatorData(CommunicatorData);
                break;
            case "Albums":
                viewModel.setAlbumCommunicatorData(CommunicatorData);
                break;
            case "Fav":
                viewModel.setFavouriteCommunicatorData(CommunicatorData);
                break;
        }
    }


}




