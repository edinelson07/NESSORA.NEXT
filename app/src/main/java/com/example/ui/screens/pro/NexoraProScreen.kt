package com.example.ui.screens.pro

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.SubscriptionState
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCyanBlue
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

@Composable
fun NexoraProScreen(
    subscription: SubscriptionState,
    onGeneratePix: (isAnnual: Boolean) -> String,
    onConfirmDev: () -> Unit,
    onShowToast: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    var isAnnualSelected by remember { mutableStateOf(true) }
    var pixCode by remember { mutableStateOf<String?>(subscription.pendingPixCode) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    fun copyPix(code: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("Pix PagBank", code))
        onShowToast("Código Pix Copia e Cola copiado!")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexoraBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        text = "NEXORA Pro & Planos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = NexoraTextPrimary
                    )
                    Text(
                        text = "IA ilimitada, modelos de retenção e faturamento oficial",
                        fontSize = 11.sp,
                        color = NexoraCyanBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Luxury Pro Banner Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(22.dp), spotColor = NexoraPurple)
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF28174E), NexoraSurfaceCard, Color(0xFF102644))
                        )
                    )
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(
                            listOf(NexoraPurple, NexoraCyanBlue, NexoraNeonGreen)
                        ),
                        RoundedCornerShape(22.dp)
                    )
                    .padding(20.dp)
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
                                    .background(NexoraPurple.copy(alpha = 0.3f))
                                    .border(1.dp, NexoraPurple, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Diamond, contentDescription = null, tint = NexoraPurple, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "NEXORA PRO",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Black,
                                    color = NexoraTextPrimary,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = if (subscription.isPro) "Plano Pro Ativo" else "Potência Máxima de Criação",
                                    fontSize = 12.sp,
                                    color = if (subscription.isPro) NexoraNeonGreen else NexoraCyanBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(NexoraNeonGlow)
                                .border(1.dp, NexoraNeonGreen, RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (subscription.isPro) "ATIVADO" else "25% OFF ANUAL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = NexoraNeonGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        BenefitItem("Geração Ilimitada de Ideias, Títulos e Roteiros com Gemini AI")
                        BenefitItem("Modelos Avançados de Retenção Narrativa e Segundo Gancho")
                        BenefitItem("Estúdio de Miniaturas com Badges Exclusivos de Alto CTR")
                        BenefitItem("Validador Completo de Publicação e Exportação JSON")
                        BenefitItem("Proteção e Auditoria de Direitos Autorais de Áudio")
                    }
                }
            }
        }

        // Cycle Selector (Mensal vs Anual)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(14.dp))
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (!isAnnualSelected) NexoraSurfaceVariant else Color.Transparent)
                        .clickable { isAnnualSelected = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mensal (R$ 39,90/mês)",
                        fontSize = 12.sp,
                        fontWeight = if (!isAnnualSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (!isAnnualSelected) NexoraTextPrimary else NexoraTextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isAnnualSelected) NexoraPurpleGlow else Color.Transparent)
                        .border(if (isAnnualSelected) 1.dp else 0.dp, if (isAnnualSelected) NexoraPurple else Color.Transparent, RoundedCornerShape(10.dp))
                        .clickable { isAnnualSelected = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Anual (R$ 349,00/ano)",
                        fontSize = 12.sp,
                        fontWeight = if (isAnnualSelected) FontWeight.Black else FontWeight.Medium,
                        color = if (isAnnualSelected) NexoraNeonGreen else NexoraTextMuted
                    )
                }
            }
        }

        // Checkout & PagBank Integration
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.2.dp, NexoraSurfaceBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = NexoraCyanBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Checkout PagBank Oficial (Pix Instantâneo)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexoraTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Gere o código Pix padronizado pelo Banco Central. A ativação no servidor é automática assim que a notificação de pagamento for recebida.",
                        fontSize = 11.sp,
                        color = NexoraTextMuted,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (pixCode == null) {
                        ElevatedButton(
                            onClick = {
                                pixCode = onGeneratePix(isAnnualSelected)
                                onShowToast("Código Pix gerado!")
                            },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = NexoraNeonGreen,
                                contentColor = NexoraBackground
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = NexoraNeonGreen)
                                .testTag("pro_generate_pix_btn")
                        ) {
                            Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAnnualSelected) "Gerar Pix de R$ 349,00 (Plano Anual)" else "Gerar Pix de R$ 39,90 (Plano Mensal)",
                                fontWeight = FontWeight.Black
                            )
                        }
                    } else {
                        Column {
                            // QR Code Simulation Canvas
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White)
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.size(100.dp)) {
                                        // Draw geometric QR code pattern representation
                                        drawRect(Color.Black, Offset(0f, 0f), androidx.compose.ui.geometry.Size(30f, 30f))
                                        drawRect(Color.Black, Offset(size.width - 30f, 0f), androidx.compose.ui.geometry.Size(30f, 30f))
                                        drawRect(Color.Black, Offset(0f, size.height - 30f), androidx.compose.ui.geometry.Size(30f, 30f))
                                        drawRect(Color.Black, Offset(40f, 40f), androidx.compose.ui.geometry.Size(20f, 20f))
                                        drawRect(Color.Black, Offset(20f, 60f), androidx.compose.ui.geometry.Size(15f, 15f))
                                        drawRect(Color.Black, Offset(70f, 20f), androidx.compose.ui.geometry.Size(15f, 15f))
                                        drawRect(Color.Black, Offset(60f, 70f), androidx.compose.ui.geometry.Size(20f, 20f))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NexoraSurfaceDark)
                                    .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(10.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Pix Copia e Cola Gerado:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NexoraNeonGreen
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = pixCode ?: "",
                                        fontSize = 10.sp,
                                        color = NexoraTextSecondary,
                                        maxLines = 2
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { pixCode?.let { copyPix(it) } },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NexoraCyanBlue)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Copiar Código Pix", fontSize = 11.sp)
                                }

                                ElevatedButton(
                                    onClick = {
                                        onConfirmDev()
                                        showSuccessDialog = true
                                    },
                                    modifier = Modifier.weight(1.2f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = NexoraNeonGreen,
                                        contentColor = NexoraBackground
                                    )
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Simular Aprovação", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = {
                Text("🎉 Parabéns! NEXORA Pro Ativado!", fontWeight = FontWeight.Bold, color = NexoraNeonGreen)
            },
            text = {
                Text(
                    text = "Seu plano NEXORA Pro foi homologado com sucesso. Todos os limites de geração com inteligência artificial, exportações de SEO e templates de retenção foram desbloqueados!",
                    color = NexoraTextSecondary,
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                ElevatedButton(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Começar a Criar", fontWeight = FontWeight.Bold)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }
}

@Composable
private fun BenefitItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(NexoraNeonGlow),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = NexoraNeonGreen,
                modifier = Modifier.size(12.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, fontSize = 12.sp, color = NexoraTextPrimary, fontWeight = FontWeight.Medium)
    }
}
