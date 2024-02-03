package com.github.jing332.filepicker.listpage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
internal fun Item(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    isCheckable: Boolean = true,
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
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
internal fun LongItem(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    isCheckable: Boolean = true,
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
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
        Column(Modifier.weight(1f).padding(start = 4.dp)) {
            Title(
                modifier = Modifier,
                title = title,
                isChecked = isChecked
            )
            Text(subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ShortItem(
    modifier: Modifier = Modifier,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    isCheckable: Boolean = true,
    icon: @Composable () -> Unit,
    title: String,
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
            Title(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
                title = title,
                isChecked = isChecked
            )
        }
    }
}

@Composable
fun Title(modifier: Modifier = Modifier, title: String, isChecked: Boolean) {
    Text(
        modifier = modifier,
        text = title,
        fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
        maxLines = 1,
        style = MaterialTheme.typography.titleMedium,
        overflow = TextOverflow.Ellipsis
    )
}