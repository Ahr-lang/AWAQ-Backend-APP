package com.example.authapp.global.network

import com.example.authapp.data.remote.AuthApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "localhost:3000/" // cámbialo por el real cuando ya funcione fuera de local

    val apiService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }
}