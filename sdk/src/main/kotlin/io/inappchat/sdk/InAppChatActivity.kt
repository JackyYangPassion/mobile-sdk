/*
 * Copyright (c) 2023.
 */

package io.inappchat.sdk

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.inappchat.sdk.ui.InAppChatContext
import io.inappchat.sdk.ui.InAppChatRoutes

class InAppChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        activity = WeakReference(this)
        setContent {
            InAppChatContext {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "chats") {
                    InAppChatRoutes(navController, this)
                }
            }
        }
    }

    fun giphy() {
//        val settings = GPHSettings(GridType.waterfall, GPHTheme.Dark)
//        settings.mediaTypeConfig = arrayOf(GPHContentType.gif)
//        val gifsDialog = GiphyDialogFragment.newInstance(settings)
//        gifsDialog.gifSelectionListener = object : GiphyDialogFragment.GifSelectionListener {
//            override fun didSearchTerm(term: String) {
//            }
//
//            override fun onDismissed(selectedContentType: GPHContentType) {
//            }
//
//            override fun onGifSelected(
//                media: Media,
//                searchTerm: String?,
//                selectedContentType: GPHContentType
//            ) {
//                (
//                        media.images.fixedWidth?.gifUrl ?: media.images.fixedWidthSmall?.gifUrl
//                        ?: media.images.original?.gifUrl
//                        )?.let { Chats.current.nextGif?.invoke(it) }
//            }
//
//        }
//        gifsDialog.show(supportFragmentManager, "gifs_dialog")
    }

    companion object {
//        var activity = WeakReference<InAppChatActivity>(null)
    }
}