package com.stanmation.mydiary.repositories

import com.stanmation.mydiary.database.DiaryDatabase
import com.stanmation.mydiary.models.PhotoItem
import com.stanmation.mydiary.models.TimelineItem
import com.stanmation.mydiary.viewmodels.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SqlDelightTimelineRepository(
    database: DiaryDatabase
) : TimelineRepository {

    private val timelineQueries = database.timelineQueries
    private val photoQueries = database.photoQueries

    private val _timelines = MutableStateFlow<List<TimelineItem>>(emptyList())
    override fun getTimelines(): StateFlow<List<TimelineItem>> = _timelines

    init {
        refresh()
    }

    private fun refresh() {
        val timelines = timelineQueries.selectAll().executeAsList()

        val result = timelines.map { t ->
            val photos = photoQueries
                .selectByTimeline(t.id)
                .executeAsList()

            TimelineItem(
                id = t.id,
                name = t.name,
                category = Category.valueOf(t.category),
                photos = photos.map {
                    PhotoItem(
                        id = it.id,
                        path = it.path,
                        dateMillis = it.dateMillis
                    )
                }
            )
        }

        _timelines.value = result
    }

    override suspend fun addTimeline(timeline: TimelineItem) {
        timelineQueries.insert(
            timeline.id,
            timeline.name,
            timeline.category.name
        )
        refresh()
    }

    override suspend fun deleteTimeline(id: String) {
        timelineQueries.deleteById(id)
        refresh()
    }

    override suspend fun addPhotos(
        timelineId: String,
        photos: List<PhotoItem>
    ) {
        photos.forEach {
            photoQueries.insert(
                it.id,
                timelineId,
                it.path,
                it.dateMillis
            )
        }
        refresh()
    }
}