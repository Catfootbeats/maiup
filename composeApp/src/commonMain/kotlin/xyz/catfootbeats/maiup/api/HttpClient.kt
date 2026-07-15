package xyz.catfootbeats.maiup.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class HttpClientFactory() {
    fun create(): HttpClient {
        return HttpClient {
            install(HttpTimeout) {
                connectTimeoutMillis = 5000
                requestTimeoutMillis = 10000
                socketTimeoutMillis = 10000
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }
}
