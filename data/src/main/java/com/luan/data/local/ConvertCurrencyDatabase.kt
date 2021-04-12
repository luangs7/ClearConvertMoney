package com.luan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.luan.domain.model.ResponseCurrencies
import com.luan.domain.model.ResponseLive

@Database(
    entities = [ResponseCurrencies::class, ResponseLive::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringMapConverter::class)
abstract class ConvertCurrencyDatabase : RoomDatabase() {
    abstract val dao: ConvertCurrencyDao
}