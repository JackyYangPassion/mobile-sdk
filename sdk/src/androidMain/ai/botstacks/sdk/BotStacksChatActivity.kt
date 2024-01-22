/*
 * Copyright (c) 2023.
 */

package ai.botstacks.sdk

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ai.botstacks.sdk.ui.BotStacksChatContext
import ai.botstacks.sdk.ui.BotStacksChatRoutes

class BotStacksChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        activity = WeakReference(this)
        setContent {
            BotStacksChatContext {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "chats") {
                    BotStacksChatRoutes(navController, this) {
                        this@BotStacksChatActivity.finish()
                    }
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