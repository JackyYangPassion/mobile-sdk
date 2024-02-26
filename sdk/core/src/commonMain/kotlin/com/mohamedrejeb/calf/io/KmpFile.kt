package com.mohamedrejeb.calf.io

/**
 * An typealias representing a file in the platform specific implementation
 */
expect class KmpFile

internal expect fun createKmpFile(path: String): KmpFile?

internal expect fun KmpFile.exists(): Boolean

internal expect fun KmpFile.readByteArray(): ByteArray

internal expect val KmpFile.name: String?

internal expect val KmpFile.path: String?

internal expect val KmpFile.isDirectory: Boolean

internal val KmpFile.isFile: Boolean
    get() = !this.isDirectory
