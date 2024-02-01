package com.github.jing332.filepicker

import com.github.jing332.filepicker.Contants.DEFAULT_ROOT_PATH
import com.github.jing332.filepicker.filetype.FileDetector
import com.github.jing332.filepicker.model.IFileModel


fun interface FileFilter {
    fun accept(file: IFileModel): Boolean
}

fun interface FileSelector {
    fun select(file: IFileModel): Boolean
}

data class FilePickerConfig(
    val rootPath: String = DEFAULT_ROOT_PATH,

    val fileDetector: FileDetector = FileDetector(),
    val fileFilter: FileFilter = FileFilter { true },
    val fileSelector: FileSelector = FileSelector { true },

    var sortConfig: SortConfig = SortConfig(),
) {
}