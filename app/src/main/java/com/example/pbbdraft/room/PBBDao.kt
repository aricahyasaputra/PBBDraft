package com.example.pbbdraft.room

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface PBBDao {

    @Insert
    suspend fun addPajak(pajak: PBB)

    @Insert
    suspend fun addSejarah(sejarah: Sejarah)
    @Update
    suspend fun updateProfile(profile: Profile)

    @Update
    suspend fun updatePajak(pajak: PBB)

    @Update
    suspend fun updateSejarah(sejarah: Sejarah)

    @Query("UPDATE pajakPBB SET statusPembayaranPajak =:status WHERE `no` =:id")
    suspend fun updateStatusPembayaran(id: Int, status:Int )

    @Delete
    suspend fun deletePajak(pajak: PBB)

    @Delete
    suspend fun deleteSejarah(sejarah: Sejarah)

    @RawQuery
    suspend fun getPajaks(query: SupportSQLiteQuery): List<PBB>

    @Query("SELECT * FROM temp WHERE id=1")
    fun getProfile(): List<Profile>

    @RawQuery
    fun getPajaksnow(query: SupportSQLiteQuery): List<PBB>

    @RawQuery
    fun getPajaknow(query: SupportSQLiteQuery): PBB

    @RawQuery
    suspend fun getPajak(query: SupportSQLiteQuery): List<PBB>

    @RawQuery
    suspend fun getSejarah(query: SupportSQLiteQuery): List<Sejarah>

    @Query("DELETE FROM pajakPBB")
    fun deletePajakAll()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name='pajakPBB'")
    fun resetPrimaryKey()
    @RawQuery
    suspend fun searchPajak(query: SupportSQLiteQuery): List<PBB>

    @RawQuery
    suspend fun searchSejarah(query: SupportSQLiteQuery): List<Sejarah>

    @Query("SELECT sum(pajakDitetapkan) FROM pajakPBB WHERE statusPembayaranPajak=:statusPembayaranPajak")
    suspend fun updateDashboardPajakTerbayar(statusPembayaranPajak: Int): Int?

    @Query("SELECT sum(pajakDitetapkan) FROM pajakPBB")
    suspend fun updateDashboardPajakTertanggung(): Int?

    @Query("SELECT count(pajakDitetapkan) FROM pajakPBB WHERE statusPembayaranPajak=:statusPembayaranPajak")
    suspend fun updateDashboardPetakTerbayar(statusPembayaranPajak: Int): Int?

    @Query("SELECT count(pajakDitetapkan) FROM pajakPBB")
    suspend fun updateDashboardPetakTertanggung(): Int?
}