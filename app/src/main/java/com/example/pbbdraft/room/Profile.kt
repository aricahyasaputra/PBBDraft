package com.example.pbbdraft.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temp")
data class Profile (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nama: String,
    val email: String,
    val url: String
)