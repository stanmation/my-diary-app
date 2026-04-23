package com.stanmation.mydiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.stanmation.mydiary.timelinelist.TimelineListScreen
import com.stanmation.mydiary.viewmodels.TimelineListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            TimelineListScreen(
                viewModel = TimelineListViewModel()
            )
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}