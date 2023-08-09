package com.example.pbbdraft.javascriptinterface

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.pbbdraft.mapdata.main
import com.example.pbbdraft.room.PBBDB
import com.example.pbbdraft.ui.ViewActivity

class WebAppInterfaceMapActivity(val appContext: Context) {
    val db by lazy { PBBDB(appContext) }
    @JavascriptInterface
    fun tampilkanString(blok: String, lat: Float, lng: Float) :String{
        val pajaksConvert = mutableListOf<String>()

        val jangkauan: Int = 150

        val latMin: Float = lat-jangkauan
        val latMax: Float = lat+jangkauan
        val lngMin: Float = lng-jangkauan
        val lngMax: Float = lng+jangkauan

        val pajaks = db.PBBDao().getPajaksnow(SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE blok=${blok} AND lat BETWEEN $latMin AND $latMax AND lng BETWEEN $lngMin AND $lngMax"))
        pajaks.forEachIndexed({index, element ->
            pajaksConvert.add(main(element.no, element.NOP, element.blok, element.persil, element.namaWajibPajak, element.alamatWajibPajak, element.alamatObjekPajak, element.kelas, element.luasObjekPajak, element.pajakDitetapkan, element.sejarahObjekPajak, element.lat, element.lng))

        })
        return pajaksConvert.toString()
    }
    @JavascriptInterface
    fun tampilkanDataPajak(id:Int){
        Handler(Looper.getMainLooper()).post {
            //Log.i("hasil", appContext.toString())
            val viewintent = Intent(appContext, ViewActivity::class.java).putExtra("intent_id", id)
            appContext.startActivity(viewintent)
        }

    }
}