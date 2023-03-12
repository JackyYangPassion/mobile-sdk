/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.content.Context
import android.content.SharedPreferences
import android.os.Build.VERSION.SDK_INT
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import io.inappchat.sdk.state.Chats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object InAppChat {

    lateinit var appContext: Context
    lateinit var prefs: SharedPreferences
    lateinit var namespace: String
    lateinit var apiKey: String
    fun init(appContext: Context, namespace: String, apiKey: String) {
        this.appContext = appContext
        this.prefs = appContext.getSharedPreferences("inappchat", Context.MODE_PRIVATE)
        this.namespace = namespace
        this.apiKey = apiKey
        Chats.current.init()
        ImageLoader.Builder(appContext)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    val scope = CoroutineScope(Dispatchers.Main)
}
