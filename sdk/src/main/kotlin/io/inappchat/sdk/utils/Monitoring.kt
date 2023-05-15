/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils


import android.content.Context
import io.inappchat.sdk.BuildConfig
import io.sentry.Hub
import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid

object Monitoring {
    lateinit var logger: Hub
    fun setup(context: Context) {
        val options = SentryOptions().apply {
            dsn =
                "https://17891a46f1414379ab8dee14743c15a6@o4505121822801920.ingest.sentry.io/4505121983168512"
        }
        logger = Hub(options)
    }

    fun error(message: String, data: Map<String, Any>? = null) {
        println(message + " ${data?.entries?.joinToString()}")
        logger.captureMessage(
            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
            SentryLevel.ERROR
        )
    }

    fun error(error: Throwable, data: Map<String, Any>? = null) {
        println(error.stackTraceToString())
        if (data != null) {
            println(data.entries.joinToString())
            logger.captureMessage("Error data: " + data.entries.joinToString())
        }
        logger.captureException(error)
    }

    fun log(message: String, data: Map<String, Any>? = null) {
        println(message + " ${data?.entries?.joinToString()}")
        logger.captureMessage(
            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
            SentryLevel.DEBUG
        )
    }

    fun info(message: String, data: Map<String, Any>? = null) {
        println(message + " ${data?.entries?.joinToString()}")
        logger.captureMessage(
            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
            SentryLevel.INFO
        )
    }

    fun warning(message: String, data: Map<String, Any>? = null) {
        println(message + " ${data?.entries?.joinToString()}")
        logger.captureMessage(
            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
            SentryLevel.WARNING
        )
    }

    fun critical(message: String, data: Map<String, Any>? = null) {
        println(message + " ${data?.entries?.joinToString()}")
        logger.captureMessage(
            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
            SentryLevel.FATAL
        )
    }
}
