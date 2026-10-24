package com.example

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import android.content.Context
import com.example.data.local.NexoraDatabase
import com.example.data.local.ProductEntity
import com.example.data.local.TvChannelEntity
import com.example.data.repository.MarketplaceRepository
import com.example.data.repository.SubscriptionRepository
import com.example.data.repository.TvChannelRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class NexoraCommercialTest {

    private lateinit var database: NexoraDatabase
    private lateinit var tvChannelRepository: TvChannelRepository
    private lateinit var marketplaceRepository: MarketplaceRepository
    private lateinit var subscriptionRepository: SubscriptionRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, NexoraDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        tvChannelRepository = TvChannelRepository(database.tvChannelDao())
        marketplaceRepository = MarketplaceRepository(
            productDao = database.productDao(),
            sellerDao = database.sellerDao(),
            orderDao = database.orderDao()
        )
        subscriptionRepository = SubscriptionRepository()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `tvAbertaCatalogContainsOfficialBrazilianBroadcasters`() {
        val catalog = TvChannelRepository.getOfficialChannelsCatalog()
        assertTrue("O catálogo deve conter pelo menos 10 emissoras", catalog.size >= 10)

        val channelIds = catalog.map { it.id }
        assertTrue(channelIds.contains("tv-brasil"))
        assertTrue(channelIds.contains("tv-cultura"))
        assertTrue(channelIds.contains("tv-camara"))
        assertTrue(channelIds.contains("tv-senado"))
        assertTrue(channelIds.contains("record-news"))
        assertTrue(channelIds.contains("jovem-pan-news"))
        assertTrue(channelIds.contains("band-oficial"))
        assertTrue(channelIds.contains("sbt-oficial"))
        assertTrue(channelIds.contains("tv-globo"))

        // Assert all have verified dates and valid URLs
        catalog.forEach { channel ->
            assertTrue(channel.name.isNotBlank())
            assertTrue(channel.officialWebUrl.startsWith("http"))
            assertTrue(channel.lastVerifiedDate.isNotBlank())
            assertTrue(channel.rightsInfo.isNotBlank())
        }
    }

    @Test
    fun `tvAbertaPersistenceAndFavoriteToggleWorks`() = runBlocking {
        tvChannelRepository.ensureDefaultChannelsIfEmpty()
        val channels = tvChannelRepository.getAllChannels().first()
        assertTrue("Canais devem ser salvos no banco Room", channels.isNotEmpty())

        val tvBrasil = channels.first { it.id == "tv-brasil" }
        val initialFav = tvBrasil.isFavorite

        tvChannelRepository.toggleFavorite(tvBrasil.id, initialFav)
        val updatedChannels = tvChannelRepository.getAllChannels().first()
        val updatedBrasil = updatedChannels.first { it.id == "tv-brasil" }
        assertEquals(!initialFav, updatedBrasil.isFavorite)
    }

    @Test
    fun `marketplacePremiumEnforcementBlocksFreeUserFromPublishing`() = runBlocking {
        val testProduct = ProductEntity(
            id = "test-prod-1",
            title = "Microfone de Teste",
            sellerId = "seller_me",
            sellerName = "Loja Teste",
            price = 499.00,
            category = "Equipamentos",
            description = "Microfone condensador para estúdio",
            isPublished = true
        )

        // Usuário Gratuito (isSellerPremium = false) NÃO pode publicar
        val freeResult = marketplaceRepository.saveProduct(testProduct, isSellerPremium = false)
        assertTrue(freeResult.isFailure)
        assertTrue(freeResult.exceptionOrNull()?.message?.contains("exclusivo para assinantes NEXORA Pro") == true)

        // Usuário Premium (isSellerPremium = true) PODE publicar com sucesso
        val premiumResult = marketplaceRepository.saveProduct(testProduct, isSellerPremium = true)
        assertTrue(premiumResult.isSuccess)

        val publishedList = marketplaceRepository.getAllPublishedProducts().first()
        assertEquals(1, publishedList.size)
        assertEquals("Microfone de Teste", publishedList.first().title)
    }

    @Test
    fun `marketplaceOrderCreationAndPixGenerationWorks`() = runBlocking {
        val product = ProductEntity(
            id = "test-prod-pix",
            title = "Pack de LUTs 4K",
            sellerId = "seller_me",
            sellerName = "Nexora Studio",
            price = 89.00,
            category = "Presets & LUTs",
            description = "LUTs profissionais",
            isPublished = true
        )

        val order = marketplaceRepository.createOrder(
            product = product,
            buyerName = "João Criador",
            buyerContact = "joao@email.com",
            quantity = 2
        )

        assertNotNull(order.id)
        assertTrue(order.id.startsWith("PED-"))
        assertEquals(178.00, order.amount, 0.01)
        assertEquals("PENDENTE_PIX", order.status)
        assertTrue(order.pixCode.contains("br.gov.bcb.pix"))
        assertTrue(order.pixCode.contains("178.00"))

        val orders = marketplaceRepository.getAllOrders().first()
        assertEquals(1, orders.size)

        // Simula pagamento Pix
        marketplaceRepository.updateOrderStatus(order.id, "PAGO_PIX")
        val updatedOrders = marketplaceRepository.getAllOrders().first()
        assertEquals("PAGO_PIX", updatedOrders.first().status)
    }

    @Test
    fun `subscriptionPixCodeGenerationConformsToPagBankStandard`() {
        val pixMonthly = subscriptionRepository.generatePixPayment(isAnnual = false)
        assertTrue(pixMonthly.contains("br.gov.bcb.pix"))
        assertTrue(pixMonthly.contains("nexora-pagbank"))
        assertTrue(pixMonthly.contains("39.90"))

        val pixAnnual = subscriptionRepository.generatePixPayment(isAnnual = true)
        assertTrue(pixAnnual.contains("br.gov.bcb.pix"))
        assertTrue(pixAnnual.contains("349.00"))
    }
}
