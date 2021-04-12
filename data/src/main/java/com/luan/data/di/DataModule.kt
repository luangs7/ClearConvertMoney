package com.luan.data.di

import android.content.Context
import androidx.room.Room
import com.luan.common.extension.resolveRetrofit
import com.luan.data.local.ConvertCurrencyDao
import com.luan.data.local.ConvertCurrencyDatabase
import com.luan.data.remote.ConvertCurrencyService
import com.luan.data.remote.ConvertCurrencyServiceMock
import com.luan.data.repository.ConvertCurrencyRepositoryImpl
import com.luan.domain.repository.ConvertCurrencyRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val moduleData = module {

    fun provideDatabase(application: Context): ConvertCurrencyDatabase {
        return Room.databaseBuilder(application, ConvertCurrencyDatabase::class.java, "convertcurrency")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideDao(database: ConvertCurrencyDatabase): ConvertCurrencyDao {
        return  database.dao
    }

    single { provideDatabase(androidContext()) }
    single { provideDao(get()) }

    factory<ConvertCurrencyService> { resolveRetrofit() ?: ConvertCurrencyServiceMock() }
    factory { ConvertCurrencyRepositoryImpl(get(),get()) as ConvertCurrencyRepository }

}