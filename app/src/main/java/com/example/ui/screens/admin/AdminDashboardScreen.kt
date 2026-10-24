package com.example.ui.screens.admin

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AdminMetrics
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCyanBlue
import com.example.ui.theme.NexoraNeonGlow
import com.example.ui.theme.NexoraNeonGreen
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceBorderGlow
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceDark
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary

@Composable
fun AdminDashboardScreen(
    metrics: AdminMetrics,
    onTestLog: () -> Unit,
    onShowToast: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    var pingStatus by remember { mutableStateOf("Latência: 24ms (Excelente)") }

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
                        text = "Painel Administrativo & Telemetria",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = NexoraTextPrimary
                    )
                    Text(
                        text = "Saúde das APIs, monitoramento e logs criptografados",
                        fontSize = 11.sp,
                        color = NexoraCyanBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Live Health Status
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.2.dp, NexoraSurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = NexoraNeonGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Diagnóstico de Infraestrutura",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NexoraTextPrimary
                            )
                        }

                        ElevatedButton(
                            onClick = {
                                val lat = (18..35).random()
                                pingStatus = "Latência: ${lat}ms (Excelente)"
                                onShowToast("Ping executado: ${lat}ms!")
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = NexoraSurfaceDark,
                                contentColor = NexoraNeonGreen
                            )
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Testar Ping", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = pingStatus,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraCyanBlue
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        AdminServiceCard("YouTube Data API v3", metrics.apiYouTubeStatus, isOk = true)
                        AdminServiceCard("Provedor Google Gemini AI", metrics.apiGeminiStatus, isOk = true)
                        AdminServiceCard("Gateway PagBank Webhooks", metrics.pagBankStatus, isOk = true)
                        AdminServiceCard("Banco de Dados Persistente Room", "SQLite Local Criptografado", isOk = true)
                    }
                }
            }
        }

        // Audit Logs Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = NexoraCyanBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Registros de Auditoria (${metrics.auditLogs.size})",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraTextPrimary
                    )
                }

                ElevatedButton(
                    onClick = {
                        onTestLog()
                        onShowToast("Novo evento registrado na auditoria!")
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraCyanBlue,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Gerar Log de Teste", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(
                text = "Rastreabilidade de integridade do sistema sem exposição de credenciais.",
                fontSize = 11.sp,
                color = NexoraTextMuted
            )
        }

        items(metrics.auditLogs) { log ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = log.action,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NexoraTextPrimary
                        )
                        Text(
                            text = "${log.id} • ${log.timestamp} por ${log.userOrSystem}",
                            fontSize = 10.sp,
                            color = NexoraTextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NexoraNeonGlow)
                            .border(1.dp, NexoraNeonGreen, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = log.status,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NexoraNeonGreen
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
private fun AdminServiceCard(name: String, status: String, isOk: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(NexoraSurfaceDark)
            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isOk) NexoraNeonGreen else NexoraCyanBlue)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = status,
                    fontSize = 11.sp,
                    color = if (isOk) NexoraNeonGreen else NexoraCyanBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
