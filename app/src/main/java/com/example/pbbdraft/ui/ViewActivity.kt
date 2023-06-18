package com.example.pbbdraft.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.pbbdraft.R
import com.example.pbbdraft.databinding.ActivityViewBinding
import com.example.pbbdraft.room.PBBDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewBinding
    val db by lazy { PBBDB(this) }
    private var pajakId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getPajak()
        binding.buttonUpdate.setOnClickListener {
            startActivity(
                Intent(applicationContext, EditActivity::class.java)
                    .putExtra("intent_id", pajakId)
            )
            finish()
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

        }
    }
}