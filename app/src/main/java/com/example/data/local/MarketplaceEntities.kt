package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val sellerId: String,
    val sellerName: String,
    val price: Double,
    val category: String, // "Equipamentos", "Iluminação & Áudio", "Presets & LUTs", "Cursos & Ebooks", "Serviços Criativos", "Merch & Vestuário"
    val description: String,
    val imageUrl: String = "",
    val stock: Int = 1,
    val isDigital: Boolean = false,
    val deliveryInfo: String = "Envio para todo o Brasil",
    val isPublished: Boolean = true,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "sellers")
data class SellerEntity(
    @PrimaryKey
    val id: String,
    val storeName: String,
    val ownerName: String,
    val description: String,
    val whatsappContact: String,
    val emailContact: String,
    val categories: String,
    val status: String = "ATIVA", // ATIVA, PENDENTE, SUSPENSA
    val isPremiumVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "marketplace_orders")
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val productId: String,
    val productTitle: String,
    val sellerId: String,
    val sellerName: String,
    val buyerName: String,
    val buyerContact: String,
    val amount: Double,
    val status: String = "PENDENTE_PIX", // PENDENTE_PIX, PAGO_PIX, ENVIADO, CONCLUIDO
    val pixCode: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
