package com.kraftnote.database

import android.content.Context
import com.kraftnote.model.Page
import com.kraftnote.repository.PageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AndroidPageRepository(private val context: Context) : PageRepository {
    private val pages = MutableStateFlow<List<Page>>(emptyList())
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    init {
        loadPages()
    }

    private fun getDataFile() = context.filesDir.resolve("pages.json")

    private fun loadPages() {
        try {
            val file = getDataFile()
            if (file.exists()) {
                val data = file.readText()
                pages.value = json.decodeFromString<List<Page>>(data)
            }
        } catch (e: Exception) {
            pages.value = emptyList()
        }
    }

    private fun savePages() {
        try {
            getDataFile().writeText(json.encodeToString(pages.value))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getAllPages(): Flow<List<Page>> = pages.asStateFlow()

    override suspend fun getPageById(id: String): Page? =
        pages.value.find { it.id == id }

    override suspend fun createPage(page: Page) {
        pages.update { it + page }
        savePages()
    }

    override suspend fun updatePage(page: Page) {
        pages.update { list -> list.map { if (it.id == page.id) page else it } }
        savePages()
    }

    override suspend fun deletePage(id: String) {
        pages.update { it.filter { page -> page.id != id } }
        savePages()
    }
}
