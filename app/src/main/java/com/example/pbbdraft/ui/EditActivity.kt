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

    override fun onResume() {
        super.onResume()

        val profile = db.PBBDao().getProfile()

        if(profile.blok != 0){
            binding.editTextBlok.setText( profile.blok.toString() )
        }
        if(profile.lat.toString() != "0.0"){
            binding.editTextLat.setText( profile.lat.toString() )
        }
        if(profile.lng.toString() != "0.0"){
            binding.editTextLng.setText( profile.lng.toString() )
        }
        if(profile.alamatWajibPajak != "Kosong"){
            //Log.i("hasil", "onResume: ${profile.alamatWajibPajak}")
            binding.editTextAlamatWajibPajak.setText( profile.alamatWajibPajak )
        }
        if(profile.alamatObjekPajak != "Kosong"){
            binding.editTextAlamatObjekPajak.setText( profile.alamatObjekPajak )
        }
        if(profile.NOP != "Kosong"){
            binding.editTextNOP.setText( profile.NOP )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        db.PBBDao().updateLatLng(0.toString().toFloat(), 0.toString().toFloat(), 0.toString().toInt())
        db.PBBDao().updateDetectionText("Kosong", "Kosong", "Kosong")
    }

    private fun setupView(){
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
        binding.editTextJenisPbb.isEnabled = false
    }

    private fun setupListener(){
        binding.buttonSave.setOnClickListener{
            erorrCheckingTextInput()

            if(binding.editTextNOP.text.toString().isNotBlank() && binding.editTextNOP.text.toString().isNotEmpty()
                && binding.editTextPersil.text.toString().isNotBlank() && binding.editTextPersil.text.toString().isNotEmpty()
                && binding.editTextAlamatObjekPajak.text.toString().isNotBlank() && binding.editTextAlamatObjekPajak.text.toString().isNotEmpty()
                && binding.editTextLat.text.toString().isNotBlank() && binding.editTextLat.text.toString().isNotEmpty()
                && binding.editTextLng.text.toString().isNotBlank() && binding.editTextLng.text.toString().isNotEmpty()
                && binding.editTextBlok.text.toString().isNotBlank() && binding.editTextBlok.text.toString().isNotEmpty() && binding.editTextBlok.text.toString() != "0"
                && binding.editTextNamaWajibPajak.text.toString().isNotBlank() && binding.editTextNamaWajibPajak.text.toString().isNotEmpty()
                && binding.editTextAlamatWajibPajak.text.toString().isNotBlank() && binding.editTextAlamatWajibPajak.text.toString().isNotEmpty()
                && binding.editTextLuas.text.toString().isNotBlank() && binding.editTextLuas.text.toString().isNotEmpty()
                && binding.editTextKelas.text.toString().isNotBlank() && binding.editTextKelas.text.toString().isNotEmpty()
                && binding.editTextTotalWajibPajak.text.toString().isNotBlank() && binding.editTextTotalWajibPajak.text.toString().isNotEmpty()
                && binding.editTextSejarahPajak.text.toString().isNotBlank() && binding.editTextSejarahPajak.text.toString().isNotEmpty()
            ){
                CoroutineScope(Dispatchers.Main).launch{
                    db.PBBDao().addPajak(
                        PBB(0, binding.editTextNOP.text.toString(), binding.editTextBlok.text.toString().toInt(), binding.editTextPersil.text.toString(), binding.editTextNamaWajibPajak.text.toString(), binding.editTextAlamatWajibPajak.text.toString(), binding.editTextAlamatObjekPajak.text.toString(), binding.editTextKelas.text.toString(), binding.editTextLuas.text.toString().toInt(), binding.editTextTotalWajibPajak.text.toString().toInt(), binding.editTextSejarahPajak.text.toString(), binding.editTextLat.text.toString().toFloat(), binding.editTextLng.text.toString().toFloat(), 0)
                    )
                    finish()
                }
            }else{
                Toast.makeText(this, "Gagal menambahkan data, mohon periksa masukan anda", Toast.LENGTH_SHORT).show()
            }

        }

        binding.textInputLayoutAlamatWajibPajak.setEndIconOnClickListener{
            //Toast.makeText(this, "Tombol qr ditekan", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@EditActivity, CameraActivity::class.java)
                .putExtra("intent_type", "AlamatWajibPajak")
            startActivity(intent)
        }
        binding.textInputLayoutAlamatObjekPajak.setEndIconOnClickListener {
            val intent = Intent(this@EditActivity, CameraActivity::class.java)
                .putExtra("intent_type", "AlamatObjekPajak")
            startActivity(intent)
        }

        binding.textInputLayoutNOP.setEndIconOnClickListener {
            val intent = Intent(this@EditActivity, CameraActivity::class.java)
                .putExtra("intent_type", "NOP")
            startActivity(intent)
        }

        binding.ivGetKoordinat.setOnClickListener {
            val intent = Intent(this@EditActivity, KoordinatActivity::class.java)
            startActivity(intent)
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
    private fun getPajak(){
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

    private fun erorrCheckingTextInput(){
        if(binding.editTextNOP.text.toString().isBlank() || binding.editTextNOP.text.toString().isEmpty()){
            binding.textInputLayoutNOP.error = "NOP kosong"
        }else if (binding.editTextNOP.text.toString().length != 10){
            binding.textInputLayoutNOP.error = "format NOP tidak sesuai"
        }else{
            binding.textInputLayoutNOP.error = null
        }

        if(binding.editTextPersil.text.toString().isBlank() || binding.editTextPersil.text.toString().isEmpty()){
            binding.textInputLayoutPersil.error = "persil kosong"
        }else{
            binding.textInputLayoutPersil.error = null
        }

        if(binding.editTextAlamatObjekPajak.text.toString().isBlank() || binding.editTextAlamatObjekPajak.text.toString().isEmpty()){
            binding.textInputLayoutAlamatObjekPajak.error = "alamat objek pajak kosong"
        }else{
            binding.textInputLayoutAlamatObjekPajak.error = null
        }

        if(binding.editTextLat.text.toString().isBlank() || binding.editTextLat.text.toString().isEmpty() || binding.editTextLat.text.toString() == "0.0"){
            binding.textInputLayoutLat.error = "latitude kosong"
        }else{
            binding.textInputLayoutLat.error = null
        }

        if(binding.editTextLng.text.toString().isBlank() || binding.editTextLng.text.toString().isEmpty() || binding.editTextLat.text.toString() == "0.0"){
            binding.textInputLayoutLng.error = "longitude kosong"
        }else{
            binding.textInputLayoutLng.error = null
        }

        if(binding.editTextBlok.text.toString().isBlank() || binding.editTextBlok.text.toString().isEmpty() || binding.editTextBlok.text.toString() == "0"){
            binding.textInputLayoutEditTextBlok.error = "blok kosong"
        }else{
            binding.textInputLayoutEditTextBlok.error = null
        }

        if(binding.editTextNamaWajibPajak.text.toString().isBlank() || binding.editTextNamaWajibPajak.text.toString().isEmpty()){
            binding.textInputLayoutNamaWajibPajak.error = "nama wajib pajak kosong"
        }else{
            binding.textInputLayoutNamaWajibPajak.error = null
        }

        if(binding.editTextAlamatWajibPajak.text.toString().isBlank() || binding.editTextAlamatWajibPajak.text.toString().isEmpty()){
            binding.textInputLayoutAlamatWajibPajak.error = "nama wajib pajak kosong"
        }else{
            binding.textInputLayoutAlamatWajibPajak.error = null
        }

        if(binding.editTextLuas.text.toString().isBlank() || binding.editTextLuas.text.toString().isEmpty()){
            binding.textInputLayoutLuas.error = "luas kosong"
        }else{
            binding.textInputLayoutLuas.error = null
        }

        if(binding.editTextKelas.text.toString().isBlank() || binding.editTextKelas.text.toString().isEmpty()){
            binding.textInputLayoutKelas.error = "kelas kosong"
        }else{
            binding.textInputLayoutKelas.error = null
        }

        if(binding.editTextTotalWajibPajak.text.toString().isBlank() || binding.editTextTotalWajibPajak.text.toString().isEmpty()){
            binding.textInputLayoutTotalWajibPajak.error = "total pajak kosong"
        }else{
            binding.textInputLayoutTotalWajibPajak.error = null
        }

        if(binding.editTextSejarahPajak.text.toString().isBlank() || binding.editTextSejarahPajak.text.toString().isEmpty()){
            binding.textInputLayoutSejarahPajak.error = "sejarah pajak kosong"
        }else{
            binding.textInputLayoutSejarahPajak.error = null
        }
    }

    private fun setupPDF(){
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
