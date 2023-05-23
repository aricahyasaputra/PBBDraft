package com.example.pbbdraft.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.webkit.WebViewAssetLoader
import com.example.pbbdraft.databinding.ActivityMapBinding
import com.example.pbbdraft.mapdata.main
import com.example.pbbdraft.room.PBBDB
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


    // Deserialization (JSON string to Kotlin object)
    return string
}

class WebAppInterface(private val pajakJson: String, context: Context) {
     val appContext : Context = context
    @JavascriptInterface
    fun tampilkanString() :String{
        return pajakJson
    }
    @JavascriptInterface
    fun tampilkanDataPajak(id:Int){
        Handler(Looper.getMainLooper()).post {
            //Log.i("hasil", appContext.toString())
            val editintent = Intent(appContext, EditActivity::class.java).putExtra("intent_id", id).putExtra("intent_type", 0)
            appContext.startActivity(editintent)
        }

    }
}

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    val db by lazy { PBBDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var hasilLoad:String = loadPajak()


        binding.webView.webViewClient = WebViewClient()



        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }
        }
        binding.webView.settings.safeBrowsingEnabled = false
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.addJavascriptInterface(WebAppInterface(hasilLoad, this), "Android")
        //binding.webView.loadUrl("file:///android_asset/javascriptMap/perblok.html")
        //binding.webView.loadUrl("file:///android_asset/javascriptMap/skripsi.html")
        binding.webView.loadUrl("https://appassets.androidplatform.net/assets/javascriptMap/skripsi.html")



    }

    override fun onBackPressed() {
        if(binding.webView.canGoBack()){
            binding.webView.goBack()
        }else{
            super.onBackPressed()
        }

    }
    fun loadPajak():String{
        val pajaksConvert = mutableListOf<String>()

        val pajaks = db.PBBDao().getPajaksnow()

            /*pajaksConvert.*/
        pajaks.forEachIndexed({index, element ->
                pajaksConvert.add(main(element.no, element.NOP, element.blok, element.persil, element.namaWajibPajak, element.alamatWajibPajak, element.alamatObjekPajak, element.kelas, element.luasObjekPajak, element.pajakDitetapkan, element.sejarahObjekPajak, element.lat, element.lng))

        })

        Log.i("konfig web", pajaksConvert.toString())
        return pajaksConvert.toString()
    }

}