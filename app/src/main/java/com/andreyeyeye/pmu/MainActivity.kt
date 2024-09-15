package com.andreyeyeye.pmu

import NewsViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.andreyeyeye.pmu.ui.theme.PMUTheme
import Window

enum class AS{
    L, R
}

class MainActivity : ComponentActivity() {
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PMUTheme {
                Window(viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PMUTheme {
        val viewModel = NewsViewModel()
        Window(viewModel)
    }
}

