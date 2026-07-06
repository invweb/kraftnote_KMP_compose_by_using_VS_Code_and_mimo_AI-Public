package com.complex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.complex.database.ApiPageRepository
import com.complex.viewmodel.PageViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    App(
                        viewModel = PageViewModel(
                            ApiPageRepository(baseUrl = "http://10.0.2.2:8080")
                        )
                    )
                }
            }
        }
    }
}
