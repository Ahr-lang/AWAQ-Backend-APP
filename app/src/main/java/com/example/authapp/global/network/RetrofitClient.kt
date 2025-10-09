package com.example.authapp.global.network

import com.example.authapp.global.util.TokenManager
import com.example.authapp.global.util.NetworkDebugUtil
import com.example.authapp.data.remote.AuthApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.net.ssl.HostnameVerifier
import okhttp3.Dns
import java.net.InetAddress

object RetrofitClient {

    fun create(tokenManager: TokenManager): AuthApiService {
        // Log current network configuration
        NetworkDebugUtil.logNetworkConfig()
        
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clientBuilder = OkHttpClient.Builder()
            // Map DEV_HOSTNAME -> DEV_HOST_IP for emulator connections while preserving Host header
            .dns(object : Dns {
                override fun lookup(hostname: String): List<InetAddress> {
                    return if (NetworkConfig.USE_LOCALHOST && (hostname == NetworkConfig.DEV_HOSTNAME || hostname == "api.ecoranger.org")) {
                        try {
                            listOf(InetAddress.getByName(NetworkConfig.DEV_HOST_IP))
                        } catch (e: Exception) {
                            Dns.SYSTEM.lookup(hostname)
                        }
                    } else {
                        Dns.SYSTEM.lookup(hostname)
                    }
                }
            })
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("apikey", "back-key-000")
                    .method(original.method, original.body)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        // Only bypass SSL for localhost development (SECURITY: Remove in production)
        if (NetworkConfig.USE_LOCALHOST) {
            // Create a trust manager that accepts all certificates (for development only)
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // Create SSL context that uses our trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create hostname verifier that accepts localhost
            val hostnameVerifier = HostnameVerifier { hostname, _ ->
                hostname == "localhost" || hostname == "127.0.0.1" || hostname == "10.0.2.2"
            }

            clientBuilder
                .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier(hostnameVerifier)
        }

        val client = clientBuilder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AuthApiService::class.java)
    }
}