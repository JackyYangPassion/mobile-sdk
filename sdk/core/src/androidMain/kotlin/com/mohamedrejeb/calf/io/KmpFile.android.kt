package com.mohamedrejeb.calf.io

import java.io.File

/**
 * An typealias representing a file in the platform specific implementation
 */
internal actual typealias KmpFile = File

internal actual fun createKmpFile(path: String): KmpFile? = File(path)

internal actual fun KmpFile.exists() = this.exists()

internal actual fun KmpFile.readByteArray(): ByteArray = this.readBytes()

internal actual val KmpFile.name: String?
    get() = this.name

internal actual val KmpFile.path: String?
    get() = this.path

internal actual val KmpFile.isDirectory: Boolean
    get() = this.isDirectory