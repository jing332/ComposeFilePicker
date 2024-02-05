package com.github.jing332.filepicker.model

import java.io.InputStream
import java.io.OutputStream

abstract class IFileModel {
    open val name: String = ""
    open val path: String = ""
    open val isDirectory: Boolean = false
    open val fileCount: Int = 0
    open val time: Long = 0
    open val size: Long = 0

    open fun createDirectory(name: String): IFileModel = throw NotImplementedError()
    open fun createFile(name: String): IFileModel = throw NotImplementedError()
    open fun inputStream(): InputStream = throw NotImplementedError()
    open fun outputStream(): OutputStream = throw NotImplementedError()

    open fun files(): List<IFileModel> = emptyList()
}