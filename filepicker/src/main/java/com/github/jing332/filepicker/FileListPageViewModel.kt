package com.github.jing332.filepicker

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.github.jing332.filepicker.model.BackFileModel
import com.github.jing332.filepicker.model.IFileModel
import kotlin.system.measureTimeMillis

class FileListPageViewModel : ViewModel() {
    val listState by lazy { LazyListState() }
    val files = mutableStateListOf<IFileModel>()

    fun updateFiles(file: IFileModel) {
        files.clear()
        files += BackFileModel()

        val cost = measureTimeMillis {
            files += file.files()

        }
        println("load files: $cost ms")
    }

    fun updateModel(src: IFileModel, target: IFileModel) {
        val index = files.indexOf(src)
        if (index != -1) {
            files[index] = target
        }
    }
}