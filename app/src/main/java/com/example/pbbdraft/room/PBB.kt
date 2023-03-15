package com.example.pbbdraft.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PBB (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val NOP:String,
    val nama:String,
    val alamat:String,
    val luasTanah:Int,
    val pajakDitetapkan:Int
)