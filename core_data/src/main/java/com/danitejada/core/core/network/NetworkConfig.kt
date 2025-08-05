package com.danitejada.core.core.network

import android.util.Log
import com.danitejada.core.BuildConfig
import com.danitejada.core.core.network.NetworkConfig.BASE_URL
import com.danitejada.core.core.network.NetworkConfig.CONNECT_TIMEOUT_MILLIS
import com.danitejada.core.core.network.NetworkConfig.SOCKET_TIMEOUT_MILLIS
import com.danitejada.core.core.network.NetworkConfig.TIMEOUT_MILLIS
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

object NetworkConfig {
  const val BASE_URL = "https://api.pexels.com/"
  const val TIMEOUT_MILLIS = 30_000L
  const val CONNECT_TIMEOUT_MILLIS = 30_000L
  const val SOCKET_TIMEOUT_MILLIS = 30_000L
}

// Create HttpClientFactory.kt
class HttpClientFactory @Inject constructor() {
  fun create(json: Json): HttpClient {
    return HttpClient(Android) {
      install(ContentNegotiation) {
        json(json) // Use the provided Json instance
      }

      install(Logging) {
        logger = object : Logger {
          override fun log(message: String) {
            Log.d("Ktor", message) // Use Android's Log
          }
        }
        level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
      }

      install(ResponseObserver) {
        onResponse { response ->
          Log.d("Ktor", "Response: ${response.status.value} ${response.status.description}")
        }
      }

      install(HttpTimeout) {
        requestTimeoutMillis = TIMEOUT_MILLIS // Total request timeout
        connectTimeoutMillis = CONNECT_TIMEOUT_MILLIS // Connection establishment timeout
        socketTimeoutMillis = SOCKET_TIMEOUT_MILLIS // Read timeout
      }

      // Configure default requests
      defaultRequest {
        url(BASE_URL)
        header("Accept", "application/json")
      }
    }
  }
}