package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv_channels")
data class TvChannelEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val category: String, // "Pública e Educativa", "Notícias 24h", "Legislativa e Cidadania", "Nacional Aberta", "Religiosa e Comunitária"
    val region: String, // "Nacional", "Brasília / DF", "São Paulo / SP", etc.
    val logoSymbol: String, // Símbolo visual / abreviação oficial
    val description: String,
    val officialStreamUrl: String, // URL oficial para player incorporado
    val officialWebUrl: String, // Portal oficial da emissora
    val streamType: String, // "EMBEDDED_STREAM", "PORTAL_OFICIAL"
    val availabilityStatus: String, // "DISPONIVEL_AO_VIVO", "PORTAL_REQUERIDO", "PROGRAMACAO_ESPECIAL"
    val lastVerifiedDate: String,
    val rightsInfo: String,
    val isFavorite: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)
