package com.intech.player.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * Self explanatory.
 *
 * @author Ivan Volnov
 * @since 09.04.18
 */
public class AndroidUtils {

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }

        return false;
    }

    public static boolean oreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
