package com.luan.clearconvertmoney.core

import android.app.Application
import com.luan.clearconvertmoney.di.convertDependency
import com.luan.data.di.NetworkModule
import com.luan.data.di.dataModule
import com.luan.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CurrencyConvertApplication : Application() {

    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidLogger()
            androidContext(this@CurrencyConvertApplication)
            modules(
                    listOf(
                            NetworkModule.dependencyModule,
                            domainModule,
                            dataModule,
                            convertDependency
                    )
            )
        }
    }

}