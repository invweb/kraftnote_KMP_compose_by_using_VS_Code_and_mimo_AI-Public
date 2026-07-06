package com.complex.server

import com.complex.model.Page
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Route.pageRoutes() {
    route("/api/pages") {
        get {
            val pages = transaction {
                Pages.selectAll()
                    .orderBy(Pages.updatedAt, SortOrder.DESC)
                    .map { row ->
                        Page(
                            id = row[Pages.id],
                            title = row[Pages.title],
                            content = row[Pages.content],
                            createdAt = row[Pages.createdAt],
                            updatedAt = row[Pages.updatedAt]
                        )
                    }
            }
            call.respond(pages)
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Missing id")
            )
            val page: Page? = transaction {
                Pages.select { Pages.id eq id }
                    .firstOrNull()
                    ?.let { row ->
                        Page(
                            id = row[Pages.id],
                            title = row[Pages.title],
                            content = row[Pages.content],
                            createdAt = row[Pages.createdAt],
                            updatedAt = row[Pages.updatedAt]
                        )
                    }
            }
            if (page != null) {
                call.respond(page)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Page not found"))
            }
        }

        post {
            val page: Page = call.receive()
            transaction {
                Pages.insert { row ->
                    row[Pages.id] = page.id
                    row[Pages.title] = page.title
                    row[Pages.content] = page.content
                    row[Pages.createdAt] = page.createdAt
                    row[Pages.updatedAt] = page.updatedAt
                }
            }
            call.respond(HttpStatusCode.Created, page)
        }

        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Missing id")
            )
            val page: Page = call.receive()
            val updated = transaction {
                Pages.update({ Pages.id eq id }) { row ->
                    row[Pages.title] = page.title
                    row[Pages.content] = page.content
                    row[Pages.updatedAt] = page.updatedAt
                }
            }
            if (updated > 0) {
                call.respond(page)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Page not found"))
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Missing id")
            )
            val deleted = transaction {
                Pages.deleteWhere { Pages.id eq id }
            }
            if (deleted > 0) {
                call.respond(HttpStatusCode.OK, mapOf("message" to "Deleted"))
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Page not found"))
            }
        }
    }
}
