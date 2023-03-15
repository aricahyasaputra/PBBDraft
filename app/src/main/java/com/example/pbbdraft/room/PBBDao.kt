package com.example.pbbdraft.room

import androidx.room.*

@Dao
interface PBBDao {

    @Insert
    suspend fun addPajak(pajak: PBB)

    @Update
    suspend fun updatePajak(pajak: PBB)

    @Delete
    suspend fun deletePajak(pajak: PBB)

    @Query("SELECT * FROM pbb")
    suspend fun getPajaks(): List<PBB>

    @Query("SELECT * FROM pbb WHERE id=:pajak_id")
    suspend fun getPajak(pajak_id: Int): List<PBB>

    @Query("SELECT * FROM pbb WHERE nama LIKE :searchQuery OR NOP LIKE :searchQuery")
    suspend fun searchPajak(searchQuery: String): List<PBB>
}