package com.luan.domain.repository

import com.luan.common.base.ViewState
import com.luan.domain.model.ResponseLive
import kotlinx.coroutines.flow.Flow

interface ConvertCurrencyRepository {

    suspend fun getCurrencies():Flow<ViewState<Map<String, String>>>

    suspend fun getLiveQuotes():Flow<ViewState<ResponseLive>>
}