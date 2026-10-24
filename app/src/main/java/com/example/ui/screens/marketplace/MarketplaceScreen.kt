package com.example.ui.screens.marketplace

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OrderEntity
import com.example.data.local.ProductEntity
import com.example.data.local.SellerEntity
import com.example.data.repository.CartItem
import com.example.data.repository.SubscriptionState
import com.example.ui.theme.NexoraBackground
import com.example.ui.theme.NexoraBrandGradient
import com.example.ui.theme.NexoraCardElevated
import com.example.ui.theme.NexoraLilacNeon
import com.example.ui.theme.NexoraMagenta
import com.example.ui.theme.NexoraPurpleElectric
import com.example.ui.theme.NexoraSurface
import com.example.ui.theme.NexoraSurfaceBorder
import com.example.ui.theme.NexoraSurfaceCard
import com.example.ui.theme.NexoraSurfaceDark
import com.example.ui.theme.NexoraTextMuted
import com.example.ui.theme.NexoraTextPrimary
import com.example.ui.theme.NexoraTextSecondary
import com.example.ui.theme.NexoraWineDark

@Composable
fun MarketplaceScreen(
    products: List<ProductEntity>,
    sellerProfile: SellerEntity?,
    sellerOrders: List<OrderEntity>,
    cartItems: List<CartItem>,
    subscription: SubscriptionState,
    onAddToCart: (ProductEntity) -> Unit,
    onUpdateCartQuantity: (String, Int) -> Unit,
    onRemoveFromCart: (String) -> Unit,
    onClearCart: () -> Unit,
    onCheckoutProduct: (ProductEntity, String, String, Int, (OrderEntity) -> Unit) -> Unit,
    onConfirmOrderPix: (String) -> Unit,
    onSaveProduct: (ProductEntity, (Result<Unit>) -> Unit) -> Unit,
    onTogglePublishStatus: (ProductEntity, Boolean, (Result<Unit>) -> Unit) -> Unit,
    onDeleteProduct: (ProductEntity) -> Unit,
    onToggleProductFavorite: (ProductEntity) -> Unit,
    onUpdateSellerProfile: (String, String, String, String, String) -> Unit,
    onNavigateToPro: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("TODOS") }
    var showOnlyFavorites by remember { mutableStateOf(false) }

    var selectedProductForDetail by remember { mutableStateOf<ProductEntity?>(null) }
    var showCartDialog by remember { mutableStateOf(false) }
    var showSellerDashboard by remember { mutableStateOf(false) }
    var showCheckoutDialog by remember { mutableStateOf(false) }
    var productForDirectCheckout by remember { mutableStateOf<ProductEntity?>(null) }
    var generatedOrder by remember { mutableStateOf<OrderEntity?>(null) }

    val categories = listOf(
        "TODOS",
        "Equipamentos",
        "Iluminação & Áudio",
        "Presets & LUTs",
        "Cursos & Ebooks",
        "Serviços Criativos",
        "Merch & Vestuário"
    )

    val filteredProducts = products.filter { product ->
        val matchesCategory = selectedCategory == "TODOS" || product.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                product.title.contains(searchQuery, ignoreCase = true) ||
                product.description.contains(searchQuery, ignoreCase = true) ||
                product.sellerName.contains(searchQuery, ignoreCase = true)
        val matchesFav = !showOnlyFavorites || product.isFavorite

        matchesCategory && matchesSearch && matchesFav
    }

    val totalCartCount = cartItems.sumOf { it.quantity }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NexoraBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Header
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NexoraSurfaceCard)
                            .border(1.dp, NexoraSurfaceBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = NexoraLilacNeon,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Loja NEXORA",
                                color = NexoraTextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NexoraWineDark)
                                    .border(1.dp, NexoraMagenta, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "MARKETPLACE",
                                    color = NexoraLilacNeon,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Produtos, Equipamentos & Ativos Digitais para Criadores",
                            color = NexoraTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Seller Dashboard Button
                    IconButton(
                        onClick = { showSellerDashboard = true },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NexoraSurfaceCard)
                            .border(1.dp, NexoraSurfaceBorder, CircleShape)
                            .testTag("btn_open_seller_dashboard")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "Painel do Vendedor",
                            tint = NexoraLilacNeon,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Cart Button with badge
                    Box {
                        IconButton(
                            onClick = { showCartDialog = true },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(NexoraSurfaceCard)
                                .border(1.dp, NexoraSurfaceBorder, CircleShape)
                                .testTag("btn_open_cart")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrinho de Compras",
                                tint = NexoraMagenta,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        if (totalCartCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(NexoraMagenta),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = totalCartCount.toString(),
                                    color = NexoraTextPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Seller Callout Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(NexoraWineDark, NexoraCardElevated)
                        )
                    )
                    .border(1.dp, NexoraMagenta.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                    .clickable { showSellerDashboard = true }
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Storefront, contentDescription = null, tint = NexoraLilacNeon, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Quer vender no Marketplace?",
                                color = NexoraTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (subscription.isPro) "Seu plano Pro está ativo! Cadastre e gerencie seus produtos." else "Venda equipamentos e predefinições com o plano NEXORA Pro.",
                            color = NexoraTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    ElevatedButton(
                        onClick = { showSellerDashboard = true },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraLilacNeon,
                            contentColor = NexoraBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (subscription.isPro) "Painel" else "Cadastrar",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Search and Favorites Filter
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text("Buscar produtos, microfones, presets...", color = NexoraTextMuted, fontSize = 13.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = NexoraLilacNeon)
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = NexoraTextPrimary,
                        unfocusedTextColor = NexoraTextPrimary,
                        focusedBorderColor = NexoraLilacNeon,
                        unfocusedBorderColor = NexoraSurfaceBorder,
                        focusedContainerColor = NexoraSurfaceCard,
                        unfocusedContainerColor = NexoraSurfaceCard
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("marketplace_search_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { showOnlyFavorites = !showOnlyFavorites },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (showOnlyFavorites) NexoraWineDark else NexoraSurfaceCard)
                        .border(
                            width = 1.dp,
                            color = if (showOnlyFavorites) NexoraMagenta else NexoraSurfaceBorder,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Icon(
                        imageVector = if (showOnlyFavorites) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favoritos",
                        tint = if (showOnlyFavorites) NexoraLilacNeon else NexoraTextSecondary
                    )
                }
            }
        }

        // Category Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) NexoraBrandGradient else Brush.linearGradient(listOf(NexoraSurfaceCard, NexoraSurfaceCard)))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) NexoraLilacNeon else NexoraSurfaceBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) NexoraTextPrimary else NexoraTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Product Count
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredProducts.size} produtos listados",
                    color = NexoraTextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Pagamento Seguro via Pix PagBank",
                    color = NexoraLilacNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Products Grid / Cards
        if (filteredProducts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = NexoraTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Nenhum produto encontrado",
                            color = NexoraTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tente outra busca ou limpe os filtros de categoria.",
                            color = NexoraTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(filteredProducts, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { onAddToCart(product) },
                    onDirectCheckout = {
                        productForDirectCheckout = product
                        showCheckoutDialog = true
                    },
                    onOpenDetail = { selectedProductForDetail = product },
                    onToggleFavorite = { onToggleProductFavorite(product) }
                )
            }
        }
    }

    // Product Detail Dialog
    if (selectedProductForDetail != null) {
        val prod = selectedProductForDetail!!
        AlertDialog(
            onDismissRequest = { selectedProductForDetail = null },
            title = {
                Text(text = prod.title, color = NexoraTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(NexoraWineDark, NexoraCardElevated)
                                )
                            )
                            .border(1.dp, NexoraMagenta, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = NexoraLilacNeon, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(prod.category, color = NexoraTextSecondary, fontSize = 12.sp)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "R$ ${String.format(java.util.Locale.GERMANY, "%.2f", prod.price)}",
                            color = NexoraLilacNeon,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexoraSurfaceDark)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (prod.isDigital) "PRODUTO DIGITAL" else "ESTOQUE: ${prod.stock} UN",
                                color = NexoraMagenta,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(text = prod.description, color = NexoraTextPrimary, fontSize = 13.sp, lineHeight = 18.sp)
                    Text(text = "Vendedor: ${prod.sellerName}", color = NexoraTextSecondary, fontSize = 12.sp)
                    Text(text = "Envio: ${prod.deliveryInfo}", color = NexoraTextMuted, fontSize = 11.sp)
                }
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        onAddToCart(prod)
                        selectedProductForDetail = null
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraLilacNeon,
                        contentColor = NexoraBackground
                    )
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Adicionar ao Carrinho", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedProductForDetail = null }) {
                    Text("Fechar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }

    // Cart Dialog
    if (showCartDialog) {
        val subtotal = cartItems.sumOf { it.product.price * it.quantity }
        AlertDialog(
            onDismissRequest = { showCartDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = NexoraMagenta)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Seu Carrinho de Compras", color = NexoraTextPrimary, fontSize = 17.sp)
                }
            },
            text = {
                if (cartItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                        Text("Seu carrinho está vazio.", color = NexoraTextSecondary, fontSize = 13.sp)
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        cartItems.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NexoraSurfaceDark)
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.product.title,
                                        color = NexoraTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "R$ ${String.format(java.util.Locale.GERMANY, "%.2f", item.product.price)} un",
                                        color = NexoraLilacNeon,
                                        fontSize = 11.sp
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onUpdateCartQuantity(item.product.id, -1) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Text("-", color = NexoraTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Text(
                                        text = "${item.quantity}",
                                        color = NexoraTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    IconButton(
                                        onClick = { onUpdateCartQuantity(item.product.id, 1) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Text("+", color = NexoraTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                    IconButton(
                                        onClick = { onRemoveFromCart(item.product.id) },
                                        modifier = Modifier.size(26.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = NexoraTextMuted, modifier = Modifier.size(15.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total do Pedido:", color = NexoraTextPrimary, fontWeight = FontWeight.Bold)
                            Text(
                                text = "R$ ${String.format(java.util.Locale.GERMANY, "%.2f", subtotal)}",
                                color = NexoraLilacNeon,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (cartItems.isNotEmpty()) {
                    ElevatedButton(
                        onClick = {
                            val first = cartItems.first().product
                            val totalQty = cartItems.first().quantity
                            productForDirectCheckout = first
                            showCartDialog = false
                            showCheckoutDialog = true
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraLilacNeon,
                            contentColor = NexoraBackground
                        )
                    ) {
                        Text("Finalizar com Pix", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showCartDialog = false }) {
                    Text("Continuar Comprando", color = NexoraTextSecondary)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }

    // Direct Checkout with Pix Dialog
    if (showCheckoutDialog && productForDirectCheckout != null) {
        val prod = productForDirectCheckout!!
        var buyerName by remember { mutableStateOf("") }
        var buyerContact by remember { mutableStateOf("") }
        var isOrderGenerated by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = {
                showCheckoutDialog = false
                generatedOrder = null
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.QrCode, contentDescription = null, tint = NexoraLilacNeon)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isOrderGenerated) "Pagamento Pix Banco Central" else "Checkout Seguro", color = NexoraTextPrimary)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (!isOrderGenerated) {
                        Text(text = "Item: ${prod.title}", color = NexoraTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "Valor: R$ ${String.format(java.util.Locale.GERMANY, "%.2f", prod.price)}",
                            color = NexoraLilacNeon,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Vendedor: ${prod.sellerName}", color = NexoraTextSecondary, fontSize = 12.sp)

                        OutlinedTextField(
                            value = buyerName,
                            onValueChange = { buyerName = it },
                            label = { Text("Nome do Comprador", color = NexoraTextMuted, fontSize = 12.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NexoraTextPrimary,
                                unfocusedTextColor = NexoraTextPrimary,
                                focusedBorderColor = NexoraLilacNeon,
                                unfocusedBorderColor = NexoraSurfaceBorder,
                                focusedContainerColor = NexoraSurfaceDark,
                                unfocusedContainerColor = NexoraSurfaceDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = buyerContact,
                            onValueChange = { buyerContact = it },
                            label = { Text("WhatsApp ou E-mail para Envio", color = NexoraTextMuted, fontSize = 12.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NexoraTextPrimary,
                                unfocusedTextColor = NexoraTextPrimary,
                                focusedBorderColor = NexoraLilacNeon,
                                unfocusedBorderColor = NexoraSurfaceBorder,
                                focusedContainerColor = NexoraSurfaceDark,
                                unfocusedContainerColor = NexoraSurfaceDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else if (generatedOrder != null) {
                        val order = generatedOrder!!
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(NexoraSurfaceDark)
                                .border(1.dp, NexoraMagenta, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.QrCode, contentDescription = null, tint = NexoraLilacNeon, modifier = Modifier.size(64.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "R$ ${String.format(java.util.Locale.GERMANY, "%.2f", order.amount)}",
                                    color = NexoraLilacNeon,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Código do Pedido: ${order.id}",
                                    color = NexoraTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        ElevatedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Código Pix Nexora", order.pixCode)
                                clipboard.setPrimaryClip(clip)
                            },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = NexoraCardElevated,
                                contentColor = NexoraLilacNeon
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copiar Código Pix Copia-e-Cola")
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(NexoraWineDark.copy(alpha = 0.5f))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Status atual: ${order.status}. Após transferir no seu banco, o vendedor recebe o aviso para envio.",
                                color = NexoraTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (!isOrderGenerated) {
                    ElevatedButton(
                        onClick = {
                            onCheckoutProduct(prod, buyerName, buyerContact, 1) { order ->
                                generatedOrder = order
                                isOrderGenerated = true
                            }
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraLilacNeon,
                            contentColor = NexoraBackground
                        )
                    ) {
                        Text("Gerar Pix", fontWeight = FontWeight.Bold)
                    }
                } else if (generatedOrder != null) {
                    ElevatedButton(
                        onClick = {
                            onConfirmOrderPix(generatedOrder!!.id)
                            showCheckoutDialog = false
                            generatedOrder = null
                        },
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0xFF36E58D),
                            contentColor = NexoraBackground
                        )
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simular Confirmação Pix", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showCheckoutDialog = false
                    generatedOrder = null
                }) {
                    Text("Cancelar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }

    // Seller Dashboard Dialog
    if (showSellerDashboard) {
        SellerDashboardDialog(
            isPremium = subscription.isPro,
            sellerProfile = sellerProfile,
            sellerProducts = products.filter { it.sellerId == (sellerProfile?.id ?: "seller_me") },
            orders = sellerOrders,
            onSaveProduct = onSaveProduct,
            onTogglePublish = onTogglePublishStatus,
            onDeleteProduct = onDeleteProduct,
            onUpdateSellerProfile = onUpdateSellerProfile,
            onNavigateToPro = {
                showSellerDashboard = false
                onNavigateToPro()
            },
            onDismiss = { showSellerDashboard = false }
        )
    }
}

@Composable
private fun ProductCard(
    product: ProductEntity,
    onAddToCart: () -> Unit,
    onDirectCheckout: () -> Unit,
    onOpenDetail: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, NexoraSurfaceBorder, RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(containerColor = NexoraSurfaceCard),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(NexoraWineDark, NexoraPurpleElectric)
                                )
                            )
                            .border(1.dp, NexoraLilacNeon.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = NexoraTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = product.title,
                            color = NexoraTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${product.category} • Vendedor: ${product.sellerName}",
                            color = NexoraTextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(onClick = onToggleFavorite, modifier = Modifier.size(34.dp)) {
                    Icon(
                        imageVector = if (product.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favoritar",
                        tint = if (product.isFavorite) NexoraLilacNeon else NexoraTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.description,
                color = NexoraTextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "R$ ${String.format(java.util.Locale.GERMANY, "%.2f", product.price)}",
                        color = NexoraLilacNeon,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (product.isDigital) "Envio Imediato Digital" else "Em estoque: ${product.stock} un",
                        color = NexoraTextMuted,
                        fontSize = 10.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onOpenDetail) {
                        Text("Detalhes", color = NexoraTextSecondary, fontSize = 12.sp)
                    }

                    IconButton(
                        onClick = onAddToCart,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NexoraCardElevated)
                            .border(1.dp, NexoraSurfaceBorder, CircleShape)
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = "Adicionar ao Carrinho", tint = NexoraMagenta, modifier = Modifier.size(16.dp))
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    ElevatedButton(
                        onClick = onDirectCheckout,
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = NexoraLilacNeon,
                            contentColor = NexoraBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Pix", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SellerDashboardDialog(
    isPremium: Boolean,
    sellerProfile: SellerEntity?,
    sellerProducts: List<ProductEntity>,
    orders: List<OrderEntity>,
    onSaveProduct: (ProductEntity, (Result<Unit>) -> Unit) -> Unit,
    onTogglePublish: (ProductEntity, Boolean, (Result<Unit>) -> Unit) -> Unit,
    onDeleteProduct: (ProductEntity) -> Unit,
    onUpdateSellerProfile: (String, String, String, String, String) -> Unit,
    onNavigateToPro: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Meus Produtos, 1: Pedidos, 2: Perfil da Loja
    var showNewProductDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Storefront, contentDescription = null, tint = NexoraLilacNeon)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Painel do Vendedor", color = NexoraTextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = NexoraTextMuted)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // If not Premium, show lock card with direct access to Pro upgrade
                if (!isPremium) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.2.dp, NexoraMagenta, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = NexoraWineDark),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = NexoraLilacNeon, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Acesso Condicionado ao Plano Premium",
                                color = NexoraTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "A publicação e o gerenciamento comercial de produtos no Marketplace é um benefício exclusivo para assinantes NEXORA Pro.",
                                color = NexoraTextSecondary,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            ElevatedButton(
                                onClick = onNavigateToPro,
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = NexoraLilacNeon,
                                    contentColor = NexoraBackground
                                ),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Assinar NEXORA Pro Agora", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    // Tabs
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = NexoraSurfaceDark,
                        contentColor = NexoraLilacNeon,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                color = NexoraLilacNeon
                            )
                        }
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Produtos (${sellerProducts.size})", fontSize = 11.sp, color = if (selectedTab == 0) NexoraLilacNeon else NexoraTextSecondary) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Pedidos (${orders.size})", fontSize = 11.sp, color = if (selectedTab == 1) NexoraLilacNeon else NexoraTextSecondary) }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("Loja", fontSize = 11.sp, color = if (selectedTab == 2) NexoraLilacNeon else NexoraTextSecondary) }
                        )
                    }

                    when (selectedTab) {
                        0 -> {
                            // Products Tab
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Seus Anúncios", color = NexoraTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                ElevatedButton(
                                    onClick = { showNewProductDialog = true },
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = NexoraLilacNeon,
                                        contentColor = NexoraBackground
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Novo Anúncio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(sellerProducts) { prod ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NexoraSurfaceDark)
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = prod.title,
                                                color = NexoraTextPrimary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "R$ ${String.format(java.util.Locale.GERMANY, "%.2f", prod.price)} • ${if (prod.isPublished) "Publicado" else "Pausado"}",
                                                color = if (prod.isPublished) Color(0xFF36E58D) else NexoraTextMuted,
                                                fontSize = 11.sp
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            IconButton(
                                                onClick = { onTogglePublish(prod, !prod.isPublished) {} },
                                                modifier = Modifier.size(30.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (prod.isPublished) Icons.Default.CheckCircle else Icons.Default.Lock,
                                                    contentDescription = "Alternar Publicação",
                                                    tint = if (prod.isPublished) Color(0xFF36E58D) else NexoraTextMuted,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            IconButton(
                                                onClick = { onDeleteProduct(prod) },
                                                modifier = Modifier.size(30.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = NexoraTextMuted, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        1 -> {
                            // Orders Tab
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (orders.isEmpty()) {
                                    item {
                                        Text("Nenhum pedido recebido até o momento.", color = NexoraTextSecondary, fontSize = 12.sp)
                                    }
                                } else {
                                    items(orders) { order ->
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(NexoraSurfaceDark)
                                                .padding(10.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(order.id, color = NexoraLilacNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                                Text(
                                                    text = "R$ ${String.format(java.util.Locale.GERMANY, "%.2f", order.amount)}",
                                                    color = NexoraTextPrimary,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Text("Produto: ${order.productTitle}", color = NexoraTextSecondary, fontSize = 11.sp)
                                            Text("Comprador: ${order.buyerName} (${order.buyerContact})", color = NexoraTextMuted, fontSize = 10.sp)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (order.status == "PAGO_PIX") Color(0x3336E58D) else NexoraWineDark)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(order.status, color = if (order.status == "PAGO_PIX") Color(0xFF36E58D) else NexoraLilacNeon, fontSize = 10.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        2 -> {
                            // Store Profile Tab
                            var storeName by remember { mutableStateOf(sellerProfile?.storeName ?: "") }
                            var ownerName by remember { mutableStateOf(sellerProfile?.ownerName ?: "") }
                            var phone by remember { mutableStateOf(sellerProfile?.whatsappContact ?: "") }
                            var email by remember { mutableStateOf(sellerProfile?.emailContact ?: "") }
                            var desc by remember { mutableStateOf(sellerProfile?.description ?: "") }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                item {
                                    OutlinedTextField(
                                        value = storeName,
                                        onValueChange = { storeName = it },
                                        label = { Text("Nome da Loja", color = NexoraTextMuted, fontSize = 11.sp) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = NexoraTextPrimary,
                                            unfocusedTextColor = NexoraTextPrimary,
                                            focusedBorderColor = NexoraLilacNeon,
                                            unfocusedBorderColor = NexoraSurfaceBorder,
                                            focusedContainerColor = NexoraSurfaceDark,
                                            unfocusedContainerColor = NexoraSurfaceDark
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                item {
                                    OutlinedTextField(
                                        value = ownerName,
                                        onValueChange = { ownerName = it },
                                        label = { Text("Nome do Responsável", color = NexoraTextMuted, fontSize = 11.sp) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = NexoraTextPrimary,
                                            unfocusedTextColor = NexoraTextPrimary,
                                            focusedBorderColor = NexoraLilacNeon,
                                            unfocusedBorderColor = NexoraSurfaceBorder,
                                            focusedContainerColor = NexoraSurfaceDark,
                                            unfocusedContainerColor = NexoraSurfaceDark
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                item {
                                    OutlinedTextField(
                                        value = phone,
                                        onValueChange = { phone = it },
                                        label = { Text("WhatsApp Comercial", color = NexoraTextMuted, fontSize = 11.sp) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = NexoraTextPrimary,
                                            unfocusedTextColor = NexoraTextPrimary,
                                            focusedBorderColor = NexoraLilacNeon,
                                            unfocusedBorderColor = NexoraSurfaceBorder,
                                            focusedContainerColor = NexoraSurfaceDark,
                                            unfocusedContainerColor = NexoraSurfaceDark
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                item {
                                    OutlinedTextField(
                                        value = desc,
                                        onValueChange = { desc = it },
                                        label = { Text("Descrição da Loja", color = NexoraTextMuted, fontSize = 11.sp) },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = NexoraTextPrimary,
                                            unfocusedTextColor = NexoraTextPrimary,
                                            focusedBorderColor = NexoraLilacNeon,
                                            unfocusedBorderColor = NexoraSurfaceBorder,
                                            focusedContainerColor = NexoraSurfaceDark,
                                            unfocusedContainerColor = NexoraSurfaceDark
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                item {
                                    ElevatedButton(
                                        onClick = {
                                            onUpdateSellerProfile(storeName, ownerName, desc, phone, email)
                                        },
                                        colors = ButtonDefaults.elevatedButtonColors(
                                            containerColor = NexoraLilacNeon,
                                            contentColor = NexoraBackground
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Salvar Dados da Loja", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = NexoraLilacNeon)
            }
        },
        containerColor = NexoraSurfaceCard
    )

    // New Product Dialog
    if (showNewProductDialog) {
        var title by remember { mutableStateOf("") }
        var priceText by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Equipamentos") }
        var description by remember { mutableStateOf("") }
        var stockText by remember { mutableStateOf("5") }
        var isDigital by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showNewProductDialog = false },
            title = {
                Text("Cadastrar Novo Produto", color = NexoraTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Título do Anúncio", color = NexoraTextMuted, fontSize = 11.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NexoraTextPrimary,
                                unfocusedTextColor = NexoraTextPrimary,
                                focusedBorderColor = NexoraLilacNeon,
                                unfocusedBorderColor = NexoraSurfaceBorder,
                                focusedContainerColor = NexoraSurfaceDark,
                                unfocusedContainerColor = NexoraSurfaceDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("Preço em R$ (ex: 299.90)", color = NexoraTextMuted, fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NexoraTextPrimary,
                                unfocusedTextColor = NexoraTextPrimary,
                                focusedBorderColor = NexoraLilacNeon,
                                unfocusedBorderColor = NexoraSurfaceBorder,
                                focusedContainerColor = NexoraSurfaceDark,
                                unfocusedContainerColor = NexoraSurfaceDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = { Text("Categoria", color = NexoraTextMuted, fontSize = 11.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NexoraTextPrimary,
                                unfocusedTextColor = NexoraTextPrimary,
                                focusedBorderColor = NexoraLilacNeon,
                                unfocusedBorderColor = NexoraSurfaceBorder,
                                focusedContainerColor = NexoraSurfaceDark,
                                unfocusedContainerColor = NexoraSurfaceDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Descrição detalhada", color = NexoraTextMuted, fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = NexoraTextPrimary,
                                unfocusedTextColor = NexoraTextPrimary,
                                focusedBorderColor = NexoraLilacNeon,
                                unfocusedBorderColor = NexoraSurfaceBorder,
                                focusedContainerColor = NexoraSurfaceDark,
                                unfocusedContainerColor = NexoraSurfaceDark
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ElevatedButton(
                                onClick = { isDigital = !isDigital },
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = if (isDigital) NexoraWineDark else NexoraSurfaceDark,
                                    contentColor = NexoraLilacNeon
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (isDigital) "Tipo: Digital (Download)" else "Tipo: Físico (Envio Sedex)")
                            }
                        }
                    }
                }
            },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        val price = priceText.toDoubleOrNull() ?: 10.0
                        val stock = stockText.toIntOrNull() ?: 1
                        val newProduct = ProductEntity(
                            id = "prod-" + System.currentTimeMillis(),
                            title = title.ifBlank { "Produto Criador" },
                            sellerId = sellerProfile?.id ?: "seller_me",
                            sellerName = sellerProfile?.storeName ?: "Nexora Studio Store",
                            price = price,
                            category = category,
                            description = description,
                            stock = stock,
                            isDigital = isDigital,
                            deliveryInfo = if (isDigital) "Download imediato com licença comercial" else "Envio via Correios para todo o Brasil",
                            isPublished = true
                        )
                        onSaveProduct(newProduct) { res ->
                            if (res.isSuccess) {
                                showNewProductDialog = false
                            }
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = NexoraLilacNeon,
                        contentColor = NexoraBackground
                    )
                ) {
                    Text("Publicar Anúncio", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewProductDialog = false }) {
                    Text("Cancelar", color = NexoraTextMuted)
                }
            },
            containerColor = NexoraSurfaceCard
        )
    }
}
