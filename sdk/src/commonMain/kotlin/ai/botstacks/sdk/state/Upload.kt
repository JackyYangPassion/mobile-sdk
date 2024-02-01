package ai.botstacks.sdk.state

import ai.botstacks.sdk.API
import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.Server
import ai.botstacks.sdk.type.AttachmentInput
import ai.botstacks.sdk.type.AttachmentType
import ai.botstacks.sdk.utils.Monitoring
import ai.botstacks.sdk.utils.bg
import ai.botstacks.sdk.utils.contentType
import ai.botstacks.sdk.utils.op
import ai.botstacks.sdk.utils.size
import ai.botstacks.sdk.utils.uuid
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.benasher44.uuid.uuid4
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.name
import com.mohamedrejeb.calf.io.path
import com.mohamedrejeb.calf.io.readByteArray
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


val uploadClient = HttpClient()


@Stable
data class Upload(val id: String = uuid(), val file: KmpFile) {

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
            try {
                val response = bg {
                    uploadClient.post {
                        url(Server.http + "/misc/upload/${uuid4()}")
                        headers {
                            append("X-API-Key", BotStacksChat.shared.apiKey)
                            append("X-Device-ID", API.deviceId)
                            append("Referer", BotStacksChat.shared.appIdentifier)
                            append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                            append(HttpHeaders.ContentType, file.contentType().orEmpty())
                            append(HttpHeaders.ContentLength, file.size().toString())
                        }

                        setBody(file.readByteArray())
                    }
                }
                if (response.status.value in 200..299) {
                    url = response.body<ByteArray>().decodeToString().let {
                        Json.decodeFromString<JsonObject>(it)
                    }["url"].toString().removeSurrounding("\"")
                } else {
                    val message = response.body<ByteArray>().decodeToString()
                    Monitoring.error("Upload response code: " + response.status.value + " Message: " + message)
                }
            } catch (err: Error) {
                println("Upload error")
                Monitoring.error(err)
                error = err
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
        val mime = file.contentType()
        if (mime != null) {
            if (mime.startsWith("image")) return AttachmentType.image
            if (mime.startsWith("video")) return AttachmentType.video
            if (mime.startsWith("audio")) return AttachmentType.audio
        }
        return AttachmentType.file
    }

    fun attachment() = url?.let { AttachmentInput(url = it, type = attachmentType(), id = id) }

    fun localAttachment() =
        AttachmentInput(url = file.path.orEmpty(), type = attachmentType(), id = id)

}

private fun customMultiPartMixedDataContent(
    file: KmpFile,
    parts: List<PartData>
): MultiPartFormDataContent {
    val mimeType = file.contentType()
    val contentType = when {
        mimeType == "image/jpeg" -> ContentType.Image.JPEG
        mimeType == "image/png" -> ContentType.Image.PNG
        mimeType == "image/gif" -> ContentType.Image.GIF
        mimeType?.startsWith("image/*") == true -> ContentType.Image.Any
        mimeType?.startsWith("video/*") == true -> ContentType.Video.Any
        mimeType?.startsWith("audio/*") == true -> ContentType.Audio.Any
        mimeType == "application/pdf" -> ContentType.Application.Pdf
        else -> ContentType.Application.OctetStream
    }
    return MultiPartFormDataContent(parts = parts, contentType = contentType)
}
