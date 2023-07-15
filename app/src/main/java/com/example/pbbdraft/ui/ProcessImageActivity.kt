package com.example.pbbdraft.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pbbdraft.R
import com.example.pbbdraft.databinding.ActivityProcessImageBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class ProcessImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProcessImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProcessImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uri = Uri.parse(intent.getStringExtra("imageUri"))
        binding.cropImageView.setImageUriAsync(uri)
        binding.simpandanocr.setOnClickListener {
            val cropped: Bitmap? = binding.cropImageView.getCroppedImage()
            detectTextInOcr(cropped)

        }
    }
    private fun detectTextInOcr(bitmap: Bitmap?){
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = bitmap?.let { InputImage.fromBitmap(it, 0) }
        //val image: InputImage
        try {
            //image = uriGambar?.let { InputImage.fromFilePath(baseContext, it) }!!

            val result = image?.let {
                recognizer.process(it)
                    .addOnSuccessListener { visionText ->
                        for (block in visionText.textBlocks) {
                            val boundingBox = block.boundingBox
                            val cornerPoints = block.cornerPoints
                            val text = block.text


                            if (text.contains("NOP")){
                                startActivity(
                                    Intent(applicationContext, SearchActivity::class.java)
                                        .putExtra("NOP", text)
                                        .putExtra("intent_type", 1)
                                )
                            }
                            Log.i("Detected Text", "detectTextInOcr: $text")
                            /*for (line in block.lines) {
                                        // ...
                                        for (element in line.elements) {
                                            // ...
                                        }
                                    }*/
                        }
                        // ...
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        // ...
                    }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }


    }
}