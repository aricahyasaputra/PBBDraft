package com.example.pbbdraft.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.Settings
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.pbbdraft.R
import com.example.pbbdraft.databinding.ActivityUploadDatabaseBinding
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.room.PBBDB
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class UploadDatabase : AppCompatActivity() {
    val db by lazy { PBBDB(this) }
    private lateinit var binding: ActivityUploadDatabaseBinding
    private val STORAGE_CODE = 1002
    private var progresBar = 0
    var handler: Handler? = null
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        Log.i("test upload", "${uri}")
        if (uri != null) {
            importDatabase(uri)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadDatabaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.uploadDatabase.setOnClickListener {
            exportDatabase()
        }
        binding.importDatabase.setOnClickListener {
            progresBar = 0
            resultLauncher.launch("*/*")
            //openFile()
        }
        binding.backupDatabase.setOnClickListener {
            val mFilePath: String = exportDatabase()
            uploadGoogleDrive(mFilePath)
        }

        val profile = db.PBBDao().getProfile()

        if (profile.nama == "Guest"){
            binding.backupDatabase.isEnabled = false
        }
    }

    private fun setupCSV(){
        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, STORAGE_CODE)
        }
    }

    private fun exportDatabase(): String{
        progresBar = 0

        setupCSV()
        val pajaks = db.PBBDao().getPajaksnow(SimpleSQLiteQuery("SELECT * FROM pajakPBB"))
        val date = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis())
        val mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "backup" + date.toString() + ".xlsx"

        val xlWb = XSSFWorkbook()
        //Instantiate Excel worksheet:
        val xlWs = xlWb.createSheet()

        binding.progressBarSecondary.min = 0
        binding.progressBarSecondary.max = pajaks.size

        //Write text value to cell located at ROW_NUMBER / COLUMN_NUMBER:

        pajaks.forEachIndexed { index, element ->
            val row = xlWs.createRow(index)
            binding.progressBarSecondary.progress++
            binding.textViewPrimary.setText("Eksporting data ${index} dari ${pajaks.size}")
            row.createCell(0).setCellValue(element.no.toString())
            row.createCell(1).setCellValue(element.NOP)
            row.createCell(2).setCellValue(element.blok.toString())
            row.createCell(3).setCellValue(element.persil)
            row.createCell(4).setCellValue(element.namaWajibPajak)
            row.createCell(5).setCellValue(element.alamatWajibPajak)
            row.createCell(6).setCellValue(element.alamatObjekPajak)
            row.createCell(7).setCellValue(element.kelas)
            row.createCell(8).setCellValue(element.luasObjekPajak.toString())
            row.createCell(9).setCellValue(element.pajakDitetapkan.toString())
            row.createCell(10).setCellValue(element.sejarahObjekPajak)
            row.createCell(11).setCellValue(element.lat.toString())
            row.createCell(12).setCellValue(element.lng.toString())
            row.createCell(13).setCellValue(element.statusPembayaranPajak.toString())
        }

        //Write file:
        val outputStream = FileOutputStream(mFilePath)
        xlWb.write(outputStream)
        xlWb.close()
        return  mFilePath

        binding.textViewPrimary.setText("Eksport data Sukses")
        /*



        csvPrinter.flush()
        csvPrinter.close()
        csvReader {  }*/
    }

    private fun uploadGoogleDrive(mFilePath: String){
        CoroutineScope(Dispatchers.IO).launch {
            GoogleSignIn.getLastSignedInAccount(this@UploadDatabase)?.let { googleAccount ->

                // get credentials
                val credential = GoogleAccountCredential.usingOAuth2(
                    this@UploadDatabase, listOf(DriveScopes.DRIVE, DriveScopes.DRIVE_FILE)
                )
                credential.selectedAccount = googleAccount.account!!

                // get Drive Instance
                val drive = Drive
                    .Builder(
                        AndroidHttp.newCompatibleTransport(),
                        JacksonFactory.getDefaultInstance(),
                        credential
                    )
                    .setApplicationName("PBB Draft")
                    .build()

                val gFolder = com.google.api.services.drive.model.File()
                // Set file name and MIME
                gFolder.name = "My Cool Folder Name"
                gFolder.mimeType = "application/vnd.google-apps.folder"

                // You can also specify where to create the new Google folder
                // passing a parent Folder Id
                val parents: MutableList<String> = ArrayList(1)
                parents.add("My Cool Folder Name")
                gFolder.parents = parents
                //drive.Files().create(gFolder).setFields("id").execute()

                val gfile = com.google.api.services.drive.model.File()
                val fileContent = FileContent("application/vnd.ms-excel", File(mFilePath))
                gfile.name = "backup" + System.currentTimeMillis().toString() + ".xlsx"
                drive.Files().create(gfile, fileContent).setFields("id").execute()

            }
        }
    }
    private fun importDatabase(uri: Uri){
        db.PBBDao().deletePajakAll()
        db.PBBDao().resetPrimaryKey()

        val inputStream = contentResolver.openInputStream(uri)
        val xlWb = WorkbookFactory.create(inputStream)
        val xlWs = xlWb.getSheetAt(0)
        val xlRows = xlWs.lastRowNum
        var i = 0
        binding.progressBarSecondary.min = 0
        binding.progressBarSecondary.max = xlRows

        CoroutineScope(Dispatchers.Main).launch{
            while (i<= xlRows){
                db.PBBDao().addPajak(
                    PBB(0, xlWs.getRow(i).getCell(1).toString(), xlWs.getRow(i).getCell(2).toString().toInt(), xlWs.getRow(i).getCell(3).toString(), xlWs.getRow(i).getCell(4).toString(), xlWs.getRow(i).getCell(5).toString(), xlWs.getRow(i).getCell(6).toString(), xlWs.getRow(i).getCell(7).toString(), xlWs.getRow(i).getCell(8).toString().toInt(), xlWs.getRow(i).getCell(9).toString().toInt(), xlWs.getRow(i).getCell(10).toString(), xlWs.getRow(i).getCell(11).toString().toFloat(), xlWs.getRow(i).getCell(12).toString().toFloat(), xlWs.getRow(i).getCell(13).toString().toInt())
                )
                binding.textViewPrimary.setText("Importing data ${i} dari ${xlRows}")
                binding.progressBarSecondary.progress = i
                i++
            }
            binding.textViewPrimary.setText("Import data Sukses")
        }
    }

}