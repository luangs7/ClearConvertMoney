package com.luan.domain.model


import com.google.gson.annotations.SerializedName

data class ResponseLive(
    @SerializedName("error")
    val error: Error? = null,
    @SerializedName("privacy")
    val privacy: String? = "",
    @SerializedName("quotes")
    val quotes: Map<String, String>?,
    @SerializedName("source")
    val source: String? = "",
    @SerializedName("success")
    val success: Boolean? = false,
    @SerializedName("terms")
    val terms: String? = "",
    @SerializedName("timestamp")
    val timestamp: Int? = 0
){

    companion object{
        const val SOURCE_DEFAULT = "USD"
    }
}