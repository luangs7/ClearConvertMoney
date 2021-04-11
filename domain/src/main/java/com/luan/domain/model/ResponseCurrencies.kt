package com.luan.domain.model


import com.google.gson.annotations.SerializedName

data class ResponseCurrencies(
    @SerializedName("currencies")
    val currencies: Map<String, String>?,
    @SerializedName("privacy")
    val privacy: String? = "",
    @SerializedName("success")
    val success: Boolean? = false,
    @SerializedName("terms")
    val terms: String? = ""
)