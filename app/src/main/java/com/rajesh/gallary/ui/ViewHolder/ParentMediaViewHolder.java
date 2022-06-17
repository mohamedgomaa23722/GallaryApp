package com.rajesh.gallary.ui.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rajesh.gallary.R;

public class ParentMediaViewHolder extends RecyclerView.ViewHolder {
    public TextView Date;
    public RecyclerView ChildRecycler;
    public ParentMediaViewHolder(@NonNull View itemView) {
        super(itemView);
        Date = itemView.findViewById(R.id.Date);
        ChildRecycler = itemView.findViewById(R.id.ChildRec);
    }

    public void InitializeChildAdapter(){

    }
}