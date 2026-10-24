package com.example.ui.screens.youtube

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.ChannelAuthStatus
import com.example.data.model.ChannelInfo
import com.example.data.model.VideoItem
import com.example.data.repository.UploadValidationResult
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraCyanBlue
import com.example.ui.theme.NexoraCyanGlow
import com.example.ui.theme.NexoraError
import com.example.ui.theme.NexoraNeonGlow
import com.example.ui.theme.NexoraNeonGreen
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
fun YouTubeManagerScreen(
    channelInfo: ChannelInfo,
    videos: List<VideoItem>,
    onConnectChannel: (name: String, handle: String) -> Unit,
    onDisconnectChannel: () -> Unit,
    onSyncStats: () -> Unit,
    onValidateUpload: (title: String, desc: String, tags: String) -> UploadValidationResult,
    onShowToast: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var showConnectDialog by remember { mutableStateOf(false) }
    var channelNameInput by remember { mutableStateOf("") }
    var channelHandleInput by remember { mutableStateOf("") }

    var testTitle by remember { mutableStateOf("Como Aumentar Visualizações no YouTube em 2026") }
    var testDesc by remember { mutableStateOf("Neste vídeo completo revelamos as melhores técnicas de retenção e CTR para o seu canal crescer de forma orgânica.\n\n0:00 - Introdução\n2:10 - O Gancho Hipnótico\n5:30 - Exemplos Reais\n\n#YouTube #Criadores") }
    var testTags by remember { mutableStateOf("youtube, algoritmo, inscritos, visualizacoes, dicas criadores") }
    var validationResult by remember { mutableStateOf<UploadValidationResult?>(null) }

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        onShowToast("$label copiado!")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexoraBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = NexoraTextPrimary)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Gerenciador do YouTube",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = NexoraTextPrimary
                    )
                    Text(
                        text = "Google Cloud OAuth 2.0 & YouTube Data API v3",
                        fontSize = 11.sp,
                        color = NexoraCyanBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Connection Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(20.dp), spotColor = NexoraCyanBlue)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0D2545), NexoraSurfaceCard)
                        )
                    )
                    .border(1.2.dp, NexoraSurfaceBorderGlow, RoundedCornerShape(20.dp))
                    .padding(18.dp)
                    .testTag("youtube_channel_status_card")
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
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(NexoraCyanBlue.copy(alpha = 0.2f))
                                    .border(1.5.dp, NexoraCyanBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.SmartDisplay, contentDescription = null, tint = NexoraCyanBlue, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = channelInfo.title,
                                    fontSize = 16.sp,
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

                        // Auth badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) NexoraNeonGlow
                                    else NexoraCyanGlow
                                )
                                .border(
                                    1.dp,
                                    if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) NexoraNeonGreen else NexoraCyanBlue,
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = channelInfo.authStatus.label,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) NexoraNeonGreen else NexoraCyanBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Inscritos", fontSize = 11.sp, color = NexoraTextMuted)
                            Text("%,d".format(channelInfo.subscriberCount), fontSize = 16.sp, fontWeight = FontWeight.Black, color = NexoraNeonGreen)
                        }
                        Column {
                            Text("Visualizações", fontSize = 11.sp, color = NexoraTextMuted)
                            Text("%,d".format(channelInfo.viewCount), fontSize = 16.sp, fontWeight = FontWeight.Black, color = NexoraCyanBlue)
                        }
                        Column {
                            Text("Vídeos", fontSize = 11.sp, color = NexoraTextMuted)
                            Text("${channelInfo.videoCount}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ElevatedButton(
                            onClick = onSyncStats,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = NexoraSurfaceDark,
                                contentColor = NexoraCyanBlue
                            ),
                            modifier = Modifier.border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sincronizar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) {
                            OutlinedButton(
                                onClick = onDisconnectChannel,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraError),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Desconectar Canal", fontSize = 11.sp)
                            }
                        } else {
                            ElevatedButton(
                                onClick = { showConnectDialog = true },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = NexoraNeonGreen,
                                    contentColor = NexoraBackground
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("youtube_connect_channel_btn")
                            ) {
                                Icon(Icons.Default.Link, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Conectar Canal OAuth", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Section: Validador de Metadados de Upload
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.2.dp, NexoraSurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Upload, contentDescription = null, tint = NexoraCyanBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Validador Oficial de Upload",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexoraTextPrimary
                        )
                    }
                    Text(
                        text = "Valida os limites rigorosos de caracteres e cotas exigidos pelo YouTube.",
                        fontSize = 11.sp,
                        color = NexoraTextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = testTitle,
                        onValueChange = { testTitle = it },
                        label = { Text("Título para Envio (${testTitle.length}/100)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexoraCyanBlue,
                            unfocusedBorderColor = NexoraSurfaceBorder,
                            focusedTextColor = NexoraTextPrimary,
                            unfocusedTextColor = NexoraTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = testDesc,
                        onValueChange = { testDesc = it },
                        label = { Text("Descrição para Envio (${testDesc.length}/5000)") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexoraCyanBlue,
                            unfocusedBorderColor = NexoraSurfaceBorder,
                            focusedTextColor = NexoraTextPrimary,
                            unfocusedTextColor = NexoraTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = testTags,
                        onValueChange = { testTags = it },
                        label = { Text("Tags") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexoraCyanBlue,
                            unfocusedBorderColor = NexoraSurfaceBorder,
                            focusedTextColor = NexoraTextPrimary,
                            unfocusedTextColor = NexoraTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ElevatedButton(
                        onClick = {
                            validationResult = onValidateUpload(testTitle, testDesc, testTags)
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraCyanBlue,
                            contentColor = NexoraBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Executar Teste de Pré-Validação", fontWeight = FontWeight.Bold)
                    }

                    validationResult?.let { res ->
                        Spacer(modifier = Modifier.height(10.dp))
                        if (res.isValid) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NexoraNeonGreen.copy(alpha = 0.15f))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NexoraNeonGreen, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Aprovado pelo validador do YouTube (${res.tagCount} tags válidas)!",
                                            color = NexoraNeonGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    ElevatedButton(
                                        onClick = {
                                            copyToClipboard("Metadados Validados", "TÍTULO:\n$testTitle\n\nDESCRIÇÃO:\n$testDesc\n\nTAGS:\n$testTags")
                                        },
                                        colors = ButtonDefaults.elevatedButtonColors(
                                            containerColor = NexoraNeonGreen,
                                            contentColor = NexoraBackground
                                        ),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text("Copiar Pacote Validado", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                res.errors.forEach { err ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(NexoraError.copy(alpha = 0.15f))
                                            .padding(8.dp)
                                    ) {
                                        Text(text = "• $err", color = NexoraError, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Vídeos Sincronizados
        item {
            Text(
                text = "Vídeos Sincronizados do Canal (${videos.size})",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NexoraTextPrimary
            )
        }

        items(videos) { video ->
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
                        Text(
                            text = "${"%,d".format(video.viewCount)} views • ${video.publishedAt}",
                            fontSize = 11.sp,
                            color = NexoraTextMuted
                        )

                        Row(
                            modifier = Modifier.clickable {
                                val url = "https://www.youtube.com/results?search_query=${Uri.encode(video.title)}"
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = NexoraCyanBlue, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Abrir no YouTube", fontSize = 11.sp, color = NexoraCyanBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    if (showConnectDialog) {
        AlertDialog(
            onDismissRequest = { showConnectDialog = false },
            title = {
                Text("Conectar Canal YouTube Oficial", fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
            },
            text = {
                Column {
                    Text(
                        text = "Informe o nome e handle do seu canal para autorizar a sincronização via Google OAuth 2.0:",
                        color = NexoraTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = channelNameInput,
                        onValueChange = { channelNameInput = it },
                        label = { Text("Nome do Canal") },
                        placeholder = { Text("Ex: Meu Canal Oficial") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexoraNeonGreen,
                            unfocusedBorderColor = NexoraSurfaceBorder,
                            focusedTextColor = NexoraTextPrimary,
                            unfocusedTextColor = NexoraTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = channelHandleInput,
                        onValueChange = { channelHandleInput = it },
                        label = { Text("Handle (@usuario)") },
                        placeholder = { Text("@meucanal") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexoraNeonGreen,
                            unfocusedBorderColor = NexoraSurfaceBorder,
                            focusedTextColor = NexoraTextPrimary,
                            unfocusedTextColor = NexoraTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        onConnectChannel(channelNameInput, channelHandleInput)
                        showConnectDialog = false
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Autorizar Sessão", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConnectDialog = false }) {
                    Text("Cancelar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }
}
