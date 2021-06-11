package com.mingz.framework.utils;

import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Tools {

    /**
     * 设置状态栏字体为黑色.
     *
     * @param activity 需要设置的Activity
     */
    public static void setBlackWordOnStatus(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
