package ai.botstacks.sdk.utils

import ai.botstacks.sdk.state.contentType
import androidx.core.net.toUri
import com.mohamedrejeb.calf.io.KmpFile

actual fun KmpFile.contentType(): String? = toUri().contentType()?.type