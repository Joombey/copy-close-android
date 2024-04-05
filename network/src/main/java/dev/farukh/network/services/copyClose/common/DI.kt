package dev.farukh.network.services.copyClose.common

import android.util.Log
import dev.farukh.network.BuildConfig
import dev.farukh.network.Tags
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

internal val commonServiceDI by DI.Module {
    bindProvider(Tags.COPY_CLOSE_COMMON) {
        Json {
            encodeDefaults = true
            isLenient = true
            ignoreUnknownKeys = true
            prettyPrint = true
            classDiscriminatorMode = ClassDiscriminatorMode.NONE
        }
    }
    bindProvider(Tags.COPY_CLOSE_COMMON) {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(instance(Tags.COPY_CLOSE_COMMON))
            }
            Logging {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.i("BACK-COMMON", message)
                    }
                }
                level = LogLevel.ALL
            }
            install(DefaultRequest) {
                url(BuildConfig.CopyCloseURL)
            }
        }
    }

    bindProvider<CommonService> {
        CommonService(instance(Tags.COPY_CLOSE_COMMON), instance(Tags.COPY_CLOSE_COMMON))
    }
}