package com.github.jing332.filepicker.filetype

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.github.jing332.filepicker.model.IFileModel

object AudioType : IFileType() {
    override val name: String = "Audio"

    override fun verify(model: IFileModel, mimeType: String): Boolean {
        return mimeType.startsWith("audio")
    }

    @Composable
    override fun IconContent() {
        Icon(Icons.Default.AudioFile, name)
    }
}