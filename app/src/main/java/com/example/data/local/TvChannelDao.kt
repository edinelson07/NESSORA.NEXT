package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TvChannelDao {

    @Query("SELECT * FROM tv_channels ORDER BY isFavorite DESC, name ASC")
    fun getAllChannels(): Flow<List<TvChannelEntity>>

    @Query("SELECT * FROM tv_channels WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteChannels(): Flow<List<TvChannelEntity>>

    @Query("SELECT * FROM tv_channels WHERE category = :category ORDER BY name ASC")
    fun getChannelsByCategory(category: String): Flow<List<TvChannelEntity>>

    @Query("SELECT * FROM tv_channels WHERE id = :id LIMIT 1")
    suspend fun getChannelById(id: String): TvChannelEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(channels: List<TvChannelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(channel: TvChannelEntity)

    @Update
    suspend fun updateChannel(channel: TvChannelEntity)

    @Query("UPDATE tv_channels SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Delete
    suspend fun deleteChannel(channel: TvChannelEntity)

    @Query("SELECT COUNT(*) FROM tv_channels")
    suspend fun getChannelCount(): Int
}
