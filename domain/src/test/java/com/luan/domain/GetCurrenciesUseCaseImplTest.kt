package com.luan.domain

import com.luan.common.base.ViewState
import com.luan.domain.interactor.GetCurrenciesUseCaseImpl
import com.luan.domain.model.Currency
import com.luan.domain.model.ResponseLive
import com.luan.domain.repository.ConvertCurrencyRepository
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito

class GetCurrenciesUseCaseImplTest : AutoCloseKoinTest() {

    private val repository = Mockito.mock(ConvertCurrencyRepository::class.java)
    private val useCase = GetCurrenciesUseCaseImpl(repository)

    private val toCompare = Currency("AED","United Arab Emirates Dirham", 3.672982)

    private val flowFromLiveApi = flow {
        emit(ViewState.loading())
        delay(1000)
        emit(
            ViewState.loaded(
                ResponseLive(
                    quotes = mapOf(
                        Pair("AED", "3.672982"),
                        Pair("AFN", "57.8936"),
                        Pair("ALL", "126.1652"),
                    )
                )
            )
        )
    }

    private val flowFromCurrenciesApi = flow {
        emit(ViewState.loading())
        delay(1000)
        emit(
            ViewState.loaded(
                mapOf(
                    Pair("AED", "United Arab Emirates Dirham"),
                    Pair("AFN", "Afghan Afghani"),
                    Pair("USD", "Albanian Lek"),
                )
            )
        )
    }


    @Test
    fun check_If_Currency_Is_Getting_The_Corrent_Information_From_Both_API(){
        runBlocking {
            whenever(repository.getCurrencies()).thenReturn(flowFromCurrenciesApi)
            whenever(repository.getLiveQuotes()).thenReturn(flowFromLiveApi)
            val result = useCase.invoke(Unit).take(2).toList()

            assert(result.last().data?.currencies?.first()?.symbol == toCompare.symbol)
            assert(result.last().data?.currencies?.first()?.value == toCompare.value)
            assert(result.last().data?.currencies?.first()?.extendedName == toCompare.extendedName)
        }
    }


}