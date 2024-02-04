package com.github.jing332.filepicker


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

object SearchType {
    const val ALL = 0
    const val FILE = 1
    const val FOLDER = 2
}

@Composable
internal fun SearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,

    type: Int,
    onTypeChange: (Int) -> Unit,

    onClose: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var showTypeOptions by rememberSaveable { mutableStateOf(false) }
        IconButton(onClick = { showTypeOptions = true }) {
            Icon(Icons.Default.AccountTree, stringResource(id = R.string.search_type))
            DropdownMenu(
                expanded = showTypeOptions,
                onDismissRequest = { showTypeOptions = false }) {
                RadioDropdownMenuItem(text = {
                    Text(stringResource(id = R.string.all))
                }, checked = type == SearchType.ALL) {
                    onTypeChange(SearchType.ALL)
                }

                RadioDropdownMenuItem(text = {
                    Text(stringResource(id = R.string.file))
                }, checked = type == SearchType.FILE) {
                    onTypeChange(SearchType.FILE)
                }

                RadioDropdownMenuItem(text = {
                    Text(stringResource(id = R.string.folder))
                }, checked = type == SearchType.FOLDER) {
                    onTypeChange(SearchType.FOLDER)
                }
            }
        }

        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleMedium) {
            DenseTextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .weight(1f),
                value = value,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.extraLarge,
                placeholder = {
                    Text(stringResource(id = R.string.search), maxLines = 1)
                },
                singleLine = true,
            )

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, stringResource(id = R.string.cancel_search))
            }
        }
    }
}