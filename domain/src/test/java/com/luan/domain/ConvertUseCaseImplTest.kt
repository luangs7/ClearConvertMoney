package com.luan.domain

import com.luan.common.base.ViewState
import com.luan.domain.interactor.ConvertUseCaseImpl
import com.luan.domain.model.Currency
import com.luan.domain.model.CurrencyConvert
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.AutoCloseKoinTest

class ConvertUseCaseImplTest : AutoCloseKoinTest() {

    private val useCase = ConvertUseCaseImpl()

    private val fromUSD = Currency("USD", "United States Dollar", 1.0)
    private val from = Currency("ANG", "Netherlands Antillean Guilder", 1.792401)
    private val to = Currency("BRL", "Brazilian real", 5.692604)

    private val valueInputedWithComma = "1,50"
    private val valueInputedWithDot = "1.50"
    private val expectedResultFromUSD = "8.54"
    private val expectedResultFromANG = "4.77"
    private val source = "USD"

    @Test
    fun check_From_USD_Conversion_With_Comma() {
        runBlocking {
            val result =
                useCase.invoke(CurrencyConvert(to, fromUSD, source, valueInputedWithComma)).take(1)
                    .toList()
            assert(result.first().data == expectedResultFromUSD)
            assert(result.first().status == ViewState.Status.LOADED)
        }
    }

    @Test
    fun check_From_USD_Conversion_With_Dot() {
        runBlocking {
            val result =
                useCase.invoke(CurrencyConvert(to, fromUSD, source, valueInputedWithDot)).take(1)
                    .toList()
            assert(result.first().data == expectedResultFromUSD)
            assert(result.first().status == ViewState.Status.LOADED)
        }
    }

    @Test
    fun check_From_ANG_Conversion_With_Comma() {
        runBlocking {
            val result =
                useCase.invoke(CurrencyConvert(to, from, source, valueInputedWithComma)).take(1)
                    .toList()
            assert(result.first().data == expectedResultFromANG)
            assert(result.first().status == ViewState.Status.LOADED)
        }
    }

    @Test
    fun check_From_ANG_Conversion_With_Dot() {
        runBlocking {
            val result =
                useCase.invoke(CurrencyConvert(to, from, source, valueInputedWithDot)).take(1)
                    .toList()
            assert(result.first().data == expectedResultFromANG)
            assert(result.first().status == ViewState.Status.LOADED)
        }
    }
}