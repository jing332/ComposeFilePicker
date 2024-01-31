package com.github.jing332.filepicker.filetype

import androidx.compose.runtime.Composable
import com.github.jing332.filepicker.model.IFileModel

object CodeType : IFileType() {
    override fun verify(model: IFileModel, mimeType: String): Boolean {
        return model.name.fileExtContains(
            "json", "xml", "yaml", "yml", "kt", "java", "js", "html", "htm", "md", "markdown"
        )
    }

    @Composable
    override fun IconContent() {
        super.IconContent()
    }
}