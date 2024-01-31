package com.github.jing332.filepicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

data class NavBarItem(val name: String, val path: String)

@Composable
fun FileNavBar(
    modifier: Modifier = Modifier,
    list: List<NavBarItem>,
    onClick: (NavBarItem) -> Unit,
) {
    LazyRow {
        itemsIndexed(list) { index, item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onClick(item) }) {
                    Text(modifier = Modifier.padding(4.dp), text = item.name)
                }

                if (index != list.size - 1) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null
                    )
                }
            }
        }
    }
}