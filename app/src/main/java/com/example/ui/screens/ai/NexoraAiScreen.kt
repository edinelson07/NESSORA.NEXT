package com.example.ui.screens.ai

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AIResponse
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraCyanBlue
import com.example.ui.theme.NexoraCyanGlow
import com.example.ui.theme.NexoraNeonGlow
import com.example.ui.theme.NexoraNeonGreen
import com.example.ui.theme.NexoraPurple
import com.example.ui.theme.NexoraPurpleGlow
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceBorderGlow
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceVariant
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary

@Composable
fun NexoraAiScreen(
    isLoading: Boolean,
    latestResponse: AIResponse?,
    history: List<AIResponse>,
    onGenerate: (taskType: String, theme: String, audience: String, tone: String) -> Unit,
    onSendToStudio: (AIResponse) -> Unit,
    onSaveAsDraft: (AIResponse) -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    var selectedTask by remember { mutableStateOf("IDEAS") }
    val selectedAudience = "Criadores de Conteúdo e Público Geral"
    val selectedTone = "Informativo, Cativante e Dinâmico"

    val taskChips = listOf(
        Pair("IDEAS", "💡 Ideias Virais"),
        Pair("TITLES", "🎯 Títulos de Alto CTR"),
        Pair("SCRIPT", "🎬 Roteiro Completo"),
        Pair("SHORTS", "⚡ Roteiro Shorts"),
        Pair("SEO", "🔍 Tags & SEO"),
        Pair("PINNED_COMMENT", "💬 Comentário Fixado"),
        Pair("SCRIPT_TO_SHORTS", "✂️ Derivar Shorts")
    )

    val samplePrompts = listOf(
        "Como crescer um canal do zero em 2026",
        "Setup barato de gravação no celular",
        "3 segredos de iluminação para iniciantes",
        "O maior erro que destrói a retenção",
        "Como monetizar o canal mais rápido"
    )

    fun copyText(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("NEXORA AI", text)
        clipboard.setPrimaryClip(clip)
        onShowToast("Conteúdo de IA copiado para a área de transferência!")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NexoraBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(NexoraNeonGlow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = NexoraNeonGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NEXORA AI",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = NexoraTextPrimary
                    )
                }
                Text(
                    text = "Assistente Inteligente & Gerador de Roteiros",
                    fontSize = 12.sp,
                    color = NexoraCyanBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Status banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(NexoraSurfaceCard)
                .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = NexoraCyanBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Google Gemini AI (Nuvem) com Motor Heurístico Offline Ativo",
                    fontSize = 11.sp,
                    color = NexoraTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Task selector chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(taskChips) { (key, label) ->
                val isSelected = selectedTask == key
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) NexoraNeonGreen else NexoraSurfaceCard)
                        .border(
                            1.dp,
                            if (isSelected) NexoraNeonGreen else NexoraSurfaceBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedTask = key }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("ai_chip_$key")
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

        Spacer(modifier = Modifier.height(10.dp))

        // Quick sample prompt suggestions
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(samplePrompts) { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NexoraSurfaceVariant)
                        .clickable {
                            inputText = prompt
                            onGenerate(selectedTask, prompt, selectedAudience, selectedTone)
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(text = "✨ $prompt", fontSize = 10.sp, color = NexoraCyanBlue)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Main input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Tema ou pergunta (ex: roteiro de 5 min)...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexoraNeonGreen,
                    unfocusedBorderColor = NexoraSurfaceBorder,
                    focusedTextColor = NexoraTextPrimary,
                    unfocusedTextColor = NexoraTextPrimary
                ),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_theme_input")
            )

            Spacer(modifier = Modifier.width(8.dp))

            ElevatedButton(
                onClick = {
                    val query = inputText.ifBlank { "Tendências do YouTube em 2026" }
                    onGenerate(selectedTask, query, selectedAudience, selectedTone)
                },
                enabled = !isLoading,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = NexoraNeonGreen,
                    contentColor = NexoraBackground
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = NexoraNeonGreen)
                    .testTag("ai_generate_btn")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = NexoraBackground,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Send, contentDescription = "Gerar", modifier = Modifier.size(18.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Conversation history list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (history.isEmpty() && latestResponse == null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(NexoraSurfaceCard)
                            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(18.dp))
                            .padding(26.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = NexoraCyanBlue,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Pronto para Criar com Inteligência",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = NexoraTextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Selecione um dos modos acima ou toque em um dos temas sugeridos para gerar roteiros com estruturas de retenção, títulos e SEO.",
                                fontSize = 12.sp,
                                color = NexoraTextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(history) { response ->
                    AiResponseCardElevated(
                        response = response,
                        onCopy = { copyText(response.content) },
                        onSendToStudio = { onSendToStudio(response) },
                        onSaveAsDraft = { onSaveAsDraft(response) },
                        onRegenerate = {
                            val query = inputText.ifBlank { response.title }
                            onGenerate(selectedTask, query, selectedAudience, selectedTone)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun AiResponseCardElevated(
    response: AIResponse,
    onCopy: () -> Unit,
    onSendToStudio: () -> Unit,
    onSaveAsDraft: () -> Unit,
    onRegenerate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = NexoraCyanBlue)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(NexoraCardElevated, NexoraSurfaceCard)
                )
            )
            .border(1.2.dp, NexoraSurfaceBorderGlow, RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("ai_response_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = response.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = NexoraNeonGreen,
                    modifier = Modifier.weight(1f)
                )

                // Provider badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (response.isOnlineAi) NexoraNeonGlow else NexoraCyanGlow)
                        .border(1.dp, if (response.isOnlineAi) NexoraNeonGreen else NexoraCyanBlue, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (response.isOnlineAi) "GEMINI PROVEDOR" else "MOTOR LOCAL",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (response.isOnlineAi) NexoraNeonGreen else NexoraCyanBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = response.content,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = NexoraTextPrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action row with 4 buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    IconButton(onClick = onCopy, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", tint = NexoraTextSecondary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onRegenerate, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Default.Refresh, contentDescription = "Regenerar", tint = NexoraCyanBlue, modifier = Modifier.size(18.dp))
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    ElevatedButton(
                        onClick = onSaveAsDraft,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraSurfaceCard,
                            contentColor = NexoraNeonGreen
                        ),
                        modifier = Modifier.border(1.dp, NexoraNeonGreen, RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Salvar Rascunho", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    ElevatedButton(
                        onClick = onSendToStudio,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraCyanBlue,
                            contentColor = NexoraBackground
                        )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ir para Estúdio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
