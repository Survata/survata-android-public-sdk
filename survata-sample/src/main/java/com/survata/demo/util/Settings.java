package com.survata.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.survata.demo.R;

public class Settings {

    public static String getPublisherId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String publisherIdString = context.getString(R.string.publisher_id);
        return sharedPreferences.getString(publisherIdString, context.getString(R.string.default_publisher_id));
    }

    public static String getPreviewId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String previewIdString = context.getString(R.string.preview_id);
        return sharedPreferences.getString(previewIdString, context.getString(R.string.default_preview_id));
    }

    public static String getContentName(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String contentNameString = context.getString(R.string.content_name);
        return sharedPreferences.getString(contentNameString, context.getString(R.string.default_content_name));
    }

    public static boolean getContentNameEnable(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String contentNameToggle = context.getString(R.string.content_name_toggle);
        return sharedPreferences.getBoolean(contentNameToggle, true);
    }

}
