package io.inappchat.inappchat.remote.core.interceptor


import io.inappchat.inappchat.remote.Constants
import io.inappchat.inappchat.remote.NetworkConfig
import io.inappchat.inappchat.remote.core.ErtcException
import io.inappchat.inappchat.remote.util.HeaderUtils
import io.inappchat.inappchat.utils.Monitoring
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.util.*

class RestInterceptor(nwConfig: NetworkConfig) : Interceptor {

    private val nwConfig: NetworkConfig = nwConfig;
    private val headerSignature = "X-Request-Signature"
    private val timestamp = "X-nonce"
    private val headerAuth = "Authorization"
    private val headerPrefix = "Bearer "
    private val serverAuth = 1
    private val serverUser = 2
    private val serverChat = 3

    private var host: String? = null

    fun setHost(host: String) {
        this.host = host
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            try {
                var request = chain.request()
                val host = this.host
                if (host != null) {
                    val newUrl = request.url()
                        .newBuilder()
                        .host(host)
                        .build()
                    request = request.newBuilder()
                        .url(newUrl)
                        .build()
                }

                val builder = request.newBuilder()
                addCommonHeaders(builder)
                val url = request.url()
                    .url()
                    .toString()
                val serverType = getServerType(
                    request.url()
                        .url()
                        .toString()
                )
                addApiKeyHeader(serverType, builder)
                if (!url.contains("refreshToken")) {
                    addAuthToken(serverType, builder)
                }
                if (serverType == serverChat) {
                    addDeviceIdHeader(builder)
                }
                val originalResponse = chain.proceed(builder.build())

                val code = originalResponse.code()
                if (code == 401) {
//          // todo call refresh token
//          if (serverType == serverUser) {
//            val resp = nwConfig.userApi.refreshUserToken(
//              nwConfig.userId, nwConfig.tenantId, headerPrefix + nwConfig.userRefreshToken
//            )
//              .execute()
//            val tokenResp = resp.body()
//            if (tokenResp != null) {
//              val newRequest = request.newBuilder()
//              newRequest.addHeader(headerAuth, headerPrefix + tokenResp.accessToken)
//              addApiKeyHeader(serverType, newRequest)
//              addCommonHeaders(newRequest)
//              nwConfig.tokenChangeListener.onUserTokenChanged(
//                tokenResp.accessToken, tokenResp.refreshToken
//              )
//              return chain.proceed(newRequest.build())
//            }
//          } else if (serverType == serverChat) {
//            val resp = nwConfig.chatApi.refreshChatToken(
//              nwConfig.tenantId, nwConfig.chatUserId, headerPrefix + nwConfig.chatRefreshToken
//            )
//              .execute()
//            val tokenResp = resp.body()
//            if (tokenResp != null) {
//              val newRequest = request.newBuilder()
//              addApiKeyHeader(serverType, newRequest)
//              addDeviceIdHeader(newRequest)
//              addCommonHeaders(newRequest)
//              newRequest.addHeader(headerAuth, headerPrefix + tokenResp.accessToken)
//              nwConfig.tokenChangeListener.onChatTokenChanged(
//                tokenResp.accessToken, tokenResp.refreshToken
//              )
//              return chain.proceed(newRequest.build())
//            }
//          }
                    return originalResponse
                } else if (code == 403) {
                    throw ErtcException(ErtcException.Error.AuthError(code), Constants.ERROR_403)
                } else if (code == 504) {
                    Monitoring.error(
                        "Server is down",
                        mapOf("url" to request.url(), "method" to request.method())
                    )
                    ErtcException(
                        ErtcException.Error.ServerDown(Constants.HTTP_504_GATEWAY_TIMEOUT),
                        "Server is down!"
                    )
                } else if (code == 408) {
                    ErtcException(
                        ErtcException.Error.SocketTimeout(Constants.HTTP_CODE_CLIENT_TIMEOUT),
                        "Server timeout! Please try again!"
                    )
                } else {
                    if (code >= 500) {
                        Monitoring.error(
                            "Server error",
                            mapOf("url" to request.url(), "method" to request.method())
                        )
                    }
                    return originalResponse
                }
                return originalResponse
            } catch (ex: Exception) {
                throw when (ex) {
                    is java.net.SocketTimeoutException -> {
                        //callback.onError(111, com.jio.bapp.network.Status.TIMEOUT, "Socket exception")
                        ErtcException(
                            ErtcException.Error.SocketTimeout(Constants.HTTP_CODE_CLIENT_TIMEOUT),
                            "Server timeout! Please try again!",
                            ex
                        )
                    }
                    is ConnectException, is java.net.UnknownHostException -> {
                        // callback.onError(222, com.jio.bapp.network.Status.NETWORK, "Network unavailable")
                        ErtcException(
                            ErtcException.Error.NoNetwork(Constants.HTTP_CODE_NETWORK),
                            "Network unavailable",
                            ex
                        )
                    }
                    is IOException -> ErtcException(
                        ErtcException.Error.IOError(0),
                        ex.message ?: "IO failure",
                        ex
                    )
                    else -> {
                        //callback.onError(333, com.jio.bapp.network.Status.UNEXPECTED, e.message)
                        ErtcException(
                            ErtcException.Error.UnknownError(Constants.HTTP_CODE_UNEXPECTED),
                            ex.message ?: "Unknown error",
                            ex
                        )
                    }
                }
            }
        }
    }

    private fun addCommonHeaders(builder: Request.Builder) {
        builder.addHeader("Content-Type", "application/json")
        builder.addHeader("Accept", "application/json")
        builder.addHeader("X-Request-ID", UUID.randomUUID().toString())
    }

    private fun addDeviceIdHeader(builder: Request.Builder) {
        builder.addHeader("deviceid", this.nwConfig.deviceId)
    }

    private fun getServerType(url: String): Int {
        return when {
            url.startsWith(this.nwConfig.authUrl) -> {
                serverAuth
            }
            url.startsWith(this.nwConfig.chatUrl) -> {
                serverChat
            }
            url.startsWith(this.nwConfig.userUrl) -> {
                serverUser
            }

            else -> {
                serverAuth
            }
        }
    }

    private fun addApiKeyHeader(serverType: Int, builder: Request.Builder) {
        var apiKey = ""
        when (serverType) {
            serverUser -> apiKey = nwConfig.userApiKey
            serverChat -> apiKey = nwConfig.chatApiKey
            serverAuth -> apiKey = nwConfig.authApiKey
        }
        val time = System.currentTimeMillis()
        val hApiKey = HeaderUtils.getHeaderSignature(apiKey, nwConfig.packageName, time)
        val hTime = HeaderUtils.getHeaderTimestamp(time)
        builder.addHeader(headerSignature, hApiKey)
        builder.addHeader(timestamp, hTime)
    }

    private fun addAuthToken(serverType: Int, builder: Request.Builder) {
        var token: String? = null
        if (serverType == serverUser && nwConfig.userToken?.isNotEmpty() == true) {
            token = nwConfig.userToken
        } else if (serverType == serverChat && nwConfig.chatToken?.isNotEmpty() == true) {
            token = nwConfig.chatToken
        }
        if (token?.isNotEmpty() == true) {
            builder.addHeader(headerAuth, headerPrefix + token)
        }
    }
}
