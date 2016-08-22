package com.seonil.scrollingimageview.util;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

/**
 * Created by Seonil on 2016. 8. 22..
 */
public class BitmapUtil {

    public static Bitmap resizeBitmapFullDisplaySize(Context context, Bitmap bitmap) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Bitmap resizeBitmap = resizeBitmap(bitmap, dm.heightPixels - getStatusBarHeight(context), false);

        return resizeBitmap;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int resizeLength, boolean isCompareWidth) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            float ratio = (float) resizeLength / (float) (isCompareWidth ? width : height);

            return resizeBitmap(bitmap, (int) (ratio * width), (int) (ratio * height));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int resizeWidth, int resizeHeight) {
        Bitmap resizeBitmap = null;

        try {
            resizeBitmap = Bitmap.createScaledBitmap(bitmap, resizeWidth, resizeHeight, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resizeBitmap;
    }

    private static int getStatusBarHeight(Context context) {
        int statusHeight = 0;
        int screenSizeType = (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);

        if (screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0)
                statusHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusHeight;
    }
}
