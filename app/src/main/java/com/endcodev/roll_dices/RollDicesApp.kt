package com.endcodev.roll_dices

import android.app.Application
import com.google.android.gms.ads.MobileAds

class RollDicesApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}