package com.example.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DraftType
import com.example.ui.components.NexoraBottomBar
import com.example.ui.components.NexoraHeader
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.ai.NexoraAiScreen
import com.example.ui.screens.audio.AudioCatalogScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.library.LibraryScreen
import com.example.ui.screens.marketplace.MarketplaceScreen
import com.example.ui.screens.pro.NexoraProScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.profile.TermsPrivacyScreen
import com.example.ui.screens.studio.StudioScreen
import com.example.ui.screens.tv.TvAbertaScreen
import com.example.ui.screens.youtube.YouTubeManagerScreen
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraLilacNeon
import com.example.ui.theme.NexoraMagenta
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceVariant
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary
import com.example.ui.viewmodel.NexoraViewModel
import com.example.ui.viewmodel.SubScreen

@Composable
fun NexoraApp(
    viewModel: NexoraViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val subScreen by viewModel.currentSubScreen.collectAsStateWithLifecycle()

    val channelInfo by viewModel.channelInfo.collectAsStateWithLifecycle()
    val channelVideos by viewModel.channelVideos.collectAsStateWithLifecycle()
    val drafts by viewModel.allDrafts.collectAsStateWithLifecycle()
    val editingDraft by viewModel.editingDraft.collectAsStateWithLifecycle()
    val audioTracks by viewModel.allAudioTracks.collectAsStateWithLifecycle()
    val subscription by viewModel.subscription.collectAsStateWithLifecycle()
    val adminMetrics by viewModel.adminMetrics.collectAsStateWithLifecycle()
    val isAiLoading by viewModel.isAiLoading.collectAsStateWithLifecycle()
    val latestAiResponse by viewModel.latestAiResponse.collectAsStateWithLifecycle()
    val aiHistory by viewModel.aiHistory.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    // TV Aberta and Marketplace State
    val tvChannels by viewModel.allTvChannels.collectAsStateWithLifecycle()
    val activePlayingChannel by viewModel.activePlayingChannel.collectAsStateWithLifecycle()
    val products by viewModel.allProducts.collectAsStateWithLifecycle()
    val sellerProfile by viewModel.sellerProfile.collectAsStateWithLifecycle()
    val sellerOrders by viewModel.sellerOrders.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var globalSearchQuery by remember { mutableStateOf("") }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = NexoraBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (subScreen == SubScreen.None) {
                NexoraHeader(
                    channelInfo = channelInfo,
                    isPro = subscription.isPro,
                    onSearchClick = { showSearchDialog = true },
                    onNotificationsClick = { showNotificationsDialog = true },
                    onProClick = { viewModel.navigateToSubScreen(SubScreen.NexoraPro) },
                    onProfileClick = { viewModel.selectTab(4) },
                    onChannelClick = { viewModel.navigateToSubScreen(SubScreen.YouTubeManager) },
                    modifier = Modifier.statusBarsPadding()
                )
            }
        },
        bottomBar = {
            if (subScreen == SubScreen.None) {
                NexoraBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        if (tab == 2 && editingDraft == null) {
                            viewModel.startNewDraft(DraftType.LONG_VIDEO)
                        } else {
                            viewModel.selectTab(tab)
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (subScreen) {
                SubScreen.YouTubeManager -> {
                    YouTubeManagerScreen(
                        channelInfo = channelInfo,
                        videos = channelVideos,
                        onConnectChannel = { name, handle ->
                            viewModel.connectMockChannel(name, handle)
                        },
                        onDisconnectChannel = {
                            viewModel.disconnectChannel()
                        },
                        onSyncStats = {
                            viewModel.syncChannelStats()
                        },
                        onValidateUpload = { title, desc, tags ->
                            viewModel.validateUploadMetadata(title, desc, tags)
                        },
                        onShowToast = { viewModel.showToast(it) },
                        onBack = { viewModel.navigateBack() }
                    )
                }
                SubScreen.AudioCatalog -> {
                    AudioCatalogScreen(
                        tracks = audioTracks,
                        onAddTrack = { title, artist, license, category, notes ->
                            viewModel.addAudioTrack(title, artist, license, category, notes)
                        },
                        onShowToast = { viewModel.showToast(it) },
                        onBack = { viewModel.navigateBack() }
                    )
                }
                SubScreen.NexoraPro -> {
                    NexoraProScreen(
                        subscription = subscription,
                        onGeneratePix = { isAnnual ->
                            viewModel.generatePixCode(isAnnual)
                        },
                        onConfirmDev = {
                            viewModel.confirmProDev()
                        },
                        onShowToast = { viewModel.showToast(it) },
                        onBack = { viewModel.navigateBack() }
                    )
                }
                SubScreen.AdminDashboard -> {
                    AdminDashboardScreen(
                        metrics = adminMetrics,
                        onTestLog = {
                            viewModel.showToast("Novo log de auditoria registrado no sistema!")
                        },
                        onShowToast = { viewModel.showToast(it) },
                        onBack = { viewModel.navigateBack() }
                    )
                }
                SubScreen.TermsPrivacy -> {
                    TermsPrivacyScreen(
                        onBack = { viewModel.navigateBack() }
                    )
                }
                SubScreen.TvAberta, SubScreen.FutureTv -> {
                    TvAbertaScreen(
                        channels = tvChannels,
                        activePlayingChannel = activePlayingChannel,
                        onPlayChannel = { viewModel.playTvChannel(it) },
                        onClosePlayer = { viewModel.closeTvPlayer() },
                        onToggleFavorite = { viewModel.toggleTvFavorite(it) },
                        onRefreshCatalog = { viewModel.refreshTvChannels() },
                        onBack = { viewModel.navigateBack() }
                    )
                }
                SubScreen.Marketplace, SubScreen.SellerDashboard -> {
                    MarketplaceScreen(
                        products = products,
                        sellerProfile = sellerProfile,
                        sellerOrders = sellerOrders,
                        cartItems = cartItems,
                        subscription = subscription,
                        onAddToCart = { viewModel.addToCart(it) },
                        onUpdateCartQuantity = { id, delta -> viewModel.updateCartItemQuantity(id, delta) },
                        onRemoveFromCart = { viewModel.removeFromCart(it) },
                        onClearCart = { viewModel.clearCart() },
                        onCheckoutProduct = { prod, name, contact, qty, onDone ->
                            viewModel.checkoutProduct(prod, name, contact, qty, onDone)
                        },
                        onConfirmOrderPix = { viewModel.confirmOrderPixPayment(it) },
                        onSaveProduct = { prod, onDone -> viewModel.saveSellerProduct(prod, onDone) },
                        onTogglePublishStatus = { prod, target, onDone -> viewModel.toggleProductPublishStatus(prod, target, onDone) },
                        onDeleteProduct = { viewModel.deleteSellerProduct(it) },
                        onToggleProductFavorite = { viewModel.toggleProductFavorite(it) },
                        onUpdateSellerProfile = { sName, oName, desc, phone, email ->
                            viewModel.updateSellerProfile(sName, oName, desc, phone, email)
                        },
                        onNavigateToPro = { viewModel.navigateToSubScreen(SubScreen.NexoraPro) },
                        onBack = { viewModel.navigateBack() }
                    )
                }
                SubScreen.NexoraAi -> {
                    NexoraAiScreen(
                        isLoading = isAiLoading,
                        latestResponse = latestAiResponse,
                        history = aiHistory,
                        onGenerate = { task, theme, audience, tone ->
                            viewModel.generateWithAi(task, theme, audience, tone)
                        },
                        onSendToStudio = { resp ->
                            viewModel.applyAiResponseToDraft(resp)
                        },
                        onSaveAsDraft = { resp ->
                            viewModel.saveAiResponseAsNewDraft(resp)
                        },
                        onShowToast = { viewModel.showToast(it) }
                    )
                }
                SubScreen.Library -> {
                    LibraryScreen(
                        drafts = drafts,
                        onOpenDraft = { draft -> viewModel.openDraftForEditing(draft) },
                        onNewDraft = { viewModel.startNewDraft(DraftType.LONG_VIDEO) },
                        onDuplicateDraft = { draft -> viewModel.duplicateDraft(draft) },
                        onDeleteDraft = { draft -> viewModel.deleteDraft(draft) },
                        onExportBackup = { onResult ->
                            viewModel.exportBackupJson(onResult)
                        },
                        onImportBackup = { json ->
                            viewModel.importBackupJson(json) {}
                        },
                        onShowToast = { viewModel.showToast(it) }
                    )
                }
                SubScreen.None -> {
                    when (currentTab) {
                        0 -> HomeScreen(
                            channelInfo = channelInfo,
                            recentVideos = channelVideos,
                            recentDrafts = drafts,
                            onOpenStudio = { viewModel.startNewDraft(DraftType.LONG_VIDEO) },
                            onOpenStudioWithTopic = { topic, type ->
                                viewModel.startNewDraft(type)
                                viewModel.updateEditingDraft { it.copy(topic = topic, title = topic) }
                            },
                            onOpenDraft = { draft -> viewModel.openDraftForEditing(draft) },
                            onDuplicateDraft = { draft -> viewModel.duplicateDraft(draft) },
                            onOpenAi = { viewModel.navigateToSubScreen(SubScreen.NexoraAi) },
                            onOpenYouTubeManager = { viewModel.navigateToSubScreen(SubScreen.YouTubeManager) },
                            onOpenAudioCatalog = { viewModel.navigateToSubScreen(SubScreen.AudioCatalog) },
                            onSyncStats = { viewModel.syncChannelStats() },
                            onShowToast = { viewModel.showToast(it) },
                            onOpenTvAberta = { viewModel.selectTab(1) },
                            onOpenMarketplace = { viewModel.selectTab(3) }
                        )
                        1 -> TvAbertaScreen(
                            channels = tvChannels,
                            activePlayingChannel = activePlayingChannel,
                            onPlayChannel = { viewModel.playTvChannel(it) },
                            onClosePlayer = { viewModel.closeTvPlayer() },
                            onToggleFavorite = { viewModel.toggleTvFavorite(it) },
                            onRefreshCatalog = { viewModel.refreshTvChannels() },
                            onBack = { viewModel.selectTab(0) }
                        )
                        2 -> StudioScreen(
                            currentDraft = editingDraft,
                            onUpdateDraft = { updater -> viewModel.updateEditingDraft(updater) },
                            onSaveDraft = { viewModel.saveCurrentDraft() },
                            onOpenAiWithTheme = { theme ->
                                viewModel.generateWithAi("IDEAS", theme, "Criadores", "Informativo")
                                viewModel.navigateToSubScreen(SubScreen.NexoraAi)
                            },
                            onShowToast = { viewModel.showToast(it) }
                        )
                        3 -> MarketplaceScreen(
                            products = products,
                            sellerProfile = sellerProfile,
                            sellerOrders = sellerOrders,
                            cartItems = cartItems,
                            subscription = subscription,
                            onAddToCart = { viewModel.addToCart(it) },
                            onUpdateCartQuantity = { id, delta -> viewModel.updateCartItemQuantity(id, delta) },
                            onRemoveFromCart = { viewModel.removeFromCart(it) },
                            onClearCart = { viewModel.clearCart() },
                            onCheckoutProduct = { prod, name, contact, qty, onDone ->
                                viewModel.checkoutProduct(prod, name, contact, qty, onDone)
                            },
                            onConfirmOrderPix = { viewModel.confirmOrderPixPayment(it) },
                            onSaveProduct = { prod, onDone -> viewModel.saveSellerProduct(prod, onDone) },
                            onTogglePublishStatus = { prod, target, onDone -> viewModel.toggleProductPublishStatus(prod, target, onDone) },
                            onDeleteProduct = { viewModel.deleteSellerProduct(it) },
                            onToggleProductFavorite = { viewModel.toggleProductFavorite(it) },
                            onUpdateSellerProfile = { sName, oName, desc, phone, email ->
                                viewModel.updateSellerProfile(sName, oName, desc, phone, email)
                            },
                            onNavigateToPro = { viewModel.navigateToSubScreen(SubScreen.NexoraPro) },
                            onBack = { viewModel.selectTab(0) }
                        )
                        4 -> ProfileScreen(
                            channelInfo = channelInfo,
                            subscription = subscription,
                            onOpenYouTubeManager = { viewModel.navigateToSubScreen(SubScreen.YouTubeManager) },
                            onOpenAudioCatalog = { viewModel.navigateToSubScreen(SubScreen.AudioCatalog) },
                            onOpenPro = { viewModel.navigateToSubScreen(SubScreen.NexoraPro) },
                            onOpenAdmin = { viewModel.navigateToSubScreen(SubScreen.AdminDashboard) },
                            onOpenTerms = { viewModel.navigateToSubScreen(SubScreen.TermsPrivacy) },
                            onOpenFutureTv = { viewModel.selectTab(1) },
                            onOpenMarketplace = { viewModel.selectTab(3) },
                            onOpenLibrary = { viewModel.navigateToSubScreen(SubScreen.Library) },
                            onOpenAi = { viewModel.navigateToSubScreen(SubScreen.NexoraAi) },
                            onShowToast = { viewModel.showToast(it) }
                        )
                    }
                }
            }
        }
    }

    // GLOBAL SEARCH DIALOG
    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = { showSearchDialog = false },
            title = {
                Text("Busca Global NEXORA", fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = globalSearchQuery,
                        onValueChange = { globalSearchQuery = it },
                        placeholder = { Text("Buscar em roteiros, títulos, tags...") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NexoraLilacNeon,
                            unfocusedBorderColor = NexoraSurfaceBorder,
                            focusedTextColor = NexoraTextPrimary,
                            unfocusedTextColor = NexoraTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "A pesquisa abrange toda a sua biblioteca local de rascunhos salvos, TV Aberta e produtos da Loja.",
                        fontSize = 11.sp,
                        color = NexoraTextMuted
                    )
                }
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        viewModel.setSearchQuery(globalSearchQuery)
                        viewModel.navigateToSubScreen(SubScreen.Library)
                        showSearchDialog = false
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraLilacNeon,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Ver Resultados na Biblioteca", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSearchDialog = false }) {
                    Text("Cancelar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }

    // NOTIFICATIONS DIALOG
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = NexoraMagenta,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Central de Notificações", fontWeight = FontWeight.Bold, color = NexoraTextPrimary)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NexoraSurfaceVariant)
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF36E58D), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Sinal de TV Aberta Ativo", color = NexoraTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("14 emissoras oficiais brasileiras sincronizadas no guia.", color = NexoraTextMuted, fontSize = 11.sp)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(NexoraSurfaceVariant)
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = NexoraLilacNeon, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Marketplace Disponível", color = NexoraTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Venda e compre equipamentos de criadores com Pix PagBank.", color = NexoraTextMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("Entendido", color = NexoraLilacNeon, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }
}
