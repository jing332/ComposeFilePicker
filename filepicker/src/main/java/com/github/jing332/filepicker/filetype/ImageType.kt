package com.github.jing332.filepicker.filetype

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.github.jing332.filepicker.model.IFileModel

object ImageType : IFileType() {
    override fun verify(model: IFileModel, mimeType: String): Boolean {
        return mimeType.startsWith("image")
    }

    @Composable
    override fun IconContent() {
        Icon(Icons.Default.Image, "image")
    }
}