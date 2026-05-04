package com.stanmation.mydiary.timelinedetail

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.stanmation.mydiary.models.PhotoItem
import com.stanmation.mydiary.models.TimelineItem
import com.stanmation.mydiary.utilities.extractPhotoDate
import com.stanmation.mydiary.viewmodels.Category
import androidx.activity.compose.BackHandler
import com.stanmation.mydiary.viewmodels.TimelineDetailViewModel
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineDetailScreen(
    viewModel: TimelineDetailViewModel,
    onBack: () -> Unit = {}
) {
    // -----------------------------
    // STATE (equivalent to @State)
    // -----------------------------
    var tappedPhoto by remember { mutableStateOf<PhotoItem?>(null) }
    var showingDeleteConfirmation by remember { mutableStateOf(false) }

    val state by viewModel.uiState.collectAsState()
    val timeline = state.timeline ?: return


    // -----------------------------
    // PHOTO PICKER
    // -----------------------------
    val context = LocalContext.current
//    val pickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickMultipleVisualMedia(10)
//    ) { uris ->
//        uris.forEach { uri ->
//            val exifDate = uri.extractPhotoDate(context)
//
//            selectedPhotos.add(
//                PhotoItem(
//                    path = uri.toString(),
//                    dateMillis = exifDate ?: System.currentTimeMillis()
//                )
//            )
//        }
//    }

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { uris ->

        val newPhotos = uris.map { uri ->
            val exifDate = uri.extractPhotoDate(context)

            PhotoItem(
                id = uri.toString(),
                path = uri.toString(),
                dateMillis = exifDate ?: System.currentTimeMillis()
            )
        }

        viewModel.addPhotos(newPhotos)
    }

    // -----------------------------
    // GROUP BY YEAR
    // -----------------------------
    val grouped = timeline.photos
        .sortedByDescending { it.dateMillis }
        .groupBy { photo ->
            Instant.fromEpochMilliseconds(photo.dateMillis)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .year
        }

    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(timeline.name) },
                actions = {
                    IconButton(onClick = {
                       viewModel.savePhotos()
                    }) {
                        Icon(Icons.Default.Done, contentDescription = null)
                    }

                    IconButton(onClick = {
                        showingDeleteConfirmation = true
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            // -----------------------------
            // PICKER + CATEGORY
            // -----------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(onClick = {
                    pickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text("Select Photos")
                }

                Spacer(modifier = Modifier.weight(1f))

                // Category badge
                Box(
                    modifier = Modifier
                        .background(Color.Gray, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = timeline.category.displayName(),
                        color = Color.White
                    )
                }
            }

            // -----------------------------
            // TIMELINE LIST
            // -----------------------------
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                grouped.forEach { (year, photos) ->
                    item {
                        Text(
                            text = year.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    items(photos) { photo ->
                        PhotoRow(
                            photo = photo,
                            onClick = { tappedPhoto = photo }
                        )
                    }
                }
            }
        }
    }


    if (showingDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showingDeleteConfirmation = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTimeline()
                    onBack()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showingDeleteConfirmation = false
                }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Timeline") },
            text = { Text("Are you sure you want to delete '${timeline.name}'?") }
        )
    }

    // -----------------------------
    // FULL SCREEN IMAGE
    // -----------------------------
    tappedPhoto?.let { photo ->
        Dialog(onDismissRequest = { tappedPhoto = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(photo.path),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TimelineDetailPreview() {
//    TimelineDetailScreen(
//        timeline = TimelineItem(
//            id = "1",
//            name = "Japan Trip",
//            photoCount = 10,
//            category = Category.TRAVEL
//        )
//    )
//}