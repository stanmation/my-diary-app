package com.stanmation.mydiary.timelinelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stanmation.mydiary.viewmodels.Category
import com.stanmation.mydiary.viewmodels.TimelineListUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTimelineDialog(
    state: TimelineListUiState,
    onNameChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onCancel: () -> Unit,
    onCreate: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("New Timeline") },
        text = {
            Column {
                OutlinedTextField(
                    value = state.newTimelineName,
                    onValueChange = onNameChange,
                    label = { Text("Timeline Name") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                CategoryDropdown(
                    selected = state.selectedCategory,
                    onSelected = onCategoryChange
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onCreate,
                enabled = state.newTimelineName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}