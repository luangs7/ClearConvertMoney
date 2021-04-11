package com.luan.domain.interactor

import com.luan.common.base.ViewState
import com.luan.domain.model.CurrencyConvert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.math.RoundingMode
import java.text.DecimalFormat

class ConvertUseCaseImpl : ConvertUseCase(){
    override suspend fun execute(params: CurrencyConvert): Flow<ViewState<String>> = flow {
        params.run {
            val valueFormatted = value.replace(",",".").toDouble()
            val result = when {
                from.symbol == source -> {
                    valueFormatted * to.value
                }
                to.symbol == source -> {
                    valueFormatted / from.value
                }
                else -> {
                    (valueFormatted / from.value) * to.value
                }
            }
            val df = DecimalFormat("#####.##").also { it.roundingMode = RoundingMode.CEILING }
            emit(ViewState.loaded(df.format(result)))
        }
    }
}

