package com.github.jing332.filepicker

import android.os.Bundle
import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.jing332.filepicker.listpage.FileListPageState
import com.github.jing332.filepicker.utils.navigate

@Composable
fun rememberFilePickerState(
    initialPath: String = Environment.getExternalStorageDirectory().absolutePath,
    rootPath: String = initialPath,
    rootName: String = "Storage",
    saveFilename: String = "",
) = rememberNavController().run {
    remember {
        FilePickerState(
            rootPath = rootPath,
            rootName = rootName,
            initialPath = initialPath,
            saveFilename = saveFilename,
            navController = this
        )
    }
}

class FilePickerState(
    rootPath: String,
    rootName: String = "Storage",
    initialPath: String,
    saveFilename: String,
    val navController: NavHostController
) {
    var rootPath: String by mutableStateOf(rootPath)
    var rootName: String by mutableStateOf(rootName)


    var saveFilename by mutableStateOf(saveFilename)
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