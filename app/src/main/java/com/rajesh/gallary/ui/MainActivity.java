package com.rajesh.gallary.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rajesh.gallary.R;
import com.rajesh.gallary.databinding.ActivityMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
    }
}