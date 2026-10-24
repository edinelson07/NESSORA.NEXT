package com.example.ui.screens.studio

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DraftEntity
import com.example.data.model.DraftType
import com.example.data.model.NarrativeSection
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraCyanBlue
import com.example.ui.theme.NexoraCyanGlow
import com.example.ui.theme.NexoraError
import com.example.ui.theme.NexoraNeonGlow
import com.example.ui.theme.NexoraNeonGreen
import com.example.ui.theme.NexoraPurple
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
fun StudioScreen(
    currentDraft: DraftEntity?,
    onUpdateDraft: ((DraftEntity) -> DraftEntity) -> Unit,
    onSaveDraft: () -> Unit,
    onOpenAiWithTheme: (theme: String) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var showThumbnailDialog by remember { mutableStateOf(false) }
    var showPublishConfirmDialog by remember { mutableStateOf(false) }
    var newTagInput by remember { mutableStateOf("") }

    val draft = currentDraft ?: DraftEntity(
        id = 0,
        title = "",
        type = "LONG_VIDEO",
        topic = "",
        status = "RASCUNHO"
    )

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        onShowToast("$label copiado com sucesso!")
    }

    fun shareText(text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Compartilhar Rascunho NEXORA"))
    }

    val toneOptions = listOf("Informativo e Dinâmico", "Ágil & Hipnótico", "Cinematográfico", "Didático / Passo a Passo", "Curiosidade & Mistério")
    val durationOptions = if (draft.type == "SHORTS") listOf("15s", "30s", "45s", "60s") else listOf("5 min", "8-10 min", "12-15 min", "20+ min")

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
                Column {
                    Text(
                        text = "Estúdio de Criação",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = NexoraTextPrimary
                    )
                    Text(
                        text = if (draft.id == 0L) "Novo Rascunho em Edição" else "Rascunho #${draft.id} • ${draft.type}",
                        fontSize = 12.sp,
                        color = NexoraCyanBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                ElevatedButton(
                    onClick = onSaveDraft,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(10.dp), spotColor = NexoraNeonGreen)
                        .testTag("studio_save_draft_btn")
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Salvar", fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }
        }

        // Stepper Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NexoraSurfaceCard,
                contentColor = NexoraNeonGreen,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.2.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("1. Tema", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("2. Roteiro", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("3. SEO", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("4. Publicar", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        // ================= TAB 0: TEMA E FORMATO =================
        if (selectedTab == 0) {
            item {
                Text(
                    text = "Escolha o Formato do Conteúdo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DraftType.entries.forEach { type ->
                        val isSelected = draft.type == type.name
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) NexoraNeonGlow else NexoraSurfaceCard)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) NexoraNeonGreen else NexoraSurfaceBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    onUpdateDraft { it.copy(type = type.name) }
                                }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) NexoraNeonGreen else NexoraTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = draft.topic,
                    onValueChange = { newTopic -> onUpdateDraft { it.copy(topic = newTopic) } },
                    label = { Text("Tema ou Assunto Central do Conteúdo") },
                    placeholder = { Text("Ex: Como gravar vídeos profissionais com celular") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexoraNeonGreen,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("studio_topic_input")
                )
            }

            item {
                Text(
                    text = "Tom de Voz Recomendado",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(toneOptions) { tone ->
                        val isSelected = draft.tone == tone
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) NexoraCyanGlow else NexoraSurfaceCard)
                                .border(1.dp, if (isSelected) NexoraCyanBlue else NexoraSurfaceBorder, RoundedCornerShape(10.dp))
                                .clickable { onUpdateDraft { it.copy(tone = tone) } }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = tone, fontSize = 11.sp, color = if (isSelected) NexoraCyanBlue else NexoraTextMuted, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Duração Alvo do Vídeo",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    durationOptions.forEach { dur ->
                        val isSelected = draft.targetDuration == dur
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) NexoraPurpleGlow else NexoraSurfaceCard)
                                .border(1.dp, if (isSelected) NexoraPurple else NexoraSurfaceBorder, RoundedCornerShape(10.dp))
                                .clickable { onUpdateDraft { it.copy(targetDuration = dur) } }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = dur, fontSize = 11.sp, color = if (isSelected) NexoraPurple else NexoraTextMuted, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = draft.targetAudience,
                    onValueChange = { newAudience -> onUpdateDraft { it.copy(targetAudience = newAudience) } },
                    label = { Text("Público-Alvo e Nível de Conhecimento") },
                    placeholder = { Text("Ex: Iniciantes em criação de conteúdo, jovens de 18-34 anos") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexoraCyanBlue,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                // Assistente Rápido
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(NexoraSurfaceCard)
                        .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NexoraCyanBlue, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Precisa de Ideias para este Tema?",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NexoraTextPrimary
                                )
                                Text(
                                    text = "Gere ângulos originais usando o motor de IA",
                                    fontSize = 11.sp,
                                    color = NexoraTextMuted
                                )
                            }
                        }
                        ElevatedButton(
                            onClick = { onOpenAiWithTheme(draft.topic) },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = NexoraCyanBlue,
                                contentColor = NexoraBackground
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Abrir IA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    ElevatedButton(
                        onClick = { selectedTab = 1 },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraNeonGreen,
                            contentColor = NexoraBackground
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Avançar para Roteiro", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ================= TAB 1: ROTEIRO & NARRATIVA =================
        if (selectedTab == 1) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gancho Inicial (0s - 15s)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraNeonGreen
                    )
                    TextButton(onClick = {
                        onUpdateDraft {
                            it.copy(hook = "Se você ainda faz [AÇÃO COMUM], está perdendo [RESULTADO]. Nos próximos minutos você vai descobrir como mudar isso definitivamente.")
                        }
                    }) {
                        Text("Usar Modelo", fontSize = 11.sp, color = NexoraCyanBlue, fontWeight = FontWeight.Bold)
                    }
                }
                OutlinedTextField(
                    value = draft.hook,
                    onValueChange = { newHook -> onUpdateDraft { it.copy(hook = newHook) } },
                    placeholder = { Text("Digite o gancho que prenderá a atenção imediata nos primeiros 15 segundos...") },
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexoraNeonGreen,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("studio_hook_input")
                )
            }

            item {
                // Quick narrative block buttons
                Text(
                    text = "Adicionar Bloco de Retenção Narrativa ao Roteiro:",
                    fontSize = 12.sp,
                    color = NexoraTextSecondary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val sections = NarrativeSection.getDefaultStructure()
                    items(sections) { sec ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexoraSurfaceCard)
                                .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable {
                                    val addition = "\n\n--- ${sec.title.uppercase()} ---\n${sec.defaultText}\n[Dica: ${sec.retentionTip}]"
                                    onUpdateDraft {
                                        it.copy(scriptBody = if (it.scriptBody.isBlank()) sec.defaultText else it.scriptBody + addition)
                                    }
                                    onShowToast("Bloco inserido!")
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "+ ${sec.title.substringBefore(" (")}", fontSize = 11.sp, color = NexoraCyanBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Roteiro e Divisão em Cenas (${draft.scriptBody.split("\\s+".toRegex()).filter { it.isNotEmpty() }.size} palavras)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraTextPrimary
                    )
                    Row {
                        IconButton(onClick = { copyToClipboard("Roteiro", draft.scriptBody) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", tint = NexoraTextMuted, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { shareText(draft.scriptBody) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Share, contentDescription = "Compartilhar", tint = NexoraTextMuted, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { onUpdateDraft { it.copy(scriptBody = "") } }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpar", tint = NexoraTextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
                OutlinedTextField(
                    value = draft.scriptBody,
                    onValueChange = { newBody -> onUpdateDraft { it.copy(scriptBody = newBody) } },
                    placeholder = { Text("Escreva ou cole o roteiro completo aqui...") },
                    minLines = 8,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexoraCyanBlue,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("studio_script_input")
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = { selectedTab = 0 }) {
                        Text("Voltar: Tema")
                    }
                    ElevatedButton(
                        onClick = { selectedTab = 2 },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraNeonGreen,
                            contentColor = NexoraBackground
                        )
                    ) {
                        Text("Avançar para SEO & Tags", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ================= TAB 2: SEO, TÍTULO & TAGS =================
        if (selectedTab == 2) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Título Oficial para o YouTube",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraTextPrimary
                    )
                    Text(
                        text = "${draft.title.length}/100",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (draft.title.length > 100) NexoraError else if (draft.title.length > 70) NexoraNeonGreen else NexoraTextMuted
                    )
                }

                LinearProgressIndicator(
                    progress = { (draft.title.length / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = if (draft.title.length > 100) NexoraError else NexoraNeonGreen,
                    trackColor = NexoraSurfaceCard
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = draft.title,
                    onValueChange = { newTitle -> onUpdateDraft { it.copy(title = newTitle) } },
                    placeholder = { Text("Ex: Como Dominar o YouTube em 2026") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexoraNeonGreen,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("studio_title_input")
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Descrição do Vídeo (Capítulos & Links)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraTextPrimary
                    )
                    Text(
                        text = "${draft.description.length}/5000",
                        fontSize = 11.sp,
                        color = if (draft.description.length > 5000) NexoraError else NexoraTextMuted
                    )
                }
                OutlinedTextField(
                    value = draft.description,
                    onValueChange = { newDesc -> onUpdateDraft { it.copy(description = newDesc) } },
                    placeholder = { Text("0:00 - Introdução\n1:30 - Passo a Passo Prático\n\n#YouTube #Criadores") },
                    minLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexoraCyanBlue,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Text(
                    text = "Tags e Palavras-Chave (Separadas por vírgula)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Tag Chips
                val tagsList = draft.tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                if (tagsList.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(tagsList) { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NexoraCyanBlue.copy(alpha = 0.15f))
                                    .border(1.dp, NexoraCyanBlue.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .clickable {
                                        val updated = tagsList.filter { it != tag }.joinToString(", ")
                                        onUpdateDraft { it.copy(tags = updated) }
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = tag, fontSize = 11.sp, color = NexoraCyanBlue, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Clear, contentDescription = "Remover", tint = NexoraCyanBlue, modifier = Modifier.size(12.dp))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTagInput,
                        onValueChange = { newTagInput = it },
                        placeholder = { Text("Nova tag...") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexoraCyanBlue,
                            unfocusedBorderColor = NexoraSurfaceBorder,
                            focusedTextColor = NexoraTextPrimary,
                            unfocusedTextColor = NexoraTextPrimary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ElevatedButton(
                        onClick = {
                            if (newTagInput.isNotBlank()) {
                                val current = draft.tags
                                val updated = if (current.isBlank()) newTagInput.trim() else "$current, ${newTagInput.trim()}"
                                onUpdateDraft { it.copy(tags = updated) }
                                newTagInput = ""
                            }
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraCyanBlue,
                            contentColor = NexoraBackground
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Adicionar", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = { selectedTab = 1 }) {
                        Text("Voltar: Roteiro")
                    }
                    ElevatedButton(
                        onClick = { selectedTab = 3 },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraNeonGreen,
                            contentColor = NexoraBackground
                        )
                    ) {
                        Text("Avançar: Publicar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // ================= TAB 3: MINIATURA & PUBLICAÇÃO =================
        if (selectedTab == 3) {
            item {
                // Miniatura Preview Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(NexoraSurfaceCard)
                        .border(1.2.dp, NexoraPurple, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = NexoraPurple)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Miniatura (Thumbnail)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NexoraTextPrimary
                                )
                            }
                            ElevatedButton(
                                onClick = { showThumbnailDialog = true },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = NexoraPurple,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("studio_edit_thumbnail_btn")
                            ) {
                                Text("Abrir Estúdio Visual", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Preview Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF07111F))
                                .border(1.dp, NexoraNeonGreen, RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NexoraNeonGreen)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("DESTAQUE", color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Black)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (draft.thumbnailTitle.isNotBlank()) draft.thumbnailTitle.uppercase() else "TÍTULO DA MINIATURA",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = if (draft.thumbnailSubtitle.isNotBlank()) draft.thumbnailSubtitle.uppercase() else "SUBTÍTULO DE ALTO CONTRASTE",
                                    color = NexoraCyanBlue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Checklist Oficial de Validação do YouTube",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    StudioChecklistItem(
                        label = "Título adequado (${draft.title.length}/100 caracteres)",
                        isValid = draft.title.isNotBlank() && draft.title.length <= 100
                    )
                    StudioChecklistItem(
                        label = "Descrição dentro do limite (${draft.description.length}/5000)",
                        isValid = draft.description.length <= 5000
                    )
                    StudioChecklistItem(
                        label = "Tags semânticas configuradas",
                        isValid = draft.tags.isNotBlank() && draft.tags.length <= 500
                    )
                    StudioChecklistItem(
                        label = "Gancho de retenção inicial preenchido",
                        isValid = draft.hook.isNotBlank()
                    )
                }
            }

            item {
                ElevatedButton(
                    onClick = { showPublishConfirmDialog = true },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraCyanBlue,
                        contentColor = NexoraBackground
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = NexoraCyanBlue)
                        .testTag("studio_prepare_upload_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.SmartDisplay, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Preparar Envio ao Canal do YouTube", fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val fullPackage = buildString {
                                append("=== PACOTE NEXORA PARA YOUTUBE ===\n\n")
                                append("TÍTULO:\n${draft.title}\n\n")
                                append("GANCHO (0-15s):\n${draft.hook}\n\n")
                                append("DESCRIÇÃO:\n${draft.description}\n\n")
                                append("TAGS:\n${draft.tags}\n\n")
                                append("ROTEIRO COMPLETO:\n${draft.scriptBody}")
                            }
                            copyToClipboard("Pacote de Publicação", fullPackage)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraNeonGreen)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copiar Pacote", fontSize = 11.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            shareText("PROJETO NEXORA:\n${draft.title}\n\n${draft.scriptBody}")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraCyanBlue)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Compartilhar", fontSize = 11.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    // THUMBNAIL STUDIO MODAL
    if (showThumbnailDialog) {
        ThumbnailStudioDialog(
            initialTitle = draft.thumbnailTitle,
            initialSubtitle = draft.thumbnailSubtitle,
            initialBgHex = draft.thumbnailBgHex,
            initialAccentHex = draft.thumbnailAccentHex,
            isShorts = draft.type == "SHORTS",
            onDismiss = { showThumbnailDialog = false },
            onSave = { newTitle, newSubtitle, newBg, newAccent ->
                onUpdateDraft {
                    it.copy(
                        thumbnailTitle = newTitle,
                        thumbnailSubtitle = newSubtitle,
                        thumbnailBgHex = newBg,
                        thumbnailAccentHex = newAccent
                    )
                }
                onShowToast("Miniatura salva no rascunho!")
            }
        )
    }

    // CONFIRM PUBLISH DIALOG
    if (showPublishConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showPublishConfirmDialog = false },
            title = {
                Text(
                    text = "Confirmar Metadados para o YouTube",
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Os metadados (Título, Descrição e Tags) estão validados e prontos para o envio oficial ao YouTube Data API v3 com sua conta autorizada.",
                        color = NexoraTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Lembre-se: O NEXORA não hospeda seus vídeos em servidores privados, respeitando sua privacidade e diretrizes do Google.",
                        color = NexoraTextMuted,
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        showPublishConfirmDialog = false
                        onUpdateDraft { it.copy(status = "PRONTO") }
                        onSaveDraft()
                        onShowToast("Status atualizado para PRONTO PARA PUBLICAÇÃO!")
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Confirmar e Salvar", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPublishConfirmDialog = false }) {
                    Text("Cancelar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }
}

@Composable
private fun StudioChecklistItem(label: String, isValid: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NexoraSurfaceCard)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = if (isValid) Icons.Default.Check else Icons.Default.Warning,
            contentDescription = null,
            tint = if (isValid) NexoraNeonGreen else NexoraWarning,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isValid) NexoraTextPrimary else NexoraTextMuted,
            fontWeight = if (isValid) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
