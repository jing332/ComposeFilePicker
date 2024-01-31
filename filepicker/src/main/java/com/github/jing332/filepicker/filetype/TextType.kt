package com.github.jing332.filepicker.filetype

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.github.jing332.filepicker.model.IFileModel

object TextType : IFileType() {
    override val name: String = "Text"

    override fun verify(model: IFileModel, mimeType: String): Boolean {
        return mimeType.startsWith("text")
    }

    @Composable
    override fun IconContent() {
        Icon(Icons.AutoMirrored.Filled.TextSnippet, name)
    }

}