package com.rajesh.gallary.network;

import com.rajesh.gallary.model.mediaModel;

import java.util.List;

public interface onLongSelected {
    public void longClicked(boolean isEnable,boolean isSelected,int Operation);

    public void OnChildSelected(List<mediaModel> data);
}
