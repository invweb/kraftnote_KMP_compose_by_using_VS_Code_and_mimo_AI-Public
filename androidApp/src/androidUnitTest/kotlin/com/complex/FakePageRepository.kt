package com.complex

import com.complex.model.Page
import com.complex.repository.PageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakePageRepository(
    initialPages: List<Page> = emptyList()
) : PageRepository {

    private val _pages = MutableStateFlow(initialPages)

    override fun getAllPages(): Flow<List<Page>> = _pages.asStateFlow()

    override suspend fun getPageById(id: String): Page? {
        return _pages.value.find { it.id == id }
    }

    override suspend fun createPage(page: Page) {
        _pages.value = _pages.value + page
    }

    override suspend fun updatePage(page: Page) {
        _pages.value = _pages.value.map { if (it.id == page.id) page else it }
    }

    override suspend fun deletePage(id: String) {
        _pages.value = _pages.value.filter { it.id != id }
    }
}
