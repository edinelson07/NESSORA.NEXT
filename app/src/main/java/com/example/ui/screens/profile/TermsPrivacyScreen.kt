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
import androidx.compose.material.icons.filled.Policy
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
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary

@Composable
fun TermsPrivacyScreen(
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
                        text = "Termos & Política de Privacidade",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = NexoraTextPrimary
                    )
                    Text(
                        text = "Em conformidade com a LGPD e Google Play Policies",
                        fontSize = 11.sp,
                        color = NexoraCyanBlue
                    )
                }
            }
        }

        item {
            PolicyBlock(
                title = "1. Coleta e Armazenamento Local",
                content = "O NEXORA armazena seus rascunhos, roteiros e preferências localmente no seu dispositivo através de banco de dados SQLite/Room criptografado pelo sistema operacional. Nenhum texto ou ideia é transmitido a terceiros sem a sua confirmação explícita de upload ou geração com IA."
            )
        }

        item {
            PolicyBlock(
                title = "2. Integração com YouTube e Google OAuth",
                content = "A conexão com o YouTube utiliza o fluxo oficial do Google OAuth 2.0 solicitando o escopo estrito necessário para gerenciamento e upload de vídeos do canal autorizado. O NEXORA nunca armazena suas senhas mestras e permite revogar o acesso a qualquer instante."
            )
        }

        item {
            PolicyBlock(
                title = "3. Inteligência Artificial e Moderação",
                content = "As consultas ao assistente de IA são enviadas de forma segura e estritamente anonimizada via backend. O conteúdo gerado não contém promessas falsas ou garantias de viralização, atuando como ferramenta de apoio à criatividade humana."
            )
        }

        item {
            PolicyBlock(
                title = "4. Exclusão e Exportação de Dados (LGPD)",
                content = "Você possui total controle sobre seus dados. A qualquer momento, é possível exportar a íntegra de suas criações em formato aberto JSON ou solicitar a exclusão irrevogável de todos os registros armazenados."
            )
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun PolicyBlock(title: String, content: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NexoraSurfaceCard)
            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = content, fontSize = 11.sp, color = NexoraTextSecondary, lineHeight = 16.sp)
        }
    }
}
