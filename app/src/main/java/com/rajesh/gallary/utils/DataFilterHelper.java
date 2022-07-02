package com.rajesh.gallary.utils;

import static com.rajesh.gallary.common.Constant.IMAGE_MEDIA_FILTER;
import static com.rajesh.gallary.common.Constant.NEW_FILTER_DATE;
import static com.rajesh.gallary.common.Constant.VIDEO_MEDIA_FILTER;

import com.rajesh.gallary.model.AlbumsAndMedia;
import com.rajesh.gallary.model.DateAndMedia;
import com.rajesh.gallary.model.mediaModel;

import java.util.ArrayList;

import java.util.List;

import javax.inject.Inject;

public class DataFilterHelper {

    SortGenericClass<DateAndMedia> dateAndMediaSortGenericClass;
    SortGenericClass<mediaModel> mediaModelSortGenericClass;
    SortGenericClass<AlbumsAndMedia> albumsAndMediaSortGenericClass;

    @Inject
    public DataFilterHelper() {
        dateAndMediaSortGenericClass = new SortGenericClass<>();
        mediaModelSortGenericClass = new SortGenericClass<>();
        albumsAndMediaSortGenericClass = new SortGenericClass<>();
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


    public List<mediaModel> SortMediaByDate(List<mediaModel> arr, String Filter) {
        //Base Case
        if (arr.size() < 2) {
            //then return the passed array
            return arr;
        }
        // now we will set the value for pivot
        mediaModel pivot = arr.get((arr.size() - 1) / 2);
        //Initialize the left array which is bigger than the pivot
        List<mediaModel> left = new ArrayList<>();
        //Initialize the right array which is smaller than the pivot
        List<mediaModel> right = new ArrayList<>();
        //then get the bigger and smaller values inside the arr
        for (int i = 0; i < arr.size(); i++) {
            if (i != (arr.size() - 1) / 2) {
                if (Filter.equals(NEW_FILTER_DATE)) {
                    System.out.println("current date : " + arr.get(i).getMediaDate() + " , pivot: " + pivot.getMediaDate());
                    if (arr.get(i).getMediaDate() >= pivot.getMediaDate() && i != (arr.size() - 1) / 2) {
                        System.out.println("bigger than date : " + arr.get(i).getMediaDate() + " , pivot: " + pivot.getMediaDate());
                        //then add the item to left side
                        left.add(arr.get(i));
                    } else if (arr.get(i).getMediaDate() < pivot.getMediaDate()) {
                        System.out.println("smaller than pivot date : " + arr.get(i).getMediaDate() + " , pivot: " + pivot.getMediaDate());
                        //then add the item to right side
                        right.add(arr.get(i));
                    }
                } else {
                    //this if we need to sort them from the oldest to newest
                    if (arr.get(i).getMediaDate() >= pivot.getMediaDate()) {
                        //then add the item to right side
                        right.add(arr.get(i));
                    } else if (arr.get(i).getMediaDate() < pivot.getMediaDate()) {
                        //then add the item to left side
                        left.add(arr.get(i));
                    }
                }
            }
        }
        System.out.println("sorted array Left = " + left.size());
        System.out.println("sorted array pivot = 1");
        System.out.println("Sorted array Left = " + right.size());
        return mediaModelSortGenericClass.AddList(SortMediaByDate(left, Filter), pivot, SortMediaByDate(right, Filter));
    }

    /**
     * At this method we implement Quick sort by new date to old ones
     * it means that :
     * 1- pivot is the middle item
     * 2- left  is bigger than pivot
     * 3- right is smaller than pivot
     * then it will give me sorted element from bigger to smaller date
     *
     * @param arr
     * @return
     */
    public List<DateAndMedia> NewMediaQuickSort(List<DateAndMedia> arr, String Filter) {
        //Base Case
        if (arr.size() < 2) {
            //then return the passed array
            return arr;
        }
        // now we will set the value for pivot
        DateAndMedia pivot = arr.get((arr.size() - 1) / 2);
        //Initialize the left array which is bigger than the pivot
        List<DateAndMedia> left = new ArrayList<>();
        //Initialize the right array which is smaller than the pivot
        List<DateAndMedia> right = new ArrayList<>();
        //then get the bigger and smaller values inside the arr
        for (int i = 0; i < arr.size(); i++) {
            if (i != (arr.size() - 1) / 2) {
                if (Filter.equals(NEW_FILTER_DATE)) {
                    if (arr.get(i).date.getRealDate() >= pivot.date.getRealDate()) {
                        //then add the item to left side
                        left.add(arr.get(i));
                    } else if (arr.get(i).date.getRealDate() < pivot.date.getRealDate()) {
                        //then add the item to right side
                        right.add(arr.get(i));
                    }
                } else {
                    //this if we need to sort them from the oldest to newest
                    if (arr.get(i).date.getRealDate() >= pivot.date.getRealDate()) {
                        //then add the item to right side
                        right.add(arr.get(i));
                    } else if (arr.get(i).date.getRealDate() < pivot.date.getRealDate()) {
                        //then add the item to left side
                        left.add(arr.get(i));
                    }
                }
            }
        }
        return dateAndMediaSortGenericClass.AddList(NewMediaQuickSort(left, Filter), pivot, NewMediaQuickSort(right, Filter));
    }

    public List<AlbumsAndMedia> QuickSortAlbumsBySize(List<AlbumsAndMedia> arr){
        //Base Case
        if (arr.size() < 2) {
            //then return the passed array
            return arr;
        }
        // now we will set the value for pivot
        AlbumsAndMedia pivot = arr.get((arr.size() - 1) / 2);
        //Initialize the left array which is bigger than the pivot
        List<AlbumsAndMedia> left = new ArrayList<>();
        //Initialize the right array which is smaller than the pivot
        List<AlbumsAndMedia> right = new ArrayList<>();
        //then get the bigger and smaller values inside the arr
        for (int i = 0; i < arr.size(); i++) {
            if (i != (arr.size() - 1) / 2) {
                    if (arr.get(i).mediaModelList.size() >= pivot.mediaModelList.size()) {
                        //then add the item to left side
                        left.add(arr.get(i));
                    } else if (arr.get(i).mediaModelList.size() < pivot.mediaModelList.size()) {
                        //then add the item to right side
                        right.add(arr.get(i));
                    }
            }
        }
        return albumsAndMediaSortGenericClass.AddList(QuickSortAlbumsBySize(left), pivot, QuickSortAlbumsBySize(right));
    }
}
