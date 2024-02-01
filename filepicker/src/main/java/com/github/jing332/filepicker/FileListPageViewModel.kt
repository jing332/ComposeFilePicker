package com.github.jing332.filepicker

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jing332.filepicker.Contants.DEFAULT_ROOT_URI
import com.github.jing332.filepicker.model.BackFileModel
import com.github.jing332.filepicker.model.IFileModel
import com.github.jing332.filepicker.utils.StringUtils.sizeToReadable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.system.measureTimeMillis

class FileListPageViewModel : ViewModel() {
    private val dateFormatter by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    val listState by lazy { LazyListState() }
    internal val files = mutableStateListOf<FileItem>()


    fun hasChecked(): Boolean {
        return files.any { it.isChecked }
    }

    private fun IFileModel.filesSorted(): List<IFileModel> {
        return this.files().sortedWith(
            compareBy(
                { !it.isDirectory },
                { it.name.lowercase(Locale.getDefault()) }
            )
        )
    }

    fun updateFiles(file: IFileModel) {
        files.clear()
        if (file.path != DEFAULT_ROOT_URI)
            files += FileItem(BackFileModel(), isCheckable = false, isBackType = true)

        val cost = measureTimeMillis {
            files += file.filesSorted().map { FileItem(it) }
        }
        println("load files: $cost ms")

        viewModelScope.launch(Dispatchers.Main) {
            for (item in files) {
                item.fileCount.intValue = withContext(Dispatchers.IO) { item.model.fileCount }
                item.fileSize.value =
                    withContext(Dispatchers.IO) { item.model.size.sizeToReadable() }
                item.fileLastModified.value = withContext(Dispatchers.IO) {
                    dateFormatter.format(item.model.time)
                }


            }
        }
    }

    fun updateModel(item: FileItem) {
        val index = files.indexOfFirst { it.key == item.key }
        if (index != -1)
            files[index] = item
    }
}

data class FileItem(
    val model: IFileModel,
    val key: String = model.path,
    val name: String = model.name,
    val isDirectory: Boolean = model.isDirectory,
    val isChecked: Boolean = false,
    val isCheckable: Boolean = true,
    val isBackType: Boolean = false,

    val fileCount: MutableIntState = mutableIntStateOf(0),
    val fileSize: MutableState<String> = mutableStateOf("0"),
    val fileLastModified: MutableState<String> = mutableStateOf(""),

    val icon: @Composable() (() -> Unit)? = null,
)