package com.example.data.repository

import com.example.data.local.DraftDao
import com.example.data.local.DraftEntity
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class DraftRepository(private val draftDao: DraftDao) {

    fun getAllDrafts(): Flow<List<DraftEntity>> = draftDao.getAllDrafts()

    fun getDraftsByType(type: String): Flow<List<DraftEntity>> = draftDao.getDraftsByType(type)

    fun searchDrafts(query: String): Flow<List<DraftEntity>> = draftDao.searchDrafts(query)

    suspend fun getDraftById(id: Long): DraftEntity? = draftDao.getDraftById(id)

    suspend fun saveDraft(draft: DraftEntity): Long {
        return if (draft.id == 0L) {
            draftDao.insertDraft(draft.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
        } else {
            draftDao.updateDraft(draft.copy(updatedAt = System.currentTimeMillis()))
            draft.id
        }
    }

    suspend fun duplicateDraft(original: DraftEntity): Long {
        val duplicated = original.copy(
            id = 0,
            title = "${original.title} (Cópia)",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        return draftDao.insertDraft(duplicated)
    }

    suspend fun deleteDraft(draft: DraftEntity) = draftDao.deleteDraft(draft)

    suspend fun deleteById(id: Long) = draftDao.deleteById(id)

    suspend fun ensureDefaultTemplatesIfEmpty() {
        if (draftDao.countDrafts() == 0) {
            val sampleDrafts = listOf(
                DraftEntity(
                    title = "Como Dominar o Algoritmo do YouTube em 2026",
                    type = "LONG_VIDEO",
                    topic = "Crescimento de Canal e Retenção",
                    targetAudience = "Criadores iniciantes e intermediários",
                    tone = "Didático e Inspirador",
                    targetDuration = "10 min",
                    hook = "Você sabia que 78% dos vídeos perdem mais da metade do público nos primeiros 30 segundos? Neste vídeo eu mostro a técnica exata para reverter isso.",
                    scriptBody = "--- BLOCO 1: GANCHO (0-30s) ---\nMostre o gráfico de retenção real na tela...\n\n--- BLOCO 2: PROMESSA (30-60s) ---\nExplique os 3 pilares de click-through rate e engajamento orgânico...\n\n--- BLOCO 3: MÉTODO PRÁTICO ---\nPasso a passo com tela dividida demonstrando títulos de alto contraste.",
                    description = "Aprenda a estruturar seus vídeos no YouTube para maximizar a retenção e cliques orgânicos sem apelar para clickbait enganoso.\n\nCapítulos:\n0:00 - O Maior Erro de Retenção\n2:15 - A Técnica do Segundo Gancho\n5:40 - Exemplos Reais e Análise\n8:20 - Checklist de Publicação\n\n#YouTube #Criadores #Crescimento",
                    tags = "youtube, algoritmo, criadores de conteudo, dicas youtube, retencao, ctr, seo youtube",
                    thumbnailTitle = "RETENÇÃO MÁXIMA",
                    thumbnailSubtitle = "O SEGREDO DO ALGORITMO",
                    thumbnailBgHex = "#07111F",
                    thumbnailAccentHex = "#36E58D",
                    status = "PRONTO",
                    isFavorite = true
                ),
                DraftEntity(
                    title = "3 Dicas Relâmpago de Iluminação para Shorts",
                    type = "SHORTS",
                    topic = "Produção de Vídeo Vertical",
                    targetAudience = "Criadores de Shorts, Reels e TikTok",
                    tone = "Ágil e Direto ao Ponto",
                    targetDuration = "45s",
                    hook = "Pare de gravar vídeos escuros! Com essa luminária de R$ 30 e uma folha branca o seu vídeo parece de cinema.",
                    scriptBody = "1. Posição da luz principal a 45 graus do rosto.\n2. Use papel manteiga como difusor caseiro para suavizar sombras duras.\n3. Luz de recorte atrás para separar você do fundo escuro.",
                    description = "Dica rápida de iluminação barata e profissional para seus Shorts e Reels!\n\n#Shorts #ProducaoDeVideo #DicasDeCriacao",
                    tags = "shorts, iluminacao, producao, tutorial rapido, setup criador",
                    thumbnailTitle = "LUZ DE CINEMA",
                    thumbnailSubtitle = "GASTANDO R$ 30",
                    thumbnailBgHex = "#0D1B2A",
                    thumbnailAccentHex = "#27B8FF",
                    status = "RASCUNHO",
                    isFavorite = false
                )
            )
            draftDao.insertAll(sampleDrafts)
        }
    }

    suspend fun exportToJson(): String {
        val drafts = draftDao.getAllDraftsSync()
        val root = JSONObject()
        root.put("version", 1)
        root.put("appName", "NEXORA")
        root.put("exportedAt", System.currentTimeMillis())

        val array = JSONArray()
        for (d in drafts) {
            val item = JSONObject()
            item.put("title", d.title)
            item.put("type", d.type)
            item.put("topic", d.topic)
            item.put("targetAudience", d.targetAudience)
            item.put("tone", d.tone)
            item.put("targetDuration", d.targetDuration)
            item.put("hook", d.hook)
            item.put("scriptBody", d.scriptBody)
            item.put("description", d.description)
            item.put("tags", d.tags)
            item.put("thumbnailTitle", d.thumbnailTitle)
            item.put("thumbnailSubtitle", d.thumbnailSubtitle)
            item.put("thumbnailBgHex", d.thumbnailBgHex)
            item.put("thumbnailAccentHex", d.thumbnailAccentHex)
            item.put("status", d.status)
            item.put("createdAt", d.createdAt)
            item.put("updatedAt", d.updatedAt)
            array.put(item)
        }
        root.put("drafts", array)
        return root.toString(2)
    }

    suspend fun importFromJson(jsonString: String): Result<Int> {
        return try {
            val root = JSONObject(jsonString)
            if (!root.has("drafts")) {
                return Result.failure(IllegalArgumentException("Formato de backup inválido: chave 'drafts' não encontrada."))
            }
            val array = root.getJSONArray("drafts")
            val list = mutableListOf<DraftEntity>()
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                list.add(
                    DraftEntity(
                        title = item.optString("title", "Rascunho Importado"),
                        type = item.optString("type", "LONG_VIDEO"),
                        topic = item.optString("topic", ""),
                        targetAudience = item.optString("targetAudience", "Geral"),
                        tone = item.optString("tone", "Informativo"),
                        targetDuration = item.optString("targetDuration", "10 min"),
                        hook = item.optString("hook", ""),
                        scriptBody = item.optString("scriptBody", ""),
                        description = item.optString("description", ""),
                        tags = item.optString("tags", ""),
                        thumbnailTitle = item.optString("thumbnailTitle", ""),
                        thumbnailSubtitle = item.optString("thumbnailSubtitle", ""),
                        thumbnailBgHex = item.optString("thumbnailBgHex", "#07111F"),
                        thumbnailAccentHex = item.optString("thumbnailAccentHex", "#36E58D"),
                        status = item.optString("status", "RASCUNHO"),
                        createdAt = item.optLong("createdAt", System.currentTimeMillis()),
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
            draftDao.insertAll(list)
            Result.success(list.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
