package com.rajesh.gallary.model;

import static com.rajesh.gallary.common.Constant.MEDIA_DATE;
import static com.rajesh.gallary.common.Constant.MEDIA_DATE_ID;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Date;
import java.util.List;

public class DateAndMedia {
    @Embedded
   public  DateOfMedia date;
    @Relation(
            parentColumn = MEDIA_DATE_ID,
            entityColumn = MEDIA_DATE
    )
   public List<mediaModel> mediaModelList;
}
