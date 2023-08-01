package com.example.pbbdraft.javascriptinterface

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.pbbdraft.mapdata.main
import com.example.pbbdraft.room.PBBDB

class WebAppInterfaceView(val appContext: Context, val id: Int) {
    val db by lazy { PBBDB(appContext) }

    @JavascriptInterface
    fun tampilkanString() :String{
        val pajaks = db.PBBDao().getPajaknow(SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE no=$id"))
        val pajaksConvert = mutableListOf<String>()
        pajaksConvert.add(main(pajaks.no, pajaks.NOP, pajaks.blok, pajaks.persil, pajaks.namaWajibPajak, pajaks.alamatWajibPajak, pajaks.alamatObjekPajak, pajaks.kelas, pajaks.luasObjekPajak, pajaks.pajakDitetapkan, pajaks.sejarahObjekPajak, pajaks.lat, pajaks.lng))
        Log.i("Hasil Convert", "tampilkanString: $pajaksConvert")

        return pajaksConvert.toString()
    }

}