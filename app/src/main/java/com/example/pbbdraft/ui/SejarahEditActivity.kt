package com.example.pbbdraft.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.pbbdraft.R
import com.example.pbbdraft.databinding.ActivitySejarahEditBinding
import com.example.pbbdraft.room.Constant
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.room.PBBDB
import com.example.pbbdraft.room.Sejarah
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SejarahEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySejarahEditBinding
    val db by lazy { PBBDB(this) }
    private var sejarahId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySejarahEditBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        setupView()
        setupListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setupView(){
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                binding.buttonUpdate.visibility = View.GONE
            }
            Constant.TYPE_EKSPORT -> {
                binding.buttonSave.visibility = View.GONE
                binding.buttonUpdate.visibility = View.GONE
                getPajak()
            }
            Constant.TYPE_UPDATE-> {
                binding.buttonSave.visibility = View.GONE
                getPajak()
            }
        }
    }

    private fun getPajak(){
        sejarahId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.Main).launch{
            val sejarah = db.PBBDao().getSejarah( SimpleSQLiteQuery("SELECT * FROM sejarahPBB WHERE no=${sejarahId}") )[0]
            binding.editTextNomorC.setText(sejarah.noC)
            binding.editTextNomorSeri.setText( sejarah.noSeri )
            binding.editTextNomorUrut.setText( sejarah.noUrut )
            binding.editTextNamaObjekPajak.setText( sejarah.namaObjekPajak )
            binding.editTextDesaObjekPajak.setText( sejarah.desaObjekPajak )
        }
    }

    private fun setupListener(){
        binding.buttonSave.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch{
                db.PBBDao().addSejarah(
                    Sejarah(0, binding.editTextNomorUrut.text.toString(), binding.editTextNomorC.text.toString(), binding.editTextNomorSeri.text.toString(), binding.editTextNamaObjekPajak.text.toString(), binding.editTextDesaObjekPajak.text.toString(), 0)
                )
                finish()
            }
        }
        binding.buttonUpdate.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch{
                //, blok=:${binding.editTextBlok.text}, persil=:${binding.editTextPersil.text}, namaWajibPajak=:${binding.editTextNamaWajibPajak.text}, alamatWajibPajak=:${binding.editTextAlamatWajibPajak.text}, alamatObjekPajak=:${binding.editTextAlamatObjekPajak.text}, kelas=:${binding.editTextKelas.text}, luasObjekPajak=:${binding.editTextLuas.text}, pajakDitetapkan=:${binding.editTextTotalWajibPajak.text}, sejarahObjekPajak=:${binding.editTextSejarahPajak.text}, lat=:${binding.editTextLat.text.toString().toFloat()}, lng=:${binding.editTextLng.text.toString().toFloat()}
                db.PBBDao().updateSejarah((Sejarah(sejarahId, binding.editTextNomorUrut.text.toString(), binding.editTextNomorC.text.toString(), binding.editTextNomorSeri.text.toString(), binding.editTextNamaObjekPajak.text.toString(), binding.editTextDesaObjekPajak.text.toString(), 0)))
                finish()
            }
        }
    }
}