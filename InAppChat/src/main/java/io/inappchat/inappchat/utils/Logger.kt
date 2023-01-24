package io.inappchat.inappchat.utils

import android.util.Log
import io.inappchat.inappchat.BuildConfig
import timber.log.Timber

/**
 * Created by meeth on 27/10/18.
 * @author meeth
 */
object Logger {

    private const val TAG = "chat_sdk_log :"
    const val MQTT = "MQTT"
    const val FCM = "FCM"
    const val HTTP = "OK_HTTP"

    private fun String.format(vararg params: Any?): String {
        return try {
            java.lang.String.format(this, params)
        } catch (e: Exception) {
            this
        }
    }

    private fun checkAndExecute(action: () -> Unit) {
        if (BuildConfig.DEBUG) {
            action.invoke()
        }
    }

    @JvmStatic
    fun d(tag: String, message: String, vararg params: Any?) {
        checkAndExecute {
            Monitoring.log("${this.TAG + tag} ${message.format(params)}")
            Timber.tag(this.TAG + tag).d(message.format(params))
        }
    }

    @JvmStatic
    fun e(tag: String, message: String, vararg params: Any?) {
        checkAndExecute {
            Monitoring.error("${this.TAG + tag} ${message.format(params)}")
            Timber.tag(this.TAG + tag).e(message.format(params))
        }
    }

    @JvmStatic
    fun i(tag: String, message: String, vararg params: Any?) {
        checkAndExecute {
            Monitoring.info("${this.TAG + tag} ${message.format(params)}")
            Timber.tag(this.TAG + tag).i(message.format(params))
        }
    }

    @JvmStatic
    fun v(tag: String, message: String, vararg params: Any?) {
        checkAndExecute {
            Monitoring.log("${this.TAG + tag} ${message.format(params)}")
            Timber.tag(this.TAG + tag).v(message.format(params))
        }
    }

    @JvmStatic
    fun w(tag: String, message: String, vararg params: Any?) {
        checkAndExecute {
            Monitoring.warning("${this.TAG + tag} ${message.format(params)}")
            Timber.tag(this.TAG + tag).w(message.format(params))
        }
    }

    @JvmStatic
    fun wtf(tag: String, message: String, vararg params: Any?) {
        checkAndExecute {
            Monitoring.critical("${this.TAG + tag} ${message.format(params)}")
            Timber.tag(this.TAG + tag).wtf(message.format(params))
        }
    }
}