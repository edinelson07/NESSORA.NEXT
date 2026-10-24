package com.example.ui.screens.profile

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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChannelAuthStatus
import com.example.data.model.ChannelInfo
import com.example.data.repository.SubscriptionState
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraError
import com.example.ui.theme.NexoraLilacGlow
import com.example.ui.theme.NexoraLilacNeon
import com.example.ui.theme.NexoraMagenta
import com.example.ui.theme.NexoraMagentaGlow
import com.example.ui.theme.NexoraPurpleElectric
import com.example.ui.theme.NexoraPurpleGlow
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceBorderGlow
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceDark
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary
import com.example.ui.theme.NexoraWineDark

@Composable
fun ProfileScreen(
    channelInfo: ChannelInfo,
    subscription: SubscriptionState,
    onOpenYouTubeManager: () -> Unit,
    onOpenAudioCatalog: () -> Unit,
    onOpenPro: () -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenFutureTv: () -> Unit,
    onOpenMarketplace: () -> Unit = {},
    onOpenLibrary: () -> Unit = {},
    onOpenAi: () -> Unit = {},
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexoraBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Profile Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(22.dp), spotColor = NexoraMagenta)
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(NexoraWineDark, NexoraCardElevated, NexoraSurfaceDark)
                        )
                    )
                    .border(
                        1.2.dp,
                        Brush.horizontalGradient(
                            listOf(NexoraMagenta.copy(alpha = 0.8f), NexoraSurfaceBorderGlow)
                        ),
                        RoundedCornerShape(22.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(NexoraLilacNeon, NexoraMagenta))
                            )
                            .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "N",
                            color = NexoraBackground,
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Criador Nexora",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = NexoraTextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (subscription.isPro) NexoraLilacNeon else NexoraWineDark
                                    )
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (subscription.isPro) "PRO" else "GRATUITO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (subscription.isPro) NexoraBackground else NexoraLilacNeon
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Canal Vinculado: ${channelInfo.title}",
                            fontSize = 12.sp,
                            color = NexoraTextSecondary
                        )
                        Text(
                            text = "Status: ${if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) "Autorizado via OAuth 2.0" else "Demonstração Ativa"}",
                            fontSize = 11.sp,
                            color = if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) Color(0xFF36E58D) else NexoraLilacNeon
                        )
                    }
                }
            }
        }

        // Section: Funcionalidades Principais
        item {
            Text(
                text = "Canais, Comércio & Criação",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NexoraTextSecondary
            )
        }

        item {
            SettingsNavigationItemElevated(
                title = "TV Aberta (Canais Brasileiros)",
                subtitle = "Transmissões ao vivo oficiais e catálogo verificado",
                icon = Icons.Default.LiveTv,
                iconTint = NexoraLilacNeon,
                glowColor = NexoraLilacGlow,
                onClick = onOpenFutureTv,
                testTag = "profile_tv_item"
            )
        }

        item {
            SettingsNavigationItemElevated(
                title = "Loja NEXORA (Marketplace)",
                subtitle = "Vitrine de produtos, compras via Pix e painel do vendedor",
                icon = Icons.Default.ShoppingBag,
                iconTint = NexoraMagenta,
                glowColor = NexoraMagentaGlow,
                onClick = onOpenMarketplace,
                testTag = "profile_store_item"
            )
        }

        item {
            SettingsNavigationItemElevated(
                title = "Assistente NEXORA AI (Gemini)",
                subtitle = "Geração de ideias, retenção e roteiros estruturados",
                icon = Icons.Default.AutoAwesome,
                iconTint = NexoraPurpleElectric,
                glowColor = NexoraPurpleGlow,
                onClick = onOpenAi,
                testTag = "profile_ai_item"
            )
        }

        item {
            SettingsNavigationItemElevated(
                title = "Biblioteca de Rascunhos",
                subtitle = "Rascunhos persistentes, backup JSON e histórico",
                icon = Icons.Default.Folder,
                iconTint = NexoraLilacNeon,
                glowColor = NexoraLilacGlow,
                onClick = onOpenLibrary,
                testTag = "profile_library_item"
            )
        }

        // Section: Integrações
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Integrações de Mídia",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NexoraTextSecondary
            )
        }

        item {
            SettingsNavigationItemElevated(
                title = "Canal Conectado no YouTube",
                subtitle = "Gestão de permissões OAuth 2.0 e estatísticas em tempo real",
                icon = Icons.Default.SmartDisplay,
                iconTint = NexoraMagenta,
                glowColor = NexoraMagentaGlow,
                onClick = onOpenYouTubeManager,
                testTag = "profile_youtube_item"
            )
        }

        item {
            SettingsNavigationItemElevated(
                title = "Catálogo de Áudio & Licenças",
                subtitle = "Royalty-free, YouTube Library e Creative Commons CC-BY",
                icon = Icons.Default.GraphicEq,
                iconTint = NexoraLilacNeon,
                glowColor = NexoraLilacGlow,
                onClick = onOpenAudioCatalog,
                testTag = "profile_audio_item"
            )
        }

        item {
            SettingsNavigationItemElevated(
                title = "Assinatura NEXORA Pro",
                subtitle = if (subscription.isPro) "Plano Pro Ativo (Acesso Ilimitado e Marketplace Liberado)" else "Assinar via PagBank Pix (R$ 39,90/mês)",
                icon = Icons.Default.Diamond,
                iconTint = NexoraMagenta,
                glowColor = NexoraMagentaGlow,
                onClick = onOpenPro,
                testTag = "profile_pro_item"
            )
        }

        // Section: Sistema & Governança
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Plataforma & Governança",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NexoraTextSecondary
            )
        }

        item {
            SettingsNavigationItemElevated(
                title = "Painel Administrativo & Telemetria",
                subtitle = "Diagnóstico de APIs, teste de pings e logs de auditoria",
                icon = Icons.Default.AdminPanelSettings,
                iconTint = NexoraLilacNeon,
                glowColor = NexoraLilacGlow,
                onClick = onOpenAdmin,
                testTag = "profile_admin_item"
            )
        }

        item {
            SettingsNavigationItemElevated(
                title = "Termos de Uso e Privacidade (LGPD)",
                subtitle = "Políticas do Google Play e conformidade de dados",
                icon = Icons.Default.Policy,
                iconTint = NexoraTextSecondary,
                glowColor = Color(0x22FFFFFF),
                onClick = onOpenTerms,
                testTag = "profile_terms_item"
            )
        }

        item {
            // Delete Account and Exit actions
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(NexoraSurfaceCard)
                    .border(1.dp, NexoraError.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .clickable { showDeleteAccountDialog = true }
                    .padding(16.dp)
                    .testTag("profile_delete_account_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NexoraError.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = null,
                            tint = NexoraError,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Exclusão de Conta e Dados Locais",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NexoraError
                        )
                        Text(
                            text = "Limpar todos os rascunhos locais e revogar permissões",
                            fontSize = 11.sp,
                            color = NexoraTextMuted
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = {
                Text("Confirmar Exclusão de Conta", fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
            },
            text = {
                Text(
                    text = "Em conformidade com a LGPD e as políticas do Google Play, você tem o direito de solicitar a exclusão de todos os dados locais e revogar suas credenciais. Seus rascunhos locais e histórico de IA serão redefinidos.",
                    color = NexoraTextSecondary,
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        showDeleteAccountDialog = false
                        onShowToast("Solicitação de exclusão processada com sucesso!")
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraError,
                        contentColor = Color.White
                    )
                ) {
                    Text("Excluir Dados", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancelar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }
}

@Composable
private fun SettingsNavigationItemElevated(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    glowColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(NexoraSurfaceCard)
            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(glowColor)
                        .border(1.dp, iconTint.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        fontSize = 11.sp,
                        color = NexoraTextSecondary,
                        lineHeight = 14.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = NexoraTextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
