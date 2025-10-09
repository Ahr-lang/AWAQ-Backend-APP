package com.example.authapp.global.network

object NetworkConfig {
    // Change these values based on your development setup
    // Development mapping:
    // - DEV_HOSTNAME is the hostname Caddy is serving (SNI and Host header)
    // - DEV_HOST_IP is the address the emulator should connect to (10.0.2.2 maps to host's localhost)
    const val LOCALHOST_PORT = 443  // Caddy HTTPS port on the host
    const val DEV_HOSTNAME = "localhost" // hostname Caddy expects (and for TLS SNI)
    const val DEV_HOST_IP = "10.0.2.2"    // emulator -> host localhost
    
    // For production, change this to your actual server URL
    const val PRODUCTION_BASE_URL = "https://api.ecoranger.org/"
    
    // Set this to false when deploying to production
    const val USE_LOCALHOST = true
    
    val BASE_URL: String
        get() = if (USE_LOCALHOST) {
            // keep hostname as localhost so TLS SNI and Host header are 'localhost'
            "https://$DEV_HOSTNAME:$LOCALHOST_PORT/"
        } else {
            PRODUCTION_BASE_URL
        }
}