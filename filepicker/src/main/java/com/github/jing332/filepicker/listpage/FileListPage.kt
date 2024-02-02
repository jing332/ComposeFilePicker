package com.github.jing332.filepicker.listpage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.github.jing332.filepicker.FilePickerConfig
import com.github.jing332.filepicker.R
import com.github.jing332.filepicker.ViewType
import com.github.jing332.filepicker.model.IFileModel
import com.github.jing332.filepicker.utils.performLongPress


@Composable
fun FileListPage(
    modifier: Modifier = Modifier,
    config: FilePickerConfig = FilePickerConfig(),
    state: FileListPageState = FileListPageState(),
    file: IFileModel,
    onBack: () -> Unit,
    onEnter: (IFileModel) -> Unit,
) {
    val hasChecked by rememberUpdatedState(newValue = state.hasChecked())

    val view = LocalView.current
    LaunchedEffect(key1 = config) {
        state.config = config
    }
    LaunchedEffect(key1 = file) {
        if (state.items.isEmpty())
            state.updateFiles(file)
    }

    LaunchedEffect(key1 = hasChecked) {
        if (hasChecked) view.performLongPress()
    }

    @Composable
    fun itemContent(item: FileItem) {
        Item(
            isChecked = item.isChecked.value,
            isCheckable = if (item.isBackType) false else item.isCheckable.value,
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
            onCheckedChange = { _ ->
                state.selector(item)
            },
            onClick = {
                if (item.isBackType)
                    onBack()
                else if (!hasChecked && !item.isChecked.value && item.isDirectory)
                    onEnter(item.model)
                else state.selector(item)
            },
            onLongClick = {
                if (item.isBackType) onBack()
                else state.selector(item)
            },
            gridType = config.viewType == ViewType.GRID
        )
    }

    if (config.viewType == ViewType.GRID)
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(2),
            state = state.gridState
        ) {
            itemsIndexed(state.items, key = { _, item -> item.key }) { _, item ->
                itemContent(item = item)
            }
        }
    else
        LazyColumn(
            modifier = modifier,
            state = state.listState
        ) {
            itemsIndexed(state.items, key = { _, item -> item.key }) { _, item ->
                itemContent(item = item)
            }
        }
}


@Composable
private fun Item(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    isCheckable: Boolean = true,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    gridType: Boolean
) {
    if (gridType)
        ShortItem(
            modifier = modifier,
            isChecked = isChecked,
            onCheckedChange = onCheckedChange,
            isCheckable = isCheckable,
            icon = icon,
            title = title,
            subtitle = subtitle,
            onClick = onClick,
            onLongClick = onLongClick
        )
    else
        LongItem(
            modifier = modifier,
            isChecked = isChecked,
            onCheckedChange = onCheckedChange,
            isCheckable = isCheckable,
            icon = icon,
            title = title,
            subtitle = subtitle,
            onClick = onClick,
            onLongClick = onLongClick
        )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LongItem(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    isCheckable: Boolean = true,
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
            .combinedClickable(
                onClick = onClick,
                onLongClick = if (isCheckable) onLongClick else null,
                onLongClickLabel = if (isCheckable) {
                    if (isChecked) "Uncheck" else "Check"
                } else
                    null
            )
            .background(color = if (isChecked) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
            .padding(vertical = 4.dp)
            .semantics(mergeDescendants = true) {
                role = Role.Checkbox
                if (isCheckable) selected = isChecked
            },
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShortItem(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    isCheckable: Boolean = true,
    icon: @Composable () -> Unit,
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .padding(4.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = if (isCheckable) onLongClick else null,
                onLongClickLabel = if (isCheckable) {
                    if (isChecked) "Uncheck" else "Check"
                } else
                    null
            ),
        colors = CardDefaults.outlinedCardColors(containerColor = if (isChecked) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .semantics(mergeDescendants = true) {
                    role = Role.Checkbox
                    if (isCheckable) selected = isChecked
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Column(Modifier.weight(1f)) {
                title()
//                subtitle()
            }
            AnimatedVisibility(visible = isCheckable) {
                Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
            }
        }

    }
}