package com.example.ui.screens.library

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DraftEntity
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraCyanBlue
import com.example.ui.theme.NexoraError
import com.example.ui.theme.NexoraNeonGreen
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceVariant
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary

@Composable
fun LibraryScreen(
    drafts: List<DraftEntity>,
    onOpenDraft: (DraftEntity) -> Unit,
    onNewDraft: () -> Unit,
    onDuplicateDraft: (DraftEntity) -> Unit,
    onDeleteDraft: (DraftEntity) -> Unit,
    onExportBackup: ((String) -> Unit) -> Unit,
    onImportBackup: (String) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("TODOS") }
    var draftToDelete by remember { mutableStateOf<DraftEntity?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }
    var exportedJsonContent by remember { mutableStateOf("") }
    var showImportDialog by remember { mutableStateOf(false) }
    var importJsonText by remember { mutableStateOf("") }

    val filterOptions = listOf(
        Pair("TODOS", "Todos (${drafts.size})"),
        Pair("LONG_VIDEO", "Vídeos Longos"),
        Pair("SHORTS", "Shorts"),
        Pair("IDEA", "Ideias"),
        Pair("SCRIPT", "Roteiros")
    )

    val filteredDrafts = drafts.filter { draft ->
        val matchesSearch = draft.title.contains(searchQuery, ignoreCase = true) ||
                draft.topic.contains(searchQuery, ignoreCase = true) ||
                draft.tags.contains(searchQuery, ignoreCase = true)
        val matchesType = selectedFilter == "TODOS" || draft.type == selectedFilter
        matchesSearch && matchesType
    }

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
        context.startActivity(Intent.createChooser(sendIntent, "Exportar Rascunho"))
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
                    Icon(Icons.Default.Folder, contentDescription = null, tint = NexoraNeonGreen, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Biblioteca de Rascunhos",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = NexoraTextPrimary
                        )
                        Text(
                            text = "${drafts.size} projetos salvos permanentemente no Room",
                            fontSize = 12.sp,
                            color = NexoraCyanBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                ElevatedButton(
                    onClick = onNewDraft,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .shadow(6.dp, RoundedCornerShape(10.dp), spotColor = NexoraNeonGreen)
                        .testTag("library_new_draft_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Novo", fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Pesquisar rascunhos por título, tema ou tag...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = NexoraTextMuted)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexoraNeonGreen,
                    unfocusedBorderColor = NexoraSurfaceBorder,
                    focusedTextColor = NexoraTextPrimary,
                    unfocusedTextColor = NexoraTextPrimary
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("library_search_input")
            )
        }

        // Filter chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filterOptions) { (key, label) ->
                    val isSelected = selectedFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) NexoraNeonGreen else NexoraSurfaceCard)
                            .border(
                                1.dp,
                                if (isSelected) NexoraNeonGreen else NexoraSurfaceBorder,
                                RoundedCornerShape(14.dp)
                            )
                            .clickable { selectedFilter = key }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("filter_$key")
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                            color = if (isSelected) NexoraBackground else NexoraTextSecondary
                        )
                    }
                }
            }
        }

        // Backup & Portabilidade Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.2.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Backup & Portabilidade (JSON)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexoraTextPrimary
                        )
                        Text(
                            text = "Exporte ou restaure todos os seus projetos entre dispositivos.",
                            fontSize = 11.sp,
                            color = NexoraTextMuted
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(
                            onClick = {
                                onExportBackup { json ->
                                    exportedJsonContent = json
                                    showExportDialog = true
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraCyanBlue),
                            modifier = Modifier.testTag("library_export_backup_btn")
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Exportar", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { showImportDialog = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraNeonGreen),
                            modifier = Modifier.testTag("library_import_backup_btn")
                        ) {
                            Icon(Icons.Default.FileUpload, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Restaurar", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // List of Drafts
        if (filteredDrafts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NexoraSurfaceCard)
                        .padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Nenhum rascunho encontrado com os filtros atuais.",
                            color = NexoraTextMuted,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ElevatedButton(
                            onClick = onNewDraft,
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = NexoraNeonGreen,
                                contentColor = NexoraBackground
                            )
                        ) {
                            Text("Criar Novo Rascunho", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(filteredDrafts) { draft ->
                LibraryDraftCardElevated(
                    draft = draft,
                    onOpen = { onOpenDraft(draft) },
                    onDuplicate = { onDuplicateDraft(draft) },
                    onDelete = { draftToDelete = draft },
                    onShare = { shareText("PROJETO: ${draft.title}\n\nGANCHO:\n${draft.hook}\n\nROTEIRO:\n${draft.scriptBody}\n\nTAGS:\n${draft.tags}") },
                    onCopy = { copyToClipboard("Roteiro", draft.scriptBody) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    // EXPORT JSON DIALOG
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = {
                Text("Backup JSON Gerado com Sucesso", fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
            },
            text = {
                Column {
                    Text(
                        text = "Seus ${drafts.size} rascunhos foram compilados em formato JSON estruturado:",
                        color = NexoraTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NexoraSurfaceVariant)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = exportedJsonContent.take(600) + if (exportedJsonContent.length > 600) "\n..." else "",
                            fontSize = 10.sp,
                            color = NexoraTextSecondary
                        )
                    }
                }
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        copyToClipboard("Backup JSON", exportedJsonContent)
                        showExportDialog = false
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Copiar Código JSON", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Fechar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }

    // CONFIRM DELETE DIALOG
    draftToDelete?.let { draft ->
        AlertDialog(
            onDismissRequest = { draftToDelete = null },
            title = {
                Text("Excluir Rascunho Permanentemente", fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
            },
            text = {
                Text(
                    text = "Tem certeza que deseja excluir o rascunho \"${draft.title}\"? Esta ação é definitiva.",
                    color = NexoraTextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        onDeleteDraft(draft)
                        draftToDelete = null
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraError,
                        contentColor = Color.White
                    )
                ) {
                    Text("Excluir", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { draftToDelete = null }) {
                    Text("Cancelar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }

    // IMPORT JSON DIALOG
    if (showImportDialog) {
        AlertDialog(
            onDismissRequest = { showImportDialog = false },
            title = {
                Text("Restaurar Rascunhos do Backup", fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
            },
            text = {
                Column {
                    Text(
                        text = "Cole o código JSON do backup ou carregue um exemplo de demonstração:",
                        color = NexoraTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = importJsonText,
                        onValueChange = { importJsonText = it },
                        placeholder = { Text("Cole o JSON aqui...") },
                        minLines = 4,
                        maxLines = 6,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexoraNeonGreen,
                            unfocusedBorderColor = NexoraSurfaceBorder,
                            focusedTextColor = NexoraTextPrimary,
                            unfocusedTextColor = NexoraTextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("import_json_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = {
                            importJsonText = """
                            {
                              "version": 1,
                              "appName": "NEXORA",
                              "drafts": [
                                {
                                  "title": "Restaurado: Como Dominar SEO no YouTube",
                                  "type": "LONG_VIDEO",
                                  "topic": "SEO e Pesquisa",
                                  "hook": "Aprenda a ranquear no topo em 2026.",
                                  "scriptBody": "Passo 1: Intencao de busca.\nPasso 2: Miniatura de alto contraste.",
                                  "description": "Tutorial completo de SEO.",
                                  "tags": "seo, youtube, tutorial",
                                  "status": "RASCUNHO"
                                }
                              ]
                            }
                            """.trimIndent()
                        }
                    ) {
                        Text("Carregar Exemplo de Demonstração", fontSize = 11.sp, color = NexoraCyanBlue)
                    }
                }
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        onImportBackup(importJsonText)
                        showImportDialog = false
                        importJsonText = ""
                    },
                    enabled = importJsonText.isNotBlank(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Restaurar Agora", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showImportDialog = false }) {
                    Text("Cancelar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }
}

@Composable
private fun LibraryDraftCardElevated(
    draft: DraftEntity,
    onOpen: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit,
    onCopy: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp), spotColor = NexoraCyanBlue)
            .clip(RoundedCornerShape(16.dp))
            .background(NexoraCardElevated)
            .border(1.2.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("library_draft_${draft.id}")
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
                                if (draft.type == "SHORTS") Color(0xFF16375E)
                                else Color(0xFF153F2E)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (draft.type == "SHORTS") "SHORTS" else "VÍDEO LONGO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (draft.type == "SHORTS") NexoraCyanBlue else NexoraNeonGreen
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Status: ${draft.status}",
                        fontSize = 11.sp,
                        color = NexoraTextMuted
                    )
                }

                Row {
                    IconButton(onClick = onCopy, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copiar Roteiro", tint = NexoraTextMuted, modifier = Modifier.size(15.dp))
                    }
                    IconButton(onClick = onShare, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Share, contentDescription = "Compartilhar", tint = NexoraTextMuted, modifier = Modifier.size(15.dp))
                    }
                    IconButton(onClick = onDuplicate, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Add, contentDescription = "Duplicar", tint = NexoraTextMuted, modifier = Modifier.size(15.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = NexoraError.copy(alpha = 0.8f), modifier = Modifier.size(15.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = draft.title.ifBlank { "Rascunho sem título" },
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = NexoraTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (draft.topic.isNotBlank()) {
                Text(
                    text = "Tema: ${draft.topic}",
                    fontSize = 11.sp,
                    color = NexoraTextSecondary,
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
                    text = "Duração alvo: ${draft.targetDuration}",
                    fontSize = 11.sp,
                    color = NexoraTextMuted
                )

                ElevatedButton(
                    onClick = onOpen,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraCyanBlue,
                        contentColor = NexoraBackground
                    ),
                    modifier = Modifier.testTag("open_draft_${draft.id}")
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar no Estúdio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
