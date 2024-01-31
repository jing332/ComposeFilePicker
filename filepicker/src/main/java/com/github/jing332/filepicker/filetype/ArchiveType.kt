package com.github.jing332.filepicker.filetype

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.github.jing332.filepicker.model.IFileModel

object ArchiveType : IFileType() {
    override fun verify(model: IFileModel, mimeType: String): Boolean {
        return model.name.fileExtContains("zip", "rar", "7z", "tar", "gz", "bz2", "xz")
    }

    @Composable
    override fun IconContent() {
        Icon(Icons.Default.Archive, contentDescription = "Archive")
    }
}