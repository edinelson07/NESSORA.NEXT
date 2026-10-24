package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AudioEntity
import com.example.data.local.DraftEntity
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.SellerEntity
import com.example.data.local.TvChannelEntity
import com.example.data.model.AIInsight
import com.example.data.model.ChannelInfo
import com.example.data.model.DraftType
import com.example.data.model.VideoItem
import com.example.data.repository.AIResponse
import com.example.data.repository.AdminMetrics
import com.example.data.repository.AdminRepository
import com.example.data.repository.AudioRepository
import com.example.data.repository.CartItem
import com.example.data.repository.DraftRepository
import com.example.data.repository.MarketplaceRepository
import com.example.data.repository.NexoraAIRepository
import com.example.data.repository.SubscriptionRepository
import com.example.data.repository.SubscriptionState
import com.example.data.repository.TvChannelRepository
import com.example.data.repository.UploadValidationResult
import com.example.data.repository.YouTubeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class SubScreen {
    object None : SubScreen()
    object YouTubeManager : SubScreen()
    object AudioCatalog : SubScreen()
    object NexoraPro : SubScreen()
    object AdminDashboard : SubScreen()
    object TermsPrivacy : SubScreen()
    object FutureTv : SubScreen()
    object TvAberta : SubScreen()
    object Marketplace : SubScreen()
    object SellerDashboard : SubScreen()
    object NexoraAi : SubScreen()
    object Library : SubScreen()
}

class NexoraViewModel(
    private val draftRepository: DraftRepository,
    private val audioRepository: AudioRepository,
    private val youtubeRepository: YouTubeRepository,
    private val aiRepository: NexoraAIRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val adminRepository: AdminRepository,
    private val tvChannelRepository: TvChannelRepository,
    private val marketplaceRepository: MarketplaceRepository
) : ViewModel() {

    // Navigation
    private val _currentTab = MutableStateFlow(0) // 0: Home, 1: TvAberta, 2: Studio, 3: Marketplace, 4: Profile
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _currentSubScreen = MutableStateFlow<SubScreen>(SubScreen.None)
    val currentSubScreen: StateFlow<SubScreen> = _currentSubScreen.asStateFlow()

    // Search & Filtering
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilterType = MutableStateFlow<String?>("TODOS")
    val selectedFilterType: StateFlow<String?> = _selectedFilterType.asStateFlow()

    // Drafts
    val allDrafts: StateFlow<List<DraftEntity>> = draftRepository.getAllDrafts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _editingDraft = MutableStateFlow<DraftEntity?>(null)
    val editingDraft: StateFlow<DraftEntity?> = _editingDraft.asStateFlow()

    // Audio
    val allAudioTracks: StateFlow<List<AudioEntity>> = audioRepository.getAllTracks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // YouTube
    val channelInfo: StateFlow<ChannelInfo> = youtubeRepository.channelInfo
    val channelVideos: StateFlow<List<VideoItem>> = youtubeRepository.channelVideos

    // AI
    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _latestAiResponse = MutableStateFlow<AIResponse?>(null)
    val latestAiResponse: StateFlow<AIResponse?> = _latestAiResponse.asStateFlow()

    private val _aiHistory = MutableStateFlow<List<AIResponse>>(emptyList())
    val aiHistory: StateFlow<List<AIResponse>> = _aiHistory.asStateFlow()

    // Subscription & Admin
    val subscription: StateFlow<SubscriptionState> = subscriptionRepository.subscription
    val adminMetrics: StateFlow<AdminMetrics> = adminRepository.metrics

    // TV Aberta (Brazilian Broadcast Channels)
    val allTvChannels: StateFlow<List<TvChannelEntity>> = tvChannelRepository.getAllChannels()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteTvChannels: StateFlow<List<TvChannelEntity>> = tvChannelRepository.getFavoriteChannels()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedTvCategory = MutableStateFlow("TODOS")
    val selectedTvCategory: StateFlow<String> = _selectedTvCategory.asStateFlow()

    private val _activePlayingChannel = MutableStateFlow<TvChannelEntity?>(null)
    val activePlayingChannel: StateFlow<TvChannelEntity?> = _activePlayingChannel.asStateFlow()

    // Marketplace / Loja
    val allProducts: StateFlow<List<ProductEntity>> = marketplaceRepository.getAllPublishedProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteProducts: StateFlow<List<ProductEntity>> = marketplaceRepository.getFavoriteProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sellerOrders: StateFlow<List<OrderEntity>> = marketplaceRepository.getAllOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sellerProfile = MutableStateFlow<SellerEntity?>(null)
    val sellerProfile: StateFlow<SellerEntity?> = _sellerProfile.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    // Feedback
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        viewModelScope.launch {
            draftRepository.ensureDefaultTemplatesIfEmpty()
            audioRepository.ensureDefaultTracksIfEmpty()
            tvChannelRepository.ensureDefaultChannelsIfEmpty()
            marketplaceRepository.ensureDefaultProductsIfEmpty()
            _sellerProfile.value = marketplaceRepository.getDefaultSeller()
        }
    }

    fun selectTab(tabIndex: Int) {
        _currentSubScreen.value = SubScreen.None
        _currentTab.value = tabIndex
    }

    fun navigateToSubScreen(subScreen: SubScreen) {
        _currentSubScreen.value = subScreen
    }

    fun navigateBack() {
        if (_currentSubScreen.value != SubScreen.None) {
            _currentSubScreen.value = SubScreen.None
        } else if (_currentTab.value != 0) {
            _currentTab.value = 0
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterType(type: String?) {
        _selectedFilterType.value = type
    }

    fun startNewDraft(type: DraftType = DraftType.LONG_VIDEO) {
        _editingDraft.value = DraftEntity(
            id = 0,
            title = "",
            type = type.name,
            topic = "",
            targetAudience = "Criadores e público geral",
            tone = "Informativo e Dinâmico",
            targetDuration = if (type == DraftType.SHORTS) "45s" else "10 min",
            hook = "",
            scriptBody = "",
            description = "",
            tags = "",
            thumbnailTitle = "",
            thumbnailSubtitle = "",
            thumbnailBgHex = "#1C0D20",
            thumbnailAccentHex = "#C77DFF",
            status = "RASCUNHO"
        )
        _currentTab.value = 2 // Switch to Studio
    }

    fun openDraftForEditing(draft: DraftEntity) {
        _editingDraft.value = draft
        _currentTab.value = 2 // Switch to Studio
    }

    fun updateEditingDraft(updater: (DraftEntity) -> DraftEntity) {
        _editingDraft.value = _editingDraft.value?.let(updater)
    }

    fun saveCurrentDraft() {
        val current = _editingDraft.value ?: return
        if (current.title.isBlank() && current.topic.isBlank()) {
            _toastMessage.value = "Por favor, digite pelo menos um título ou tema."
            return
        }
        val finalizedTitle = current.title.ifBlank { "Rascunho: ${current.topic}" }
        val toSave = current.copy(title = finalizedTitle)
        viewModelScope.launch {
            val id = draftRepository.saveDraft(toSave)
            _editingDraft.value = toSave.copy(id = id)
            adminRepository.addAuditLog("Rascunho salvo: $finalizedTitle", "SUCESSO")
            _toastMessage.value = "Rascunho salvo com sucesso na memória persistente!"
        }
    }

    fun duplicateDraft(draft: DraftEntity) {
        viewModelScope.launch {
            draftRepository.duplicateDraft(draft)
            _toastMessage.value = "Rascunho duplicado!"
        }
    }

    fun deleteDraft(draft: DraftEntity) {
        viewModelScope.launch {
            draftRepository.deleteDraft(draft)
            if (_editingDraft.value?.id == draft.id) {
                _editingDraft.value = null
            }
            _toastMessage.value = "Rascunho excluído."
        }
    }

    fun generateWithAi(taskType: String, theme: String, audience: String, tone: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            try {
                val response = aiRepository.generateContent(taskType, theme, audience, tone)
                _latestAiResponse.value = response
                _aiHistory.value = listOf(response) + _aiHistory.value
                adminRepository.addAuditLog("Geração de IA: $taskType", if (response.isOnlineAi) "GEMINI" else "LOCAL")
            } catch (e: Exception) {
                _toastMessage.value = "Erro na geração: ${e.message}"
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun applyAiResponseToDraft(response: AIResponse) {
        val current = _editingDraft.value ?: DraftEntity(
            id = 0,
            title = response.title,
            type = DraftType.LONG_VIDEO.name,
            topic = response.title,
            scriptBody = response.content
        )
        _editingDraft.value = current.copy(
            title = if (current.title.isBlank()) response.title else current.title,
            scriptBody = if (current.scriptBody.isBlank()) response.content else current.scriptBody + "\n\n" + response.content,
            tags = if (current.tags.isBlank() && response.tags.isNotEmpty()) response.tags.joinToString(", ") else current.tags
        )
        _currentTab.value = 2
        _toastMessage.value = "Conteúdo inserido no Estúdio de Criação!"
    }

    fun saveAiResponseAsNewDraft(response: AIResponse) {
        viewModelScope.launch {
            val newDraft = DraftEntity(
                id = 0,
                title = response.title,
                type = if (response.title.contains("Shorts", ignoreCase = true)) DraftType.SHORTS.name else DraftType.LONG_VIDEO.name,
                topic = response.title,
                scriptBody = response.content,
                tags = response.tags.joinToString(", "),
                status = "RASCUNHO"
            )
            val id = draftRepository.saveDraft(newDraft)
            adminRepository.addAuditLog("Rascunho criado via IA: ${response.title}", "SUCESSO")
            _toastMessage.value = "Salvo na Biblioteca como rascunho #$id!"
        }
    }

    fun syncChannelStats() {
        youtubeRepository.syncStats()
        adminRepository.addAuditLog("Sincronização manual com YouTube", "SUCESSO")
        _toastMessage.value = "Métricas do canal sincronizadas com o YouTube!"
    }

    fun showToast(message: String) {
        _toastMessage.value = message
    }

    fun exportBackupJson(onResult: (String) -> Unit) {
        viewModelScope.launch {
            val json = draftRepository.exportToJson()
            onResult(json)
        }
    }

    fun importBackupJson(jsonString: String, onDone: (Result<Int>) -> Unit) {
        viewModelScope.launch {
            val result = draftRepository.importFromJson(jsonString)
            if (result.isSuccess) {
                _toastMessage.value = "Backup restaurado: ${result.getOrNull()} rascunhos adicionados!"
            } else {
                _toastMessage.value = "Falha ao importar backup: ${result.exceptionOrNull()?.message}"
            }
            onDone(result)
        }
    }

    fun validateUploadMetadata(title: String, description: String, tags: String): UploadValidationResult {
        return youtubeRepository.validateUploadMetadata(title, description, tags)
    }

    fun connectMockChannel(name: String, handle: String) {
        youtubeRepository.connectMockAuthorizedChannel(name, handle)
        _toastMessage.value = "Canal conectado com sucesso via OAuth 2.0!"
    }

    fun disconnectChannel() {
        youtubeRepository.disconnectChannel()
        _toastMessage.value = "Canal desconectado."
    }

    fun addAudioTrack(title: String, artist: String, license: String, category: String, notes: String) {
        viewModelScope.launch {
            audioRepository.addTrack(
                AudioEntity(
                    title = title,
                    artist = artist,
                    licenseType = license,
                    category = category,
                    notes = notes
                )
            )
            _toastMessage.value = "Faixa de áudio cadastrada na biblioteca!"
        }
    }

    fun generatePixCode(isAnnual: Boolean): String {
        return subscriptionRepository.generatePixPayment(isAnnual)
    }

    fun confirmProDev() {
        subscriptionRepository.confirmPaymentDevSimulation()
        _toastMessage.value = "NEXORA Pro ativado com sucesso! Benefícios comerciais liberados."
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // ==========================================
    // TV ABERTA METHODS
    // ==========================================

    fun selectTvCategory(category: String) {
        _selectedTvCategory.value = category
    }

    fun playTvChannel(channel: TvChannelEntity) {
        _activePlayingChannel.value = channel
    }

    fun closeTvPlayer() {
        _activePlayingChannel.value = null
    }

    fun toggleTvFavorite(channel: TvChannelEntity) {
        viewModelScope.launch {
            tvChannelRepository.toggleFavorite(channel.id, channel.isFavorite)
            val action = if (channel.isFavorite) "removido dos" else "adicionado aos"
            _toastMessage.value = "${channel.name} $action favoritos!"
        }
    }

    fun refreshTvChannels() {
        viewModelScope.launch {
            tvChannelRepository.resetToOfficialCatalog()
            _toastMessage.value = "Catálogo de TV Aberta sincronizado com emissoras oficiais!"
        }
    }

    fun addCustomTvChannel(channel: TvChannelEntity) {
        viewModelScope.launch {
            tvChannelRepository.addOrUpdateChannel(channel)
            _toastMessage.value = "Canal ${channel.name} salvo no catálogo!"
        }
    }

    // ==========================================
    // MARKETPLACE / LOJA METHODS
    // ==========================================

    fun toggleProductFavorite(product: ProductEntity) {
        viewModelScope.launch {
            marketplaceRepository.toggleFavoriteProduct(product.id, product.isFavorite)
            val action = if (product.isFavorite) "removido dos" else "adicionado aos"
            _toastMessage.value = "${product.title} $action favoritos!"
        }
    }

    fun addToCart(product: ProductEntity) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(product = product, quantity = 1))
        }
        _cartItems.value = current
        _toastMessage.value = "${product.title} adicionado ao carrinho!"
    }

    fun removeFromCart(productId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.product.id == productId }
    }

    fun updateCartItemQuantity(productId: String, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val item = current[index]
            val newQty = item.quantity + delta
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = item.copy(quantity = newQty)
            }
            _cartItems.value = current
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun checkoutProduct(
        product: ProductEntity,
        buyerName: String,
        buyerContact: String,
        quantity: Int,
        onOrderCreated: (OrderEntity) -> Unit
    ) {
        viewModelScope.launch {
            val order = marketplaceRepository.createOrder(product, buyerName, buyerContact, quantity)
            _toastMessage.value = "Pedido ${order.id} gerado! Conclua o Pix para envio."
            onOrderCreated(order)
        }
    }

    fun confirmOrderPixPayment(orderId: String) {
        viewModelScope.launch {
            marketplaceRepository.updateOrderStatus(orderId, "PAGO_PIX")
            _toastMessage.value = "Pagamento Pix aprovado! Vendedor notificado para expedição."
        }
    }

    fun saveSellerProduct(
        product: ProductEntity,
        onComplete: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            val isPremium = subscription.value.isPro
            val result = marketplaceRepository.saveProduct(product, isPremium)
            if (result.isSuccess) {
                _toastMessage.value = "Produto ${product.title} salvo com sucesso na loja!"
            } else {
                _toastMessage.value = result.exceptionOrNull()?.message ?: "Falha ao salvar produto."
            }
            onComplete(result)
        }
    }

    fun toggleProductPublishStatus(
        product: ProductEntity,
        targetPublished: Boolean,
        onComplete: (Result<Unit>) -> Unit
    ) {
        viewModelScope.launch {
            val isPremium = subscription.value.isPro
            val result = marketplaceRepository.toggleProductPublishStatus(product.id, targetPublished, isPremium)
            if (result.isSuccess) {
                val statusStr = if (targetPublished) "publicado na vitrine" else "pausado"
                _toastMessage.value = "${product.title} $statusStr!"
            } else {
                _toastMessage.value = result.exceptionOrNull()?.message ?: "Falha ao alterar status."
            }
            onComplete(result)
        }
    }

    fun deleteSellerProduct(product: ProductEntity) {
        viewModelScope.launch {
            marketplaceRepository.deleteProduct(product)
            _toastMessage.value = "Produto excluído do catálogo."
        }
    }

    fun updateSellerProfile(storeName: String, ownerName: String, desc: String, phone: String, email: String) {
        viewModelScope.launch {
            val current = _sellerProfile.value ?: marketplaceRepository.getDefaultSeller()
            val updated = current.copy(
                storeName = storeName.ifBlank { current.storeName },
                ownerName = ownerName.ifBlank { current.ownerName },
                description = desc.ifBlank { current.description },
                whatsappContact = phone.ifBlank { current.whatsappContact },
                emailContact = email.ifBlank { current.emailContact }
            )
            marketplaceRepository.updateSellerProfile(updated)
            _sellerProfile.value = updated
            _toastMessage.value = "Perfil da loja comercial atualizado!"
        }
    }
}
