package com.github.jing332.filepicker.filetype

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.github.jing332.filepicker.model.IFileModel

object DatabaseType : IFileType() {
    override fun verify(model: IFileModel, mimeType: String): Boolean {
        return model.name.fileExtContains("db")
    }

    @Composable
    override fun IconContent() {
        Icon(Icons.Default.DataUsage, contentDescription = "Database")
    }
}