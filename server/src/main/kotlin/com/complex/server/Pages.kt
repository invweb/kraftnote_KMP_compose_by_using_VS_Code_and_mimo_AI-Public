package com.complex.server

import org.jetbrains.exposed.sql.Table

object Pages : Table("pages") {
    val id = varchar("id", 64)
    val title = varchar("title", 255)
    val content = text("content")
    val createdAt = long("created_at")
    val updatedAt = long("updated_at")

    override val primaryKey = PrimaryKey(id)
}
