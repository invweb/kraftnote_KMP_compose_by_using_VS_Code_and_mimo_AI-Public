package com.complex.viewmodel

import com.complex.model.Page
import com.complex.repository.PageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PageViewModel(
    private val repository: PageRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _pages = MutableStateFlow<List<Page>>(emptyList())
    val pages: StateFlow<List<Page>> = _pages.asStateFlow()

    private val _currentPage = MutableStateFlow<Page?>(null)
    val currentPage: StateFlow<Page?> = _currentPage.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    init {
        scope.launch {
            repository.getAllPages().collect { pages ->
                _pages.value = pages
            }
        }
    }

    fun selectPage(id: String) {
        scope.launch {
            _currentPage.value = repository.getPageById(id)
        }
    }

    fun startEditing() {
        _isEditing.value = true
    }

    fun stopEditing() {
        _isEditing.value = false
    }

    @OptIn(ExperimentalUuidApi::class)
    fun createNewPage(title: String = "") {
        scope.launch {
            val now = Clock.System.now().toEpochMilliseconds()
            val newPage = Page(
                id = Uuid.random().toString(),
                title = title,
                content = "",
                createdAt = now,
                updatedAt = now
            )
            repository.createPage(newPage)
            _currentPage.value = newPage
            _isEditing.value = true
        }
    }

    fun updatePageContent(id: String, title: String, content: String) {
        scope.launch {
            val existingPage = repository.getPageById(id) ?: return@launch
            val updatedPage = existingPage.copy(
                title = title,
                content = content,
                updatedAt = Clock.System.now().toEpochMilliseconds()
            )
            repository.updatePage(updatedPage)
            _currentPage.value = updatedPage
        }
    }

    fun deletePage(id: String) {
        scope.launch {
            repository.deletePage(id)
            if (_currentPage.value?.id == id) {
                _currentPage.value = null
                _isEditing.value = false
            }
        }
    }
}
