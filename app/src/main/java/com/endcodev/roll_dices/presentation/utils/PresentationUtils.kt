package com.endcodev.roll_dices.presentation.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

object StoreUtils {
    fun openPlayStore(context: Context, appPackageName: String) {
        try {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    /**
     * Gets the current app version.
     * @return The current app version as an Int.
     */
    fun getVersion(context: Context): Int {
        val appVersion: Int
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appVersion = pInfo.longVersionCode.toInt()
        } else {
            @Suppress("DEPRECATION")
            appVersion = pInfo.versionCode
        }
        return appVersion
    }
}