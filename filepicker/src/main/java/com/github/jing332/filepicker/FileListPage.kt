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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jing332.filepicker.filetype.FileDetector
import com.github.jing332.filepicker.model.IFileModel


@Composable
fun FileListPage(
    modifier: Modifier = Modifier,
    file: IFileModel,
    onBack: () -> Unit,
    onEnter: (IFileModel) -> Unit
) {
    val vm: FileListPageViewModel = viewModel(key = file.name + "_" + file.path)
    val isSelectMode by rememberUpdatedState(newValue = vm.hasChecked())

    LaunchedEffect(key1 = file) {
        if (vm.files.isEmpty())
            vm.updateFiles(file)
    }
    LazyColumn(
        modifier = modifier,
        state = vm.listState
    ) {
        itemsIndexed(vm.files, key = { _, item -> item.key }) { _, item ->
            Item(
                isChecked = item.isChecked,
                icon = {
                    if (item.isDirectory) {
                        Icon(
                            imageVector = Icons.Filled.Folder,
                            contentDescription = stringResource(R.string.folder)
                        )
                    } else {
                        val fileType = FileDetector.detect(item.model)
                        if (fileType == null)
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.InsertDriveFile,
                                contentDescription = stringResource(R.string.file)
                            )
                        else
                            fileType.IconContent()
                    }
                },
                title = {
                    Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                },
                subtitle = {
                    val text = if (item.isBackType)
                        stringResource(R.string.back_to_previous_dir)
                    else
                        "${item.fileLastModified.value} | " +
                                if (item.isDirectory) stringResource(
                                    R.string.item_desc,
                                    item.fileCount.intValue
                                )
                                else item.fileSize.value
                    Row {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                onClick = {
                    if (item.isBackType)
                        onBack()
                    else if (isSelectMode && item.isCheckable)
                        vm.updateModel(item.copy(isChecked = !item.isChecked))
                    else if (!item.isChecked && item.isDirectory)
                        onEnter(item.model)
                    else
                        vm.updateModel(item.copy(isChecked = !item.isChecked))
                },
                onLongClick = {
                    if (item.isCheckable) {
                        vm.updateModel(item.copy(isChecked = !item.isChecked))
                    }
                }
            )
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
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(vertical = 4.dp),
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