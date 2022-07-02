package com.rajesh.gallary.utils;

import java.util.ArrayList;
import java.util.List;

public class SortGenericClass<T> {
    public List<T> AddList(List<T> left, T pivot, List<T> right) {
        List<T> result = new ArrayList<>();
        result.addAll(left);
        result.add(pivot);
        result.addAll(right);
        return result;
    }
}