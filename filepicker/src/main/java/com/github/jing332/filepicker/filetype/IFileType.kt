package com.github.jing332.filepicker.filetype

import androidx.compose.runtime.Composable
import com.github.jing332.filepicker.model.IFileModel
import java.net.URLConnection

abstract class IFileType {
    open val name: String = ""

    open fun verify(model: IFileModel, mimeType: String): Boolean = false
    fun verify(model: IFileModel): Boolean =
        verify(model, URLConnection.getFileNameMap().getContentTypeFor(model.name) ?: "")


    @Composable
    open fun IconContent() {
    }

    fun String.fileExtContains(vararg names: String): Boolean {
        for (name in names) {
            if (this.endsWith(".$name", ignoreCase = true))
                return true
        }

        return false
    }
}