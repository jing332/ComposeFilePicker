package com.github.jing332.filepicker.model

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import java.io.InputStream
import java.io.OutputStream

class AndroidDocumentFile(private val context: Context, val file: DocumentFile) : IFileModel() {
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
        return file.listFiles().map { AndroidDocumentFile(context, it) }
    }

    override fun inputStream(): InputStream {
        return context.contentResolver.openInputStream(file.uri)!!
    }

    override fun outputStream(): OutputStream {
        return context.contentResolver.openOutputStream(file.uri)!!
    }

    override fun createFile(name: String): IFileModel {
        val f = file.createFile("", name)
        return AndroidDocumentFile(context, f!!)
    }

    override fun createDirectory(name: String): IFileModel {
        val f = file.createDirectory(name)
        return AndroidDocumentFile(context, f!!)
    }
}