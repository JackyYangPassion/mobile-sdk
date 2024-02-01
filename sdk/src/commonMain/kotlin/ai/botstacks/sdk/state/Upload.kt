package ai.botstacks.sdk.state

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ai.botstacks.sdk.API
import ai.botstacks.sdk.BotStacksChat
import ai.botstacks.sdk.Server
import ai.botstacks.sdk.type.AttachmentInput
import ai.botstacks.sdk.type.AttachmentType
import ai.botstacks.sdk.utils.Monitoring
import ai.botstacks.sdk.utils.bg
import ai.botstacks.sdk.utils.contentType
import ai.botstacks.sdk.utils.op
import ai.botstacks.sdk.utils.uuid
import androidx.core.net.toFile
import com.benasher44.uuid.uuid4
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.path
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.headersOf
import io.ktor.http.set
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.source
import org.json.JSONObject
import java.util.UUID
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
            var response: HttpResponse? = null
            try {
                response = bg {
                    val body = file.asRequestBody()
                    uploadClient.request {
                        url(Server.http + "/misc/upload/${uuid4()}")
                        headers {
                            set("X-API-Key", BotStacksChat.shared.apiKey)
                            set("X-Device-ID", API.deviceId)
                            set("Referer",  BotStacksChat.shared.appIdentifier)
                        }
                        setBody(body)
                    }
                }
                if (response.status.value in 200..299) {
                    url = response.body<JsonObject>().let { it["url"].toString() }
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

    fun localAttachment() = AttachmentInput(url = file.path.orEmpty(), type = attachmentType(), id = id)

}
