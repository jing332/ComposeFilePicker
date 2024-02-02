package com.github.jing332.filepicker.listpage

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.jing332.filepicker.FileFilter
import com.github.jing332.filepicker.FilePickerConfig
import com.github.jing332.filepicker.SortConfig
import com.github.jing332.filepicker.SortType
import com.github.jing332.filepicker.model.BackFileModel
import com.github.jing332.filepicker.model.IFileModel
import com.github.jing332.filepicker.utils.StringUtils.sizeToReadable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.system.measureTimeMillis

@Composable
fun rememberFileListPageState() = remember {
    FileListPageState()
}

class FileListPageState() {
    companion object {
        private val dateFormatter by lazy {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        }
    }

    val listState by lazy { LazyListState() }

    internal var config: FilePickerConfig? = null
    internal val items = mutableStateListOf<FileItem>()

    internal fun models() = items.map { it.model }
    internal fun findCheckedItems() = items.filter { it.isChecked.value }

    internal fun check(item: FileItem, checked: Boolean = true) {
        item.isChecked.value = checked
    }

    internal fun selector(item: FileItem): Boolean {
        if (item.isBackType) return false

        if (item.isChecked.value) {
            item.isChecked.value = false
            return false
        }

        val list = config!!.fileSelector.select(findCheckedItems().map { it.model }, item.model)
        for (i in items) {
            i.isChecked.value = list.find { it == i.model } != null
        }

        return list.find { it == item.model } != null
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

    internal suspend fun updateFiles(file: IFileModel) = coroutineScope {
        items.clear()
        if (file.path != config!!.rootPath)
            items += FileItem(BackFileModel(), isBackType = true).apply {
                isCheckable.value = false
            }

        val cost = measureTimeMillis {
            items += file.filesSortAndFilter(config!!.sortConfig, config!!.fileFilter).map {
                FileItem(it)
            }
        }
        println("load files: $cost ms")

        launch(Dispatchers.Main) {
            for (item in items) {
                item.fileCount.intValue = withContext(Dispatchers.IO) { item.model.fileCount }
                item.fileSize.value =
                    withContext(Dispatchers.IO) { item.model.size.sizeToReadable() }
                item.fileLastModified.value = withContext(Dispatchers.IO) {
                    dateFormatter.format(item.model.time)
                }

                item.isCheckable.value = config!!.fileSelector.isCheckable(item.model)
            }
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