package io.inappchat.inappchat.utils

import android.webkit.MimeTypeMap

/**
 * Created by DK on 24/08/20.
 */
object Utils {

    val pattern = Regex("<@+[a-zA-Z\\d._ @-]+>|<@channel>|<@here>")
    val copyPattern = Regex("<@+[a-zA-Z\\d. _]+>|<@channel>|<@here>")

    fun getMimeType(url: String, mediaType: String): String {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }

        if (type == null) {
            val extension = url.substring(url.lastIndexOf(".") + 1)
            type = "$mediaType/$extension"
        }
        return type
    }

    fun getFileName(filePath: String): String? {
        var fileName = filePath.substring(filePath.lastIndexOf('/') + 1)
        if (fileName.length > 30) {
            fileName = fileName.substring(fileName.length - 30, fileName.length)
        }
        return fileName
    }

}