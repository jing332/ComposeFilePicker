package com.github.jing332.filepicker

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.jing332.filepicker.model.IFileModel
import com.github.jing332.filepicker.utils.performLongPress


@Composable
fun FileListPage(
    modifier: Modifier = Modifier,
    file: IFileModel,
    onBack: () -> Unit,
    onEnter: (IFileModel) -> Unit,

    selectedCount: Int,
    onSelectedCountChange: (Int) -> Unit
) {
    val vm: FileListPageViewModel = viewModel(key = file.name + "_" + file.path)
    val hasChecked by rememberUpdatedState(newValue = vm.hasChecked())
    val config = LocalFilePickerConfig.current

    val view = LocalView.current
    LaunchedEffect(key1 = file) {
        if (vm.files.isEmpty())
            vm.updateFiles(file, config)
    }

    LaunchedEffect(key1 = hasChecked) {
        if (hasChecked) view.performLongPress()
    }

    LaunchedEffect(key1 = selectedCount) {
        if (selectedCount == 0)
            vm.cancelSelect()
    }

    LazyColumn(
        modifier = modifier,
        state = vm.listState
    ) {
        itemsIndexed(vm.files, key = { _, item -> item.key }) { _, item ->
            fun check(checked: Boolean = !item.isChecked.value) {
                item.isChecked.value = checked
                onSelectedCountChange(vm.selectedCount())
            }

            Item(
                isChecked = item.isChecked.value,
                isCheckable = item.isCheckable.value,
                icon = {
                    if (item.isDirectory) {
                        Icon(
                            imageVector = Icons.Filled.Folder,
                            contentDescription = stringResource(R.string.folder)
                        )
                    } else {
                        val fileType = config.fileDetector.detect(item.model)
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
                onCheckedChange = {
                    item.isChecked.value = it
                },
                onClick = {
                    if (item.isBackType)
                        onBack()
                    else if (!hasChecked && !item.isChecked.value && item.isDirectory)
                        onEnter(item.model)
                    else if (config.fileSelector.select(item.model))
                        check()
                    else
                        check()
                },
                onLongClick = {
                    if (item.isBackType)
                        onBack()
                    else if (!config.fileSelector.select(item.model)) return@Item
                    else if (item.isCheckable.value) {
                        check()
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
    onCheckedChange: (Boolean) -> Unit,
    isCheckable: Boolean = false,
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

        AnimatedVisibility(visible = isCheckable) {
            Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
        }
    }
}