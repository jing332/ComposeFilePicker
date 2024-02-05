package com.github.jing332.filepicker

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.max

data class NavBarItem(val name: String, val path: String)

fun toNavBarItems(
    rootPath: String,
    rootName: String = "root",
    path: String,
    separator: String = "/"
): List<NavBarItem> {
    val items = mutableListOf<NavBarItem>()
    var currentPath = rootPath
    items.add(NavBarItem(rootName, rootPath))

    val paths = path.removePrefix(rootPath).split(separator).filter { it.isNotEmpty() }
    for (p in paths) {
        currentPath += separator + p
        items.add(NavBarItem(p, currentPath))
    }
    return items
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileNavBar(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    list: List<NavBarItem>,
    onClick: (NavBarItem) -> Unit,
) {
    LaunchedEffect(key1 = list.size) {
        state.animateScrollToItem(max(0, list.size - 1))
    }
    val context = LocalContext.current
    LazyRow(modifier, state = state) {
        itemsIndexed(list, key = { _, item -> item.path }) { index, item ->
            Row(
                modifier = Modifier.animateItemPlacement(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .combinedClickable(
                            onClick = {
                                onClick(item)
                            },
                            onLongClick = {
                                Toast
                                    .makeText(context, item.path, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        ),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(2.dp)
                            .defaultMinSize(minWidth = 48.dp, minHeight = 24.dp),
                        text = item.name,
                        textAlign = TextAlign.Center
                    )
                }

                if (index != list.size - 1) {
                    Icon(
                        modifier = Modifier,
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}