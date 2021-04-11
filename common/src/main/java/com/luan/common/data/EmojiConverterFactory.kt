package com.luan.common.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.luan.common.domain.Emoji
import java.lang.reflect.Type

class EmojiConverterFactory : JsonDeserializer<List<Emoji>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Emoji> {
        return json?.asJsonObject!!.entrySet().map {
            Emoji(key = it.key, source = it.value.asString)
        }
    }

}