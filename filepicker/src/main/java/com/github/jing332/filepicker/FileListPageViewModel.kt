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
import com.github.jing332.filepicker.model.BackFileModel
import com.github.jing332.filepicker.model.IFileModel
import com.github.jing332.filepicker.utils.StringUtils.sizeToReadable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        return files.any { it.isChecked.value }
    }

    private fun IFileModel.filesSortAndFilter(
        sort: SortConfig,
        filter: FileFilter
    ): List<IFileModel> {
        return this.files().filter { filter.accept(it) }.sortedWith(
            compareBy(
                { !it.isDirectory },
                {
                    val str = when (sort.sortBy) {
                        SortType.NAME -> it.name
                        SortType.SIZE -> it.size.toString()
                        SortType.DATE -> it.time.toString()
                        SortType.TYPE -> it.name.split(".").lastOrNull() ?: ""
                        else -> it.name
                    }
                    str.lowercase(Locale.getDefault())
                }
            )
        ).run {
            if (sort.reverse) reversed() else this
        }
    }

    fun updateFiles(file: IFileModel, config: FilePickerConfig) {
        files.clear()
        if (file.path != config.rootPath)
            files += FileItem(BackFileModel(), isBackType = true).apply {
                isCheckable.value = false
            }

        val cost = measureTimeMillis {
            files += file.filesSortAndFilter(config.sortConfig, config.fileFilter).map {
                FileItem(it).apply {
                    isCheckable.value = config.fileSelector.select(model)
                }
            }
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

    fun selectedCount(): Int {
        return files.count { it.isChecked.value }
    }

    fun cancelSelect() {
        files.forEach { it.isChecked.value = false }
    }
}

data class FileItem(
    val model: IFileModel,
    val key: String = model.path,
    val name: String = model.name,
    val isDirectory: Boolean = model.isDirectory,
    val isBackType: Boolean = false,

    val isChecked: MutableState<Boolean> = mutableStateOf(false),
    val isCheckable: MutableState<Boolean> = mutableStateOf(true),

    val fileCount: MutableIntState = mutableIntStateOf(0),
    val fileSize: MutableState<String> = mutableStateOf("0"),
    val fileLastModified: MutableState<String> = mutableStateOf(""),

    val icon: @Composable() (() -> Unit)? = null,
)