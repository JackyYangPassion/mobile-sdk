/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk.utils


import android.content.Context
import com.rollbar.android.Rollbar
import io.inappchat.inappchat.BuildConfig

object Monitoring {
    lateinit var logger: Rollbar
    fun setup(context: Context) {
        logger = Rollbar(
            context, "3fd589b8d11b4c89aedafa3e1c6af534", BuildConfig.ENV, true,
            false, null, "full", -1,
            false
        )
        info("init rollbar")
    }

    fun error(message: String, data: Map<String, Any>? = null) = logger.error(message, data)
    fun error(error: Throwable, data: Map<String, Any>? = null) = logger.error(error, data)

    fun log(message: String, data: Map<String, Any>? = null) = logger.debug(message, data)
    fun info(message: String, data: Map<String, Any>? = null) = logger.info(message, data)
    fun warning(message: String, data: Map<String, Any>? = null) = logger.warning(message, data)
    fun critical(message: String, data: Map<String, Any>? = null) = logger.critical(message, data)
}
