package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraBrandGradient
import com.example.ui.theme.NexoraLilacGlow
import com.example.ui.theme.NexoraLilacNeon
import com.example.ui.theme.NexoraMagenta
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceBorderGlow
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceDark
import com.example.ui.theme.NexoraTextMuted

@Composable
fun NexoraBottomBar(
    currentTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        // Base bar background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .shadow(16.dp, RoundedCornerShape(26.dp), spotColor = NexoraMagenta)
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(NexoraSurfaceCard, NexoraSurfaceDark)
                    )
                )
                .border(
                    width = 1.2.dp,
                    brush = Brush.horizontalGradient(
                        listOf(NexoraSurfaceBorder, NexoraSurfaceBorderGlow, NexoraSurfaceBorder)
                    ),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tab 0: Início
                BottomNavItem(
                    label = "Início",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    isSelected = currentTab == 0,
                    onClick = { onTabSelected(0) },
                    testTag = "nav_item_home"
                )

                // Tab 1: TV Aberta (Canais Brasileiros)
                BottomNavItem(
                    label = "TV Aberta",
                    selectedIcon = Icons.Filled.LiveTv,
                    unselectedIcon = Icons.Outlined.LiveTv,
                    isSelected = currentTab == 1,
                    onClick = { onTabSelected(1) },
                    testTag = "nav_item_tv"
                )

                // Tab 2: Prominent Center Creation FAB (+)
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .testTag("nav_item_center_space"),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(12.dp, CircleShape, spotColor = NexoraLilacNeon)
                            .clip(CircleShape)
                            .background(NexoraBrandGradient)
                            .border(1.5.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onTabSelected(2) }
                            .testTag("nav_item_create_fab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Criar Conteúdo",
                            tint = NexoraBackground,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                // Tab 3: Loja (Marketplace)
                BottomNavItem(
                    label = "Loja",
                    selectedIcon = Icons.Filled.ShoppingBag,
                    unselectedIcon = Icons.Outlined.ShoppingBag,
                    isSelected = currentTab == 3,
                    onClick = { onTabSelected(3) },
                    testTag = "nav_item_store"
                )

                // Tab 4: Perfil
                BottomNavItem(
                    label = "Perfil",
                    selectedIcon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.Person,
                    isSelected = currentTab == 4,
                    onClick = { onTabSelected(4) },
                    testTag = "nav_item_profile"
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) NexoraLilacGlow else Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = label,
                tint = if (isSelected) NexoraLilacNeon else NexoraTextMuted,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) NexoraLilacNeon else NexoraTextMuted
            )
        }
    }
}
