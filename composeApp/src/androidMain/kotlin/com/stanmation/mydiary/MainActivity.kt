package com.stanmation.mydiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.stanmation.mydiary.timelinedetail.TimelineDetailScreen
import com.stanmation.mydiary.timelinelist.TimelineListScreen
import com.stanmation.mydiary.viewmodels.TimelineItem
import com.stanmation.mydiary.viewmodels.TimelineListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            var selectedTimeline by remember { mutableStateOf<TimelineItem?>(null) }

            if (selectedTimeline == null) {
                TimelineListScreen(
                    viewModel = TimelineListViewModel(),
                    onTimelineClick = { timeline ->
                        selectedTimeline = timeline
                    }
                )
            } else {
                TimelineDetailScreen(
                    timeline = selectedTimeline!!
                )
            }
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}