package com.complex

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.window.ComposeUIViewController
import com.complex.database.ApiPageRepository
import com.complex.viewmodel.PageViewModel

fun CreateViewController() = ComposeUIViewController {
    MaterialTheme {
        Surface {
            App(
                viewModel = PageViewModel(
                    ApiPageRepository(baseUrl = "http://localhost:8080")
                )
            )
        }
    }
}
