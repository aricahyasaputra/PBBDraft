package com.example.pbbdraft.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temp")
data class Profile (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nama: String,
    val email: String,
    val url: String,
    val lat: Float,
    val lng: Float,
    val blok: Int,
    val NOP: String,
    val alamatWajibPajak: String,
    val alamatObjekPajak: String
)