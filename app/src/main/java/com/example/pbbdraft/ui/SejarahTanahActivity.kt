package com.example.pbbdraft.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.pbbdraft.databinding.ActivitySejarahTanahBinding
import com.example.pbbdraft.room.Constant
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.room.PBBDB
import com.example.pbbdraft.room.Sejarah
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SejarahTanahActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySejarahTanahBinding
    lateinit var SejarahAdapter: SejarahAdapter
    val db by lazy { PBBDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySejarahTanahBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupRecyclerView()
        setupSearchSejarah()
        binding.tvEmptySearch.visibility = View.GONE

        binding.fabTambahSejarah.setOnClickListener {
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }
    override fun onStart(){
        super.onStart()
        loadSejarah()
    }

    private fun loadSejarah(){
        CoroutineScope(Dispatchers.IO).launch{
            val sejarah = db.PBBDao().getSejarah(SimpleSQLiteQuery("SELECT * FROM sejarahPBB"))
            withContext(Dispatchers.Main){
                SejarahAdapter.setData( sejarah )
            }
        }
    }

    private fun intentEdit(sejarahId: Int, intentType: Int){
        startActivity(
            Intent(applicationContext, SejarahEditActivity::class.java)
                .putExtra("intent_id", sejarahId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun intentView(sejarahId: Int){
        startActivity(
            Intent(applicationContext, SejarahEditActivity::class.java)
                .putExtra("intent_id", sejarahId)
        )
    }

    private fun setupRecyclerView(){
        SejarahAdapter = SejarahAdapter(arrayListOf(), object : SejarahAdapter.OnAdapterListener{
            override fun onClik(sejarah: Sejarah) {
                intentView(sejarah.no)
            }

            override fun onUpdate(sejarah: Sejarah) {
                intentEdit(sejarah.no, Constant.TYPE_UPDATE)
            }

            override fun onDelete(sejarah: Sejarah) {
                deleteDialog(sejarah)
            }

        })
        binding.listSejarah.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = SejarahAdapter
        }
    }

    private fun deleteDialog(sejarah: Sejarah){
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin hapus ${sejarah.namaObjekPajak}?")
            setNegativeButton("Batal"){ dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus"){ dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch{
                    db.PBBDao().deleteSejarah(sejarah)
                    loadSejarah()
                }
            }
        }
        alertDialog.show()
    }

    private fun setupSearchSejarah(){
        binding.buttonSearch.setOnClickListener {
            val sejarahDicari:String = binding.editTextSejarahDicari.text.toString()
            if(sejarahDicari.isBlank() || sejarahDicari.isEmpty()){
                binding.layoutEditTextSejarahDicari.error = "Input Kosong"
            }else{
                binding.layoutEditTextSejarahDicari.error = null
            }
            CoroutineScope(Dispatchers.IO).launch{
                val sejarah = db.PBBDao().searchSejarah(SimpleSQLiteQuery("SELECT * FROM sejarahPBB WHERE namaObjekPajak like '%${sejarahDicari}%'"))
                withContext(Dispatchers.Main){
                    if (sejarah.isEmpty()){
                        binding.tvEmptySearch.visibility = View.VISIBLE
                    }else{
                        binding.tvEmptySearch.visibility = View.GONE
                    }
                    SejarahAdapter.setData( sejarah )
                }
            }
        }
    }

}