package com.github.jing332.filepicker

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BasicToolbar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
    actions: @Composable() (RowScope.() -> Unit) = {},
) {
    Column(modifier) {
        TopAppBar(title = title, navigationIcon = navigationIcon, actions = actions)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .shadow(4.dp)
        )
    }
}

@Composable
fun FilePickerToolbar(
    modifier: Modifier,
    title: String,
    sortConfig: SortConfig,
    onSortConfigChange: (SortConfig) -> Unit,

    selectedCount: Int,
    onCancelSelect: () -> Unit,
    onConfirmSelect: () -> Unit,
) {
    Crossfade(targetState = selectedCount > 0) {
        if (it)
            BasicToolbar(title = { Text(text = "$selectedCount") }, modifier = modifier,
                navigationIcon = {
                    IconButton(onClick = onCancelSelect) {
                        Icon(Icons.Default.Close, stringResource(R.string.cancel_select))
                    }
                },
                actions = {
                    TextButton(onClick = onConfirmSelect) {
                        Text(stringResource(id = R.string.select))
                    }
//                    IconButton(onClick = {  }) {
//                        Icon(Icons.Default.MoreVert, stringResource(R.string.more_options))
//                    }
                })
        else
            BasicToolbar(modifier = modifier, title = { Text(title) }, actions = {
//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(Icons.Default.Search, stringResource(R.string.search))
//        }
                var showSortConfigDialog by remember { mutableStateOf(false) }
                if (showSortConfigDialog)
                    SortSettingsDialog(
                        onDismissRequest = { showSortConfigDialog = false },
                        sortConfig = sortConfig,
                        onConfirm = onSortConfigChange
                    )
                var showOptions by rememberSaveable { mutableStateOf(false) }
                IconButton(onClick = { showOptions = true }) {
                    Icon(Icons.Default.MoreVert, stringResource(R.string.more_options))
                    if (showOptions)
                        DropdownMenu(
                            expanded = showOptions,
                            onDismissRequest = { showOptions = false }) {
                            DropdownMenuItem(
                                text = { Text(stringResource(id = R.string.sort_by)) },
                                onClick = {
                                    showOptions = false
                                    showSortConfigDialog = true
                                }
                            )
                        }
                }
            })
    }
}
