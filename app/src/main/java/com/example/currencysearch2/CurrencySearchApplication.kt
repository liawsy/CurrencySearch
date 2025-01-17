package com.example.currencysearch2

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CurrencySearchApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CurrencySearchApplication)
            modules(appModule)
        }
    }

}