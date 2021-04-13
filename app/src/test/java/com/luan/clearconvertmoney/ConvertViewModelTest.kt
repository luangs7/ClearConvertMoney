package com.luan.clearconvertmoney

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.luan.clearconvertmoney.helper.CoroutinesTestRule
import com.luan.clearconvertmoney.helper.currency
import com.luan.clearconvertmoney.helper.listCurrency
import com.luan.clearconvertmoney.presentation.ConvertViewModel
import com.luan.common.base.ViewState
import com.luan.domain.exceptions.CurrencyServiceException
import com.luan.domain.interactor.ConvertUseCase
import com.luan.domain.interactor.GetCurrenciesUseCase
import com.luan.domain.model.CurrencyResponse
import com.luan.domain.model.ResponseCurrencies
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.Test
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito

class ConvertViewModelTest : AutoCloseKoinTest() {

    @get:Rule
    val testRule = CoroutinesTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val convertUseCase = Mockito.mock(ConvertUseCase::class.java)
    private val getCurrenciesUseCase = Mockito.mock(GetCurrenciesUseCase::class.java)
    private val viewModel: ConvertViewModel by lazy { ConvertViewModel(convertUseCase, getCurrenciesUseCase) }


    @Test
    fun check_Get_Currencies_List_Success(){
        val observer = viewModel.getCurrenciesResponse.test()
        testRule.launch {
            whenever(getCurrenciesUseCase(anyOrNull())).thenReturn(flowOf(ViewState.loaded(CurrencyResponse(currencies = listCurrency, source = "USD"))))
            viewModel.getCurrencies()
        }

        val history = observer
            .assertHasValue()
            .valueHistory()

        assert(history.last().status == ViewState.Status.LOADED)
        assert(history.last().data?.currencies?.first()?.symbol == listCurrency.first().symbol)
    }

    @Test
    fun check_Set_Currency_From(){
        val observer = viewModel.currencyFrom.test()

        viewModel.setCurrencyFrom(currency)

        val history = observer
            .assertHasValue()
            .valueHistory()

        assert(history.last().symbol == currency.symbol)
    }

    @Test
    fun check_Set_Currency_To(){
        val observer = viewModel.currencyTo.test()

        viewModel.setCurrencyTo(currency)

        val history = observer
            .assertHasValue()
            .valueHistory()

        assert(history.last().symbol == currency.symbol)
    }
}