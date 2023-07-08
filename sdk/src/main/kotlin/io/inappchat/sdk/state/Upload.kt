package io.inappchat.sdk.state

import android.webkit.MimeTypeMap
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.inappchat.sdk.Servers
import io.inappchat.sdk.type.AttachmentInput
import io.inappchat.sdk.type.AttachmentType
import io.inappchat.sdk.utils.Monitoring
import io.inappchat.sdk.utils.bg
import io.inappchat.sdk.utils.op
import io.inappchat.sdk.utils.uuid
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.internal.closeQuietly
import org.json.JSONObject
import java.io.File
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


fun File.mimeType(): String =
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(this.extension)
                ?: "application/octet-stream"

val uploadClient = OkHttpClient.Builder().build()

@Stable
data class Upload(val id: String = uuid(), val file: File) {
    var uploading by mutableStateOf(false)
    var url by mutableStateOf<String?>(null)
    var error by mutableStateOf<Error?>(null)

    private var _await: Continuation<String>? = null
    suspend fun await() = suspendCoroutine<String> { cont ->
        url?.let {
            cont.resume(it)
        } ?: error?.let {
            cont.resumeWithException(Error(it))
        } ?: run {
            _await = cont
            if (!uploading) upload()
        }
    }

    suspend fun awaitAttachment() = await().let { attachment()!! }

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
                error = err
            } finally {
                response?.closeQuietly()
            }
            if (url == null && error == null) {
                error = Error("Unknown error occurred")
            }
            uploading = false
            url?.let {
                _await?.resume(it)
                _await = null
            } ?: error?.let {
                _await?.resumeWithException(it)
                _await = null
            }
        })
    }

    fun attachmentType(): AttachmentType {
        val mime = file.mimeType()
        if (mime.startsWith("image")) return AttachmentType.image
        if (mime.startsWith("video")) return AttachmentType.video
        if (mime.startsWith("audio")) return AttachmentType.audio
        return AttachmentType.file
    }

    fun attachment() = url?.let { AttachmentInput(url = it, type = attachmentType(), id = id) }
}
