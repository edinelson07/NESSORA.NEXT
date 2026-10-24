package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DraftDao {
    @Query("SELECT * FROM drafts ORDER BY updatedAt DESC")
    fun getAllDrafts(): Flow<List<DraftEntity>>

    @Query("SELECT * FROM drafts WHERE id = :id LIMIT 1")
    suspend fun getDraftById(id: Long): DraftEntity?

    @Query("SELECT * FROM drafts WHERE type = :type ORDER BY updatedAt DESC")
    fun getDraftsByType(type: String): Flow<List<DraftEntity>>

    @Query("SELECT * FROM drafts WHERE title LIKE '%' || :query || '%' OR topic LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchDrafts(query: String): Flow<List<DraftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: DraftEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(drafts: List<DraftEntity>)

    @Update
    suspend fun updateDraft(draft: DraftEntity)

    @Delete
    suspend fun deleteDraft(draft: DraftEntity)

    @Query("DELETE FROM drafts WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM drafts")
    suspend fun countDrafts(): Int

    @Query("SELECT * FROM drafts")
    suspend fun getAllDraftsSync(): List<DraftEntity>
}
