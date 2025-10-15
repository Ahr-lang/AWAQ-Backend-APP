package com.example.mawi_app_back.data.remote

import com.example.mawi_app_back.data.local.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenManager.token.first() }

        val builder = chain.request().newBuilder()
            // Admin endpoints always use the back key
            .header("apikey", "back-key-000")

        if (!token.isNullOrEmpty()) {
            builder.header("Authorization", "Bearer $token")
        }
        return chain.proceed(builder.build())
    }
}
