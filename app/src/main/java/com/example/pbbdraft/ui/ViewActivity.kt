package com.example.pbbdraft.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.webkit.WebViewAssetLoader
import com.example.pbbdraft.R
import com.example.pbbdraft.databinding.ActivityViewBinding
import com.example.pbbdraft.javascriptinterface.WebAppInterfaceView
import com.example.pbbdraft.mapdata.main
import com.example.pbbdraft.room.PBBDB
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileOutputStream


class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewBinding
    val db by lazy { PBBDB(this) }
    private var pajakId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewBinding.inflate(layoutInflater)
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

        getPajak()

        binding.webView.addJavascriptInterface(WebAppInterfaceView(this, pajakId), "Android")
        binding.webView.loadUrl("https://appassets.androidplatform.net/assets/javascriptMap/personal-view.html")

        binding.buttonUpdate.setOnClickListener {
            startActivity(
                Intent(applicationContext, EditActivity::class.java)
                    .putExtra("intent_id", pajakId)
                    .putExtra("intent_type", 2)
            )
            finish()
        }
        binding.buttonExport.setOnClickListener {
            startActivity(
                Intent(applicationContext, EditActivity::class.java)
                    .putExtra("intent_id", pajakId)
                    .putExtra("intent_type", 0)
            )
            finish()
        }

        binding.buttonBatalBayar.setOnClickListener {
            pajakId = intent.getIntExtra("intent_id", 0)
            CoroutineScope(Dispatchers.Main).launch {
                val pajaks = db.PBBDao().getPajak( SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE no=${pajakId}") )[0]
                if (pajaks.statusPembayaranPajak == 0){
                    db.PBBDao().updateStatusPembayaran(pajakId, 1)
                    binding.textViewStatusPembayaran.text = "Sudah Bayar"
                    binding.buttonBatalBayar.text = "Batal Bayar"
                }else{
                    db.PBBDao().updateStatusPembayaran(pajakId, 0)
                    binding.textViewStatusPembayaran.text = "Belum Bayar"
                    binding.buttonBatalBayar.text = "Sudah Bayar"
                }
            }
        }

        binding.buttonSejarahTanah.setOnClickListener {
            startActivity(
                Intent(applicationContext, SejarahTanahActivity::class.java)
            )
        }
    }

    private fun getPajak(){
        pajakId = intent.getIntExtra("intent_id", 0)

        CoroutineScope(Dispatchers.IO).launch{
            val pajaks = db.PBBDao().getPajak( SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE no=${pajakId}") )[0]
            binding.NOP.text = pajaks.NOP
            binding.blok.text = pajaks.blok.toString()
            binding.persil.text = pajaks.persil
            binding.namaWajibPajak.text = pajaks.namaWajibPajak
            binding.alamatWajibPajak.text = pajaks.alamatObjekPajak
            binding.alamatWajibPajak.text = pajaks.alamatWajibPajak
            binding.kelas.text = pajaks.kelas
            binding.luasObjekPajak.text = pajaks.luasObjekPajak.toString()
            binding.pajakDitetapkan.text = pajaks.pajakDitetapkan.toString()
            binding.sejarahTanah.text = pajaks.sejarahObjekPajak
            if (pajaks.statusPembayaranPajak == 1){
                binding.textViewStatusPembayaran.text = "Sudah Bayar"
            }else{
                binding.buttonBatalBayar.text = "Sudah Bayar"
            }
        }
    }



}