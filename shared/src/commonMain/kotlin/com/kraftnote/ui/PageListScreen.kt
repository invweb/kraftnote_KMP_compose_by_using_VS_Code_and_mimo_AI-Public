package com.kraftnote.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kraftnote.model.Page
import com.kraftnote.viewmodel.PageViewModel
import kraftnote.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageListScreen(
    viewModel: PageViewModel,
    onNavigateToEditor: () -> Unit
) {
    val pages by viewModel.pages.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.my_pages)) },
                actions = {
                    val createPageText = stringResource(Res.string.create_page)
                    val newPageTitleText = stringResource(Res.string.new_page_title)
                    IconButton(onClick = {
                        viewModel.createNewPage(newPageTitleText)
                        onNavigateToEditor()
                    }) {
                        Icon(Icons.Default.Add, contentDescription = createPageText)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(pages) { page ->
                PageListItem(
                    page = page,
                    onClick = {
                        viewModel.selectPage(page.id)
                        onNavigateToEditor()
                    },
                    onDelete = {
                        viewModel.deletePage(page.id)
                    }
                )
            }
        }
    }
}

@Composable
fun PageListItem(
    page: Page,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = page.title.ifEmpty { stringResource(Res.string.untitled) },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = page.content.take(100).ifEmpty { stringResource(Res.string.empty_page) },
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${stringResource(Res.string.updated)}${formatTimestamp(page.updatedAt)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(Res.string.delete),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val totalSeconds = timestamp / 1000
    val seconds = totalSeconds % 60
    val totalMinutes = totalSeconds / 60
    val minutes = totalMinutes % 60
    val totalHours = totalMinutes / 60
    val hours = totalHours % 24
    val days = totalHours / 24

    val year = 1970 + (days / 365).toInt()
    val dayOfYear = (days % 365).toInt()
    val month = (dayOfYear / 30 + 1).coerceIn(1, 12)
    val day = (dayOfYear % 30 + 1)

    return "${year.toString().padStart(4, '0')}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
