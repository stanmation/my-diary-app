package com.stanmation.mydiary.repositories

import com.stanmation.mydiary.models.PhotoItem
import com.stanmation.mydiary.models.TimelineItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InMemoryTimelineRepository : TimelineRepository {

    private val _timelines = MutableStateFlow<List<TimelineItem>>(emptyList())
    override fun getTimelines(): StateFlow<List<TimelineItem>> = _timelines
    override suspend fun addTimeline(timeline: TimelineItem) {
        _timelines.value = _timelines.value + timeline

    }

    override suspend fun deleteTimeline(id: String) {
        _timelines.value = _timelines.value.filterNot { it.id == id }
    }

    override suspend fun addPhotos(
        timelineId: String,
        photos: List<PhotoItem>
    ) {
        _timelines.value = _timelines.value.map { timeline ->
            if (timeline.id == timelineId) {
                timeline.copy(
                    photos = timeline.photos + photos
                )
            } else timeline
        }
    }
}