package com.example.pbbdraft.room

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface PBBDao {

    @Insert
    suspend fun addPajak(pajak: PBB)
    @Update
    suspend fun updateProfile(profile: Profile)

    @Update
    suspend fun updatePajak(pajak: PBB)

    @Delete
    suspend fun deletePajak(pajak: PBB)

    @RawQuery
    suspend fun getPajaks(query: SupportSQLiteQuery): List<PBB>

    @Query("SELECT * FROM temp WHERE id=1")
    fun getProfile(): List<Profile>

    @RawQuery
    fun getPajaksnow(query: SupportSQLiteQuery): List<PBB>

    @RawQuery
    suspend fun getPajak(query: SupportSQLiteQuery): List<PBB>

    @Query("DELETE FROM pajakPBB")
    fun deletePajakAll()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name='pajakPBB'")
    fun resetPrimaryKey()
    @RawQuery
    suspend fun searchPajak(query: SupportSQLiteQuery): List<PBB>
}