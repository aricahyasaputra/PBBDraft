package com.example.pbbdraft.mapdata

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pbbdraft.room.PBBDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject


class MapAdapter: AppCompatActivity() {
    val db by lazy { PBBDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadPajak()



/*        class WebAppInterface(private val mContext: Context, private val listAngka: List<Int> = numbers) {

            *//** Show a toast from the web page  *//*
            @JavascriptInterface
            fun showToast(toast: String) {
                Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
            }
            @JavascriptInterface
            fun tampilkanString() :String{
                return nilaiString
            }
        }
    }*/


/*    fun sendPajak(){
        CoroutineScope(Dispatchers.IO).launch{
            val pajaks = db.PBBDao().getPajaks()
        }*/
    }
    fun loadPajak(){
        CoroutineScope(Dispatchers.IO).launch{
            val pajaks = db.PBBDao().getPajaks()

            //var pajaksConvert = MutableList<PBB>()
            /*pajaksConvert.*/
            pajaks.forEachIndexed({index, element ->
                PBB(element.no, element.NOP, element.blok, element.persil, element.namaWajibPajak, element.alamatWajibPajak, element.alamatObjekPajak, element.kelas, element.luasObjekPajak, element.pajakDitetapkan, element.sejarahObjekPajak, element.lat, element.lng)

            })
            //val pajaksJson = ListPBB(pajaks)
        }
    }
}