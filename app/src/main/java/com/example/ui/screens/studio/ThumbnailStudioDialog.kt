package com.example.ui.screens.studio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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

@Composable
fun ThumbnailStudioDialog(
    initialTitle: String,
    initialSubtitle: String,
    initialBgHex: String,
    initialAccentHex: String,
    isShorts: Boolean,
    onDismiss: () -> Unit,
    onSave: (title: String, subtitle: String, bgHex: String, accentHex: String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle.ifBlank { "TÍTULO IMPACTANTE" }) }
    var subtitle by remember { mutableStateOf(initialSubtitle.ifBlank { "PASSO A PASSO" }) }
    var badgeText by remember { mutableStateOf("NOVO MÉTODO") }
    var selectedColorIndex by remember { mutableStateOf(0) }
    var isAspect16x9 by remember { mutableStateOf(!isShorts) }

    val palettes = listOf(
        Pair(Color(0xFF07111F), Color(0xFF36E58D)), // Neon Green Cyber
        Pair(Color(0xFF0B192C), Color(0xFF27B8FF)), // Cyber Cyan
        Pair(Color(0xFF1E0E3E), Color(0xFF8B6CFF)), // Electric Purple
        Pair(Color(0xFF1F0D0D), Color(0xFFFF5757)), // Crimson Alert
        Pair(Color(0xFF1A1608), Color(0xFFFFC107))  // Golden Glow
    )

    val currentBg = palettes[selectedColorIndex].first
    val currentAccent = palettes[selectedColorIndex].second

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(20.dp)),
            color = NexoraSurfaceCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Estúdio de Miniaturas (Thumbnail)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NexoraTextPrimary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = NexoraTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Ratio Toggle: 16:9 vs 9:16
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isAspect16x9) NexoraNeonGreen else NexoraSurfaceVariant)
                            .clickable { isAspect16x9 = true }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "16:9 (YouTube Horizontal)",
                            color = if (isAspect16x9) NexoraBackground else NexoraTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (!isAspect16x9) NexoraCyanBlue else NexoraSurfaceVariant)
                            .clickable { isAspect16x9 = false }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "9:16 (Shorts Vertical)",
                            color = if (!isAspect16x9) NexoraBackground else NexoraTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // LIVE PREVIEW CANVAS
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(if (isAspect16x9) 16f / 9f else 9f / 16f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, currentAccent, RoundedCornerShape(12.dp))
                        .background(currentBg)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Stylized canvas grid lines in background
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawLine(
                            color = currentAccent.copy(alpha = 0.15f),
                            start = Offset(0f, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2f
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        // Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(currentAccent)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badgeText.uppercase(),
                                color = NexoraBackground,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Title Text High Contrast
                        Text(
                            text = title.uppercase(),
                            color = Color.White,
                            fontSize = if (isAspect16x9) 18.sp else 16.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Subtitle
                        Text(
                            text = subtitle.uppercase(),
                            color = currentAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Palette selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tema de Cores:",
                        fontSize = 12.sp,
                        color = NexoraTextSecondary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        palettes.forEachIndexed { index, pair ->
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(pair.second)
                                    .border(
                                        width = if (selectedColorIndex == index) 2.dp else 1.dp,
                                        color = if (selectedColorIndex == index) Color.White else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColorIndex = index },
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedColorIndex == index) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Inputs
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Texto Principal (Título)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexoraNeonGreen,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("thumbnail_title_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Subtítulo de Contraste") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NexoraCyanBlue,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("thumbnail_subtitle_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Actions
                ElevatedButton(
                    onClick = {
                        val bgHex = "#%06X".format(0xFFFFFF and currentBg.value.toInt())
                        val accentHex = "#%06X".format(0xFFFFFF and currentAccent.value.toInt())
                        onSave(title, subtitle, bgHex, accentHex)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("thumbnail_save_btn"),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraNeonGreen,
                        contentColor = NexoraBackground
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Salvar Miniatura no Rascunho", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
