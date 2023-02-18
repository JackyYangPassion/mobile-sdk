/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.content.Context

object InAppChat {

    lateinit var appContext: Context
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
        this.namespace = namespace
        this.apiKey = apiKey
    }
}