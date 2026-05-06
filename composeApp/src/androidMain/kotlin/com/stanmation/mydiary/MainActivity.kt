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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.stanmation.mydiary.db.DatabaseDriverFactory
import com.stanmation.mydiary.models.TimelineItem
import com.stanmation.mydiary.repositories.InMemoryTimelineRepository
import com.stanmation.mydiary.repositories.SqlDelightTimelineRepository
import com.stanmation.mydiary.timelinedetail.TimelineDetailScreen
import com.stanmation.mydiary.timelinelist.TimelineListScreen
import com.stanmation.mydiary.viewmodels.Category
import com.stanmation.mydiary.viewmodels.TimelineDetailViewModel
import com.stanmation.mydiary.viewmodels.TimelineListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val driver = remember {
                DatabaseDriverFactory(this).createDriver()
            }

            // ✅ 2. Create database
            val database = remember {
                com.stanmation.mydiary.database.DiaryDatabase(driver)
            }

            // ✅ 3. Create repository (replace InMemory)
            val databaseRepository = remember {
                SqlDelightTimelineRepository(database)
            }

//            val timelineRepository = remember { InMemoryTimelineRepository() }

            val timelineListViewModel = remember {
                TimelineListViewModel(databaseRepository)
            }

            var selectedTimeline by remember { mutableStateOf<TimelineItem?>(null) }

//            LaunchedEffect(Unit) {
//                databaseRepository.addTimeline(
//                    TimelineItem(
//                        id = "1",
//                        name = "Japan Trip",
//                        category = Category.TRAVEL,
//                        photos = emptyList()
//                    )
//                )
//            }


            if (selectedTimeline == null) {
                TimelineListScreen(
                    viewModel = timelineListViewModel,
                    onTimelineClick = { timeline ->
                        selectedTimeline = timeline
                    }
                )

            } else {
                selectedTimeline?.let { timeline ->
                    val detailViewModel = remember(timeline.id) {
                        TimelineDetailViewModel(databaseRepository, timeline.id)
                    }

                    TimelineDetailScreen(
                        viewModel = detailViewModel,
                        onBack = {
                            selectedTimeline = null
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