package com.kraftnote.repository

import com.kraftnote.model.Page
import kotlinx.coroutines.flow.Flow

interface PageRepository {
    fun getAllPages(): Flow<List<Page>>
    suspend fun getPageById(id: String): Page?
    suspend fun createPage(page: Page)
    suspend fun updatePage(page: Page)
    suspend fun deletePage(id: String)
}
