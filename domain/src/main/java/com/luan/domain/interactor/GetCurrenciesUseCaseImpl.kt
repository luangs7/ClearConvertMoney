package com.luan.domain.interactor

import android.util.Log
import com.luan.common.base.ViewState
import com.luan.common.interactor.UseCase
import com.luan.domain.model.Currency
import com.luan.domain.model.CurrencyResponse
import com.luan.domain.model.ResponseLive
import com.luan.domain.repository.ConvertCurrencyRepository
import kotlinx.coroutines.flow.*

class GetCurrenciesUseCaseImpl(
    private val repository: ConvertCurrencyRepository
): GetCurrenciesUseCase(){
     override suspend fun execute(params: Unit): Flow<ViewState<CurrencyResponse>> = flow {
         val listCurrencies = arrayListOf<Currency>()
         repository.getCurrencies().zip(repository.getLiveQuotes()){ currencies, live ->
             currencies.data?.entries?.forEach {
                listCurrencies.add(Currency(it.key,it.value, live.data?.quotes?.get(it.key)?.toDouble() ?: 0.0))
             }
             emit(ViewState.loaded(CurrencyResponse(listCurrencies,live.data?.source ?: ResponseLive.SOURCE_DEFAULT)))
         }
     }
 }