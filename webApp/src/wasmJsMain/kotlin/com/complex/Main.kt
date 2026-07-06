package com.complex

import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.complex.database.ApiPageRepository
import com.complex.viewmodel.PageViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(title = "Complex - Page Editor") {
        val viewModel = remember {
            PageViewModel(ApiPageRepository(baseUrl = "http://localhost:8080"))
        }
        App(viewModel = viewModel)
    }
}
