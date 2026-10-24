package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.NexoraApp
import com.example.ui.theme.NexoraTheme
import com.example.ui.viewmodel.NexoraViewModel
import com.example.ui.viewmodel.NexoraViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: NexoraViewModel by viewModels {
        NexoraViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexoraTheme {
                NexoraApp(viewModel = viewModel)
            }
        }
    }
}
