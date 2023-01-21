package io.inappchat.inappchat.remote

import io.inappchat.inappchat.remote.service.ChatApi
import io.inappchat.inappchat.remote.service.UserApi
import io.inappchat.inappchat.remote.TokenChangeListener
import io.inappchat.inappchat.remote.NetworkConfig

/** Created by DK on 15/11/18.  */
class NetworkConfig  constructor(builder: Builder) {
    val authApiKey: String
    val authUrl: String
    val chatApiKey: String
    val chatUrl: String
    val userApiKey: String
    val userUrl: String
    val packageName: String
    val chatToken: String?
    val chatRefreshToken: String?
    val userToken: String?
    val userRefreshToken: String?
    val userId: String?
    val chatUserId: String?
    val tenantId: String?
    val deviceId: String?
    var chatApi: ChatApi? = null
    var userApi: UserApi? = null
    val tokenChangeListener: TokenChangeListener?

    init {
        authApiKey = builder.authApiKey!!
        authUrl = builder.authUrl!!
        chatApiKey = builder.chatApiKey!!
        chatUrl = builder.chatUrl!!
        userApiKey = builder.userApiKey!!
        userUrl = builder.userUrl!!
        packageName = builder.packageName!!
        userToken = builder.userToken
        userRefreshToken = builder.userRefreshToken
        chatToken = builder.chatToken
        chatRefreshToken = builder.chatRefreshToken
        userId = builder.userId
        chatUserId = builder.chatUserId
        tenantId = builder.tenantId
        deviceId = builder.deviceId
        tokenChangeListener = builder.tokenChangeListener
    }

    class Builder {
        var authApiKey: String? = null
         var authUrl: String? = null
         var chatApiKey: String? = null
         var chatUrl: String? = null
         var userApiKey: String? = null
         var userUrl: String? = null
         var packageName: String? = null
         var chatToken: String? = null
         var chatRefreshToken: String? = null
         var userToken: String? = null
         var userRefreshToken: String? = null
         var userId: String? = null
         var chatUserId: String? = null
         var tenantId: String? = null
         var deviceId: String? = null
         var tokenChangeListener: TokenChangeListener? = null
        fun authApiKey(apiKey: String): Builder {
            authApiKey = apiKey
            return this
        }

        fun authUrl(url: String): Builder {
            authUrl = url
            return this
        }

        fun chatApiKey(apiKey: String): Builder {
            chatApiKey = apiKey
            return this
        }

        fun chatUrl(url: String): Builder {
            if (url != null) {
                chatUrl = url.substring(0, url.length - 3)
            }
            return this
        }

        fun userApiKey(apiKey: String): Builder {
            userApiKey = apiKey
            return this
        }

        fun userUrl(url: String): Builder {
            if (url != null) {
                userUrl = url.substring(0, url.length - 3)
            }
            return this
        }

        fun userToken(token: String): Builder {
            userToken = token
            return this
        }

        fun userRefreshToken(token: String): Builder {
            userRefreshToken = token
            return this
        }

        fun chatToken(token: String): Builder {
            chatToken = token
            return this
        }

        fun chatRefreshToken(token: String): Builder {
            chatRefreshToken = token
            return this
        }

        fun packageName(packageName: String): Builder {
            this.packageName = packageName
            return this
        }

        fun userId(userId: String): Builder {
            this.userId = userId
            return this
        }

        fun chatUserId(chatUserId: String): Builder {
            this.chatUserId = chatUserId
            return this
        }

        fun tenantId(tenantId: String): Builder {
            this.tenantId = tenantId
            return this
        }

        fun deviceId(deviceId: String?): Builder {
            this.deviceId = deviceId
            return this
        }

        fun tokenChangeListener(listener: TokenChangeListener): Builder {
            tokenChangeListener = listener
            return this
        }

        fun build(): NetworkConfig {
            return NetworkConfig(this)
        }
    }
}