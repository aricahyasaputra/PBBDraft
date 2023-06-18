package com.example.pbbdraft.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.pbbdraft.databinding.ActivitySearchBinding
import com.example.pbbdraft.room.Constant
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.room.PBBDB
import com.google.android.material.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchBinding
    lateinit var PBBAdapter: PBBAdapter
    val db by lazy { PBBDB(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root

        val PBBList = listOf("blok6", "blok8", "blok9", "blok10", "blok11", "blok12", "blok13", "blok14", "blok15", "blok16", "blok17", "blok18", "blok19", "blok25", "blok26", "blok27")
        val adapterSpinnerBlok = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, PBBList)
        binding.blokSpinner.adapter = adapterSpinnerBlok



        binding.blokSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Toast.makeText(this@SearchActivity, "${adapterView?.getItemAtPosition(position).toString()}", Toast.LENGTH_SHORT).show()
                val blokDipilih:String = adapterView?.getItemAtPosition(position).toString()
                val parseBlok = blokDipilih.substring(4)
                Toast.makeText(this@SearchActivity, "${parseBlok}", Toast.LENGTH_SHORT).show()
                loadPajak(parseBlok)
            }
        }

        //Menampilkan layout
        setContentView(view)
        //Inisialisasi listener pada tombol tambah
        setupListener()
        //Inisialisasi Recyclerview
        setupRecyclerView()
    }
    override fun onStart(){
        super.onStart()
        val getBlok = binding.blokSpinner.selectedItem.toString()
        val parseBlok = getBlok.substring(4)
        //load data pajak dari room database
        loadPajak(parseBlok)
    }

    fun loadPajak(blok:String){
        CoroutineScope(Dispatchers.IO).launch{
            val pajaks = db.PBBDao().getPajaks(SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE blok=${blok}"))
            Log.d("MainActivity", "dbResponse: $pajaks")
            withContext(Dispatchers.Main){
                PBBAdapter.setData( pajaks )
            }
        }
    }
    fun setupListener(){
        binding.buttonCreate.setOnClickListener {
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(pajakId: Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", pajakId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun intentView(pajakId: Int, intentType: Int, pajakBlok:String){
        startActivity(
            Intent(applicationContext, ViewActivity::class.java)
                .putExtra("intent_id", pajakId)
                .putExtra("intent_type", intentType)
        )
    }
    private fun setupRecyclerView(){
        PBBAdapter = PBBAdapter(arrayListOf(), object : PBBAdapter.OnAdapterListener{
            override fun onClik(pajak: PBB) {
                intentView(pajak.no, Constant.TYPE_READ, binding.blokSpinner.selectedItem.toString())
            }

            override fun onUpdate(pajak: PBB) {
                intentEdit(pajak.no, Constant.TYPE_UPDATE)
            }

            override fun onDelete(pajak: PBB) {
                deleteDialog(pajak)
            }

        })
        binding.listPajak.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = PBBAdapter
        }
    }

    private fun deleteDialog(pajak: PBB){
        val alertDialog = AlertDialog.Builder(this)
        val getBlok = binding.blokSpinner.selectedItem.toString()
        val parseBlok = getBlok.substring(4)

        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin hapus ${pajak.NOP}?")
            setNegativeButton("Batal"){ dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus"){ dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch{
                    db.PBBDao().deletePajak(pajak)
                    loadPajak(parseBlok)
                }
            }
        }
        alertDialog.show()
    }
}