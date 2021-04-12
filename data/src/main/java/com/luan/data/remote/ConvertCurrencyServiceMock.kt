package com.luan.data.remote

import com.luan.domain.model.ResponseCurrencies
import com.luan.domain.model.ResponseLive
import kotlinx.coroutines.delay
import retrofit2.Response

class ConvertCurrencyServiceMock : ConvertCurrencyService {
    override suspend fun getLive(): Response<ResponseLive> {
        delay(2000)
        return Response.success(
            ResponseLive(
                quotes = mapOf(
                    Pair("AED", "3.672982"),
                    Pair("AFN", "57.8936"),
                    Pair("ALL", "126.1652")
                ),
                success = true,
                source = "USD"
            )
        )
    }

    override suspend fun currenciesList(): Response<ResponseCurrencies> {
        delay(2000)
        return Response.success(
            ResponseCurrencies(
                currencies = mapOf(
                    Pair("AED", "United Arab Emirates Dirham"),
                    Pair("AFN", "Afghan Afghani"),
                    Pair("USD", "Albanian Lek")
                ),
                success = true)
        )
    }


}