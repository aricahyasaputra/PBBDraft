package com.example.pbbdraft.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.webkit.WebViewAssetLoader
import com.example.pbbdraft.databinding.ActivityMapBinding
import com.example.pbbdraft.mapdata.main
import com.example.pbbdraft.room.PBBDB


class WebAppInterfaceMapActivity(context: Context) {
    val appContext : Context = context
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

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    val db by lazy { PBBDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
        binding.webView.addJavascriptInterface(WebAppInterfaceMapActivity( this), "Android")
        binding.webView.loadUrl("https://appassets.androidplatform.net/assets/javascriptMap/skripsi.html")



    }

    override fun onBackPressed() {
        if(binding.webView.canGoBack()){
            binding.webView.goBack()
        }else{
            super.onBackPressed()
        }

    }

}