package com.example.ui.screens.audio

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.AudioEntity
import com.example.data.model.AudioLicense
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraCyanBlue
import com.example.ui.theme.NexoraCyanGlow
import com.example.ui.theme.NexoraNeonGreen
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary
import com.example.ui.theme.NexoraWarning

@Composable
fun AudioCatalogScreen(
    tracks: List<AudioEntity>,
    onAddTrack: (title: String, artist: String, license: String, category: String, notes: String) -> Unit,
    onShowToast: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("TODOS") }
    var showAddDialog by remember { mutableStateOf(false) }

    var titleInput by remember { mutableStateOf("") }
    var artistInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("Tecnologia") }
    var licenseInput by remember { mutableStateOf(AudioLicense.ROYALTY_FREE.name) }
    var notesInput by remember { mutableStateOf("") }

    val filterOptions = listOf(
        Pair("TODOS", "Todas as Faixas"),
        Pair(AudioLicense.YOUTUBE_AUDIO_LIBRARY.name, "YouTube Audio Library"),
        Pair(AudioLicense.ROYALTY_FREE.name, "Royalty-Free"),
        Pair(AudioLicense.CREATIVE_COMMONS_BY.name, "Creative Commons (CC-BY)"),
        Pair(AudioLicense.REQUIRES_LICENSE.name, "Requer Licença Comercial")
    )

    val filteredTracks = tracks.filter { track ->
        val matchesSearch = track.title.contains(searchQuery, ignoreCase = true) ||
                track.artist.contains(searchQuery, ignoreCase = true) ||
                track.category.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == "TODOS" || track.licenseType == selectedFilter
        matchesSearch && matchesFilter
    }

    fun copyAttribution(track: AudioEntity) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val text = "Música: ${track.title} por ${track.artist} (${track.licenseType}). Notas: ${track.notes}"
        clipboard.setPrimaryClip(ClipData.newPlainText("Crédito de Áudio", text))
        onShowToast("Crédito do áudio copiado para a descrição do seu vídeo!")
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = NexoraTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Catálogo de Áudio & Licenças",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = NexoraTextPrimary
                        )
                        Text(
                            text = "Proteção contra reivindicações de direitos autorais",
                            fontSize = 11.sp,
                            color = NexoraCyanBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                ElevatedButton(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .shadow(6.dp, RoundedCornerShape(10.dp), spotColor = NexoraNeonGreen)
                        .testTag("audio_add_track_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Adicionar", fontWeight = FontWeight.Black, fontSize = 11.sp)
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar faixa, artista ou categoria...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = NexoraTextMuted)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexoraCyanBlue,
                    unfocusedBorderColor = NexoraSurfaceBorder,
                    focusedTextColor = NexoraTextPrimary,
                    unfocusedTextColor = NexoraTextPrimary
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // License Filter Row
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filterOptions) { (key, label) ->
                    val isSelected = selectedFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NexoraCyanGlow else NexoraSurfaceCard)
                            .border(1.dp, if (isSelected) NexoraCyanBlue else NexoraSurfaceBorder, RoundedCornerShape(12.dp))
                            .clickable { selectedFilter = key }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) NexoraCyanBlue else NexoraTextSecondary
                        )
                    }
                }
            }
        }

        // Compliance Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.2.dp, NexoraSurfaceBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = NexoraNeonGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Diretrizes de Monetização: O YouTube penaliza vídeos com reivindicações de Content ID. Copie e cole os créditos das faixas Creative Commons na descrição para garantir monetização integral.",
                        fontSize = 11.sp,
                        color = NexoraTextMuted,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        items(filteredTracks) { track ->
            val isSafe = track.licenseType != AudioLicense.REQUIRES_LICENSE.name
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = NexoraCyanBlue)
                    .clip(RoundedCornerShape(16.dp))
                    .background(NexoraCardElevated)
                    .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(NexoraCyanBlue.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.GraphicEq, contentDescription = null, tint = NexoraCyanBlue, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = track.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NexoraTextPrimary
                                )
                                Text(
                                    text = "${track.artist} • ${track.category}",
                                    fontSize = 11.sp,
                                    color = NexoraTextMuted
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSafe) NexoraNeonGreen.copy(alpha = 0.15f) else NexoraWarning.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (isSafe) "Livre / Seguro" else "Verificar Licença",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSafe) NexoraNeonGreen else NexoraWarning
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Licença: ${track.licenseType}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NexoraCyanBlue
                    )

                    if (track.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Instruções: ${track.notes}",
                            fontSize = 11.sp,
                            color = NexoraTextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = { copyAttribution(track) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraNeonGreen)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copiar Atribuição", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text("Cadastrar Nova Referência de Áudio", fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Título da Música") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = artistInput,
                        onValueChange = { artistInput = it },
                        label = { Text("Autor / Artista") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = categoryInput,
                        onValueChange = { categoryInput = it },
                        label = { Text("Estilo / Categoria") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Instruções de Atribuição") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        if (titleInput.isNotBlank()) {
                            onAddTrack(titleInput, artistInput, licenseInput, categoryInput, notesInput)
                            showAddDialog = false
                            titleInput = ""
                            artistInput = ""
                            notesInput = ""
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Salvar Faixa", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }
}
