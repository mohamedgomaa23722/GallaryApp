package com.rajesh.gallary.ui.Activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivityMainBinding;


import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private NavController navController;
    private int oldId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav);
        navController = navHostFragment.getNavController();

        binding.TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MoveUp(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @SuppressLint("NonConstantResourceId")
    private void MoveUp(int id) {
        navController.navigate(navigationAction(id));
        oldId = id;
    }

    public int navigationAction(int NewId) {
        if (oldId == 0) {
            //At this case we can have two Actions :
            // Go to fragment one or fragment two
            if (NewId == 1)
                return R.id.action_allMediaFragment_to_albumFragment;
            return R.id.action_allMediaFragment_to_favouriteFragment;
        } else if (oldId == 1) {
            //also we have two actions at this case:
            // go to fragment zero or fragment two
            if (NewId == 0)
                return R.id.action_albumFragment_to_allMediaFragment;
            return R.id.action_albumFragment_to_favouriteFragment;
        } else {
            if (NewId == 0)
                return R.id.action_favouriteFragment_to_allMediaFragment;
            return R.id.action_favouriteFragment_to_albumFragment;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}