package com.luan.domain.model


import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ResponseLive(
    @SerializedName("quotes")
    var quotes: Map<String, String>?,
    @PrimaryKey
    @SerializedName("source")
    val source: String = SOURCE_DEFAULT,
    @SerializedName("success")
    val success: Boolean = false
){

    @SerializedName("error")
    @Ignore
    val error: Error? = null

    companion object{
        const val SOURCE_DEFAULT = "USD"
    }
}