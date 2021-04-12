package com.luan.domain.di

import com.luan.domain.interactor.ConvertUseCase
import com.luan.domain.interactor.ConvertUseCaseImpl
import com.luan.domain.interactor.GetCurrenciesUseCase
import com.luan.domain.interactor.GetCurrenciesUseCaseImpl
import org.koin.dsl.module

val domainModule = module {

    factory { ConvertUseCaseImpl() as ConvertUseCase }
    factory { GetCurrenciesUseCaseImpl(get()) as GetCurrenciesUseCase }

}