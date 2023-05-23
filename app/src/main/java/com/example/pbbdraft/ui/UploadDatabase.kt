package com.example.pbbdraft.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.Settings
import android.provider.SyncStateContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.pbbdraft.R
import com.example.pbbdraft.databinding.ActivityUploadDatabaseBinding
import com.example.pbbdraft.room.PBB
import com.example.pbbdraft.room.PBBDB
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date


class UploadDatabase : AppCompatActivity() {
    val db by lazy { PBBDB(this) }
    private lateinit var binding: ActivityUploadDatabaseBinding
    private val STORAGE_CODE = 1002
    var resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        Log.i("test upload", "${uri}")
        if (uri != null) {
            importDatabase(uri)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_database)
        binding = ActivityUploadDatabaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.uploadDatabase.setOnClickListener {
            //

            setupCSV()
            val pajaks = db.PBBDao().getPajaksnow()
            val mFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "backup" + System.currentTimeMillis().toString() + ".xlsx"

            val xlWb = XSSFWorkbook()
            //Instantiate Excel worksheet:
            val xlWs = xlWb.createSheet()


            //Write text value to cell located at ROW_NUMBER / COLUMN_NUMBER:

            pajaks.forEachIndexed { index, element ->
                val row = xlWs.createRow(index)
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
            }

            //Write file:
            val outputStream = FileOutputStream(mFilePath)
            xlWb.write(outputStream)
            xlWb.close()
            /*



            csvPrinter.flush()
            csvPrinter.close()
            csvReader {  }*/
        }
        binding.importDatabase.setOnClickListener {
            resultLauncher.launch("*/*")
            //openFile()
        }
    }

    private fun setupCSV(){
        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            val permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, STORAGE_CODE)
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
        CoroutineScope(Dispatchers.IO).launch{
            while (i<= xlRows){
                db.PBBDao().addPajak(
                    PBB(0, xlWs.getRow(i).getCell(1).toString(), xlWs.getRow(i).getCell(2).toString().toInt(), xlWs.getRow(i).getCell(3).toString(), xlWs.getRow(i).getCell(4).toString(), xlWs.getRow(i).getCell(5).toString(), xlWs.getRow(i).getCell(6).toString(), xlWs.getRow(i).getCell(7).toString(), xlWs.getRow(i).getCell(8).toString().toInt(), xlWs.getRow(i).getCell(9).toString().toInt(), xlWs.getRow(i).getCell(10).toString(), xlWs.getRow(i).getCell(11).toString().toFloat(), xlWs.getRow(i).getCell(12).toString().toFloat())
                )
                i++
            }
            finish()
        }
    }

}