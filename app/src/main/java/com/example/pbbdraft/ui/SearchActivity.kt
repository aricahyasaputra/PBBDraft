package com.example.pbbdraft.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val intentType = intent.getIntExtra("intent_type", 0)
        val NOP : String? = intent.getStringExtra("NOP")

        if(NOP != null){
            binding.editTextPajakDicari.setText(NOP)
        }

        val filterList = listOf("Nama", "NOP", "Blok")
        val adapterFilterSpinner = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, filterList)
        binding.filterSpinner.adapter = adapterFilterSpinner

        binding.filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                //Toast.makeText(this@SearchActivity, "${adapterView?.getItemAtPosition(position).toString()}", Toast.LENGTH_SHORT).show()
                val filterDipilih:String = adapterView?.getItemAtPosition(position).toString()
                if (filterDipilih != "Nama"){
                    binding.tvTutorial.visibility = View.GONE
                }
                if (filterDipilih == "Blok"){
                    binding.blokSpinner.visibility = View.VISIBLE
                    binding.buttonSearch.isEnabled = false
                    binding.editTextPajakDicari.isEnabled = false
                    loadPajak("6")
                }else{
                    loadPajak("0")
                    binding.blokSpinner.visibility = View.GONE
                    binding.buttonSearch.isEnabled = true
                    binding.editTextPajakDicari.isEnabled = true
                }
            }
        }

        val PBBList = listOf("blok6", "blok8", "blok9", "blok10", "blok11", "blok12", "blok13", "blok14", "blok15", "blok16", "blok17", "blok18", "blok19", "blok25", "blok26", "blok27")
        val adapterSpinnerBlok = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, PBBList)
        binding.blokSpinner.adapter = adapterSpinnerBlok

        if(intentType == Constant.TYPE_EKSPORT){
            binding.buttonCreate.visibility = View.GONE
            binding.filterSpinner.visibility = View.GONE
            binding.buttonSearch.visibility = View.GONE
            binding.editTextPajakDicari.visibility = View.GONE
        } else if (intentType == Constant.TYPE_CHECK_IS_PAJAK_TERBAYAR || intentType == Constant.TYPE_CHECK_IS_PAJAK_BELUM_TERBAYAR){
            binding.buttonCreate.visibility = View.GONE
            binding.filterSpinner.visibility = View.GONE
            binding.buttonSearch.visibility = View.GONE
            binding.editTextPajakDicari.visibility = View.GONE
            binding.blokSpinner.visibility = View.GONE
            //binding.tvFilterDataBlok.visibility = View.GONE

            val params = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            binding.listPajak.layoutParams = params

        }

        binding.blokSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(binding.filterSpinner.selectedItem.toString() == "Blok" || intentType == Constant.TYPE_EKSPORT){
                    //Toast.makeText(this@SearchActivity, "${adapterView?.getItemAtPosition(position).toString()}", Toast.LENGTH_SHORT).show()
                    val blokDipilih:String = adapterView?.getItemAtPosition(position).toString()
                    val parseBlok = blokDipilih.substring(4)
                    Toast.makeText(this@SearchActivity, parseBlok, Toast.LENGTH_SHORT).show()
                    loadPajak(parseBlok)
                }
            }
        }

        //Menampilkan layout
        setContentView(view)
        //Inisialisasi listener pada tombol tambah
        setupListener()
        //Inisialisasi Recyclerview
        setupRecyclerView()
        setupSearchPajak()
    }

    override fun onStart() {
        val intentType = intent.getIntExtra("intent_type", 0)
        super.onStart()
        if(intentType == Constant.TYPE_CHECK_IS_PAJAK_TERBAYAR || intentType == Constant.TYPE_CHECK_IS_PAJAK_BELUM_TERBAYAR || intentType == Constant.TYPE_EKSPORT){
            loadPajak("0")
            binding.tvTutorial.visibility = View.GONE
        }
    }

    fun loadPajak(blok:String){
        val intentType = intent.getIntExtra("intent_type", 0)
        CoroutineScope(Dispatchers.IO).launch{
            when(intentType){
                2 -> {
                    val pajaks = db.PBBDao().getPajaks(SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE blok=${blok}"))
                    withContext(Dispatchers.Main){
                        if (pajaks.isEmpty() && binding.tvTutorial.visibility == View.GONE){
                            binding.tvEmptySearch.visibility = View.VISIBLE
                        }else{
                            binding.tvEmptySearch.visibility = View.GONE
                        }
                        PBBAdapter.setData( pajaks )
                    }
                }
                Constant.TYPE_EKSPORT -> {
                    val pajaks = db.PBBDao().getPajaks(SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE blok=${blok}"))
                    withContext(Dispatchers.Main){
                        PBBAdapter.setData( pajaks )
                    }
                }
                Constant.TYPE_CHECK_IS_PAJAK_TERBAYAR -> {
                    val pajaks = db.PBBDao().getPajaks(SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE statusPembayaranPajak=1"))
                    withContext(Dispatchers.Main){
                        PBBAdapter.setData( pajaks )
                    }
                }
                Constant.TYPE_CHECK_IS_PAJAK_BELUM_TERBAYAR -> {
                    val pajaks = db.PBBDao().getPajaks(SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE statusPembayaranPajak=0"))
                    withContext(Dispatchers.Main){
                        PBBAdapter.setData( pajaks )
                    }
                }
            }

            //Log.d("MainActivity", "dbResponse: $pajaks")

        }
    }
    fun setupListener(){
        binding.buttonCreate.setOnClickListener {
            intentEdit(0, Constant.TYPE_CREATE)
        }
        binding.tvEmptySearch.visibility = View.GONE
    }

    fun intentEdit(pajakId: Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", pajakId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun intentView(pajakId: Int){
        startActivity(
            Intent(applicationContext, ViewActivity::class.java)
                .putExtra("intent_id", pajakId)
        )
    }
    private fun setupRecyclerView(){
        val intentType = intent.getIntExtra("intent_type", 0)
        PBBAdapter = PBBAdapter(arrayListOf(), object : PBBAdapter.OnAdapterListener{

            override fun onClik(pajak: PBB) {
                when(intentType){
                    Constant.TYPE_UPDATE -> {
                        intentView(pajak.no)
                    }
                    Constant.TYPE_EKSPORT -> {
                        intentEdit(pajak.no, Constant.TYPE_EKSPORT)
                    }
                }

            }

            override fun onUpdate(pajak: PBB) {
                intentEdit(pajak.no, Constant.TYPE_UPDATE)
            }

            override fun onDelete(pajak: PBB) {
                deleteDialog(pajak)
            }

        }, intentType)
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

    private fun setupSearchPajak(){
        binding.buttonSearch.setOnClickListener {
            val pajakDicari:String = binding.editTextPajakDicari.text.toString()

            binding.tvTutorial.visibility = View.GONE

            if (pajakDicari.isEmpty() || pajakDicari.isBlank()){
                binding.layoutEditTextPajakDicari.error = "Input Kosong"
            }else{
                val getFilter = binding.filterSpinner.selectedItem.toString()
                binding.layoutEditTextPajakDicari.error = null

                if(getFilter == "Nama"){
                    CoroutineScope(Dispatchers.IO).launch{
                        val pajaks = db.PBBDao().searchPajak(SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE namaWajibPajak like '%${pajakDicari}%'"))
                        Log.d("MainActivity", "dbResponse: $pajaks")
                        withContext(Dispatchers.Main){
                            if (pajaks.isEmpty()){
                                binding.tvEmptySearch.visibility = View.VISIBLE
                            }else{
                                binding.tvEmptySearch.visibility = View.GONE
                            }
                            PBBAdapter.setData( pajaks )
                        }
                    }
                }else{
                    CoroutineScope(Dispatchers.IO).launch{
                        val pajaks = db.PBBDao().searchPajak(SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE NOP like '%${pajakDicari}%'"))
                        Log.d("MainActivity", "dbResponse: $pajaks")
                        withContext(Dispatchers.Main){
                            if (pajaks.isEmpty()){
                                binding.tvEmptySearch.visibility = View.VISIBLE
                            }else{
                                binding.tvEmptySearch.visibility = View.GONE
                            }
                            PBBAdapter.setData( pajaks )
                        }
                    }
                }
            }
        }
    }

}