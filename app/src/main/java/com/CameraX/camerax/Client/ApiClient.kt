package com.CameraX.camerax.Client

import com.CameraX.camerax.API.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

        const val BASE_URL = "https://ocr-api.azurewebsites.net/"

        val client = OkHttpClient()
            .newBuilder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory((RxJava2CallAdapterFactory.create()))
            .addConverterFactory((GsonConverterFactory.create()))
            .client(client)
            .build()
            .create(ApiService::class.java)

        fun buildService(): ApiService {
            return retrofit
        }

}