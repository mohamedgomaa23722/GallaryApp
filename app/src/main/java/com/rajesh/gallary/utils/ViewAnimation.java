package com.rajesh.gallary.utils;

import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewAnimation {

    private Transition transition;
    private ViewGroup viewGroup;
    public static final int ANIMATION_DURATION = 500;

    public ViewAnimation(ViewGroup viewGroup, List<View> view) {
        Transition transition1 = new Slide(Gravity.BOTTOM);
        transition1.setDuration(ANIMATION_DURATION);
        for (View v : view) {
            transition1.addTarget(v);
        }
        TransitionManager.beginDelayedTransition(viewGroup, transition1);
    }

    public ViewAnimation(ViewGroup viewGroup, View view, int slide) {
        Transition transition1 = new Slide(slide);
        transition1.setDuration(ANIMATION_DURATION);
        transition1.addTarget(view);
        TransitionManager.beginDelayedTransition(viewGroup, transition1);
    }
    
}
