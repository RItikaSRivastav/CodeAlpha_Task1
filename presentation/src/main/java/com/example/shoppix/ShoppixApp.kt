package com.example.shoppix

import android.app.Application
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.shoppix.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ShoppixApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ShoppixApp)
            modules(listOf(
                presentationModule,
                dataModule,
                domainModule
            ))
        }

    }


    }
