package com.example.pbbdraft.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sejarahPBB")
data class Sejarah(
    @PrimaryKey(autoGenerate = true)
    val no: Int,
    val noUrut: String,
    val noC: String,
    val noSeri: String,
    val namaObjekPajak: String,
    val desaObjekPajak: String,
    val isTextLineThrough: Int
)
