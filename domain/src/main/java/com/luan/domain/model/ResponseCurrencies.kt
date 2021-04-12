package com.luan.domain.model


import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class ResponseCurrencies(
    @SerializedName("currencies")
    val currencies: Map<String, String>?,
    @PrimaryKey
    @SerializedName("success")
    val success: Boolean = false,
)