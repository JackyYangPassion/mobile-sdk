package ai.botstacks.sdk.internal.ui.components


import ai.botstacks.sdk.internal.ui.rootViewController
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cocoapods.Giphy.GPHContentType
import cocoapods.Giphy.GPHContentTypeGifs
import cocoapods.Giphy.GPHFileExtensionGif
import cocoapods.Giphy.GPHMedia
import cocoapods.Giphy.GPHRenditionTypeFixedWidth
import cocoapods.Giphy.GPHStickerColumnCountThree
import cocoapods.Giphy.GPHTheme
import cocoapods.Giphy.GPHThemeTypeAutomatic
import cocoapods.Giphy.GiphyDelegateProtocol
import cocoapods.Giphy.GiphyViewController
import cocoapods.Giphy.urlWithRendition
import platform.UIKit.UIApplication
import platform.darwin.NSObject

@Composable
internal actual fun GiphyModalSheet(
    modifier: Modifier,
    onCancel: () -> Unit,
    onSelection: (String) -> Unit
) {
    val giphy = GiphyViewController().apply {
        this.setMediaConfigWithTypes(listOf(GPHContentTypeGifs))
        this.setStickerColumnCount(GPHStickerColumnCountThree)
        this.setTheme(GPHTheme().apply { setType(GPHThemeTypeAutomatic) })

        this.setDelegate(object : NSObject(), GiphyDelegateProtocol {
            override fun didSelectMediaWithGiphyViewController(
                giphyViewController: GiphyViewController,
                media: GPHMedia,
                contentType: GPHContentType
            ) {
                val url = media.urlWithRendition(
                    GPHRenditionTypeFixedWidth,
                    GPHFileExtensionGif
                ) ?: media.contentUrl() ?: media.source()

                if (url != null) {
                    onSelection(url)
                    giphyViewController.dismissViewControllerAnimated(true, null)
                }
            }
            override fun didDismissWithController(controller: GiphyViewController?) {
                onCancel()
            }
        })
    }

    UIApplication.rootViewController()?.presentViewController(giphy, animated = true, completion = null)
}