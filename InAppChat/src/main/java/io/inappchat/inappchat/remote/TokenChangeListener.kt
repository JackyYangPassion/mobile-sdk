package io.inappchat.inappchat.remote

interface TokenChangeListener {
    fun onChatTokenChanged(token: String?, refreshToken: String?)
    fun onUserTokenChanged(token: String?, refreshToken: String?)
}