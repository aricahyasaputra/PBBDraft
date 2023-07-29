package com.example.pbbdraft.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader
import com.example.pbbdraft.databinding.ActivityPetaBlokFullscreenBinding

class PetaBlokFullscreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetaBlokFullscreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPetaBlokFullscreenBinding.inflate(layoutInflater)
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
        binding.webView.loadUrl("https://appassets.androidplatform.net/assets/javascriptMap/peta-blok-lengkap-fullscreen.html")
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}