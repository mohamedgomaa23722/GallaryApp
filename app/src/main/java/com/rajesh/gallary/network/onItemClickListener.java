package com.rajesh.gallary.network;

public interface onItemClickListener<T> {
    public void onClickedItem(T itemData);

    public void onLongTouch(T itemData);
}
