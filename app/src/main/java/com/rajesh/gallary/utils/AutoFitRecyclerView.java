package com.rajesh.gallary.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

public class AutoFitRecyclerView {
    private Context context;


    public AutoFitRecyclerView(Context context) {
        this.context = context;
    }


    public int calculateNoOfColumns(int GridCount) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = (int) displayMetrics.widthPixels / GridCount;
        return width;
    }


}

