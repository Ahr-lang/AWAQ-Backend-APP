package com.example.authapp.global.util

import android.util.Log
import com.example.authapp.global.network.NetworkConfig

object NetworkDebugUtil {
    private const val TAG = "NetworkDebug"
    
    fun logNetworkConfig() {
        Log.d(TAG, "=== Network Configuration ===")
        Log.d(TAG, "Base URL: ${NetworkConfig.BASE_URL}")
    Log.d(TAG, "Use Localhost: ${NetworkConfig.USE_LOCALHOST}")
    Log.d(TAG, "Dev Hostname: ${NetworkConfig.DEV_HOSTNAME}")
    Log.d(TAG, "Dev Host IP: ${NetworkConfig.DEV_HOST_IP}")
    Log.d(TAG, "Dev Host Port: ${NetworkConfig.LOCALHOST_PORT}")
        Log.d(TAG, "============================")
    }
    
    fun logConnectionAttempt(url: String) {
        Log.d(TAG, "Attempting connection to: $url")
    }
    
    fun logConnectionSuccess(url: String) {
        Log.d(TAG, "Successfully connected to: $url")
    }
    
    fun logConnectionError(url: String, error: String) {
        Log.e(TAG, "Connection failed to: $url - Error: $error")
    }
}