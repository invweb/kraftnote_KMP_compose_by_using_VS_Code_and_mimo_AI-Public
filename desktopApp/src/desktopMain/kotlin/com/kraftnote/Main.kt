package com.kraftnote

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import com.kraftnote.database.ApiPageRepository
import com.kraftnote.viewmodel.PageViewModel

fun main() = application {
    val windowState = rememberWindowState(
        width = 800.dp,
        height = 600.dp
    )

    val viewModel = remember {
        PageViewModel(ApiPageRepository(baseUrl = "http://localhost:8080"))
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "KraftNote - Page Editor"
    ) {
        App(viewModel = viewModel)
    }
}
