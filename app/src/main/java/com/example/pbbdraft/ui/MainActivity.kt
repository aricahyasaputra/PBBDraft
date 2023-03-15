package com.example.pbbdraft

import android.content.DialogInterface
import android.content.Intent
import android.graphics.DiscretePathEffect
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pbbdraft.room.Constant
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.room.PBBDB
import com.example.pbbdraft.ui.PBBAdapter
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    val db by lazy { PBBDB(this) }
    lateinit var PBBAdapter: PBBAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupListener()
        setupRecyclerView()

    }
    override fun onStart(){
        super.onStart()
        loadPajak()
    }

    fun loadPajak(){
        CoroutineScope(Dispatchers.IO).launch{
            val pajaks = db.PBBDao().getPajaks()
            Log.d("MainActivity", "dbResponse: $pajaks")
            withContext(Dispatchers.Main){
                PBBAdapter.setData( pajaks )
            }
        }
    }

    fun setupListener(){
        button_create.setOnClickListener {
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

    private fun setupRecyclerView(){
        PBBAdapter = PBBAdapter(arrayListOf(), object : PBBAdapter.OnAdapterListener{
            override fun onClik(pajak: PBB) {
                intentEdit(pajak.id, Constant.TYPE_READ)
            }

            override fun onUpdate(pajak: PBB) {
                intentEdit(pajak.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(pajak: PBB) {
                deleteDialog(pajak)
            }

        })
        list_pajak.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = PBBAdapter
        }
    }

    private fun deleteDialog(pajak: PBB){
        val alertDialog = AlertDialog.Builder(this)
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
                    loadPajak()
                }
            }
        }
        alertDialog.show()
    }
}