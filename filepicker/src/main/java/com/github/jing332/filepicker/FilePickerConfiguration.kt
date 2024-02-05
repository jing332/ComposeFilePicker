package com.github.jing332.filepicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.github.jing332.filepicker.filetype.FileDetector
import com.github.jing332.filepicker.model.IFileModel


object ViewType {
    const val LIST = 0
    const val GRID = 1
}

fun interface FileFilter {
    fun accept(file: IFileModel): Boolean
}

fun interface FileSelector {
    fun select(checkedList: List<IFileModel>, check: IFileModel): List<IFileModel>
    fun isCheckable(file: IFileModel): Boolean = true
}


data class FilePickerConfiguration(
    val fileDetector: FileDetector = FileDetector(),
    val fileFilter: FileFilter = FileFilter { true },
    val fileSelector: FileSelector = object : FileSelector {
        override fun select(checkedList: List<IFileModel>, check: IFileModel): List<IFileModel> {
            return listOf(check)
        }

        override fun isCheckable(file: IFileModel): Boolean {
            return !file.isDirectory
        }
    },

    ) {
    var viewType by mutableIntStateOf(ViewType.LIST)
    var sortConfig by mutableStateOf(SortConfig())
}