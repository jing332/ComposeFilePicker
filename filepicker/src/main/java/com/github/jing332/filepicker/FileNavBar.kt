package com.github.jing332.filepicker

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp

data class NavBarItem(val name: String, val path: String)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FileNavBar(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    list: List<NavBarItem>,
    onClick: (NavBarItem) -> Unit,
) {
    LaunchedEffect(key1 = list.size) {
        state.animateScrollToItem(list.size - 1)
    }

    LazyRow(modifier, state = state) {
        itemsIndexed(list) { index, item -> Row(
                modifier = Modifier.animateItemPlacement(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onClick(item) }
                        .minimumInteractiveComponentSize())
                {
                    Text(text = item.name)
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