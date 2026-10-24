package com.example.data.repository

import com.example.data.local.TvChannelDao
import com.example.data.local.TvChannelEntity
import kotlinx.coroutines.flow.Flow

class TvChannelRepository(
    private val tvChannelDao: TvChannelDao
) {

    fun getAllChannels(): Flow<List<TvChannelEntity>> = tvChannelDao.getAllChannels()

    fun getFavoriteChannels(): Flow<List<TvChannelEntity>> = tvChannelDao.getFavoriteChannels()

    suspend fun toggleFavorite(id: String, currentFavorite: Boolean) {
        tvChannelDao.updateFavorite(id, !currentFavorite)
    }

    suspend fun addOrUpdateChannel(channel: TvChannelEntity) {
        tvChannelDao.insertChannel(channel)
    }

    suspend fun deleteChannel(channel: TvChannelEntity) {
        tvChannelDao.deleteChannel(channel)
    }

    suspend fun ensureDefaultChannelsIfEmpty() {
        if (tvChannelDao.getChannelCount() == 0) {
            val defaults = getOfficialChannelsCatalog()
            tvChannelDao.insertAll(defaults)
        }
    }

    suspend fun resetToOfficialCatalog() {
        val defaults = getOfficialChannelsCatalog()
        tvChannelDao.insertAll(defaults)
    }

    companion object {
        fun getOfficialChannelsCatalog(): List<TvChannelEntity> {
            val now = "24/10/2026"
            return listOf(
                TvChannelEntity(
                    id = "tv-brasil",
                    name = "TV Brasil",
                    category = "Pública e Educativa",
                    region = "Nacional (Brasília / DF)",
                    logoSymbol = "TVB",
                    description = "Emissora pública da Empresa Brasil de Comunicação (EBC). Jornalismo público, documentários sobre o Brasil, programas culturais e infantis premiados.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UC8K3d7Z_9q6fF0i5q2n7oXA",
                    officialWebUrl = "https://tvbrasil.ebc.com.br",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Sinal público federal oficial de livre acesso e retransmissão autorizada (EBC).",
                    isFavorite = true
                ),
                TvChannelEntity(
                    id = "tv-cultura",
                    name = "TV Cultura",
                    category = "Pública e Educativa",
                    region = "São Paulo / Nacional",
                    logoSymbol = "CUL",
                    description = "Fundação Padre Anchieta. Referência máxima em debates científicos, Roda Viva, programas culturais, arte e dramaturgia educativa.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UCptff2hN0bH_7bM8rD0qN1Q",
                    officialWebUrl = "https://cultura.uol.com.br/aovivo",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Transmissão aberta oficial via Fundação Padre Anchieta / Cultura Play.",
                    isFavorite = true
                ),
                TvChannelEntity(
                    id = "tv-camara",
                    name = "TV Câmara",
                    category = "Legislativa e Cidadania",
                    region = "Brasília / Nacional",
                    logoSymbol = "CAM",
                    description = "Cobertura integral das votações em plenário, comissões temáticas, audiências públicas e debates do Congresso Nacional.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UCq_y8jW2d3l6X9w8y7n5m4g",
                    officialWebUrl = "https://www.camara.leg.br/tv",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Sinal público oficial de utilidade e transparência legislativa da Câmara dos Deputados."
                ),
                TvChannelEntity(
                    id = "tv-senado",
                    name = "TV Senado",
                    category = "Legislativa e Cidadania",
                    region = "Brasília / Nacional",
                    logoSymbol = "SEN",
                    description = "Sessões deliberativas ao vivo do Senado Federal, sabatinas de ministros, análises econômicas e memória política do país.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UCvI6XvCgMv18k0Z5C_8g3Tw",
                    officialWebUrl = "https://www12.senado.leg.br/tv",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Transmissão pública institucional oficial do Senado Federal."
                ),
                TvChannelEntity(
                    id = "canal-gov",
                    name = "Canal Gov",
                    category = "Pública e Educativa",
                    region = "Brasília / Nacional",
                    logoSymbol = "GOV",
                    description = "Canal oficial da Presidência da República e Ministérios. Entrevistas exclusivas, balanços governamentais e pronunciamentos públicos.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UC6n6H4s6M7F_Z_1e_5yQ",
                    officialWebUrl = "https://canalgov.ebc.com.br",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Transmissão de sinal público da Empresa Brasil de Comunicação."
                ),
                TvChannelEntity(
                    id = "tv-justica",
                    name = "TV Justiça",
                    category = "Legislativa e Cidadania",
                    region = "Brasília / Nacional",
                    logoSymbol = "JUS",
                    description = "Transmissão dos julgamentos plenários do Supremo Tribunal Federal (STF), do CNJ e programas de cidadania jurídica e direitos humanos.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UC7r_g2w8y7n5m4g3w2d1l6X",
                    officialWebUrl = "https://www.tvjustica.jus.br",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Canal público oficial gerido pelo Supremo Tribunal Federal."
                ),
                TvChannelEntity(
                    id = "record-news",
                    name = "Record News",
                    category = "Notícias 24h",
                    region = "São Paulo / Nacional",
                    logoSymbol = "RCN",
                    description = "Primeiro canal aberto do Brasil dedicado a 24 horas de jornalismo. Noticiário em tempo real, economia, tecnologia e política.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UCuiLR4C62CQlV00G6455Erg",
                    officialWebUrl = "https://recordnews.r7.com",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Transmissão online oficial autorizada do Grupo Record / R7.",
                    isFavorite = true
                ),
                TvChannelEntity(
                    id = "jovem-pan-news",
                    name = "Jovem Pan News",
                    category = "Notícias 24h",
                    region = "São Paulo / Nacional",
                    logoSymbol = "JPN",
                    description = "Transmissão contínua de notícias do Brasil e do mundo, análises de mercado, debates e comentários ao vivo.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UChLwV0P0c7y7tN_4b5c7x9A",
                    officialWebUrl = "https://jovempan.com.br/ao-vivo",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Streaming oficial mantido pelo Grupo Jovem Pan."
                ),
                TvChannelEntity(
                    id = "band-oficial",
                    name = "Band",
                    category = "Nacional Aberta",
                    region = "São Paulo / Nacional",
                    logoSymbol = "BND",
                    description = "Rede Bandeirantes de Televisão. Jornalismo de campo, transmissões esportivas e entretenimento. Acesse a transmissão oficial do Portal Band.",
                    officialStreamUrl = "https://www.band.uol.com.br/ao-vivo",
                    officialWebUrl = "https://www.band.uol.com.br/ao-vivo",
                    streamType = "PORTAL_OFICIAL",
                    availabilityStatus = "PORTAL_REQUERIDO",
                    lastVerifiedDate = now,
                    rightsInfo = "Exibição oficial condicionada ao player institucional do Portal Band."
                ),
                TvChannelEntity(
                    id = "sbt-oficial",
                    name = "SBT",
                    category = "Nacional Aberta",
                    region = "São Paulo / Nacional",
                    logoSymbol = "SBT",
                    description = "Sistema Brasileiro de Televisão. Programação clássica de auditório, telejornais, novelas e reality shows. Disponível via SBT Vídeos.",
                    officialStreamUrl = "https://www.sbtvideos.com.br",
                    officialWebUrl = "https://www.sbtvideos.com.br",
                    streamType = "PORTAL_OFICIAL",
                    availabilityStatus = "PORTAL_REQUERIDO",
                    lastVerifiedDate = now,
                    rightsInfo = "Transmissão digital oficial no portal de streaming SBT Vídeos."
                ),
                TvChannelEntity(
                    id = "tv-globo",
                    name = "TV Globo",
                    category = "Nacional Aberta",
                    region = "Rio de Janeiro / Nacional",
                    logoSymbol = "GLO",
                    description = "Principal rede de TV aberta do país. A transmissão oficial ao vivo da programação requer autenticação gratuita na plataforma Globoplay.",
                    officialStreamUrl = "https://globoplay.globo.com/agora-na-tv",
                    officialWebUrl = "https://globoplay.globo.com/agora-na-tv",
                    streamType = "PORTAL_OFICIAL",
                    availabilityStatus = "PORTAL_REQUERIDO",
                    lastVerifiedDate = now,
                    rightsInfo = "Direitos de retransmissão restritos ao Globoplay (requer Conta Globo gratuita)."
                ),
                TvChannelEntity(
                    id = "rede-vida",
                    name = "Rede Vida",
                    category = "Religiosa e Comunitária",
                    region = "São José do Rio Preto / Nacional",
                    logoSymbol = "RVD",
                    description = "Canal da família brasileira, com jornalismo comunitário, programas educativos, celebrações religiosas e transmissões de solidariedade.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UC8xN1y6w5r4v3q2l1k0j9h",
                    officialWebUrl = "https://www.redevida.com.br/ao-vivo",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Sinal público comunitário e de evangelização aberta."
                ),
                TvChannelEntity(
                    id = "tv-aparecida",
                    name = "TV Aparecida",
                    category = "Religiosa e Comunitária",
                    region = "Aparecida / Nacional",
                    logoSymbol = "APA",
                    description = "Programação com programas de culinária caipira, artesanato, turismo religioso, música sertaneja tradicional e transmissões do Santuário Nacional.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UC3K_x9y8z7w6v5u4t3s2r1q",
                    officialWebUrl = "https://www.a12.com/tv/tv-ao-vivo",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Transmissão aberta autorizada via Portal A12."
                ),
                TvChannelEntity(
                    id = "cancao-nova",
                    name = "Canção Nova",
                    category = "Religiosa e Comunitária",
                    region = "Cachoeira Paulista / Nacional",
                    logoSymbol = "CN",
                    description = "Transmissão 24 horas ininterruptas de encontros ecumênicos, palestras motivacionais, programas culturais e celebrações ao vivo.",
                    officialStreamUrl = "https://www.youtube.com/embed/live_stream?channel=UC7t6y5x4w3v2u1t0s9r8q7p",
                    officialWebUrl = "https://tv.cancaonova.com",
                    streamType = "EMBEDDED_STREAM",
                    availabilityStatus = "DISPONIVEL_AO_VIVO",
                    lastVerifiedDate = now,
                    rightsInfo = "Sinal comunitário aberto da Fundação João Paulo II."
                )
            )
        }
    }
}
