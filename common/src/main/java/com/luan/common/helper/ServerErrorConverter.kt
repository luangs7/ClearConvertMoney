package com.luan.common.helper

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

const val UNKNOW_ERROR = "Ocorreu um erro em sua requisição"

fun <T> Response<T>.getException():Exception = Exception(this.errorBody()?.string() ?: UNKNOW_ERROR)

val errorResponseBody = UNKNOW_ERROR.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())