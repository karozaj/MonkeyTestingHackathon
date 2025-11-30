package com.example.monkeytestinghackathon

import android.app.Application
import com.example.monkeytestinghackathon.di.appModule
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}