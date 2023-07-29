package com.example.pbbdraft.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.pbbdraft.room.PBBDB
import com.example.pbbdraft.room.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.pbbdraft.databinding.ActivityEditBinding
import com.example.pbbdraft.room.PBB
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.PdfTemplate
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.io.InputStream


class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    val db by lazy { PBBDB(this) }
    private var pajakId: Int = 0
    private val STORAGE_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        //Menampilkan layout
        setContentView(view)
        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //Menampilkan tombol update/save/export
        setupView()
        //Setup Onclick Listener tombol update/save

        //setupPDF()
        //Inisialisasi pajak id dari activity sebelumnya
        setupListener()
        //Setup Onclick Listener tombol export
        pajakId = intent.getIntExtra("intent_id", 0)
        //Log.i("Pajak id", pajakId.toString())

    }


    fun setupView(){
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                binding.buttonUpdate.visibility = View.GONE
                binding.buttonEksport.visibility = View.GONE
            }
            Constant.TYPE_EKSPORT -> {
                binding.buttonSave.visibility = View.GONE
                binding.buttonUpdate.visibility = View.GONE
                getPajak()
            }
            Constant.TYPE_UPDATE-> {
                binding.buttonSave.visibility = View.GONE
                binding.buttonEksport.visibility = View.GONE
                getPajak()
            }
        }
    }

    fun setupListener(){
        binding.buttonSave.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch{
                db.PBBDao().addPajak(
                    PBB(0, binding.editTextNOP.text.toString(), binding.editTextBlok.text.toString().toInt(), binding.editTextPersil.text.toString(), binding.editTextNamaWajibPajak.text.toString(), binding.editTextAlamatWajibPajak.text.toString(), binding.editTextAlamatObjekPajak.text.toString(), binding.editTextKelas.text.toString(), binding.editTextLuas.text.toString().toInt(), binding.editTextTotalWajibPajak.text.toString().toInt(), binding.editTextSejarahPajak.text.toString(), binding.editTextLat.text.toString().toFloat(), binding.editTextLng.text.toString().toFloat(), 0)
                )
                finish()
            }
        }
        binding.buttonUpdate.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch{
                //, blok=:${binding.editTextBlok.text}, persil=:${binding.editTextPersil.text}, namaWajibPajak=:${binding.editTextNamaWajibPajak.text}, alamatWajibPajak=:${binding.editTextAlamatWajibPajak.text}, alamatObjekPajak=:${binding.editTextAlamatObjekPajak.text}, kelas=:${binding.editTextKelas.text}, luasObjekPajak=:${binding.editTextLuas.text}, pajakDitetapkan=:${binding.editTextTotalWajibPajak.text}, sejarahObjekPajak=:${binding.editTextSejarahPajak.text}, lat=:${binding.editTextLat.text.toString().toFloat()}, lng=:${binding.editTextLng.text.toString().toFloat()}
                db.PBBDao().updatePajak(PBB(pajakId, binding.editTextNOP.text.toString(), binding.editTextBlok.text.toString().toInt(), binding.editTextPersil.text.toString(), binding.editTextNamaWajibPajak.text.toString(), binding.editTextAlamatWajibPajak.text.toString(), binding.editTextAlamatObjekPajak.text.toString(), binding.editTextKelas.text.toString(), binding.editTextLuas.text.toString().toInt(), binding.editTextTotalWajibPajak.text.toString().toInt(), binding.editTextSejarahPajak.text.toString(), binding.editTextLat.text.toString().toFloat(), binding.editTextLng.text.toString().toFloat(), 0))
                startActivity(
                    Intent(applicationContext, ViewActivity::class.java)
                        .putExtra("intent_id", pajakId)
                )
                finish()
            }
        }
        binding.buttonEksport.setOnClickListener {
            setupPDF()
        }
    }
    fun getPajak(){
        pajakId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.Main).launch{
            val pajaks = db.PBBDao().getPajak( SimpleSQLiteQuery("SELECT * FROM pajakPBB WHERE no=${pajakId}") )[0]
            binding.editTextNOP.setText(pajaks.NOP)
            binding.editTextBlok.setText( pajaks.blok.toString() )
            binding.editTextPersil.setText( pajaks.persil)
            binding.editTextNamaWajibPajak.setText( pajaks.namaWajibPajak )
            binding.editTextAlamatWajibPajak.setText( pajaks.alamatWajibPajak )
            binding.editTextAlamatObjekPajak.setText( pajaks.alamatObjekPajak )
            binding.editTextKelas.setText( pajaks.kelas )
            binding.editTextLuas.setText( pajaks.luasObjekPajak.toString() )
            binding.editTextTotalWajibPajak.setText( pajaks.pajakDitetapkan.toString() )
            binding.editTextSejarahPajak.setText( pajaks.sejarahObjekPajak )
            binding.editTextJenisPbb.setText( "PBB" )
            binding.editTextLat.setText( pajaks.lat.toString() )
            binding.editTextLng.setText( pajaks.lng.toString() )

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setupPDF(){
        binding.buttonEksport.setOnClickListener{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, STORAGE_CODE)
                }else{
                    savePDF()
                }
            }else{
                savePDF()
            }
        }
    }

    private fun savePDF(){

        val mDoc = com.itextpdf.text.Document()
        val mFileName = binding.editTextNamaWajibPajak.text.toString();
        val mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + mFileName + System.currentTimeMillis().toString()+ ".pdf"

        try {
            mDoc.open()

            mDoc.add(Paragraph("NOP :" + binding.editTextNOP.text.toString()))
            mDoc.add(Paragraph("Blok : " + binding.editTextBlok.text.toString()))
            mDoc.add(Paragraph("Persil : " + binding.editTextPersil.text.toString()))
            mDoc.add(Paragraph("Nama Wajib Pajak : " + binding.editTextNamaWajibPajak.text.toString()))
            mDoc.add(Paragraph("Alamat Wajib Pajak : " + binding.editTextAlamatWajibPajak.text.toString()))
            mDoc.add(Paragraph("Alamat Objek Pajak : " + binding.editTextAlamatObjekPajak.text.toString()))
            mDoc.add(Paragraph("Kelas : " + binding.editTextKelas.text.toString()))
            mDoc.add(Paragraph("Luas : " + binding.editTextLuas.text.toString()))
            mDoc.add(Paragraph("Jumlah Pajak : " + binding.editTextTotalWajibPajak.text.toString()))
            mDoc.add(Paragraph("Sejarah Objek Pajak : " + binding.editTextSejarahPajak.text.toString()))
            mDoc.close()
            Toast.makeText(this, "$mFileName.pdf\n is create to \n$mFilePath", Toast.LENGTH_SHORT).show()

        }catch (e: Exception){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
