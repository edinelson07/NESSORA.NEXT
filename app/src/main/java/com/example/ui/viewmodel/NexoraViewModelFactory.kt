package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.NexoraDatabase
import com.example.data.repository.AdminRepository
import com.example.data.repository.AudioRepository
import com.example.data.repository.DraftRepository
import com.example.data.repository.MarketplaceRepository
import com.example.data.repository.NexoraAIRepository
import com.example.data.repository.SubscriptionRepository
import com.example.data.repository.TvChannelRepository
import com.example.data.repository.YouTubeRepository

class NexoraViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NexoraViewModel::class.java)) {
            val db = NexoraDatabase.getDatabase(context)
            val draftRepo = DraftRepository(db.draftDao())
            val audioRepo = AudioRepository(db.audioDao())
            val youtubeRepo = YouTubeRepository()
            val aiRepo = NexoraAIRepository()
            val subscriptionRepo = SubscriptionRepository()
            val adminRepo = AdminRepository()
            val tvRepo = TvChannelRepository(db.tvChannelDao())
            val marketplaceRepo = MarketplaceRepository(
                productDao = db.productDao(),
                sellerDao = db.sellerDao(),
                orderDao = db.orderDao()
            )

            return NexoraViewModel(
                draftRepository = draftRepo,
                audioRepository = audioRepo,
                youtubeRepository = youtubeRepo,
                aiRepository = aiRepo,
                subscriptionRepository = subscriptionRepo,
                adminRepository = adminRepo,
                tvChannelRepository = tvRepo,
                marketplaceRepository = marketplaceRepo
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
