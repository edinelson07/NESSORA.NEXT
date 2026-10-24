package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioDao {
    @Query("SELECT * FROM audio_tracks ORDER BY addedAt DESC")
    fun getAllTracks(): Flow<List<AudioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: AudioEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tracks: List<AudioEntity>)

    @Update
    suspend fun updateTrack(track: AudioEntity)

    @Delete
    suspend fun deleteTrack(track: AudioEntity)

    @Query("SELECT * FROM audio_tracks")
    suspend fun getAllTracksSync(): List<AudioEntity>
}
