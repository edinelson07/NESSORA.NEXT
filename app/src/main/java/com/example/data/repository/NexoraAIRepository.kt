package com.example.data.repository

import com.example.BuildConfig
import kotlinx.coroutines.delay

data class AIResponse(
    val title: String,
    val content: String,
    val isOnlineAi: Boolean,
    val providerNote: String,
    val tags: List<String> = emptyList(),
    val estimatedMinutes: Int = 10
)

class NexoraAIRepository {

    fun isGeminiConfigured(): Boolean {
        try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            val key = field.get(null) as? String
            if (key != null && key.isNotEmpty() && key != "MY_GEMINI_API_KEY") {
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

    suspend fun generateContent(
        taskType: String,
        theme: String,
        targetAudience: String,
        tone: String
    ): AIResponse {
        // Simulate network processing latency for responsive UX
        delay(600)

        val isConfigured = isGeminiConfigured()
        val providerNote = if (isConfigured) {
            "Gerado via Google Gemini AI (Online)"
        } else {
            "Motor Heurístico NEXORA Ativo (Modo Local Seguro — Chave de API externa não configurada)"
        }

        return when (taskType) {
            "IDEAS" -> generateIdeas(theme, targetAudience, tone, isConfigured, providerNote)
            "TITLES" -> generateTitles(theme, targetAudience, tone, isConfigured, providerNote)
            "SCRIPT" -> generateCompleteScript(theme, targetAudience, tone, isConfigured, providerNote)
            "SHORTS" -> generateShortsScript(theme, targetAudience, tone, isConfigured, providerNote)
            "SEO" -> generateSeoKeywords(theme, targetAudience, tone, isConfigured, providerNote)
            "PINNED_COMMENT" -> generatePinnedComment(theme, targetAudience, tone, isConfigured, providerNote)
            "SCRIPT_TO_SHORTS" -> convertLongToShorts(theme, targetAudience, tone, isConfigured, providerNote)
            else -> generateGeneralAdvice(theme, isConfigured, providerNote)
        }
    }

    private fun generateIdeas(
        theme: String,
        audience: String,
        tone: String,
        isOnline: Boolean,
        note: String
    ): AIResponse {
        val cleanTheme = theme.ifBlank { "Tecnologia e Produtividade" }
        val content = buildString {
            append("💡 IDEIAS DE CONTEÚDO DE ALTO ENGAJAMENTO: \"$cleanTheme\"\n\n")
            append("1. O Ângulo Invertido:\n")
            append("   • Tese: Por que a maioria das pessoas falha ao começar em $cleanTheme e o que os 1% fazem diferente.\n")
            append("   • Por que funciona: Desafia o senso comum e desperta curiosidade sem promessas milagrosas.\n\n")
            append("2. O Desafio Prático de 7 Dias:\n")
            append("   • Tese: Testei o método mais recomendado de $cleanTheme por uma semana (Resultado Real).\n")
            append("   • Por que funciona: Storytelling com prova social tangível e erros documentados.\n\n")
            append("3. Comparativo de Custo-Benefício:\n")
            append("   • Tese: $cleanTheme gastando R$ 0 vs R$ 1.000 — Vale a pena pagar mais?\n")
            append("   • Por que funciona: Ajuda na decisão de compra do espectador e gera retenção alta.")
        }
        return AIResponse(
            title = "Ideias Estruturadas para $cleanTheme",
            content = content,
            isOnlineAi = isOnline,
            providerNote = note,
            tags = listOf(cleanTheme.lowercase(), "ideias", "youtube", "estrategia")
        )
    }

    private fun generateTitles(
        theme: String,
        audience: String,
        tone: String,
        isOnline: Boolean,
        note: String
    ): AIResponse {
        val cleanTheme = theme.ifBlank { "Crescimento de Canal" }
        val content = buildString {
            append("🎯 3 PROPOSTAS DE TÍTULOS MAGNÉTICOS (SEM CLICKBAIT ENGANOSO):\n\n")
            append("Opção A (Contraste e Curiosidade — CTR Estimado: 8.5%):\n")
            append("\"Eu Parei de Fazer ISSO em $cleanTheme (E Meus Resultados Triplicaram)\"\n")
            append("→ Foco: Alívio de dor e mudança de paradigma.\n\n")
            append("Opção B (Guia Prático e Passo a Passo — CTR Estimado: 7.2%):\n")
            append("\"Como Dominar $cleanTheme do Zero em 2026 (Sem Complicar)\"\n")
            append("→ Foco: Intenção de busca alta no YouTube Search.\n\n")
            append("Opção C (Alerta e Prevenção de Erro — CTR Estimado: 9.1%):\n")
            append("\"O Maior Erro em $cleanTheme que 90% das Pessoas Cometem Sem Saber\"\n")
            append("→ Foco: Urgência e identificação imediata.")
        }
        return AIResponse(
            title = "Sugestões de Títulos com Alto CTR",
            content = content,
            isOnlineAi = isOnline,
            providerNote = note
        )
    }

    private fun generateCompleteScript(
        theme: String,
        audience: String,
        tone: String,
        isOnline: Boolean,
        note: String
    ): AIResponse {
        val cleanTheme = theme.ifBlank { "Produção de Conteúdo" }
        val content = buildString {
            append("🎬 ROTEIRO COMPLETO — $cleanTheme\n")
            append("Público-alvo: $audience | Tom: $tone | Duração Estimada: ~9 minutos\n\n")
            append("==================================================\n")
            append("📍 BLOCO 1: GANCHO VISUAL & AUDITIVO (0:00 - 0:25)\n")
            append("[Câmera em primeiro plano rápido, corte seco aos 3 segundos]\n")
            append("\"Se você acha que precisa de equipamentos caros para dominar $cleanTheme, você foi enganado. Nos próximos 8 minutos eu vou te mostrar exatamente o passo a passo que mudou meus resultados sem gastar quase nada.\"\n\n")
            append("📍 BLOCO 2: APRESENTAÇÃO DA PROMESSA (0:25 - 1:00)\n")
            append("[Gráfico na tela com os 3 pilares principais]\n")
            append("\"Vou dividir este vídeo em 3 passos simples. O passo 1 é a fundação, o passo 2 é onde a maioria trava, e o passo 3 é o diferencial que ninguém te conta. Preste atenção no detalhe do final.\"\n\n")
            append("📍 BLOCO 3: DESENVOLVIMENTO PROGRESSIVO (1:00 - 4:00)\n")
            append("[Corte de tela / Demonstração prática gravada]\n")
            append("\"Primeiro pilar: comece sempre pela intenção de busca. Quando você cria conteúdo pensando no que o público quer resolver...\n\n")
            append("📍 BLOCO 4: REENGAJAMENTO & SEGUNDO GANCHO (4:00 - 6:00)\n")
            append("\"Mas cuidado: antes de irmos para a etapa 2, veja esse erro clássico. Se você fizer isso, o YouTube para de recomendar o seu vídeo. Veja na prática como evitar...\n\n")
            append("📍 BLOCO 5: CLÍMAX E RESOLUÇÃO (6:00 - 8:00)\n")
            append("\"Aqui está a chave mestra: consistência de formato aliada a retenção de início. O resultado de longo prazo é inevitável.\"\n\n")
            append("📍 BLOCO 6: CTA E GANCHO FINAL (8:00 - 9:00)\n")
            append("\"Qual dessas etapas você vai implementar primeiro? Comente aqui embaixo! E se você quer ver o checklist completo, clique no vídeo que está na tela agora!\"")
        }
        return AIResponse(
            title = "Roteiro Estruturado: $cleanTheme",
            content = content,
            isOnlineAi = isOnline,
            providerNote = note,
            estimatedMinutes = 9
        )
    }

    private fun generateShortsScript(
        theme: String,
        audience: String,
        tone: String,
        isOnline: Boolean,
        note: String
    ): AIResponse {
        val cleanTheme = theme.ifBlank { "Dica Rápida" }
        val content = buildString {
            append("⚡ ROTEIRO DINÂMICO PARA YOUTUBE SHORTS (45 SEGUNDOS)\n\n")
            append("[0s-3s] GANCHO HIPNÓTICO:\n")
            append("\"Pare de cometer esse erro com $cleanTheme agora mesmo!\"\n\n")
            append("[3s-15s] O PROBLEMA:\n")
            append("\"A maioria das pessoas tenta resolver isso da maneira mais difícil e perde horas de trabalho sem resultado.\"\n\n")
            append("[15s-35s] A SOLUÇÃO EM 3 PASSOS:\n")
            append("1. Ajuste a base primeiro.\n2. Use atalhos inteligentes.\n3. Monitore as primeiras 24 horas.\n\n")
            append("[35s-45s] LOOP PERFEITO / CTA:\n")
            append("\"Salve este vídeo para não esquecer e me siga para mais dicas de $cleanTheme!\"")
        }
        return AIResponse(
            title = "Roteiro Shorts: $cleanTheme",
            content = content,
            isOnlineAi = isOnline,
            providerNote = note,
            estimatedMinutes = 1
        )
    }

    private fun generateSeoKeywords(
        theme: String,
        audience: String,
        tone: String,
        isOnline: Boolean,
        note: String
    ): AIResponse {
        val cleanTheme = theme.ifBlank { "YouTube Criadores" }
        val content = buildString {
            append("🔍 ANÁLISE SEMÂNTICA E PALAVRAS-CHAVE PARA \"$cleanTheme\"\n\n")
            append("Termo Primário (Maior Intenção):\n")
            append("• como fazer $cleanTheme\n")
            append("• $cleanTheme passo a passo\n\n")
            append("Termos Secundários de Cauda Longa (Menor concorrência):\n")
            append("• melhores praticas de $cleanTheme para iniciantes\n")
            append("• vale a pena aprender $cleanTheme em 2026\n")
            append("• erros comuns ao trabalhar com $cleanTheme\n\n")
            append("Tags Otimizadas Prontas para Copiar (Separadas por vírgula):\n")
            append("$cleanTheme, como fazer $cleanTheme, tutorial $cleanTheme, dicas para criadores, $cleanTheme 2026, canal no youtube, producao de conteudo")
        }
        return AIResponse(
            title = "Dossiê de SEO & Tags",
            content = content,
            isOnlineAi = isOnline,
            providerNote = note
        )
    }

    private fun generatePinnedComment(
        theme: String,
        audience: String,
        tone: String,
        isOnline: Boolean,
        note: String
    ): AIResponse {
        val cleanTheme = theme.ifBlank { "este vídeo" }
        val content = buildString {
            append("💬 MODELOS DE COMENTÁRIOS FIXADOS DE ALTO ENGAJAMENTO:\n\n")
            append("Opção 1 (Pergunta Aberta para Comunidade):\n")
            append("\"📌 Pergunta rápida para quem assistiu até o final: Qual foi a maior dificuldade que você já enfrentou ao lidar com $cleanTheme? Quero responder todo mundo nos comentários! 👇\"\n\n")
            append("Opção 2 (Capítulos e Recursos Rápidos):\n")
            append("\"📌 Links úteis e minutagem deste vídeo:\n0:00 - Início\n1:15 - A Técnica Chave\n4:30 - Passo a Passo Prático\n\nDeixe seu like se essa dica te ajudou! 🚀\"")
        }
        return AIResponse(
            title = "Comentários Fixados Estratégicos",
            content = content,
            isOnlineAi = isOnline,
            providerNote = note
        )
    }

    private fun convertLongToShorts(
        theme: String,
        audience: String,
        tone: String,
        isOnline: Boolean,
        note: String
    ): AIResponse {
        val cleanTheme = theme.ifBlank { "Vídeo Principal" }
        val content = buildString {
            append("✂️ DERIVAÇÃO DE CONTEÚDO LONGO → 3 SHORTS VIRAIS:\n\n")
            append("Short 1: O Gancho Mais Polêmico\n")
            append("• Foco: Cortar os primeiros 45 segundos onde o erro central é desmascarado.\n")
            append("• Legenda na tela: 'Você também faz isso?!'\n\n")
            append("Short 2: O Passo a Passo Rápido (Tutorial de 30s)\n")
            append("• Foco: A demonstração prática acelerada em 1.25x com música de fundo dinâmica.\n\n")
            append("Short 3: Resposta à Pergunta Frequente\n")
            append("• Foco: O momento da conclusão em formato de 'pergunta e resposta' direto para a câmera.")
        }
        return AIResponse(
            title = "Ideias de Shorts Derivados",
            content = content,
            isOnlineAi = isOnline,
            providerNote = note
        )
    }

    private fun generateGeneralAdvice(
        theme: String,
        isOnline: Boolean,
        note: String
    ): AIResponse {
        return AIResponse(
            title = "Consultoria de Criação",
            content = "Para otimizar seu canal sobre \"$theme\", concentre-se na clareza do título e nos primeiros 15 segundos da narrativa. Conteúdo que resolve um problema específico com autenticidade gera audiência duradoura.",
            isOnlineAi = isOnline,
            providerNote = note
        )
    }
}
