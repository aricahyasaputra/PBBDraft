package com.example.pbbdraft.mapdata

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
data class PBB(val no: Int,
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
               val lng: Float)

fun main(no: Int,
         NOP: String,
         blok: Int,
         persil: String,
         namaWajibPajak: String,
         alamatWajibPajak: String,
         alamatObjekPajak: String,
         kelas: String,
         luasObjekPajak: Int,
         pajakDitetapkan: Int,
         sejarahObjekPajak: String,
         lat: Float,
         lng: Float):String {
    // Serialization (Kotlin object to JSON string)

    val data = PBB(no, NOP, blok, persil, namaWajibPajak, alamatWajibPajak, alamatObjekPajak, kelas, luasObjekPajak, pajakDitetapkan, sejarahObjekPajak, lat, lng)
    val string = Json.encodeToString(data)
    //println(string) // {"name":"Louis","yearOfBirth":1901}
    //Log.i("hasil", string)

    // Deserialization (JSON string to Kotlin object)
    return string
}
