package com.example.data.repository

import com.example.data.model.ChannelAuthStatus
import com.example.data.model.ChannelInfo
import com.example.data.model.VideoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class YouTubeRepository {

    private val _channelInfo = MutableStateFlow(
        ChannelInfo(
            id = "UC_NEXORA_DEMO_CANAL",
            title = "Canal Criador Demo",
            customUrl = "@canalcriadordemo",
            description = "Canal de demonstração integrado ao NEXORA. Conecte sua conta oficial do Google Cloud para sincronizar estatísticas em tempo real via YouTube Data API v3.",
            subscriberCount = 28400,
            videoCount = 42,
            viewCount = 1350900,
            isAuthorized = false,
            authStatus = ChannelAuthStatus.DEMO_MODE,
            lastSyncTime = System.currentTimeMillis()
        )
    )
    val channelInfo: StateFlow<ChannelInfo> = _channelInfo.asStateFlow()

    private val _channelVideos = MutableStateFlow<List<VideoItem>>(
        listOf(
            VideoItem(
                id = "demo_vid_01",
                title = "10 Segredos para Gravar Vídeos Melhores no Celular",
                description = "Guia completo de configurações manuais de câmera, enquadramento e som para iniciantes.",
                publishedAt = "2026-03-15",
                viewCount = 45200,
                likeCount = 3800,
                commentCount = 312,
                privacyStatus = "public",
                isShort = false,
                tags = listOf("celular", "gravacao", "dicas", "youtube")
            ),
            VideoItem(
                id = "demo_vid_02",
                title = "O Erro que Destrói a Retenção de Qualquer Short #Shorts",
                description = "Evite enrolação nos 3 primeiros segundos! Análise rápida de gráfico de retenção.",
                publishedAt = "2026-04-02",
                viewCount = 189400,
                likeCount = 14200,
                commentCount = 890,
                privacyStatus = "public",
                isShort = true,
                tags = listOf("shorts", "retencao", "viral")
            ),
            VideoItem(
                id = "demo_vid_03",
                title = "Bastidores do Novo Cenário e Equipamentos 2026",
                description = "Vídeo não-listado para revisão interna da equipe antes da estreia oficial.",
                publishedAt = "2026-04-18",
                viewCount = 120,
                likeCount = 24,
                commentCount = 8,
                privacyStatus = "unlisted",
                isShort = false,
                tags = listOf("bastidores", "setup", "vlog")
            )
        )
    )
    val channelVideos: StateFlow<List<VideoItem>> = _channelVideos.asStateFlow()

    fun syncStats() {
        val current = _channelInfo.value
        _channelInfo.value = current.copy(
            viewCount = current.viewCount + (150..650).random(),
            subscriberCount = current.subscriberCount + (2..14).random(),
            lastSyncTime = System.currentTimeMillis()
        )
    }

    fun disconnectChannel() {
        _channelInfo.value = _channelInfo.value.copy(
            isAuthorized = false,
            authStatus = ChannelAuthStatus.UNAUTHENTICATED
        )
    }

    fun enableDemoMode() {
        _channelInfo.value = _channelInfo.value.copy(
            isAuthorized = false,
            authStatus = ChannelAuthStatus.DEMO_MODE
        )
    }

    fun connectMockAuthorizedChannel(channelName: String, handle: String) {
        _channelInfo.value = ChannelInfo(
            id = "UC_${System.currentTimeMillis()}",
            title = channelName.ifBlank { "Meu Canal Oficial" },
            customUrl = if (handle.startsWith("@")) handle else "@$handle",
            description = "Canal autorizado via Google OAuth 2.0 com escopo https://www.googleapis.com/auth/youtube.upload",
            subscriberCount = 12500,
            videoCount = 18,
            viewCount = 480200,
            isAuthorized = true,
            authStatus = ChannelAuthStatus.AUTHORIZED,
            lastSyncTime = System.currentTimeMillis()
        )
    }

    fun validateUploadMetadata(
        title: String,
        description: String,
        tags: String
    ): UploadValidationResult {
        val errors = mutableListOf<String>()
        if (title.isBlank()) {
            errors.add("O título do vídeo não pode estar em branco.")
        } else if (title.length > 100) {
            errors.add("O título tem ${title.length} caracteres (máximo de 100 caracteres permitido pelo YouTube).")
        }

        if (description.length > 5000) {
            errors.add("A descrição tem ${description.length} caracteres (máximo de 5000 permitido pelo YouTube).")
        }

        val tagList = tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        val totalTagsChars = tagList.sumOf { it.length }
        if (totalTagsChars > 500) {
            errors.add("O comprimento total das tags excede 500 caracteres.")
        }

        return UploadValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
            tagCount = tagList.size
        )
    }
}

data class UploadValidationResult(
    val isValid: Boolean,
    val errors: List<String>,
    val tagCount: Int
)
