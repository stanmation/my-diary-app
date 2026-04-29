package com.stanmation.mydiary.timelinelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.ui.tooling.preview.Preview
import com.stanmation.mydiary.viewmodels.TimelineItem
import com.stanmation.mydiary.viewmodels.TimelineListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineListScreen(viewModel: TimelineListViewModel,
                       onTimelineClick: (TimelineItem) -> Unit) {
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
            EmptyState()
        } else {
            LazyColumn(contentPadding = padding) {
                itemsIndexed(state.timelines) { index, timeline ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTimelineClick(timeline) }
                            .padding(16.dp)
                    ) {
                        Text(timeline.name, style = MaterialTheme.typography.titleMedium)
                        Text("${timeline.photoCount} photos")
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

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Timelines Yet",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create your first timeline to start organizing your photos",
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimelineListPreview() {
    TimelineListScreen(
        viewModel = TimelineListViewModel(),
        onTimelineClick = {}
    )
}