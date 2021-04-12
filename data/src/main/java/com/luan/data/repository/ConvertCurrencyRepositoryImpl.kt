package com.luan.data.repository

import com.luan.common.base.ViewState
import com.luan.common.extension.then
import com.luan.data.local.ConvertCurrencyDao
import com.luan.data.remote.ConvertCurrencyService
import com.luan.domain.exceptions.CurrencyListEmptyException
import com.luan.domain.exceptions.CurrencyServiceException
import com.luan.domain.model.ResponseLive
import com.luan.domain.repository.ConvertCurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class ConvertCurrencyRepositoryImpl(
    private val service: ConvertCurrencyService,
    private val dao: ConvertCurrencyDao
) : ConvertCurrencyRepository {

    override suspend fun getCurrencies(): Flow<ViewState<Map<String, String>>> = flow {
        emit(ViewState.loading())
        val response = service.currenciesList()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (result.success) {
                    dao.saveCurrencies(result)
                    emit(ViewState.loaded(result.currencies))
                } else {
                    emit(ViewState.error<Map<String, String>>(CurrencyServiceException()))
                }
            } ?: kotlin.run { emit(ViewState.error<Map<String, String>>(CurrencyListEmptyException())) }
        } else {
            dao.getCurrencies().collect {
                if(it.currencies.isNullOrEmpty().not()) {
                    emit(ViewState.loaded(it.currencies))
                }else {
                    emit(ViewState.error<Map<String, String>>(CurrencyListEmptyException()))
                }
            }
        }
    }

    override suspend fun getLiveQuotes(): Flow<ViewState<ResponseLive>> = flow {
        emit(ViewState.loading<ResponseLive>())
        val response = service.getLive()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (result.success) {
                    result.quotes = transformQuotes(result.quotes)
                    dao.saveLive(result)
                    emit(ViewState.loaded(result))
                }else{
                    emit(ViewState.error<ResponseLive>(CurrencyServiceException(result.error?.info)))
                }
            } ?: kotlin.run { emit(ViewState.error<ResponseLive>(CurrencyListEmptyException())) }
        } else {
            dao.getLive().collect { result ->
                if(result.quotes.isNullOrEmpty().not()){
                    emit(ViewState.loaded(result))
                }else{
                    emit(ViewState.error<ResponseLive>(CurrencyListEmptyException()))
                }

            }
        }
    }

    private fun transformQuotes(quotes: Map<String, String>?): HashMap<String, String> {
        val newQuotes = hashMapOf<String, String>()
        quotes?.forEach {
            newQuotes[(it.key == "${ResponseLive.SOURCE_DEFAULT}${ResponseLive.SOURCE_DEFAULT}") then ResponseLive.SOURCE_DEFAULT
                ?: it.key.replace(ResponseLive.SOURCE_DEFAULT, "")] = it.value
        }

        return newQuotes
    }
}