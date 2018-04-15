package com.intech.player.android.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.intech.player.R;

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

    public static Drawable okIcon(Context context) {
        final Drawable icon = context.getResources().getDrawable(R.drawable.ic_ok);
        icon
                .mutate()
                .setColorFilter(
                        context.getResources().getColor(R.color.colorSecondaryDark),
                        PorterDuff.Mode.SRC_IN);

        return icon;
    }

    public static boolean oreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
