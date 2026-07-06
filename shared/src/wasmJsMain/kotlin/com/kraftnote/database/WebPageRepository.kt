package com.kraftnote.database

import com.kraftnote.model.Page
import com.kraftnote.repository.PageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WebPageRepository : PageRepository {
    private val pages = MutableStateFlow<List<Page>>(emptyList())

    override fun getAllPages(): Flow<List<Page>> {
        return pages.asStateFlow()
    }

    override suspend fun getPageById(id: String): Page? {
        return pages.value.find { it.id == id }
    }

    override suspend fun createPage(page: Page) {
        pages.update { currentPages -> currentPages + page }
    }

    override suspend fun updatePage(page: Page) {
        pages.update { currentPages ->
            currentPages.map { if (it.id == page.id) page else it }
        }
    }

    override suspend fun deletePage(id: String) {
        pages.update { currentPages -> currentPages.filter { it.id != id } }
    }
}
