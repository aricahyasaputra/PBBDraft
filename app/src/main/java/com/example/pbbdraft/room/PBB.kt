package com.example.pbbdraft.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pajakPBB")
data class PBB (
    @PrimaryKey(autoGenerate = true)
    val no: Int,
    val NOP: String,
    val blok: Int,
    val persil: String,
    val namaWajibPajak: String,
    val alamatWajibPajak: String,
    val alamatObjekPajak: String,
    val kelas: String,
    val luasObjekPajak: Int,
    val pajakDitetapkan: Int,
    val sejarahObjekPajak: String,
    val lat: Float,
    val lng: Float,
    val statusPembayaranPajak: Int
)