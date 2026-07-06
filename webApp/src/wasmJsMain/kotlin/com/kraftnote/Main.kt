package com.kraftnote

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.kraftnote.database.ApiPageRepository
import com.kraftnote.viewmodel.PageViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(title = "KraftNote - Page Editor") {
        val viewModel = remember {
            PageViewModel(ApiPageRepository(baseUrl = "http://localhost:8080"))
        }
        App(viewModel = viewModel)
    }
}
