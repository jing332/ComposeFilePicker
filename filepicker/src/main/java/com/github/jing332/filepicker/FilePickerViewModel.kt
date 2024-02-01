package com.github.jing332.filepicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.github.jing332.filepicker.listpage.FileListPageState

class FilePickerViewModel : ViewModel() {
    var currentPath by mutableStateOf("")
    val fileListStates = mutableMapOf<String, FileListPageState>()

}