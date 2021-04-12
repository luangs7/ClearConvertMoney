package com.luan.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class StringMapConverter {
    @TypeConverter
    fun fromString(value: String?): Map<String, String> {
        val mapType: Type = object :
            TypeToken<Map<String?, String?>?>() {}.getType()
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMap(map: Map<String?, String?>?): String {
        return Gson().toJson(map)
    }
}