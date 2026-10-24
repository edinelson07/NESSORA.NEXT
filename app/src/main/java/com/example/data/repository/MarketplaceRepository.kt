package com.example.data.repository

import com.example.data.local.OrderDao
import com.example.data.local.OrderEntity
import com.example.data.local.ProductDao
import com.example.data.local.ProductEntity
import com.example.data.local.SellerDao
import com.example.data.local.SellerEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

data class CartItem(
    val product: ProductEntity,
    val quantity: Int
)

class MarketplaceRepository(
    private val productDao: ProductDao,
    private val sellerDao: SellerDao,
    private val orderDao: OrderDao
) {

    fun getAllPublishedProducts(): Flow<List<ProductEntity>> = productDao.getAllPublishedProducts()

    fun getFavoriteProducts(): Flow<List<ProductEntity>> = productDao.getFavoriteProducts()

    fun getProductsBySeller(sellerId: String): Flow<List<ProductEntity>> = productDao.getProductsBySeller(sellerId)

    fun getAllOrders(): Flow<List<OrderEntity>> = orderDao.getAllOrders()

    fun getOrdersBySeller(sellerId: String): Flow<List<OrderEntity>> = orderDao.getOrdersBySeller(sellerId)

    suspend fun getSeller(sellerId: String): SellerEntity? = sellerDao.getSellerById(sellerId)

    suspend fun getDefaultSeller(): SellerEntity {
        val existing = sellerDao.getDefaultSeller()
        if (existing != null) return existing

        val defaultSeller = SellerEntity(
            id = "seller_me",
            storeName = "Nexora Studio Store",
            ownerName = "Criador Nexora",
            description = "Equipamentos de estúdio, predefinições cinematográficas e microfones selecionados por criadores.",
            whatsappContact = "(11) 98765-4321",
            emailContact = "vendas@nexoracreator.com.br",
            categories = "Equipamentos, Iluminação & Áudio, Presets & LUTs",
            status = "ATIVA",
            isPremiumVerified = true
        )
        sellerDao.insertOrUpdateSeller(defaultSeller)
        return defaultSeller
    }

    suspend fun updateSellerProfile(seller: SellerEntity) {
        sellerDao.insertOrUpdateSeller(seller)
    }

    suspend fun toggleFavoriteProduct(productId: String, currentFavorite: Boolean) {
        productDao.updateFavorite(productId, !currentFavorite)
    }

    /**
     * Publishes a product into the Marketplace.
     * Enforces that seller must be a verified Premium subscriber to publish new active products.
     */
    suspend fun saveProduct(
        product: ProductEntity,
        isSellerPremium: Boolean
    ): Result<Unit> {
        if (!isSellerPremium && product.isPublished) {
            return Result.failure(
                IllegalStateException("A publicação de produtos na Loja Nexora é um benefício exclusivo para assinantes NEXORA Pro ativos.")
            )
        }
        productDao.insertProduct(product)
        return Result.success(Unit)
    }

    suspend fun toggleProductPublishStatus(
        productId: String,
        targetPublished: Boolean,
        isSellerPremium: Boolean
    ): Result<Unit> {
        if (targetPublished && !isSellerPremium) {
            return Result.failure(
                IllegalStateException("Reative o seu plano NEXORA Pro para republicar produtos no catálogo.")
            )
        }
        productDao.updatePublishStatus(productId, targetPublished)
        return Result.success(Unit)
    }

    suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    suspend fun createOrder(
        product: ProductEntity,
        buyerName: String,
        buyerContact: String,
        quantity: Int
    ): OrderEntity {
        val orderId = "PED-" + UUID.randomUUID().toString().take(8).uppercase()
        val total = product.price * quantity
        val pixCode = generateMarketplacePixCode(orderId, total, product.sellerName)

        val order = OrderEntity(
            id = orderId,
            productId = product.id,
            productTitle = "${product.title} (x$quantity)",
            sellerId = product.sellerId,
            sellerName = product.sellerName,
            buyerName = buyerName.ifBlank { "Comprador Nexora" },
            buyerContact = buyerContact.ifBlank { "contato@cliente.com" },
            amount = total,
            status = "PENDENTE_PIX",
            pixCode = pixCode
        )
        orderDao.insertOrder(order)
        return order
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        orderDao.updateOrderStatus(orderId, newStatus)
    }

    fun generateMarketplacePixCode(orderId: String, amount: Double, receiver: String): String {
        val cleanAmount = String.format(java.util.Locale.US, "%.2f", amount)
        return "00020126580014br.gov.bcb.pix0136nexora-marketplace@pagbank.com.br520400005303986540$cleanAmount" +
                "5802BR5920${receiver.take(20).padEnd(20, ' ')}6009SAO PAULO62140510$orderId" +
                "6304A1B2"
    }

    suspend fun ensureDefaultProductsIfEmpty() {
        if (productDao.getProductCount() == 0) {
            val defaults = getDefaultProductsCatalog()
            productDao.insertAll(defaults)
        }
    }

    companion object {
        fun getDefaultProductsCatalog(): List<ProductEntity> {
            return listOf(
                ProductEntity(
                    id = "prod-mic-shure",
                    title = "Microfone Shure MV7 USB/XLR para Criadores",
                    sellerId = "seller_me",
                    sellerName = "Nexora Studio Store",
                    price = 1899.00,
                    category = "Iluminação & Áudio",
                    description = "Microfone dinâmico profissional com saídas USB e XLR simultâneas, processamento DSP interno e isolamento de voz para gravação de podcast e vídeos.",
                    imageUrl = "",
                    stock = 7,
                    isDigital = false,
                    deliveryInfo = "Frete grátis via Sedex (2-4 dias úteis)",
                    isPublished = true,
                    isFavorite = true
                ),
                ProductEntity(
                    id = "prod-softbox-rgb",
                    title = "Painel LED RGB 60W com Controle via App",
                    sellerId = "seller_me",
                    sellerName = "Nexora Studio Store",
                    price = 459.90,
                    category = "Iluminação & Áudio",
                    description = "Iluminação de estúdio bi-color 2500K-8500K com efeitos cinematográficos FX de relâmpago, sirene e vela, montagem universal em tripé.",
                    imageUrl = "",
                    stock = 12,
                    isDigital = false,
                    deliveryInfo = "Envio com rastreamento para todo o Brasil",
                    isPublished = true
                ),
                ProductEntity(
                    id = "prod-luts-4k",
                    title = "Pack 50+ LUTs Cinematográficos para YouTube & Reels",
                    sellerId = "seller_me",
                    sellerName = "Nexora Studio Store",
                    price = 89.00,
                    category = "Presets & LUTs",
                    description = "Perfis de cor profissionais .cube compatíveis com Premiere, DaVinci Resolve, Final Cut e CapCut Desktop. Tons de pele perfeitos e alto contraste.",
                    imageUrl = "",
                    stock = 999,
                    isDigital = true,
                    deliveryInfo = "Download imediato com chave de ativação por e-mail",
                    isPublished = true,
                    isFavorite = true
                ),
                ProductEntity(
                    id = "prod-course-script",
                    title = "Masterclass: Engenharia de Roteiros Virais 2.0",
                    sellerId = "seller_me",
                    sellerName = "Nexora Studio Store",
                    price = 297.00,
                    category = "Cursos & Ebooks",
                    description = "Treinamento intensivo com 24 aulas práticas sobre estrutura narrativa, retenção nos primeiros 15 segundos e otimização de CTR em thumbnails.",
                    imageUrl = "",
                    stock = 500,
                    isDigital = true,
                    deliveryInfo = "Acesso vitalício à plataforma e comunidade de criadores",
                    isPublished = true
                ),
                ProductEntity(
                    id = "prod-hoodie-nexora",
                    title = "Moletom Oversized Nexora Creator Edition - Algodão Pesado",
                    sellerId = "seller_me",
                    sellerName = "Nexora Studio Store",
                    price = 219.00,
                    category = "Merch & Vestuário",
                    description = "Moletom preto premium com detalhes bordados em lilás e roxo neon. Caimento streetwear moderno, ideal para gravações e uso diário.",
                    imageUrl = "",
                    stock = 25,
                    isDigital = false,
                    deliveryInfo = "Envio nacional em embalagem personalizada",
                    isPublished = true
                ),
                ProductEntity(
                    id = "prod-cam-streamcam",
                    title = "Placa de Captura 4K60 HDR USB 3.2 Ultra-Low Latency",
                    sellerId = "seller_me",
                    sellerName = "Nexora Studio Store",
                    price = 680.00,
                    category = "Equipamentos",
                    description = "Transforme sua câmera DSLR ou mirrorless em webcam de altíssima definição com passthrough sem atraso para streaming no YouTube e Twitch.",
                    imageUrl = "",
                    stock = 4,
                    isDigital = false,
                    deliveryInfo = "Sedex com seguro de transporte",
                    isPublished = true
                )
            )
        }
    }
}
