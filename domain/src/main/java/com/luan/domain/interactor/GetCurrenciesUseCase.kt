package com.luan.domain.interactor

import com.luan.common.base.ViewState
import com.luan.common.interactor.UseCase
import com.luan.domain.model.Currency
import com.luan.domain.model.CurrencyResponse
import kotlinx.coroutines.flow.Flow

abstract class GetCurrenciesUseCase: UseCase<Unit,Flow<ViewState<CurrencyResponse>>>()