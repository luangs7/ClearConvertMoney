package com.luan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.luan.domain.model.ResponseCurrencies
import com.luan.domain.model.ResponseLive
import kotlinx.coroutines.flow.Flow

@Dao
interface ConvertCurrencyDao {

    @Query("SELECT * FROM ResponseLive")
    fun getLive():Flow<ResponseLive>

    @Query("SELECT * FROM ResponseCurrencies")
    fun getCurrencies():Flow<ResponseCurrencies>

    @Insert
    fun saveLive(live:ResponseLive)

    @Insert
    fun saveCurrencies(currencies: ResponseCurrencies)

}