package com.metacodersbd.dailybook

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class dailybook : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}