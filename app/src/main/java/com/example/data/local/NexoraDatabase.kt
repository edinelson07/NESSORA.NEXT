package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        DraftEntity::class,
        AudioEntity::class,
        TvChannelEntity::class,
        ProductEntity::class,
        SellerEntity::class,
        OrderEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class NexoraDatabase : RoomDatabase() {
    abstract fun draftDao(): DraftDao
    abstract fun audioDao(): AudioDao
    abstract fun tvChannelDao(): TvChannelDao
    abstract fun productDao(): ProductDao
    abstract fun sellerDao(): SellerDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: NexoraDatabase? = null

        fun getDatabase(context: Context): NexoraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NexoraDatabase::class.java,
                    "nexora_creator_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
