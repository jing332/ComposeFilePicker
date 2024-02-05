package com.github.jing332.filepicker.model

import java.io.File

data class NormalFile(
    private val file: File,
) : IFileModel() {
    override val name: String
        get() = file.name
    override val path: String
        get() = file.absolutePath
    override val isDirectory: Boolean
        get() = file.isDirectory
    override val fileCount: Int
        get() = file.list()?.size ?: 0
    override val time: Long
        get() = file.lastModified()
    override val size: Long
        get() = file.length()

    override fun files(): List<IFileModel> {
        return file.listFiles()?.map { NormalFile(it) } ?: emptyList()
    }

    override fun createDirectory(name: String): IFileModel {
        val f = file.resolve(name)
        f.mkdir()
        return NormalFile(f)
    }

    override fun createFile(name: String): IFileModel {
        val f = file.resolve(name)
        f.createNewFile()
        return NormalFile(f)
    }

    override fun inputStream() = file.inputStream()
    override fun outputStream() = file.outputStream()
}