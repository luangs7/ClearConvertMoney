package com.luan.data

import com.luan.common.base.ViewState
import com.luan.data.local.ConvertCurrencyDao
import com.luan.data.remote.ConvertCurrencyService
import com.luan.data.repository.ConvertCurrencyRepositoryImpl
import com.luan.domain.exceptions.CurrencyListEmptyException
import com.luan.domain.exceptions.CurrencyServiceException
import com.luan.domain.model.ResponseCurrencies
import com.luan.domain.model.ResponseLive
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito
import retrofit2.Response

class ConvertCurrencyRepositoryImplTest : AutoCloseKoinTest() {

    private val service = Mockito.mock(ConvertCurrencyService::class.java)
    private val dao = Mockito.mock(ConvertCurrencyDao::class.java)
    private val repository :ConvertCurrencyRepositoryImpl by lazy { ConvertCurrencyRepositoryImpl(service, dao) }

    @Test
    fun check_Get_Currencies_With_Server_Error_And_Dao_Empty(){
        runBlocking {
            whenever(service.currenciesList()).thenReturn(Response.error(404,"Not found".toResponseBody()))
            whenever(dao.getCurrencies()).thenReturn(flowOf(ResponseCurrencies(currencies = emptyMap(),success = true)))

            val result = repository.getCurrencies().take(2).toList()
            assert(result.last().status == ViewState.Status.LOADED)
        }
    }

    @Test
    fun check_Get_Currencies_With_Server_Error_And_Dao_Not_Empty(){
        runBlocking {
            whenever(service.currenciesList()).thenReturn(Response.error(404,"Not found".toResponseBody()))
            whenever(dao.getCurrencies()).thenReturn(flowOf(ResponseCurrencies(currencies = mapOf(Pair("AED", "United Arab Emirates Dirham")),success = true)))

            val result = repository.getCurrencies().take(2).toList()
            assert(result.last().status == ViewState.Status.LOADED)
            assert(result.last().data.isNullOrEmpty().not())
        }
    }

    @Test
    fun check_Get_Currencies_From_Server(){
        runBlocking {
            whenever(service.currenciesList()).thenReturn(Response.success(ResponseCurrencies(currencies = mapOf(Pair("AED", "United Arab Emirates Dirham")),success = true)))

            val result = repository.getCurrencies().take(2).toList()
            assert(result.last().status == ViewState.Status.LOADED)
            assert(result.last().data.isNullOrEmpty().not())
        }
    }

    @Test
    fun check_Get_Currencies_From_Server_With_Error_Code(){
        runBlocking {
            whenever(service.currenciesList()).thenReturn(Response.success(ResponseCurrencies(currencies = mapOf(Pair("AED", "United Arab Emirates Dirham")),success = false)))
            whenever(dao.getCurrencies()).thenReturn(flowOf(ResponseCurrencies(currencies = mapOf(Pair("AED", "United Arab Emirates Dirham")),success = true)))

            val result = repository.getCurrencies().take(2).toList()
            assert(result.last().status == ViewState.Status.LOADED)
            assert(result.last().data.isNullOrEmpty().not())
        }
    }

    @Test
    fun check_Get_Quotes_With_Server_Error_And_Dao_Empty(){
        runBlocking {
            whenever(service.getLive()).thenReturn(Response.error(404,"Not found".toResponseBody()))
            whenever(dao.getLive()).thenReturn(flowOf(ResponseLive(quotes = emptyMap(),success = true)))

            val result = repository.getLiveQuotes().take(2).toList()
            assert(result.last().status == ViewState.Status.LOADED)
        }
    }

    @Test
    fun check_Get_Quotes_With_Server_Error_And_Dao_Not_Empty(){
        runBlocking {
            whenever(service.getLive()).thenReturn(Response.error(404,"Not found".toResponseBody()))
            whenever(dao.getLive()).thenReturn(flowOf(ResponseLive(quotes = mapOf(Pair("AED", "3.672982")),success = true)))

            val result = repository.getLiveQuotes().take(2).toList()
            assert(result.last().status == ViewState.Status.LOADED)
            assert(result.last().data?.quotes.isNullOrEmpty().not())
        }
    }


    @Test
    fun check_Get_Quotes_From_Server(){
        runBlocking {
            whenever(service.getLive()).thenReturn(Response.success(ResponseLive(quotes = mapOf(Pair("AED", "3.672982")),success = true)))

            val result = repository.getLiveQuotes().take(2).toList()
            assert(result.last().status == ViewState.Status.LOADED)
            assert(result.last().data?.quotes.isNullOrEmpty().not())
        }
    }


    @Test
    fun check_Get_Quotes_From_Server_With_Error_Code(){
        runBlocking {
            whenever(service.getLive()).thenReturn(Response.success(ResponseLive(quotes = null,success = false)))
            whenever(dao.getLive()).thenReturn(flowOf(ResponseLive(quotes = mapOf(Pair("AED", "3.672982")),success = true)))

            val result = repository.getLiveQuotes().take(2).toList()
            assert(result.last().status == ViewState.Status.LOADED)
            assert(result.last().data?.quotes.isNullOrEmpty().not())
        }
    }
}