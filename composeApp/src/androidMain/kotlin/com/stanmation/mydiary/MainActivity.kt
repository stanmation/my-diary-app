package com.stanmation.mydiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stanmation.mydiary.models.TimelineItem
import com.stanmation.mydiary.repositories.InMemoryTimelineRepository
import com.stanmation.mydiary.timelinedetail.TimelineDetailScreen
import com.stanmation.mydiary.timelinelist.TimelineListScreen
import com.stanmation.mydiary.viewmodels.Category
import com.stanmation.mydiary.viewmodels.TimelineDetailViewModel
import com.stanmation.mydiary.viewmodels.TimelineListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // -----------------------------
            // Fake repository + ViewModel
            // -----------------------------
            val repository = remember { InMemoryTimelineRepository() }

            val timelineListViewModel = remember {
                TimelineListViewModel(repository)
            }

            // -----------------------------
            // Navigation state
            // -----------------------------
            var selectedTimeline by remember { mutableStateOf<TimelineItem?>(null) }

            // -----------------------------
            // Seed data (so list is not empty)
            // -----------------------------
            LaunchedEffect(Unit) {
                repository.addTimeline(
                    TimelineItem(
                        id = "1",
                        name = "Japan Trip",
                        category = Category.TRAVEL,
                        photos = emptyList()
                    )
                )
            }


            if (selectedTimeline == null) {

                // 👇 LIST SCREEN (reads selectedTimeline)
                TimelineListScreen(
                    viewModel = timelineListViewModel,
                    onTimelineClick = { timeline ->
                        selectedTimeline = timeline
                    }
                )

            } else {

                // 👇 DETAIL SCREEN (reads selectedTimeline)
                selectedTimeline?.let { timeline ->

                    val detailViewModel = remember(timeline.id) {
                        TimelineDetailViewModel(repository, timeline.id)
                    }

                    TimelineDetailScreen(
                        viewModel = detailViewModel,
                        onBack = {
                            selectedTimeline = null // ✅ NOW THIS IS USED
                        }
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}