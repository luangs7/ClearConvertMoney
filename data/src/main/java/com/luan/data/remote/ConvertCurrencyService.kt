package com.luan.data.remote

import com.luan.domain.model.ResponseCurrencies
import com.luan.domain.model.ResponseLive
import retrofit2.Response
import retrofit2.http.GET

interface ConvertCurrencyService {

    @GET("/live")
    suspend fun getLive(): Response<ResponseLive>

    @GET("/list")
    suspend fun currenciesList(): Response<ResponseCurrencies>

}