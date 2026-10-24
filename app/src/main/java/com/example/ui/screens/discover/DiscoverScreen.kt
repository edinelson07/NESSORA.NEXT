package com.example.ui.screens.discover

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCyanBlue
import com.example.ui.theme.NexoraNeonGreen
import com.example.ui.theme.NexoraPurple
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceVariant
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary

data class TrendTopic(
    val title: String,
    val searchIntent: String,
    val competitionLevel: String,
    val sampleTitle: String,
    val tags: List<String>
)

@Composable
fun DiscoverScreen(
    onSelectTopicForStudio: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val trends = remember {
        listOf(
            TrendTopic(
                title = "Inteligência Artificial para Criadores em 2026",
                searchIntent = "Como automatizar tarefas de roteiro e miniatura sem perder qualidade",
                competitionLevel = "Média / Alta Demanda",
                sampleTitle = "Como Criadores Estão Usando IA Sem Sofrer Penalização no YouTube",
                tags = listOf("ia", "ferramentas", "produtividade", "automacao")
            ),
            TrendTopic(
                title = "Shorts Virais e Retenção nos Primeiros 3 Segundos",
                searchIntent = "Dicas práticas de edição acelerada e ganchos em vídeo vertical",
                competitionLevel = "Alta / Busca Constante",
                sampleTitle = "A Técnica do Loop Perfeito para Dobrar Visualizações no Shorts",
                tags = listOf("shorts", "retencao", "edicao", "viral")
            ),
            TrendTopic(
                title = "Equipamentos Baratos para Gravação em Casa",
                searchIntent = "Setup de baixo custo com microfones lapela e luzes acessíveis",
                competitionLevel = "Baixa / Excelente Oportunidade",
                sampleTitle = "Montei um Setup de Gravação Profissional com Menos de R$ 250",
                tags = listOf("setup", "microfone", "iluminacao", "custo beneficio")
            )
        )
    }

    val filtered = if (searchQuery.isBlank()) trends else trends.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.searchIntent.contains(searchQuery, ignoreCase = true)
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = NexoraCyanBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Descobrir & Tendências",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NexoraTextPrimary
                    )
                    Text(
                        text = "Intenção de busca real e temas com alta demanda",
                        fontSize = 12.sp,
                        color = NexoraTextMuted
                    )
                }
            }
        }

        // Search Input
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Pesquisar palavra-chave ou nicho...") },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("discover_search_input")
            )
        }

        // Info Note
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Aviso de Transparência: O algoritmo NEXORA analisa dados públicos agregados e padrões de busca sem inventar volumes fictícios. As sugestões servem como base para criação humana.",
                    fontSize = 11.sp,
                    color = NexoraTextMuted,
                    lineHeight = 16.sp
                )
            }
        }

        items(filtered) { trend ->
            TrendItemCard(
                trend = trend,
                onApplyToStudio = { onSelectTopicForStudio(trend.title) },
                onSearchYouTube = {
                    val url = "https://www.youtube.com/results?search_query=${Uri.encode(trend.title)}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // ignore intent failure
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun TrendItemCard(
    trend: TrendTopic,
    onApplyToStudio: () -> Unit,
    onSearchYouTube: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NexoraSurfaceCard)
            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag("trend_card")
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trend.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NexoraTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NexoraCyanBlue.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = trend.competitionLevel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraCyanBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Intenção de Busca: ${trend.searchIntent}",
                fontSize = 12.sp,
                color = NexoraTextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NexoraSurfaceVariant)
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = "Exemplo de Título Otimizado:",
                        fontSize = 10.sp,
                        color = NexoraTextMuted
                    )
                    Text(
                        text = trend.sampleTitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NexoraNeonGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { onSearchYouTube() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Ver no YouTube",
                        tint = NexoraTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pesquisar no YouTube", fontSize = 11.sp, color = NexoraTextMuted)
                }

                ElevatedButton(
                    onClick = onApplyToStudio,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Usar no Estúdio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
