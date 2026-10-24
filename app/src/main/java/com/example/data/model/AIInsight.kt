package com.example.data.model

data class AIInsight(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // "RETENÇÃO", "CTR", "SEO", "TENDÊNCIA"
    val potentialScore: Int, // 1 to 100
    val actionText: String
)
