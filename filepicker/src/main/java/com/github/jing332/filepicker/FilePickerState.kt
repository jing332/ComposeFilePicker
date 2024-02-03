package com.github.jing332.filepicker

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.github.jing332.filepicker.listpage.FileListPageState
import com.github.jing332.filepicker.utils.navigate

data class FilePickerState(
    val rootPath: String,
    val initialPath: String,
    val navController: NavHostController
) {
    //    var currentModel by mutableStateOf<IFileModel?>(null)
    var currentPath by mutableStateOf(initialPath)
    val fileListStates = mutableMapOf<String, FileListPageState>()

    val currentListState get() = fileListStates[currentPath]

    fun getListState(path: String): FileListPageState {
        return fileListStates[path] ?: FileListPageState().apply {
            fileListStates[path] = this
        }
    }

    fun navigate(path: String, state: FileListPageState = FileListPageState()) {
        fileListStates[path] = state
        navController.navigate(Contants.ROUTE_PAGE, Bundle().apply {
            putString(Contants.ARG_PATH, path)
        })
    }

    fun reload(path: String = currentPath) {
        navController.popBackStack()
        navigate(path)
    }
}