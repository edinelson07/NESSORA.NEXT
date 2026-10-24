package com.example.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AdminAuditLog(
    val id: String,
    val action: String,
    val timestamp: String,
    val userOrSystem: String,
    val status: String
)

data class AdminMetrics(
    val totalDrafts: Int = 0,
    val activeChannels: Int = 1,
    val apiYouTubeStatus: String = "Online (Quota OK)",
    val apiGeminiStatus: String = "Pronto",
    val pagBankStatus: String = "Homologado",
    val auditLogs: List<AdminAuditLog> = emptyList()
)

class AdminRepository {
    private val _metrics = MutableStateFlow(
        AdminMetrics(
            totalDrafts = 2,
            activeChannels = 1,
            apiYouTubeStatus = "Ativo (Demo & OAuth v3)",
            apiGeminiStatus = "Pronto (Fallback Local Ativo)",
            pagBankStatus = "Ambiente Sandbox Configurado",
            auditLogs = listOf(
                AdminAuditLog("LOG-01", "Inicialização do banco local Room", "Agora", "Sistema", "SUCESSO"),
                AdminAuditLog("LOG-02", "Validação de esquema de rascunhos", "10 min atrás", "Sistema", "OK"),
                AdminAuditLog("LOG-03", "Verificação de permissão de rede", "12 min atrás", "Segurança", "CONFIRMADO")
            )
        )
    )
    val metrics: StateFlow<AdminMetrics> = _metrics.asStateFlow()

    fun updateDraftCount(count: Int) {
        _metrics.value = _metrics.value.copy(totalDrafts = count)
    }

    fun addAuditLog(action: String, status: String) {
        val newLog = AdminAuditLog(
            id = "LOG-${System.currentTimeMillis() % 10000}",
            action = action,
            timestamp = "Agora",
            userOrSystem = "Criador",
            status = status
        )
        _metrics.value = _metrics.value.copy(
            auditLogs = listOf(newLog) + _metrics.value.auditLogs.take(9)
        )
    }
}
