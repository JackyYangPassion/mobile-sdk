/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.content.Context
import android.content.SharedPreferences
import io.inappchat.sdk.state.Chats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

object InAppChat {

    lateinit var appContext: Context
    lateinit var prefs: SharedPreferences
    var namespace: String
        get() = _namespace
        private set(ns) {
            _namespace = ns
        }
    private lateinit var _namespace: String

    var apiKey: String
        get() = _apiKey
        private set(ns) {
            _apiKey = ns
        }
    private lateinit var _apiKey: String

    fun init(appContext: Context, namespace: String, apiKey: String) {
        this.appContext = appContext
        this.prefs = appContext.getSharedPreferences("inappchat", Context.MODE_PRIVATE)
        this.namespace = namespace
        this.apiKey = apiKey
        Chats.current.init()
    }

    val scope = CoroutineScope(Dispatchers.Main)
}
