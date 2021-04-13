package com.luan.clearconvertmoney.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luan.common.base.ViewState
import com.luan.common.extension.debounce
import com.luan.common.extension.safeLet
import com.luan.common.extension.toSingleEvent
import com.luan.domain.interactor.ConvertUseCase
import com.luan.domain.interactor.GetCurrenciesUseCase
import com.luan.domain.model.Currency
import com.luan.domain.model.CurrencyConvert
import com.luan.domain.model.CurrencyResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ConvertViewModel(
    private val convertUseCase: ConvertUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {


    private val _convertResult = MutableLiveData<ViewState<String>>()
    val convertResult = _convertResult.toSingleEvent()

    private val _getCurrenciesResponse = MutableLiveData<ViewState<CurrencyResponse>>()
    val getCurrenciesResponse = _getCurrenciesResponse.toSingleEvent()

    private val _currencyFrom = MutableLiveData<Currency>()
    val currencyFrom: LiveData<Currency>  = _currencyFrom

    private val _currencyTo = MutableLiveData<Currency>()
    val currencyTo: LiveData<Currency> = _currencyTo

    private val _getCurrenciesFiltered = MutableLiveData<List<Currency>>()
    val getCurrenciesFiltered = _getCurrenciesFiltered.toSingleEvent()

    val enableButtons = MutableLiveData<Boolean>(false)

    init {
        getCurrencies()
    }


    fun getCurrencies() {
        viewModelScope.launch {
            getCurrenciesUseCase(Unit)
                .onStart { _getCurrenciesResponse.postValue(ViewState.loading()) }
                .catch { _getCurrenciesResponse.postValue(ViewState.error(it)) }
                .collect { _getCurrenciesResponse.postValue(it) }
        }
    }

    fun convert(valueToFormat:String?) {
        viewModelScope.launch {
            safeLet(
                _currencyTo.value,
                _currencyFrom.value,
                _getCurrenciesResponse.value?.data?.source,
                valueToFormat
            ) { to, from, source, value ->
                convertUseCase.invoke(CurrencyConvert(to, from, source, value))
                    .onStart { _convertResult.postValue(ViewState.loading()) }
                    .catch { _convertResult.postValue(ViewState.error(it)) }
                    .collect { _convertResult.postValue(it) }
            }
        }
    }


    fun setCurrencyFrom(currency: Currency) {
        _currencyFrom.postValue(currency)
    }

    fun setCurrencyTo(currency: Currency) {
        _currencyTo.postValue(currency)
    }

    @SuppressLint("DefaultLocale")
    fun search(query:String?){
        if(query.isNullOrEmpty().not()) {
            _getCurrenciesFiltered.postValue(_getCurrenciesResponse.value?.data?.currencies?.filter {
                it.symbol.toLowerCase().contains(query!!.toLowerCase()) || it.extendedName.toLowerCase().contains(query.toLowerCase())  })
        }else{
            _getCurrenciesFiltered.postValue(_getCurrenciesResponse.value?.data?.currencies)
        }
    }

    fun enableButtons(){
        enableButtons.postValue(true)
    }

    fun getCurrenciesList():List<Currency>? = _getCurrenciesResponse.value?.data?.currencies

}