package com.kraftnote.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kraftnote.viewmodel.PageViewModel
import kraftnote.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkdownEditorScreen(
    viewModel: PageViewModel,
    onNavigateBack: () -> Unit
) {
    val currentPage by viewModel.currentPage.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()

    var title by remember { mutableStateOf(currentPage?.title ?: "") }
    var content by remember { mutableStateOf(currentPage?.content ?: "") }

    LaunchedEffect(currentPage) {
        currentPage?.let {
            title = it.title
            content = it.content
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.editor)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.back))
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            currentPage?.let { page ->
                                viewModel.updatePageContent(page.id, title, content)
                                viewModel.stopEditing()
                                onNavigateBack()
                            }
                        }
                    ) {
                        Text(stringResource(Res.string.save))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(Res.string.title_hint)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(Res.string.content_hint)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 300.dp),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (content.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.preview),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                MarkdownPreview(content = content)
            }
        }
    }
}

@Composable
fun MarkdownPreview(content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            content.lines().forEach { line ->
                when {
                    line.startsWith("# ") -> {
                        Text(
                            text = line.removePrefix("# "),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    line.startsWith("## ") -> {
                        Text(
                            text = line.removePrefix("## "),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                    line.startsWith("### ") -> {
                        Text(
                            text = line.removePrefix("### "),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    line.startsWith("- ") || line.startsWith("* ") -> {
                        Text(
                            text = "• ${line.drop(2)}",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    line.startsWith("**") && line.endsWith("**") -> {
                        Text(
                            text = line.removeSurrounding("**"),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    line.startsWith("*") && line.endsWith("*") && !line.startsWith("**") -> {
                        Text(
                            text = line.removeSurrounding("*"),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    line.isBlank() -> {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    else -> {
                        Text(
                            text = line,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
