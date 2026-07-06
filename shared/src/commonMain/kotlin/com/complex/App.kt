package com.complex

import androidx.compose.runtime.*
import com.complex.ui.MarkdownEditorScreen
import com.complex.ui.PageListScreen
import com.complex.viewmodel.PageViewModel

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
