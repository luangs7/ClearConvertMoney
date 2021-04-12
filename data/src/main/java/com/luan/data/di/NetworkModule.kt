package com.luan.data.di



import com.google.gson.GsonBuilder
import com.luan.data.remote.AccessTokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
                .addInterceptor(AccessTokenInterceptor())
                .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            return builder.build()
        }


        private fun provideRetrofit(
            client: OkHttpClient
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://api.currencylayer.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().enableComplexMapKeySerialization().create()))
                .build()
        }
    }


}
