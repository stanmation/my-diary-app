package com.stanmation.mydiary.viewmodels

import com.stanmation.mydiary.models.PhotoItem
import com.stanmation.mydiary.models.TimelineItem
import com.stanmation.mydiary.repositories.TimelineRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimelineDetailUiState(
    val timeline: TimelineItem? = null
)

class TimelineDetailViewModel(
    private val repository: TimelineRepository,
    private val timelineId: String
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(TimelineDetailUiState())
    val uiState: StateFlow<TimelineDetailUiState> = _uiState

    init {
        observeTimeline()
    }

    private fun observeTimeline() {
        scope.launch {
            repository.getTimelines().collect { timelines ->
                val timeline = timelines.find { it.id == timelineId }

                _uiState.update {
                    it.copy(
                        timeline = timeline
                    )
                }
            }
        }
    }


    fun addPhotos(newPhotos: List<PhotoItem>) {
        scope.launch {
            repository.addPhotos(timelineId, newPhotos)
        }
    }

    fun deleteTimeline() {
        scope.launch {
            repository.deleteTimeline(timelineId)
        }
    }
}