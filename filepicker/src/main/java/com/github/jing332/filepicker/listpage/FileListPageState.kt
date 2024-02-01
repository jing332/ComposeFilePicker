package com.github.jing332.filepicker.listpage

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewModelScope
import com.github.jing332.filepicker.FileFilter
import com.github.jing332.filepicker.FilePickerConfig
import com.github.jing332.filepicker.SortConfig
import com.github.jing332.filepicker.SortType
import com.github.jing332.filepicker.model.BackFileModel
import com.github.jing332.filepicker.model.IFileModel
import com.github.jing332.filepicker.utils.StringUtils.sizeToReadable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.system.measureTimeMillis

@Composable
fun rememberFileListPageState() = remember {
    FileListPageState()
}

class FileListPageState {
    val listState by lazy { LazyListState() }

    internal val items = mutableStateListOf<FileItem>()

    val selectedCount: MutableIntState = mutableIntStateOf(0)

    internal fun updateSelectedCount() {
        selectedCount.intValue = items.count { it.isChecked.value }
    }

    internal fun check(item: FileItem, checked: Boolean = true) {
        item.isChecked.value = checked
    }

    fun checkAll() {
        for (item in items) {
            check(item, true)
        }
    }

    fun uncheckAll() {
        for (item in items) {
            check(item, false)
        }
    }

    fun hasChecked(): Boolean {
        return items.any { it.isChecked.value }
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
}

internal data class FileItem(
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