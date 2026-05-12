package com.stanmation.mydiary.timelinelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stanmation.mydiary.models.TimelineItem
import com.stanmation.mydiary.viewmodels.TimelineListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineListScreen(
    viewModel: TimelineListViewModel,
    onTimelineClick: (TimelineItem) -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Timelines") },
                actions = {
                    IconButton(onClick = { viewModel.onAddClicked() }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        if (state.timelines.isEmpty()) {
            Box(modifier = Modifier.padding(padding)) {
                Text("No timelines yet", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                itemsIndexed(state.timelines) { index, timeline ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onTimelineClick(timeline)
                            }
                            .padding(16.dp)
                    ) {
                        Text(timeline.name)
                        Text("${timeline.photos.size} photos")
                    }
                }
            }
        }
    }

    if (state.isShowingCreate) {
        CreateTimelineDialog(
            state = state,
            onNameChange = viewModel::onNameChanged,
            onCategoryChange = viewModel::onCategorySelected,
            onCancel = viewModel::onCancelCreate,
            onCreate = viewModel::createTimeline
        )
     }
 }
