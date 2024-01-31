package com.github.jing332.filepicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jing332.filepicker.model.BackFileModel
import com.github.jing332.filepicker.model.IFileModel
import com.github.jing332.filepicker.model.NormalFile
import com.github.jing332.filepicker.utils.StringUtils.sizeToReadable
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun FileListPage(
    modifier: Modifier = Modifier,
    file: IFileModel,
    onBack: () -> Unit,
    onEnter: (IFileModel) -> Unit
) {
    val vm: FileListPageViewModel = viewModel(key = file.name + "_" + file.path)

    LaunchedEffect(key1 = file) {
        if (vm.files.isEmpty())
            vm.updateFiles(file)
    }
    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = vm.listState
        ) {
            itemsIndexed(vm.files) { index, file ->
                Item(
                    modifier = Modifier,
                    isChecked = file.isChecked,
                    icon = {
                        if (file.isDirectory) {
                            Icon(
                                imageVector = Icons.Filled.Folder,
                                contentDescription = "folder"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                                contentDescription = "file"
                            )
                        }
                    },
                    title = {
                        Text(text = file.name, style = MaterialTheme.typography.titleMedium)
                    },
                    subtitle = {
//                        val date = remember(file.time) {
//                            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//                                .format(file.time)
//                        }
//                        val text = "$date | " +
//                                if (file.isDirectory) "${file.fileCount}项"
//                                else file.size.sizeToReadable()
//                        Row {
//                            Text(
//                                text = text,
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                        }
                    },
                    onClick = {
                        when (file) {
                            is BackFileModel -> onBack()

                            is NormalFile -> {
                                if (!file.isChecked && file.isDirectory)
                                    onEnter(file)
                                else {
                                    vm.updateModel(file, file.copy(isChecked = !file.isChecked))
                                }
                            }
                        }
                    },
                    onLongClick = {
                        if (file is NormalFile) {
                            vm.updateModel(file, file.copy(isChecked = !file.isChecked))
                        }
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Item(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Row(
        modifier
            .minimumInteractiveComponentSize()
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier.padding(8.dp)) {
            icon()
        }
        Column(Modifier.weight(1f)) {
            title()
            subtitle()
        }

        if (isChecked)
            Checkbox(checked = true, onCheckedChange = null)
    }
}