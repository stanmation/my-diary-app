package com.stanmation.mydiary.repositories

import com.stanmation.mydiary.models.PhotoItem
import com.stanmation.mydiary.models.TimelineItem
import kotlinx.coroutines.flow.StateFlow

interface TimelineRepository {

    fun getTimelines(): StateFlow<List<TimelineItem>>

    suspend fun addTimeline(timeline: TimelineItem)

    suspend fun deleteTimeline(id: String)

    suspend fun addPhotos(
        timelineId: String,
        photos: List<PhotoItem>
    )
}