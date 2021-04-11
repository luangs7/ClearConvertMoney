package com.luan.domain.model


import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: Int? = 0,
    @SerializedName("info")
    val info: String? = ""
)