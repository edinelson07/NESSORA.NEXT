package com.example.ui.screens.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DraftEntity
import com.example.data.model.ChannelAuthStatus
import com.example.data.model.ChannelInfo
import com.example.data.model.DraftType
import com.example.data.model.VideoItem
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraCyanBlue
import com.example.ui.theme.NexoraCyanGlow
import com.example.ui.theme.NexoraLilacGlow
import com.example.ui.theme.NexoraLilacNeon
import com.example.ui.theme.NexoraMagenta
import com.example.ui.theme.NexoraMagentaGlow
import com.example.ui.theme.NexoraNeonGlow
import com.example.ui.theme.NexoraNeonGreen
import com.example.ui.theme.NexoraPurple
import com.example.ui.theme.NexoraPurpleElectric
import com.example.ui.theme.NexoraPurpleGlow
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceBorderGlow
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceDark
import com.example.ui.theme.NexoraSurfaceVariant
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary
import com.example.ui.theme.NexoraWarning

@Composable
fun HomeScreen(
    channelInfo: ChannelInfo,
    recentVideos: List<VideoItem>,
    recentDrafts: List<DraftEntity>,
    onOpenStudio: () -> Unit,
    onOpenStudioWithTopic: (String, DraftType) -> Unit,
    onOpenDraft: (DraftEntity) -> Unit,
    onDuplicateDraft: (DraftEntity) -> Unit,
    onOpenAi: () -> Unit,
    onOpenYouTubeManager: () -> Unit,
    onOpenAudioCatalog: () -> Unit,
    onSyncStats: () -> Unit,
    onShowToast: (String) -> Unit,
    onOpenTvAberta: () -> Unit = {},
    onOpenMarketplace: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        onShowToast("$label copiado!")
    }

    fun shareText(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Compartilhar com NEXORA"))
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexoraBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Hero Greeting Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Painel do Criador",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = NexoraTextPrimary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Impulsione retenção, roteiros e miniaturas hoje",
                        fontSize = 13.sp,
                        color = NexoraCyanBlue,
                        fontWeight = FontWeight.Medium
                    )
                }

                ElevatedButton(
                    onClick = onSyncStats,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraSurfaceCard,
                        contentColor = NexoraNeonGreen
                    ),
                    modifier = Modifier
                        .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(12.dp))
                        .testTag("home_sync_stats_btn")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Atualizar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Channel Status Hero Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = NexoraCyanBlue)
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF0F2648), NexoraSurfaceCard, Color(0xFF0A182E))
                        )
                    )
                    .border(
                        1.2.dp,
                        Brush.horizontalGradient(
                            listOf(NexoraCyanBlue.copy(alpha = 0.6f), NexoraSurfaceBorderGlow)
                        ),
                        RoundedCornerShape(22.dp)
                    )
                    .padding(18.dp)
                    .testTag("home_channel_card")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onOpenYouTubeManager() }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(NexoraCyanBlue.copy(alpha = 0.2f))
                                    .border(1.5.dp, NexoraCyanBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartDisplay,
                                    contentDescription = "Canal",
                                    tint = NexoraCyanBlue,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = channelInfo.title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = NexoraTextPrimary
                                )
                                Text(
                                    text = channelInfo.customUrl,
                                    fontSize = 12.sp,
                                    color = NexoraTextSecondary
                                )
                            }
                        }

                        // Status Tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED)
                                        NexoraNeonGreen.copy(alpha = 0.2f)
                                    else
                                        NexoraCyanBlue.copy(alpha = 0.2f)
                                )
                                .border(
                                    1.dp,
                                    if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) NexoraNeonGreen else NexoraCyanBlue,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = channelInfo.authStatus.label,
                                color = if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) NexoraNeonGreen else NexoraCyanBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats strip with pill containers
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        MetricPill("Inscritos", "%,d".format(channelInfo.subscriberCount), NexoraNeonGreen)
                        MetricPill("Vídeos Ativos", "${channelInfo.videoCount}", NexoraCyanBlue)
                        MetricPill("Visualizações", "%,d".format(channelInfo.viewCount), NexoraPurple)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Origem: YouTube Data API v3",
                            fontSize = 10.sp,
                            color = NexoraTextMuted
                        )

                        OutlinedButton(
                            onClick = onOpenYouTubeManager,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraCyanBlue),
                            modifier = Modifier.testTag("home_manage_youtube_btn")
                        ) {
                            Text("Gerenciar Canal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Quick Creator Actions (6 High Polish Cards)
        item {
            Text(
                text = "Ações Rápidas de Criação",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NexoraTextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    QuickActionCard(
                        title = "TV Aberta",
                        subtitle = "Canais ao Vivo",
                        icon = Icons.Default.LiveTv,
                        accentColor = NexoraLilacNeon,
                        glowColor = NexoraLilacGlow,
                        onClick = onOpenTvAberta,
                        testTag = "quick_action_tv"
                    )
                }
                item {
                    QuickActionCard(
                        title = "Loja Criador",
                        subtitle = "Marketplace Pix",
                        icon = Icons.Default.ShoppingBag,
                        accentColor = NexoraMagenta,
                        glowColor = NexoraMagentaGlow,
                        onClick = onOpenMarketplace,
                        testTag = "quick_action_store"
                    )
                }
                item {
                    QuickActionCard(
                        title = "Novo Roteiro",
                        subtitle = "16:9 Longo",
                        icon = Icons.Default.Edit,
                        accentColor = NexoraLilacNeon,
                        glowColor = NexoraLilacGlow,
                        onClick = onOpenStudio,
                        testTag = "quick_action_script"
                    )
                }
                item {
                    QuickActionCard(
                        title = "Short Viral",
                        subtitle = "9:16 Dinâmico",
                        icon = Icons.Default.Bolt,
                        accentColor = NexoraCyanBlue,
                        glowColor = NexoraCyanGlow,
                        onClick = { onOpenStudioWithTopic("Shorts Viral sobre Tema em Alta", DraftType.SHORTS) },
                        testTag = "quick_action_shorts"
                    )
                }
                item {
                    QuickActionCard(
                        title = "NEXORA AI",
                        subtitle = "Assistente",
                        icon = Icons.Default.AutoAwesome,
                        accentColor = NexoraPurple,
                        glowColor = NexoraPurpleGlow,
                        onClick = onOpenAi,
                        testTag = "quick_action_ai"
                    )
                }
                item {
                    QuickActionCard(
                        title = "Miniaturas",
                        subtitle = "Design & CTR",
                        icon = Icons.Default.Image,
                        accentColor = NexoraNeonGreen,
                        glowColor = NexoraNeonGlow,
                        onClick = onOpenStudio,
                        testTag = "quick_action_thumbnails"
                    )
                }
                item {
                    QuickActionCard(
                        title = "Áudio & Trilha",
                        subtitle = "Licenças Livres",
                        icon = Icons.Default.GraphicEq,
                        accentColor = NexoraCyanBlue,
                        glowColor = NexoraCyanGlow,
                        onClick = onOpenAudioCatalog,
                        testTag = "quick_action_audio"
                    )
                }
            }
        }

        // Tendências com Botão de Ação Imediata
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = NexoraNeonGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ideias em Alta no YouTube",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraTextPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                HomeTrendCard(
                    title = "O Método dos Primeiros 3 Segundos em Shorts",
                    description = "Ganchos hipnóticos que dobram a taxa de retenção média e geram compartilhamento.",
                    badge = "ALTA RETENÇÃO",
                    onAction = {
                        onOpenStudioWithTopic("Como Criar Ganchos de 3 Segundos no Shorts", DraftType.SHORTS)
                    }
                )
                HomeTrendCard(
                    title = "Como Criar Vídeos com IA sem Penalização do Algoritmo",
                    description = "Técnicas para usar roteiros e ferramentas mantendo originalidade e alto tempo de exibição.",
                    badge = "TENDÊNCIA 2026",
                    onAction = {
                        onOpenStudioWithTopic("Guia Prático: Usando IA no YouTube com Sucesso", DraftType.LONG_VIDEO)
                    }
                )
            }
        }

        // Conteúdos em Elaboração (Rascunhos Recentes)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rascunhos em Andamento",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextPrimary
                )
                Text(
                    text = "Ver todos",
                    fontSize = 12.sp,
                    color = NexoraNeonGreen,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onOpenStudio() }
                )
            }
        }

        if (recentDrafts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NexoraSurfaceCard)
                        .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nenhum rascunho salvo ainda.",
                            color = NexoraTextMuted,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ElevatedButton(
                            onClick = onOpenStudio,
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = NexoraNeonGreen,
                                contentColor = NexoraBackground
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Criar Primeiro Conteúdo", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(recentDrafts.take(3)) { draft ->
                HomeDraftCard(
                    draft = draft,
                    onOpen = { onOpenDraft(draft) },
                    onDuplicate = { onDuplicateDraft(draft) },
                    onShare = { shareText("PROJETO: ${draft.title}\n\nROTEIRO:\n${draft.scriptBody}") }
                )
            }
        }

        // Desempenho Recente dos Vídeos com Ações Reais
        item {
            Text(
                text = "Desempenho dos Vídeos Sincronizados",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = NexoraTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(recentVideos) { video ->
            HomeVideoCard(
                video = video,
                onOpenLink = {
                    val url = "https://www.youtube.com/results?search_query=${Uri.encode(video.title)}"
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                },
                onCopyTitle = {
                    copyToClipboard("Título do Vídeo", video.title)
                },
                onCreateSequel = {
                    onOpenStudioWithTopic("Continuação / Parte 2: ${video.title}", if (video.isShort) DraftType.SHORTS else DraftType.LONG_VIDEO)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
private fun MetricPill(label: String, value: String, accentColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(NexoraSurfaceDark.copy(alpha = 0.6f))
            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column {
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = accentColor
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = NexoraTextMuted,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    glowColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp), spotColor = accentColor)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(NexoraCardElevated, NexoraSurfaceCard)
                )
            )
            .border(1.2.dp, NexoraSurfaceBorder, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(14.dp)
            .testTag(testTag)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(glowColor)
                    .border(1.dp, accentColor.copy(alpha = 0.8f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NexoraTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = NexoraTextMuted
            )
        }
    }
}

@Composable
private fun HomeTrendCard(
    title: String,
    description: String,
    badge: String,
    onAction: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NexoraSurfaceCard)
            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NexoraNeonGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = badge, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = NexoraNeonGreen)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                fontSize = 11.sp,
                color = NexoraTextSecondary,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ElevatedButton(
                    onClick = onAction,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraCyanBlue,
                        contentColor = NexoraBackground
                    )
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Criar Roteiro Agora", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun HomeDraftCard(
    draft: DraftEntity,
    onOpen: () -> Unit,
    onDuplicate: () -> Unit,
    onShare: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NexoraSurfaceCard)
            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("home_draft_${draft.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (draft.type == "SHORTS") NexoraCyanBlue.copy(alpha = 0.2f)
                                else NexoraNeonGreen.copy(alpha = 0.2f)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (draft.type == "SHORTS") "SHORTS" else "VÍDEO LONGO",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (draft.type == "SHORTS") NexoraCyanBlue else NexoraNeonGreen
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Status: ${draft.status}",
                        fontSize = 11.sp,
                        color = NexoraTextMuted
                    )
                }

                Row {
                    IconButton(onClick = onShare, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Share, contentDescription = "Compartilhar", tint = NexoraTextMuted, modifier = Modifier.size(15.dp))
                    }
                    IconButton(onClick = onDuplicate, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Duplicar", tint = NexoraTextMuted, modifier = Modifier.size(15.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = draft.title.ifBlank { "Rascunho sem título" },
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NexoraTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (draft.topic.isNotBlank()) {
                Text(
                    text = "Tema: ${draft.topic}",
                    fontSize = 11.sp,
                    color = NexoraTextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Duração: ${draft.targetDuration}",
                    fontSize = 11.sp,
                    color = NexoraTextMuted
                )

                ElevatedButton(
                    onClick = onOpen,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Continuar Edição", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
private fun HomeVideoCard(
    video: VideoItem,
    onOpenLink: () -> Unit,
    onCopyTitle: () -> Unit,
    onCreateSequel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NexoraSurfaceCard)
            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("home_video_${video.id}")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = video.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextPrimary,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (video.privacyStatus) {
                                "public" -> NexoraNeonGreen.copy(alpha = 0.15f)
                                "unlisted" -> NexoraWarning.copy(alpha = 0.15f)
                                else -> NexoraTextMuted.copy(alpha = 0.15f)
                            }
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = when (video.privacyStatus) {
                            "public" -> "Público"
                            "unlisted" -> "Não Listado"
                            else -> "Privado"
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (video.privacyStatus) {
                            "public" -> NexoraNeonGreen
                            "unlisted" -> NexoraWarning
                            else -> NexoraTextMuted
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Visibility, contentDescription = null, tint = NexoraCyanBlue, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${"%,d".format(video.viewCount)} visualizações",
                        fontSize = 11.sp,
                        color = NexoraTextSecondary
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = onCopyTitle,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraTextSecondary)
                    ) {
                        Text("Copiar Título", fontSize = 10.sp)
                    }

                    ElevatedButton(
                        onClick = onCreateSequel,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraCyanBlue,
                            contentColor = NexoraBackground
                        )
                    ) {
                        Text("Criar Parte 2", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
