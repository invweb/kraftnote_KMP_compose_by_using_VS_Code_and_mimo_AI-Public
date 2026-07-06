package com.complex.database

import com.complex.model.Page
import com.complex.repository.PageRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ApiPageRepository(
    private val baseUrl: String = "http://10.0.2.2:8080"
) : PageRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val pages = MutableStateFlow<List<Page>>(emptyList())
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    init {
        scope.launch {
            while (isActive) {
                refreshPages()
                delay(3000)
            }
        }
    }

    private suspend fun refreshPages() {
        try {
            val response: List<Page> = client.get("$baseUrl/api/pages").body()
            pages.value = response
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getAllPages(): Flow<List<Page>> = pages.asStateFlow()

    override suspend fun getPageById(id: String): Page? {
        return try {
            client.get("$baseUrl/api/pages/$id").body()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createPage(page: Page) {
        client.post("$baseUrl/api/pages") {
            contentType(ContentType.Application.Json)
            setBody(page)
        }
        refreshPages()
    }

    override suspend fun updatePage(page: Page) {
        client.put("$baseUrl/api/pages/${page.id}") {
            contentType(ContentType.Application.Json)
            setBody(page)
        }
        refreshPages()
    }

    override suspend fun deletePage(id: String) {
        client.delete("$baseUrl/api/pages/$id")
        refreshPages()
    }
}
