package com.rajesh.gallary.utils;

import static com.rajesh.gallary.common.Constant.AppName;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class ShareAndRateHelper {
    private Activity context;

    public ShareAndRateHelper( Activity context) {
        this.context = context;
    }

    public void ShareApp() {
        String message = "https://play.google.com/store/apps/details?id=" + AppName;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        context.startActivity(Intent.createChooser(share, "Share with Friends on "));
    }

    public void RateUS() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AppName));
        context.startActivity(i);
    }
}
