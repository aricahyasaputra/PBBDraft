package com.example.pbbdraft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.room.PBBDB
import com.example.pbbdraft.room.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.pbbdraft.databinding.ActivityEditBinding

private lateinit var binding: ActivityEditBinding
class EditActivity : AppCompatActivity() {

    val db by lazy { PBBDB(this) }
    private var pajakId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
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
                binding.buttonUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
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

    fun setupListener(){
        binding.buttonSave.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                db.PBBDao().addPajak(
                    PBB(0, binding.editNOP.text.toString(), binding.editNama.text.toString(), binding.editAlamat.text.toString(), binding.editLuas.text.toString().toInt(), binding.editPajakDitetapkan.text.toString().toInt())
                )
                finish()
            }
        }
        binding.buttonUpdate.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                db.PBBDao().updatePajak(
                    PBB(pajakId, binding.editNOP.text.toString(), binding.editNama.text.toString(), binding.editAlamat.text.toString(), binding.editLuas.text.toString().toInt(), binding.editPajakDitetapkan.text.toString().toInt())
                )
                finish()
            }
        }
    }
    fun getPajak(){
        pajakId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch{
            val pajaks = db.PBBDao().getPajak( pajakId )[0]
            binding.editNOP.setText( pajaks.NOP )
            binding.editNama.setText( pajaks.nama )
            binding.editAlamat.setText( pajaks.alamat )
            binding.editLuas.setText( pajaks.luasTanah.toString() )
            binding.editPajakDitetapkan.setText( pajaks.pajakDitetapkan.toString() )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
