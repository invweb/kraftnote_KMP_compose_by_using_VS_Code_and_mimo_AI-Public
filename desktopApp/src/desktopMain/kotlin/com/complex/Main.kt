package com.complex

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import com.complex.database.ApiPageRepository
import com.complex.viewmodel.PageViewModel

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
        title = "Complex - Page Editor"
    ) {
        App(viewModel = viewModel)
    }
}
