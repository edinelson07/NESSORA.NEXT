package com.example.data.model

enum class DraftType(
    val label: String,
    val description: String,
    val defaultAspect: String
) {
    LONG_VIDEO("Vídeo Longo", "Estrutura aprofundada para YouTube horizontal (16:9)", "16:9"),
    SHORTS("YouTube Shorts", "Roteiro dinâmico e retenção rápida vertical (9:16)", "9:16"),
    IDEA("Ideia de Conteúdo", "Conceito inicial, ângulo original e público-alvo", "16:9"),
    SCRIPT("Roteiro Completo", "Divisão em cenas, ganchos e blocos narrativos", "16:9");

    companion object {
        fun fromString(value: String): DraftType {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: LONG_VIDEO
        }
    }
}
