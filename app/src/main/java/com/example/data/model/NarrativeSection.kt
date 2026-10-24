package com.example.data.model

data class NarrativeSection(
    val id: String,
    val title: String,
    val purpose: String,
    val defaultText: String,
    val retentionTip: String
) {
    companion object {
        fun getDefaultStructure(): List<NarrativeSection> = listOf(
            NarrativeSection(
                id = "hook",
                title = "1. Gancho Forte Inicial (0s - 15s)",
                purpose = "Parar a rolagem imediatamente e capturar a atenção sem promessas falsas",
                defaultText = "Se você ainda faz [AÇÃO COMUM], está perdendo [RESULTADO DESEJADO]. Nos próximos minutos você vai descobrir como mudar isso definitivamente.",
                retentionTip = "Comece no auge da ação ou apresente um contraste visual chocante."
            ),
            NarrativeSection(
                id = "promise",
                title = "2. Apresentação da Promessa (15s - 45s)",
                purpose = "Validar a expectativa do espectador e estabelecer o valor entregue",
                defaultText = "Neste vídeo, eu vou te guiar passo a passo por 3 etapas práticas que qualquer criador pode aplicar hoje mesmo.",
                retentionTip = "Seja específico: não prometa viralizar, prometa resolver uma dor clara."
            ),
            NarrativeSection(
                id = "development_1",
                title = "3. Desenvolvimento Progressivo (45s - 3m)",
                purpose = "Entregar a primeira grande vitória ou revelação rápida",
                defaultText = "Primeiro pilar: a estrutura base. O maior erro da maioria é pular o planejamento e ir direto para a edição.",
                retentionTip = "Mantenha o ritmo dinâmico com cortes de corte de cena a cada 4-7 segundos."
            ),
            NarrativeSection(
                id = "reengagement",
                title = "4. Reengajamento & Segundo Gancho (3m - 5m)",
                purpose = "Evitar a queda típica de retenção no meio do vídeo",
                defaultText = "Mas atenção: existe um detalhe crucial que quase todo mundo esquece, e se você errar aqui, todo o resto desmorona.",
                retentionTip = "Abra uma nova 'open loop' (curiosidade) antes de fechar o assunto anterior."
            ),
            NarrativeSection(
                id = "climax",
                title = "5. Clímax e Demonstração Prática (5m - 8m)",
                purpose = "A prova incontestável ou aplicação do método na prática",
                defaultText = "Veja na prática o antes e depois. Quando aplicamos essa mudança, o resultado foi imediato.",
                retentionTip = "Mostre a tela, números reais ou o processo visual acontecendo."
            ),
            NarrativeSection(
                id = "cta",
                title = "6. Chamada para Ação Orgânica (8m - 9m)",
                purpose = "Incentivar engajamento conectado ao tema",
                defaultText = "Qual dessas etapas você já aplica no seu canal? Deixe nos comentários abaixo com o link do seu projeto.",
                retentionTip = "Faça perguntas específicas em vez do genérico 'inscreva-se'."
            ),
            NarrativeSection(
                id = "next_hook",
                title = "7. Gancho para o Próximo Conteúdo (Final)",
                purpose = "Direcionar para a próxima tela final e criar efeito maratona",
                defaultText = "E agora que você domina isso, assista ao próximo vídeo que está aparecendo na tela para ver como automatizar esse fluxo.",
                retentionTip = "Nunca anuncie 'para finalizar'. Aponte direto para o card final."
            )
        )
    }
}
