package com.complex

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

@Composable
fun MaterialThemeWrapper(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface {
            content()
        }
    }
}
