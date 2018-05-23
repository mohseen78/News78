package com.example.abdulmohseen.news78.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.format.DateUtils;

import com.example.abdulmohseen.news78.News78Application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilityMethods {
    /**
     * Method to detect network connection on the device
     */
    public static boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) News78Application.getNews78ApplicationInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static String manipulateDateFormat(String post_date) {

        if (post_date == null)
            return "";       //if no date is returned by the API then set corresponding date view to empty

        if (post_date.equals("0001-01-01T00:00:00Z"))
            return "";

        SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date date = null;
        try {
            date = existingUTCFormat.parse(post_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            // Converting timestamp into x ago format
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    Long.parseLong(String.valueOf(date.getTime())),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            return timeAgo + "";
        } else {
            return post_date;
        }
    }
}
