package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_tracks")
data class AudioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val artist: String,
    val sourceUrl: String = "",
    val licenseType: String,
    val category: String = "Geral",
    val notes: String = "",
    val linkedDraftId: Long? = null,
    val addedAt: Long = System.currentTimeMillis()
)
