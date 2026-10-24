package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drafts")
data class DraftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val type: String, // DraftType name
    val topic: String,
    val targetAudience: String = "Criadores e público geral",
    val tone: String = "Informativo e Dinâmico",
    val targetDuration: String = "8-12 min",
    val hook: String = "",
    val scriptBody: String = "",
    val description: String = "",
    val tags: String = "", // Comma-separated
    val thumbnailTitle: String = "",
    val thumbnailSubtitle: String = "",
    val thumbnailBgHex: String = "#07111F",
    val thumbnailAccentHex: String = "#36E58D",
    val status: String = "RASCUNHO", // RASCUNHO, PRONTO, PUBLICADO
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
