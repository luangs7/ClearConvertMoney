package com.luan.data.repository

import com.luan.common.base.ViewState
import com.luan.common.extension.then
import com.luan.data.local.ConvertCurrencyDao
import com.luan.data.remote.ConvertCurrencyService
import com.luan.domain.exceptions.CurrencyListEmptyException
import com.luan.domain.exceptions.CurrencyServiceException
import com.luan.domain.model.ResponseLive
import com.luan.domain.repository.ConvertCurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class ConvertCurrencyRepositoryImpl(
    private val service: ConvertCurrencyService,
    private val dao: ConvertCurrencyDao
) : ConvertCurrencyRepository {

    override suspend fun getCurrencies(): Flow<ViewState<Map<String, String>>> = flow {
        getCurrenciesFromApi()
                .catch { getCurrenciesFromLocal().collect { emit(it) } }
                .collect { emit(it) }
    }

    private suspend fun getCurrenciesFromApi(): Flow<ViewState<Map<String, String>>> = flow {
        val response = service.currenciesList()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (result.success) {
                    dao.saveCurrencies(result)
                    emit(ViewState.loaded(result.currencies))
                } else {
                    throw CurrencyServiceException(result.error?.info)
                }
            } ?: kotlin.run { throw CurrencyListEmptyException() }
        } else {
            throw CurrencyServiceException()
        }
    }.flowOn(Dispatchers.Default)

    private suspend fun getCurrenciesFromLocal(): Flow<ViewState<Map<String, String>>> = flow {
        dao.getCurrencies().collect { result ->
            result?.let {
                emit(ViewState.loaded(it.currencies))
            } ?: kotlin.run {
                throw CurrencyListEmptyException()
            }
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun getLiveQuotes(): Flow<ViewState<ResponseLive>> = flow {
        getLiveQuotesFromApi()
                .catch { getLiveQuotesFromDao().collect { emit(it) } }
                .collect { emit(it) }
    }.flowOn(Dispatchers.Default)


    private fun getLiveQuotesFromApi(): Flow<ViewState<ResponseLive>> = flow {
        val response = service.getLive()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (result.success) {
                    result.quotes = transformQuotes(result.quotes)
                    dao.saveLive(result)
                    emit(ViewState.loaded(result))
                } else {
                    throw CurrencyServiceException(result.error?.info)
                }
            } ?: kotlin.run { throw CurrencyListEmptyException() }
        }
    }.flowOn(Dispatchers.Default)

    private fun getLiveQuotesFromDao(): Flow<ViewState<ResponseLive>> = flow {
        dao.getLive().collect { result ->
            result?.let {
                emit(ViewState.loaded(it))
            } ?: kotlin.run {
                throw CurrencyListEmptyException()
            }
        }
    }.flowOn(Dispatchers.Default)

    private fun transformQuotes(quotes: Map<String, String>?): HashMap<String, String> {
        val newQuotes = hashMapOf<String, String>()
        quotes?.forEach {
            newQuotes[(it.key == "${ResponseLive.SOURCE_DEFAULT}${ResponseLive.SOURCE_DEFAULT}") then ResponseLive.SOURCE_DEFAULT
                    ?: it.key.replace(ResponseLive.SOURCE_DEFAULT, "")] = it.value
        }
        return newQuotes
    }
}