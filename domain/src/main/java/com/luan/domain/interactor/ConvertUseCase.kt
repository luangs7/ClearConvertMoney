package com.luan.domain.interactor

import com.luan.common.base.ViewState
import com.luan.common.interactor.UseCase
import com.luan.domain.model.CurrencyConvert
import kotlinx.coroutines.flow.Flow

abstract class ConvertUseCase : UseCase<CurrencyConvert,Flow<ViewState<String>>>()