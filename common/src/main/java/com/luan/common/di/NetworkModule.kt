package com.luan.common.di


import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.luan.common.data.EmojiConverterFactory
import com.luan.common.domain.Emoji
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class NetworkModule {
    companion object {
        val dependencyModule = module {
            single {
                provideOkHttpClient()
            }
            single { provideRetrofit(get()) }
        }

        private fun provideOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
                .followRedirects(true)
                .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            return builder.build()
        }


        private fun provideRetrofit(
            client: OkHttpClient
        ): Retrofit {
            val listType: Type = object : TypeToken<MutableList<Emoji>>() {}.type
            val builder = GsonBuilder().registerTypeAdapter(listType, EmojiConverterFactory())

            return Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .build()
        }
    }


}
