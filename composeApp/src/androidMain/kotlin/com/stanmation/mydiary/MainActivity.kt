package com.stanmation.mydiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.stanmation.mydiary.db.DatabaseDriverFactory
import com.stanmation.mydiary.models.TimelineItem
import com.stanmation.mydiary.repositories.SqlDelightTimelineRepository
import com.stanmation.mydiary.timelinedetail.TimelineDetailScreen
import com.stanmation.mydiary.timelinelist.TimelineListScreen
import com.stanmation.mydiary.viewmodels.TimelineDetailViewModel
import com.stanmation.mydiary.viewmodels.TimelineListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val driver = remember {
                DatabaseDriverFactory(this).createDriver()
            }

            val database = remember {
                com.stanmation.mydiary.database.DiaryDatabase(driver)
            }

            val databaseRepository = remember {
                SqlDelightTimelineRepository(database)
            }

            val timelineListViewModel = remember {
                TimelineListViewModel(databaseRepository)
            }

            var selectedTimeline by remember { mutableStateOf<TimelineItem?>(null) }

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
