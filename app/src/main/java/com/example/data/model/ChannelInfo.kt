package com.example.data.model

data class ChannelInfo(
    val id: String,
    val title: String,
    val customUrl: String,
    val description: String,
    val subscriberCount: Long,
    val videoCount: Long,
    val viewCount: Long,
    val isAuthorized: Boolean = false,
    val authStatus: ChannelAuthStatus = ChannelAuthStatus.DEMO_MODE,
    val lastSyncTime: Long = System.currentTimeMillis()
)

enum class ChannelAuthStatus(val label: String) {
    AUTHORIZED("Canal Conectado"),
    DEMO_MODE("Modo Demonstração (Sem OAuth)"),
    EXPIRED("Sessão Expirada"),
    UNAUTHENTICATED("Desconectado")
}

data class VideoItem(
    val id: String,
    val title: String,
    val description: String,
    val publishedAt: String,
    val viewCount: Long,
    val likeCount: Long,
    val commentCount: Long,
    val privacyStatus: String, // "public", "private", "unlisted"
    val isShort: Boolean = false,
    val tags: List<String> = emptyList()
)
