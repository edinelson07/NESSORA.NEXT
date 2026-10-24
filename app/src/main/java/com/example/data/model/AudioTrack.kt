package com.example.data.model

data class AudioTrack(
    val id: Long = 0,
    val title: String,
    val artist: String,
    val sourceUrl: String = "",
    val licenseType: AudioLicense = AudioLicense.ROYALTY_FREE,
    val category: String = "Geral",
    val notes: String = "",
    val linkedDraftId: Long? = null,
    val addedAt: Long = System.currentTimeMillis()
)

enum class AudioLicense(val label: String, val safeForMonetization: Boolean) {
    ROYALTY_FREE("Royalty-Free / Licença Livre", true),
    YOUTUBE_AUDIO_LIBRARY("Biblioteca de Áudio do YouTube", true),
    CREATIVE_COMMONS_BY("Creative Commons (Atribuição CC-BY)", true),
    REQUIRES_LICENSE("Requer Licença Comercial Prévia", false),
    CUSTOM_PURCHASE("Licenciado Diretamente do Autor", true)
}
