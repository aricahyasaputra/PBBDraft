package com.example.pbbdraft.ui

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import com.example.pbbdraft.room.PBBDB
import com.example.pbbdraft.room.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.pbbdraft.databinding.ActivityEditBinding
import com.example.pbbdraft.room.PBB
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream


class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    val db by lazy { PBBDB(this) }
    private var pajakId: Int = 0
    private val STORAGE_CODE = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupView()
        setupListener()
        setupPDF()
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
                    PBB(0, binding.editNOP.text.toString(), binding.editBlok.text.toString().toInt(), binding.editPersil.text.toString(), binding.editNama.text.toString(), binding.editAlamatWajibPajak.text.toString(), binding.editAlamatObjekPajak.text.toString(), binding.editKelas.text.toString(), binding.editLuas.text.toString().toInt(), binding.editPajakDitetapkan.text.toString().toInt(), binding.editSejarahTanah.text.toString(), binding.editLat.text.toString().toFloat(), binding.editLng.text.toString().toFloat())
                )
                finish()
            }
        }
        binding.buttonUpdate.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch{
                db.PBBDao().updatePajak(
                    PBB(pajakId, binding.editNOP.text.toString(), binding.editBlok.text.toString().toInt(), binding.editPersil.text.toString(), binding.editNama.text.toString(), binding.editAlamatWajibPajak.text.toString(), binding.editAlamatObjekPajak.text.toString(), binding.editKelas.text.toString(), binding.editLuas.text.toString().toInt(), binding.editPajakDitetapkan.text.toString().toInt(), binding.editSejarahTanah.text.toString(), binding.editLat.text.toString().toFloat(), binding.editLng.text.toString().toFloat())
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
            binding.editBlok.setText( pajaks.blok.toString() )
            binding.editPersil.setText( pajaks.persil)
            binding.editNama.setText( pajaks.namaWajibPajak )
            binding.editAlamatWajibPajak.setText( pajaks.alamatWajibPajak )
            binding.editAlamatObjekPajak.setText( pajaks.alamatObjekPajak )
            binding.editKelas.setText( pajaks.kelas )
            binding.editLuas.setText( pajaks.luasObjekPajak.toString() )
            binding.editPajakDitetapkan.setText( pajaks.pajakDitetapkan.toString() )
            binding.editSejarahTanah.setText( pajaks.sejarahObjekPajak )
            binding.editLat.setText( pajaks.lat.toString() )
            binding.editLng.setText( pajaks.lng.toString() )

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    fun setupPDF(){
        binding.buttonExport.setOnClickListener{
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
        val mFileName = binding.editNama.text.toString();

        val mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + mFileName + System.currentTimeMillis().toString()+ ".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()
            mDoc.add(Paragraph("NOP :" + binding.editNOP.text.toString()))
            mDoc.add(Paragraph("Blok : " + binding.editBlok.text.toString()))
            mDoc.add(Paragraph("Persil : " + binding.editPersil.text.toString()))
            mDoc.add(Paragraph("Nama Wajib Pajak : " + binding.editNama.text.toString()))
            mDoc.add(Paragraph("Alamat Wajib Pajak : " + binding.editAlamatWajibPajak.text.toString()))
            mDoc.add(Paragraph("Alamat Objek Pajak : " + binding.editAlamatObjekPajak.text.toString()))
            mDoc.add(Paragraph("Kelas : " + binding.editKelas.text.toString()))
            mDoc.add(Paragraph("Luas : " + binding.editLuas.text.toString()))
            mDoc.add(Paragraph("Jumlah Pajak : " + binding.editPajakDitetapkan.text.toString()))
            mDoc.add(Paragraph("Sejarah Objek Pajak : " + binding.editSejarahTanah.text.toString()))
            mDoc.close()
            Toast.makeText(this, "$mFileName.pdf\n is create to \n$mFilePath", Toast.LENGTH_SHORT).show()

        }catch (e: Exception){
            Toast.makeText(this, ""+e.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
