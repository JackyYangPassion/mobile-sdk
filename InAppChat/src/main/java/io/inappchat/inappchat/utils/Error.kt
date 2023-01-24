package io.inappchat.inappchat.utils

import io.inappchat.inappchat.remote.Constants.HTTP_CODE_CLIENT_TIMEOUT
import io.inappchat.inappchat.remote.Constants.HTTP_CODE_NETWORK
import io.inappchat.inappchat.remote.Constants.HTTP_CODE_UNEXPECTED
import io.reactivex.rxjava3.functions.Consumer
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException

class Error<T : Throwable>(private val callback: ErrorCallback) : Consumer<T> {
    interface ErrorCallback {
        fun onError(httpCode: Int, status: Status?, message: String?)
    }

    override fun accept(e: T) {
        if (e is HttpException) {
            val exception = e
            val code = exception.code()
            val responseBody = exception.response()!!.errorBody()
            val errorMessage = getErrorMessage(responseBody)
            if (code == 401) {
                callback.onError(code, Status.UNAUTHENTICATED, errorMessage)
            } else if (code >= 400 && code < 500) {
                callback.onError(code, Status.SERVER_ERROR, errorMessage)
            } else if (code >= 500 && code < 600) {
                callback.onError(code, Status.SERVER_TIMEOUT, errorMessage)
            } else {
                callback.onError(code, Status.UNEXPECTED, errorMessage)
            }
        } else if (e is SocketTimeoutException) {
            callback.onError(HTTP_CODE_CLIENT_TIMEOUT, Status.CLIENT_TIMEOUT, "Socket exception")
        } else if (e is IOException) {
            callback.onError(HTTP_CODE_NETWORK, Status.NETWORK, "Network unavailable")
        } else {
            callback.onError(HTTP_CODE_UNEXPECTED, Status.UNEXPECTED, e?.message)
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String? {
        return try {
            responseBody!!.string()
        } catch (e: Exception) {
            e.message
        }
    }
}