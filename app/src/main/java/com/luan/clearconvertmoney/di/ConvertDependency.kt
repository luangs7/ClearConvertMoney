package com.luan.clearconvertmoney.di

import com.luan.clearconvertmoney.presentation.ConvertViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val convertDependency = module {

    viewModel { ConvertViewModel(get(),get()) }

}