package com.rajesh.gallary.utils;

import static com.rajesh.gallary.common.Constant.IMAGE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.VIDEO_MEDIA_FILTER;

import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.mediaModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class DataFilterHelper {

    @Inject
    public DataFilterHelper() {
    }

    //Filter Media Data we can do as query but i prefer to use programatic way
    public List<mediaModel> FilterMediaData(List<mediaModel> mediaModelList, String TypeFilter) {
        List<mediaModel> mediaModelListResult = new ArrayList<>();
        if (TypeFilter.equals(IMAGE_MEDIA_FILTER)) {
            for (int i = 0; i < mediaModelList.size(); i++) {
                if (mediaModelList.get(i).isImage())
                    mediaModelListResult.add(mediaModelList.get(i));
            }
        } else if (TypeFilter.equals(VIDEO_MEDIA_FILTER)) {
            for (int i = 0; i < mediaModelList.size(); i++) {
                if (!mediaModelList.get(i).isImage())
                    mediaModelListResult.add(mediaModelList.get(i));
            }
        } else {
            return mediaModelList;
        }
        return mediaModelListResult;
    }

    /**
     * Filter Data Depend on it's type is it Image , Video or all
     *
     * @param dateAndMediaList
     * @param TypeFilter
     * @return
     */
    public List<DateAndMedia> FilterMediaAndDate(List<DateAndMedia> dateAndMediaList, String TypeFilter) {
        List<DateAndMedia> DateAndMediaResult = new ArrayList<>();

        if (TypeFilter.equals(IMAGE_MEDIA_FILTER)) {
            for (int i = 0; i < dateAndMediaList.size(); i++) {
                List<mediaModel> mediaModelsResult = new ArrayList<>();
                for (int j = 0; j < dateAndMediaList.get(i).mediaModelList.size(); j++) {
                    if (dateAndMediaList.get(i).mediaModelList.get(j).isImage()) {
                        mediaModelsResult.add(dateAndMediaList.get(i).mediaModelList.get(j));
                    }
                }
                if (!mediaModelsResult.isEmpty()) {
                    DateAndMediaResult.add(new DateAndMedia(dateAndMediaList.get(i).date, mediaModelsResult));
                }
            }
        } else if (TypeFilter.equals(VIDEO_MEDIA_FILTER)) {
            for (int i = 0; i < dateAndMediaList.size(); i++) {
                List<mediaModel> mediaModelsResult = new ArrayList<>();
                for (int j = 0; j < dateAndMediaList.get(i).mediaModelList.size(); j++) {
                    if (!dateAndMediaList.get(i).mediaModelList.get(j).isImage()) {
                        mediaModelsResult.add(dateAndMediaList.get(i).mediaModelList.get(j));
                    }
                }
                if (!mediaModelsResult.isEmpty())
                    DateAndMediaResult.add(new DateAndMedia(dateAndMediaList.get(i).date, mediaModelsResult));
            }
        } else {
            return dateAndMediaList;
        }
        return DateAndMediaResult;
    }

    /**
     * Sort date and media table relational by oldest or newest media
     * @param dateAndMediaList
     * @param Filter
     * @return
     */
    public List<DateAndMedia> SortDataByDate(List<DateAndMedia> dateAndMediaList, String Filter) {
        if (Filter.equals(NEW_FILTER_DATE))
            Collections.sort(dateAndMediaList, (t1, t2) -> Long.compare(t2.date.getRealDate(), t1.date.getRealDate()));
        else
            Collections.reverse(dateAndMediaList);

        return dateAndMediaList;
    }

    public List<mediaModel> SortMediaByDate (List<mediaModel> mediaData, String Filter){
        if (Filter.equals(NEW_FILTER_DATE))
            Collections.sort(mediaData, (t1, t2) -> Long.compare(t2.getMediaDate(), t1.getMediaDate()));
        else
            Collections.reverse(mediaData);

        return mediaData;
    }

}
