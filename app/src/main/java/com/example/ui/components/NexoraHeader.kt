package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChannelAuthStatus
import com.example.data.model.ChannelInfo
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraLilacGlow
import com.example.ui.theme.NexoraLilacNeon
import com.example.ui.theme.NexoraMagenta
import com.example.ui.theme.NexoraPurpleElectric
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceBorderGlow
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceDark
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary
import com.example.ui.theme.NexoraWineDark

@Composable
fun NexoraHeader(
    channelInfo: ChannelInfo,
    isPro: Boolean,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProClick: () -> Unit,
    onProfileClick: () -> Unit,
    onChannelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(NexoraSurfaceDark, NexoraBackground)
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(NexoraSurfaceBorder, NexoraSurfaceBorderGlow, NexoraSurfaceBorder)
                ),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Slogan
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onProfileClick() }
                        .padding(vertical = 4.dp)
                        .testTag("header_logo_group")
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(10.dp, RoundedCornerShape(12.dp), spotColor = NexoraMagenta)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(NexoraLilacNeon, NexoraPurpleElectric, NexoraMagenta)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "N",
                            color = NexoraBackground,
                            fontWeight = FontWeight.Black,
                            fontSize = 25.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "NEXORA",
                                color = NexoraTextPrimary,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Glowing Status Dot
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED)
                                            Color(0xFF36E58D)
                                        else
                                            NexoraLilacNeon
                                    )
                                    .shadow(4.dp, CircleShape, spotColor = NexoraLilacNeon)
                            )
                        }
                        Text(
                            text = "Inteligência para Criadores",
                            color = NexoraLilacNeon,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Actions: Pro Badge, Search, Notifications, Avatar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pro Badge
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = if (isPro) 6.dp else 2.dp,
                                shape = RoundedCornerShape(20.dp),
                                spotColor = if (isPro) NexoraLilacNeon else NexoraMagenta
                            )
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isPro)
                                    Brush.horizontalGradient(listOf(NexoraLilacNeon, NexoraMagenta))
                                else
                                    Brush.horizontalGradient(listOf(NexoraWineDark, NexoraSurfaceCard))
                            )
                            .border(
                                width = 1.dp,
                                color = if (isPro) NexoraLilacNeon else NexoraSurfaceBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onProClick() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("header_pro_badge")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = "NEXORA Pro",
                                tint = if (isPro) NexoraBackground else NexoraLilacNeon,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isPro) "PRO" else "ASSINAR",
                                color = if (isPro) NexoraBackground else NexoraTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    // Search Button
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NexoraSurfaceCard)
                            .border(1.dp, NexoraSurfaceBorder, CircleShape)
                            .testTag("header_search_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Pesquisar",
                            tint = NexoraTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Notifications Button
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NexoraSurfaceCard)
                            .border(1.dp, NexoraSurfaceBorder, CircleShape)
                            .testTag("header_notifications_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificações",
                            tint = NexoraTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(NexoraWineDark, NexoraCardElevated)
                                )
                            )
                            .border(1.5.dp, NexoraLilacNeon.copy(alpha = 0.8f), CircleShape)
                            .clickable { onProfileClick() }
                            .testTag("header_avatar_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = NexoraLilacNeon,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sub-bar: Channel quick indicator pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NexoraSurfaceCard.copy(alpha = 0.7f))
                    .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(10.dp))
                    .clickable { onChannelClick() }
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SmartDisplay,
                        contentDescription = null,
                        tint = NexoraMagenta,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Canal: ${channelInfo.title}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${channelInfo.customUrl})",
                        fontSize = 10.sp,
                        color = NexoraTextMuted
                    )
                }

                Text(
                    text = if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) "● Conectado" else "● Modo Demo",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (channelInfo.authStatus == ChannelAuthStatus.AUTHORIZED) Color(0xFF36E58D) else NexoraLilacNeon
                )
            }
        }
    }
}
