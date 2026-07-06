package com.complex.database

import com.complex.model.Page
import com.complex.repository.PageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class DesktopPageRepository : PageRepository {
    private val pages = MutableStateFlow<List<Page>>(emptyList())
    private val dataFile = File(System.getProperty("user.home"), ".complex/pages.json")
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    init {
        loadPages()
    }

    private fun loadPages() {
        try {
            if (dataFile.exists()) {
                val data = dataFile.readText()
                val loadedPages = json.decodeFromString<List<Page>>(data)
                pages.value = loadedPages
            }
        } catch (e: Exception) {
            pages.value = emptyList()
        }
    }

    private fun savePages() {
        try {
            dataFile.parentFile?.mkdirs()
            dataFile.writeText(json.encodeToString(pages.value))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getAllPages(): Flow<List<Page>> {
        return pages.asStateFlow()
    }

    override suspend fun getPageById(id: String): Page? {
        return pages.value.find { it.id == id }
    }

    override suspend fun createPage(page: Page) {
        pages.update { currentPages -> currentPages + page }
        savePages()
    }

    override suspend fun updatePage(page: Page) {
        pages.update { currentPages ->
            currentPages.map { if (it.id == page.id) page else it }
        }
        savePages()
    }

    override suspend fun deletePage(id: String) {
        pages.update { currentPages -> currentPages.filter { it.id != id } }
        savePages()
    }
}
