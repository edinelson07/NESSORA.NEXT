package com.example.ui.screens.tv

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.local.TvChannelEntity
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraBrandGradient
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraLilacGlow
import com.example.ui.theme.NexoraLilacNeon
import com.example.ui.theme.NexoraMagenta
import com.example.ui.theme.NexoraPurpleElectric
import com.example.ui.theme.NexoraSurface
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceBorderGlow
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceDark
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary
import com.example.ui.theme.NexoraWineDark

@Composable
fun TvAbertaScreen(
    channels: List<TvChannelEntity>,
    activePlayingChannel: TvChannelEntity?,
    onPlayChannel: (TvChannelEntity) -> Unit,
    onClosePlayer: () -> Unit,
    onToggleFavorite: (TvChannelEntity) -> Unit,
    onRefreshCatalog: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        if (activePlayingChannel != null) {
            onClosePlayer()
        } else {
            onBack()
        }
    }

    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("TODOS") }
    var showOnlyFavorites by remember { mutableStateOf(false) }
    var showComplianceDialog by remember { mutableStateOf(false) }
    var channelForCompliance by remember { mutableStateOf<TvChannelEntity?>(null) }

    val categories = listOf(
        "TODOS",
        "Pública e Educativa",
        "Notícias 24h",
        "Legislativa e Cidadania",
        "Nacional Aberta",
        "Religiosa e Comunitária"
    )

    val filteredChannels = channels.filter { channel ->
        val matchesCategory = selectedCategory == "TODOS" || channel.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                channel.name.contains(searchQuery, ignoreCase = true) ||
                channel.category.contains(searchQuery, ignoreCase = true) ||
                channel.region.contains(searchQuery, ignoreCase = true)
        val matchesFav = !showOnlyFavorites || channel.isFavorite

        matchesCategory && matchesSearch && matchesFav
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexoraBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Top Header
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NexoraSurfaceCard)
                            .border(1.dp, NexoraSurfaceBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = NexoraLilacNeon,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TV Aberta",
                                color = NexoraTextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NexoraWineDark)
                                    .border(1.dp, NexoraMagenta, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "BRASIL",
                                    color = NexoraLilacNeon,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Transmissões Oficiais Autorizadas e Emissoras Nacionais",
                            color = NexoraTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = onRefreshCatalog,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NexoraSurfaceCard)
                        .border(1.dp, NexoraSurfaceBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Sincronizar Catálogo",
                        tint = NexoraTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Active Live Player Container
        if (activePlayingChannel != null) {
            item {
                ActiveTvPlayerCard(
                    channel = activePlayingChannel,
                    onClose = onClosePlayer,
                    onOpenOfficialPortal = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(activePlayingChannel.officialWebUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }

        // Search Bar & Favorites Filter
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text("Buscar canal, categoria ou estado...", color = NexoraTextMuted, fontSize = 13.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = NexoraLilacNeon)
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary,
                        focusedBorderColor = NexoraLilacNeon,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedContainerColor = NexoraSurfaceCard,
                        unfocusedContainerColor = NexoraSurfaceCard
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tv_search_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { showOnlyFavorites = !showOnlyFavorites },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (showOnlyFavorites) NexoraWineDark else NexoraSurfaceCard)
                        .border(
                            width = 1.dp,
                            color = if (showOnlyFavorites) NexoraMagenta else NexoraSurfaceBorder,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (showOnlyFavorites) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Filtrar Favoritos",
                        tint = if (showOnlyFavorites) NexoraLilacNeon else NexoraTextSecondary
                    )
                }
            }
        }

        // Category Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) NexoraBrandGradient else Brush.linearGradient(listOf(NexoraSurfaceCard, NexoraSurfaceCard)))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NexoraLilacNeon else NexoraSurfaceBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) NexoraTextPrimary else NexoraTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Official Compliance Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(NexoraWineDark.copy(alpha = 0.5f), NexoraSurfaceCard)
                        )
                    )
                    .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NexoraLilacNeon,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Transmissões diretas e autorizadas de emissoras brasileiras públicas, educativas e comerciais, em total conformidade com a legislação.",
                        color = NexoraTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Channels Counter
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredChannels.size} canais disponíveis",
                    color = NexoraTextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Checagem: Outubro/2026",
                    color = NexoraTextMuted,
                    fontSize = 11.sp
                )
            }
        }

        // Channels List
        if (filteredChannels.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.LiveTv,
                            contentDescription = null,
                            tint = NexoraTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Nenhum canal encontrado",
                            color = NexoraTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tente alterar os termos de busca ou remover os filtros.",
                            color = NexoraTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(filteredChannels, key = { it.id }) { channel ->
                TvChannelCard(
                    channel = channel,
                    isPlaying = activePlayingChannel?.id == channel.id,
                    onPlay = { onPlayChannel(channel) },
                    onToggleFavorite = { onToggleFavorite(channel) },
                    onOpenCompliance = {
                        channelForCompliance = channel
                        showComplianceDialog = true
                    },
                    onOpenWebPortal = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(channel.officialWebUrl))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }

    // Dialog for rights and compliance
    if (showComplianceDialog && channelForCompliance != null) {
        val ch = channelForCompliance!!
        AlertDialog(
            onDismissRequest = { showComplianceDialog = false },
            title = {
                Text(text = "Informações do Canal: ${ch.name}", color = NexoraTextPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Região: ${ch.region}", color = NexoraLilacNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Categoria: ${ch.category}", color = NexoraTextSecondary, fontSize = 12.sp)
                    Text(text = ch.description, color = NexoraTextPrimary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NexoraSurfaceDark)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Direitos & Transmissão: ${ch.rightsInfo}\nÚltima verificação oficial: ${ch.lastVerifiedDate}",
                            color = NexoraTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showComplianceDialog = false }) {
                    Text("Fechar", color = NexoraLilacNeon)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun ActiveTvPlayerCard(
    channel: TvChannelEntity,
    onClose: () -> Unit,
    onOpenOfficialPortal: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = NexoraMagenta)
            .border(1.2.dp, NexoraMagenta, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = NexoraSurfaceDark),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Player Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NexoraCardElevated)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF36E58D))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AO VIVO OFICIAL: ${channel.name}",
                        color = NexoraTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onOpenOfficialPortal, modifier = Modifier.size(30.dp)) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Portal Oficial",
                            tint = NexoraLilacNeon,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onClose, modifier = Modifier.size(30.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar Player",
                            tint = NexoraTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Stream Frame / WebView
            if (channel.streamType == "EMBEDDED_STREAM") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Black)
                ) {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    mediaPlaybackRequiresUserGesture = false
                                    cacheMode = WebSettings.LOAD_DEFAULT
                                }
                                webChromeClient = WebChromeClient()
                                webViewClient = WebViewClient()
                                loadUrl(channel.officialStreamUrl)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                // Portal Requerido Frame
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NexoraSurface)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LiveTv,
                        contentDescription = null,
                        tint = NexoraLilacNeon,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Transmissão Oficial no Portal da Emissora",
                        color = NexoraTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Por conformidade jurídica com a emissora (${channel.name}), o sinal oficial está hospedado no portal institucional.",
                        color = NexoraTextSecondary,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ElevatedButton(
                        onClick = onOpenOfficialPortal,
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraLilacNeon,
                            contentColor = NexoraBackground
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Assistir no Portal Oficial", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Player Footer Details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NexoraSurfaceDark)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Região: ${channel.region}",
                    color = NexoraTextMuted,
                    fontSize = 11.sp
                )
                Text(
                    text = "Verificado: ${channel.lastVerifiedDate}",
                    color = NexoraTextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun TvChannelCard(
    channel: TvChannelEntity,
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onOpenCompliance: () -> Unit,
    onOpenWebPortal: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = if (isPlaying) 1.5.dp else 1.dp,
                brush = if (isPlaying) NexoraBrandGradient else Brush.linearGradient(listOf(NexoraSurfaceBorder, NexoraSurfaceBorder)),
                shape = RoundedCornerShape(18.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) NexoraCardElevated else NexoraSurfaceCard
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Channel Logo Badge & Info
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(NexoraWineDark, NexoraPurpleElectric)
                                )
                            )
                            .border(1.dp, NexoraLilacNeon.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = channel.logoSymbol,
                            color = NexoraTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = channel.name,
                                color = NexoraTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (channel.availabilityStatus == "DISPONIVEL_AO_VIVO") {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF36E58D))
                                )
                            }
                        }
                        Text(
                            text = "${channel.category} • ${channel.region}",
                            color = NexoraTextSecondary,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Favorite Icon
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (channel.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favoritar",
                        tint = if (channel.isFavorite) NexoraLilacNeon else NexoraTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = channel.description,
                color = NexoraTextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(
                        onClick = onOpenCompliance,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(14.dp), tint = NexoraTextMuted)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Direitos & Detalhes", color = NexoraTextMuted, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(onClick = onOpenWebPortal, modifier = Modifier.size(30.dp)) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Portal Oficial",
                            tint = NexoraTextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                ElevatedButton(
                    onClick = onPlay,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (isPlaying) NexoraMagenta else NexoraLilacNeon,
                        contentColor = NexoraBackground
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_play_${channel.id}")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Fullscreen else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPlaying) "No Player" else "Assistir",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
