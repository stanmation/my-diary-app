package com.stanmation.mydiary.models

import com.stanmation.mydiary.viewmodels.Category

data class TimelineItem(
    val id: String,
    val name: String,
    val photoCount: Int = 0,
    val category: Category,
    var photos: List<PhotoItem> = emptyList()
)