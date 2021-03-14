package com.example.hammoqassignment.utils

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics


object CommonFunctions {

    fun getScreenHeight(activity: Activity): Int {
        return getWindowSize(activity).heightPixels
    }

    fun getScreenWidth(activity: Activity): Int {
        return getWindowSize(activity).widthPixels
    }

    private fun getWindowSize(activity: Activity): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        } else {
            activity.display?.getMetrics(displayMetrics)
        }
        return displayMetrics
    }
}