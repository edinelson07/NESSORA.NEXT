package com.example.data.repository

import com.example.data.local.AudioDao
import com.example.data.local.AudioEntity
import com.example.data.model.AudioLicense
import kotlinx.coroutines.flow.Flow

class AudioRepository(private val audioDao: AudioDao) {

    fun getAllTracks(): Flow<List<AudioEntity>> = audioDao.getAllTracks()

    suspend fun addTrack(track: AudioEntity): Long = audioDao.insertTrack(track)

    suspend fun deleteTrack(track: AudioEntity) = audioDao.deleteTrack(track)

    suspend fun ensureDefaultTracksIfEmpty() {
        if (audioDao.getAllTracksSync().isEmpty()) {
            val defaults = listOf(
                AudioEntity(
                    title = "Cybernetic Pulse",
                    artist = "Nexora Studio Labs",
                    sourceUrl = "https://studio.youtube.com/channel/audio-library",
                    licenseType = AudioLicense.YOUTUBE_AUDIO_LIBRARY.name,
                    category = "Tecnologia / Eletrônica",
                    notes = "Uso livre no YouTube com monetização ativa sem atribuição necessária.",
                    addedAt = System.currentTimeMillis()
                ),
                AudioEntity(
                    title = "Midnight Focus Beat",
                    artist = "Lofi Chill Creative",
                    sourceUrl = "https://freemusicarchive.org/cc-by",
                    licenseType = AudioLicense.CREATIVE_COMMONS_BY.name,
                    category = "Lofi / Relaxante",
                    notes = "Exige créditos na descrição do vídeo: 'Música por Lofi Chill Creative (CC-BY 4.0)'.",
                    addedAt = System.currentTimeMillis() - 86400000L
                ),
                AudioEntity(
                    title = "Heroic Journey Trailer",
                    artist = "Epic Symphony",
                    sourceUrl = "https://audiojungle.net/licenses",
                    licenseType = AudioLicense.REQUIRES_LICENSE.name,
                    category = "Épico / Cinema",
                    notes = "Atenção: Requer compra de licença comercial individual antes de publicar com monetização.",
                    addedAt = System.currentTimeMillis() - 172800000L
                )
            )
            audioDao.insertAll(defaults)
        }
    }
}
