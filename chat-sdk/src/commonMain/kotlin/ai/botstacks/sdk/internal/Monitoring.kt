/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk.internal

import ai.botstacks.sdk.SdkConfig

internal object Monitoring {
    private val printLogs = SdkConfig.DEBUG

    fun setup() {
        println("Monitoring :: printLogs=$printLogs")
//        Sentry.init {
//            it.dsn = "https://17891a46f1414379ab8dee14743c15a6@o4505121822801920.ingest.sentry.io/4505121983168512"
//        }
    }

    fun error(message: String, data: Map<String, Any>? = null) {
        if (printLogs) {
            println(message + " ${data?.entries?.joinToString().orEmpty()}")
        }
//        Sentry.captureMessage(
//            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
//        )
    }

    fun error(error: Throwable, message: String? = null) {
        if (printLogs) {
            if (message != null) {
                println(message)
            }
            println(error.stackTraceToString())
        }

//            Sentry.captureMessage("Error data: " + data.entries.joinToString())
//        Sentry.captureException(error)
    }

    fun log(message: String, data: Map<String, Any>? = null) {
        if (printLogs) {
            println(message + " ${data?.entries?.joinToString().orEmpty()}")
        }
//        Sentry.captureMessage(
//            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
//        )
    }

    fun info(message: String, data: Map<String, Any>? = null) {
        if (printLogs) {
            println(message + " ${data?.entries?.joinToString().orEmpty()}")
        }
//        Sentry.captureMessage(
//            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
//        )
    }

    fun warning(message: String, data: Map<String, Any>? = null) {
        if (printLogs) {
            println(message + " ${data?.entries?.joinToString().orEmpty()}")
        }
//        Sentry.captureMessage(
//            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
//        )
    }

    fun critical(message: String, data: Map<String, Any>? = null) {
        if (printLogs) {
            println(message + " ${data?.entries?.joinToString().orEmpty()}")
        }
//        Sentry.captureMessage(
//            if (data != null) message + " Data: ${data.entries.joinToString()}" else message,
//        )
    }
}
