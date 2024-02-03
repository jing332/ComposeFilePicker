package com.github.jing332.filepicker.model

import androidx.documentfile.provider.DocumentFile

class AndroidDocumentFile(val file: DocumentFile) : IFileModel() {
    override val name: String
        get() = file.name ?: ""

    override val path: String
        get() = file.uri.toString()

    override val isDirectory: Boolean
        get() = file.isDirectory

    override val size: Long
        get() = file.length()

    override val time: Long
        get() = file.lastModified()

    override fun files(): List<IFileModel> {
        return file.listFiles().map { AndroidDocumentFile(it) }
    }
}