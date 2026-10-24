package com.example.ui.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCyanBlue
import com.example.ui.theme.NexoraNeonGreen
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary

@Composable
fun FutureTvScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

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
                        text = "TV Aberta & Legendas (Fase Futura)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NexoraTextPrimary
                    )
                    Text(
                        text = "Arquitetura e diretrizes legais de streaming",
                        fontSize = 11.sp,
                        color = NexoraCyanBlue
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tv, contentDescription = null, tint = NexoraNeonGreen, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Especificação Arquitetural Fase 5",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexoraTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Este módulo foi isolado na arquitetura para preservar o lançamento ágil e o foco comercial nas ferramentas prioritárias para criadores de conteúdo do YouTube.",
                        fontSize = 12.sp,
                        color = NexoraTextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Diretrizes de Implementação Futura:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraCyanBlue
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("• Fontes Oficiais: Apenas feeds de emissoras públicas (ex: TV Brasil, EBC, PBS) com consentimento expresso e distribuição aberta legalizada.", fontSize = 11.sp, color = NexoraTextMuted)
                        Text("• Conformidade Regulatória: Bloqueio estrito de qualquer interceptação de transmissões pagas ou que violem acordos de retransmissão.", fontSize = 11.sp, color = NexoraTextMuted)
                        Text("• Legendas Acessíveis por IA: Transcrição em tempo real via modelos locais para garantir acessibilidade sem onerar quotas externas de nuvem.", fontSize = 11.sp, color = NexoraTextMuted)
                        Text("• Geolocalização: Verificação de disponibilidade regional por IP/GPS para respeitar restrições territoriais dos canais.", fontSize = 11.sp, color = NexoraTextMuted)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
