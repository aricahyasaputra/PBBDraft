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

    @Query("SELECT * FROM blok6")
    suspend fun getPajaks(): List<PBB>

    @Query("SELECT * FROM blok6 WHERE no=:no")
    suspend fun getPajak(no: Int): List<PBB>

    //@Query("SELECT * FROM 'blok6' WHERE nama LIKE :searchQuery OR NOP LIKE :searchQuery")
    //suspend fun searchPajak(searchQuery: String): List<PBB>
}