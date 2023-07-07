package io.inappchat.sdk.state

import android.webkit.MimeTypeMap
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.util.Util
import io.inappchat.sdk.Servers
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import io.inappchat.sdk.utils.uuid
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.Okio
import okio.Source
import okio.source
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.io.InputStream


fun File.mimeType(): String =
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(this.extension)
                ?: "application/octet-stream"

val uploadClient = OkHttpClient.Builder().build()

@Stable
data class Upload(val id: String = uuid(), val file: File) {
    var progress by mutableStateOf(0.0)
    var uploading by mutableStateOf(false)
    var url by mutableStateOf<String?>(null)
    var error by mutableStateOf<Error?>(null)


    fun upload() {
        if (url != null) return
        if (uploading) return
        uploading = true
        error = null
        op({
            var response: Response? = null
            try {
                response = bg {
                    val body = file.asRequestBody(file.mimeType().toMediaTypeOrNull())
                    val request = Request.Builder().url(Servers.get().http + "/misc/upload/${file.name}")
                            .post(body)
                            .build()
                    uploadClient.newCall(request).execute()
                }
                if (!response.isSuccessful) {
                    Monitoring.error("Upload response code: " + response.code + " Message: " + response.message)
                } else {
                    url = response.body?.string()?.let { JSONObject(it).getString("url") }
                }
            } catch (err: Error) {
                Monitoring.error(err)
            } finally {
                response?.closeQuietly()
            }
            if (url == null && error == null) {
                error = Error("Unknown error occurred")
            }
            uploading = false
        })
    }
}
