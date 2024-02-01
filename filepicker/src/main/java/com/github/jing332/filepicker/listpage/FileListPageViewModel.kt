package com.github.jing332.filepicker.listpage

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
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
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.system.measureTimeMillis

class FileListPageViewModel : ViewModel() {
    private val dateFormatter by lazy {
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    internal var state: FileListPageState? = null
    internal val files: SnapshotStateList<FileItem>
        get() = state?.items!!


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
                FileItem(it)
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
}
