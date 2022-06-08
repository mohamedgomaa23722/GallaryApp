package com.rajesh.gallary.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AlbumsAndMedia {
    @Embedded
    public Albums albums;
    @Relation(
            parentColumn = "albumId",
            entityColumn = "AlbumID"
    )
    public List<mediaModel> mediaModelList;
}
