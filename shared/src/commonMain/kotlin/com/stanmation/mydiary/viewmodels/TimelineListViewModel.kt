package com.stanmation.mydiary.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class TimelineItem(
    val id: String,
    val name: String,
    val photoCount: Int,
    val category: Category
)

enum class Category {
    FITNESS, TRAVEL, FOOD;

    fun displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }

    companion object {
        val allValues = entries
    }
}

data class TimelineListUiState(
    val timelines: List<TimelineItem> = emptyList(),
    val isShowingCreate: Boolean = false,
    val newTimelineName: String = "",
    val selectedCategory: Category = Category.FITNESS
)

class TimelineListViewModel {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(TimelineListUiState())
    val state: StateFlow<TimelineListUiState> = _state

    fun observeState(onEach: (TimelineListUiState) -> Unit) {
        scope.launch {
            state.collect {
                onEach(it)
            }
        }
    }
    fun onAddClicked() {
        _state.update { it.copy(isShowingCreate = true) }
    }

    fun onCancelCreate() {
        _state.update {
            it.copy(
                isShowingCreate = false,
                newTimelineName = "",
                selectedCategory = Category.FITNESS
            )
        }
    }

    fun onNameChanged(name: String) {
        _state.update { it.copy(newTimelineName = name) }
    }

    fun onCategorySelected(category: Category) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun createTimeline() {
        val current = _state.value
        val name = current.newTimelineName.trim()
        if (name.isEmpty()) return

        val newItem = TimelineItem(
            id = Clock.System.now().toEpochMilliseconds().toString(),
            name = name,
            photoCount = 0,
            category = current.selectedCategory
        )

        _state.update {
            it.copy(
                timelines = listOf(newItem) + it.timelines,
                isShowingCreate = false,
                newTimelineName = "",
                selectedCategory = Category.FITNESS
            )
        }
    }

    fun deleteTimeline(index: Int) {
        _state.update {
            it.copy(
                timelines = it.timelines.toMutableList().also { list ->
                    list.removeAt(index)
                }
            )
        }
    }
}