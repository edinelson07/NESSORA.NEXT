# NEXORA — Inteligência para Criadores & Versão Comercial

> Plataforma Android nativa para criadores de conteúdo do YouTube, com nova identidade visual premium (Bordô, Lilás Neon, Roxo Elétrico e Magenta), Guia e Player de TV Aberta com Emissoras Oficiais Brasileiras, Marketplace Comercial com Checkout Pix (PagBank) e Painel do Vendedor restrito ao plano Premium, Estúdio de Roteiros, Assistente IA e persistência local robusta via Room SQLite.

---

## 1. Decisão Tecnológica e Arquitetura

O usuário solicitou inicialmente preferência por Flutter com Dart caso compatível com o ambiente, ou uma alternativa robusta caso o ambiente exigisse outra tecnologia Android.

**Decisão Adotada: Kotlin Nativo com Jetpack Compose (Material 3)**
* **Motivo:** O ambiente do Google AI Studio Build é nativo Android (Gradle Kotlin DSL, Kotlin 2.2, AGP 9.1, KSP, Room SQLite e Compose Material 3), garantindo performance em 60fps, compilação de APKs e integração com reprodutores de vídeo oficiais, WebView oficial, APIs do Google e pagamentos Pix.
* **Arquitetura:** MVVM (Model-View-ViewModel) desacoplado com Repository Pattern, StateFlow reativo e injeção por ViewModelProvider.Factory.
* **Persistência de Dados (Room Database v2):**
  - `DraftEntity` & `DraftDao`: Rascunhos de roteiros, tags, miniaturas e status.
  - `AudioEntity` & `AudioDao`: Trilhas e efeitos com conformidade de licenças.
  - `TvChannelEntity` & `TvChannelDao`: Catálogo e favoritos de canais brasileiros com transmissões oficiais.
  - `ProductEntity` & `ProductDao`: Catálogo público de produtos e inventário do marketplace.
  - `SellerEntity` & `SellerDao`: Perfis de lojas de criadores e verificação Premium.
  - `OrderEntity` & `OrderDao`: Histórico de pedidos e códigos Pix Copia-e-Cola no padrão Banco Central.

---

## 2. Identidade Visual Oficial

Seguindo a direção artística solicitada:
* **Fundo Principal:** Preto profundo avermelhado (`#080307`).
* **Superfícies & Cartões:** Tons de vinho e grafite (`#140813`, `#1C0D20`, `#26122B`).
* **Bordas & Brilhos:** Bordô e púrpura luminoso (`#47204E`, `#7A2E8A`).
* **Lilás Neon (Destaque Principal):** `#C77DFF`.
* **Roxo Elétrico & Magenta:** `#922BFF` e `#F044C8`.
* **Vinho / Bordô Escuro:** `#5B1024`.
* **Tipografia:** Branco suave (`#FFFFFF`) para títulos e cinza frio suave (`#B9A8BE`) para textos secundários.
* **Logotipo:** Símbolo "N" geométrico estilizado em gradiente lilás/magenta com o slogan *"NEXORA — Inteligência para Criadores"*.

---

## 3. Módulos e Funcionalidades Comerciais

### 3.1. TV Aberta — Canais Brasileiros com Transmissões Oficiais
Permite que o criador e usuário assista a emissoras nacionais, legislativas, educativas e religiosas com sinal aberto autorizado:
- **Player Integrado em Compose com WebView:** Reprodução oficial em 16:9, suporte a tela cheia, indicador pulsante *"AO VIVO OFICIAL"* e tratamento de falhas de conexão.
- **Conformidade com Direitos Autorais:**
  - Emissoras com stream incorporável direto: TV Brasil (EBC), TV Cultura (Fundação Padre Anchieta), TV Câmara, TV Senado, Canal Gov, TV Justiça, Record News, Jovem Pan News, Rede Vida, TV Aparecida, Canção Nova.
  - Emissoras com exibição via portal institucional oficial: TV Globo (Globoplay), Band (Portal Band), SBT (SBT Vídeos). O app identifica com transparência a necessidade do portal oficial e fornece acesso direto.
- **Recursos da Interface:**
  - Busca em tempo real por nome do canal, estado ou categoria.
  - Filtros por categoria: Pública e Educativa, Notícias 24h, Legislativa e Cidadania, Nacional Aberta, Religiosa e Comunitária.
  - Favoritos persistentes com um toque.
  - Botão de sincronização para atualizar a grade sem necessitar de novo APK.

### 3.2. Loja NEXORA — Marketplace para Vendedores
Área comercial real para criadores comprarem e venderem equipamentos de produção e produtos digitais:
- **Vitrine Pública de Produtos:** Busca dinâmica, filtros por categoria (Equipamentos, Iluminação & Áudio, Presets & LUTs, Cursos & Ebooks, Serviços Criativos, Merch & Vestuário).
- **Detalhes do Produto & Carrinho:** Cálculo de frete/envio digital, controle de quantidade, cálculo automático de subtotal e total.
- **Checkout Seguro com Pix:** Geração de código Pix Copia-e-Cola no padrão oficial do Banco Central (`br.gov.bcb.pix`) preparado para o gateway PagBank, com QR Code estilizado e simulação de confirmação em ambiente de sandbox.
- **Painel do Vendedor & Controle de Acesso Premium:**
  - **Acesso Condicionado ao Plano Premium:** A publicação de produtos exige assinatura ativa do **NEXORA Pro**. Usuários do plano gratuito que tentam cadastrar produtos são direcionados à tela explicativa de benefícios do Pro.
  - **Gerenciamento de Produtos:** Cadastro com título, descrição, categoria, preço em R$, estoque e tipo (físico vs digital), além de ativação/pausa e exclusão.
  - **Gestão de Pedidos:** Acompanhamento de pedidos recebidos com atualização de status (Pendente Pix, Pago Pix, Enviado, Concluído).
  - **Dados da Loja:** Edição do nome fantasia, responsável, WhatsApp comercial e descrição institucional.

### 3.3. Estúdio de Roteiros, Miniaturas & IA (Preservados e Melhorados)
- **Estrutura de Retenção Narrativa:** Blocos interativos para ganchos de 0-15s, pontes de curiosidade, reengajamento e CTAs.
- **Estúdio de Miniaturas:** Canvas interativo com proporções 16:9 e 9:16, contrastes elevados e badges promocionais.
- **NEXORA AI (Google Gemini):** Geração de ideias com ângulos virais, títulos sem clickbait, tags SEO e derivação de Shorts, com fallback heurístico local quando sem chave de API.
- **Integração YouTube Data API v3:** Sincronização de métricas de inscritos e visualizações, links diretos para o YouTube e validador de metadados pré-upload.
- **Biblioteca com Backup JSON:** Exportação e restauração completa de dados locais em JSON padronizado.

---

## 4. Como Executar os Testes e Compilar o APK

### Executar Testes Unitários e Robolectric:
```bash
gradle :app:testDebugUnitTest
```
A suíte inclui:
- `tvAbertaCatalogContainsOfficialBrazilianBroadcasters`: Valida a integridade do catálogo oficial de emissoras brasileiras.
- `tvAbertaPersistenceAndFavoriteToggleWorks`: Valida persistência em SQLite e alternância de favoritos.
- `marketplacePremiumEnforcementBlocksFreeUserFromPublishing`: Garante no nível de repositório que usuários gratuitos são bloqueados de publicar produtos, enquanto assinantes Premium são autorizados.
- `marketplaceOrderCreationAndPixGenerationWorks`: Valida criação de pedidos e payload Pix Banco Central.
- `subscriptionPixCodeGenerationConformsToPagBankStandard`: Valida conformidade com padrão Pix PagBank.

### Compilar o APK Debug:
```bash
gradle :app:assembleDebug
```
O APK instalável será gerado em: `app/build/outputs/apk/debug/app-debug.apk`.

### Compilar o APK Release Assinado:
```bash
gradle :app:assembleRelease
```
