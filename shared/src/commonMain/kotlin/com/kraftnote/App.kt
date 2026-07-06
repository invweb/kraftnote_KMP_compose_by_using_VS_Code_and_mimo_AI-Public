package com.kraftnote

import androidx.compose.runtime.*
import com.kraftnote.ui.MarkdownEditorScreen
import com.kraftnote.ui.PageListScreen
import com.kraftnote.viewmodel.PageViewModel

@Composable
fun App(viewModel: PageViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.PageList) }

    when (currentScreen) {
        Screen.PageList -> {
            PageListScreen(
                viewModel = viewModel,
                onNavigateToEditor = { currentScreen = Screen.Editor }
            )
        }
        Screen.Editor -> {
            MarkdownEditorScreen(
                viewModel = viewModel,
                onNavigateBack = { currentScreen = Screen.PageList }
            )
        }
    }
}

enum class Screen {
    PageList,
    Editor
}
