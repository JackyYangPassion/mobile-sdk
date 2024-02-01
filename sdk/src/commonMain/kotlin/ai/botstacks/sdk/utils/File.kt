package ai.botstacks.sdk.utils

import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.io.readByteArray

expect fun KmpFile.contentType(): String?
expect fun KmpFile.size(): Long

//fun KmpFile.readChannel(): Sequence<Byte> {
//    return this.readByteArray().asSequence()
//}