package com.example.pbbdraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.room.PBBDB
import com.example.pbbdraft.room.Constant
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditActivity : AppCompatActivity() {

    val db by lazy { PBBDB(this) }
    private var pajakId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupView()
        setupListener()
        pajakId = intent.getIntExtra("intent_id", 0)
        Toast.makeText(this, pajakId.toString(), Toast.LENGTH_SHORT).show()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getPajak()
            }
            Constant.TYPE_UPDATE-> {
                button_save.visibility = View.GONE
                getPajak()
            }
        }
    }

    fun setupListener(){
        button_save.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                db.PBBDao().addPajak(
                    PBB(0, edit_NOP.text.toString(), edit_nama.text.toString(), edit_alamat.text.toString(), edit_luas.text.toString().toInt(), edit_pajak_ditetapkan.text.toString().toInt())
                )
                finish()
            }
        }
        button_update.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                db.PBBDao().updatePajak(
                    PBB(pajakId, edit_NOP.text.toString(), edit_nama.text.toString(), edit_alamat.text.toString(), edit_luas.text.toString().toInt(), edit_pajak_ditetapkan.text.toString().toInt())
                )
                finish()
            }
        }
    }
    fun getPajak(){
        pajakId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch{
            val pajaks = db.PBBDao().getPajak( pajakId )[0]
            edit_NOP.setText( pajaks.NOP )
            edit_nama.setText( pajaks.nama )
            edit_alamat.setText( pajaks.alamat )
            edit_luas.setText( pajaks.luasTanah.toString() )
            edit_pajak_ditetapkan.setText( pajaks.pajakDitetapkan.toString() )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
