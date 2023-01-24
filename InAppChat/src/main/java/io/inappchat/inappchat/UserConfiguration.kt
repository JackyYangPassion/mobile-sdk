package io.inappchat.inappchat

import android.content.Context

class UserConfiguration constructor(builder: Builder) {
    val name: String?
    val status: String?
    val appUserId: String?
    val loginType: String?
    val profilePic: String?
    val profilePicThumb: String?
    val profileStatus: String?
    val appState: String?
    val userId: String?
    val accessToken: String?
    val refreshToken: String?
    val fcmToken: String?
    val payload: String?
    val context: Context?

    init {
        name = builder.name
        status = builder.status
        appUserId = builder.appUserId
        loginType = builder.loginType
        profilePic = builder.profilePic
        profilePicThumb = builder.profilePicThumb
        profileStatus = builder.profileStatus
        appState = builder.appState
        userId = builder.userId
        accessToken = builder.accessToken
        refreshToken = builder.refreshToken
        fcmToken = builder.fcmToken
        payload = builder.payload
        context = builder.context
    }

    class Builder {
        var name: String? = null
        var status: String? = null
        var appUserId: String? = null
        var loginType: String? = null
        var profilePic: String? = null
        var profilePicThumb: String? = null
        var profileStatus: String? = null
        var appState: String? = null
        var userId: String? = null
        var accessToken: String? = null
        var refreshToken: String? = null
        var fcmToken: String? = null
        var payload: String? = null
        var context: Context? = null
        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun status(status: String): Builder {
            this.status = status
            return this
        }

        fun appUserId(appUserId: String): Builder {
            this.appUserId = appUserId
            return this
        }

        fun loginType(loginType: String): Builder {
            this.loginType = loginType
            return this
        }

        fun profilePic(profilePic: String): Builder {
            this.profilePic = profilePic
            return this
        }

        fun profilePicThumb(profilePicThumb: String): Builder {
            this.profilePicThumb = profilePicThumb
            return this
        }

        fun profileStatus(profileStatus: String): Builder {
            this.profileStatus = profileStatus
            return this
        }

        fun appState(appState: String): Builder {
            this.appState = appState
            return this
        }

        fun userId(userId: String): Builder {
            this.userId = userId
            return this
        }

        fun accessToken(accessToken: String): Builder {
            this.accessToken = accessToken
            return this
        }

        fun refreshToken(refreshToken: String): Builder {
            this.refreshToken = refreshToken
            return this
        }

        fun fcmToken(fcmToken: String): Builder {
            this.fcmToken = fcmToken
            return this
        }

        fun payload(payload: String): Builder {
            this.payload = payload
            return this
        }

        fun context(context: Context): Builder {
            this.context = context
            return this
        }

        fun build(): UserConfiguration {
            return UserConfiguration(this)
        }
    }
}