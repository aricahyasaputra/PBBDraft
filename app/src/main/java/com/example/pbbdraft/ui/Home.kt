package com.example.pbbdraft.ui

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pbbdraft.databinding.FragmentHomeBinding
import com.example.pbbdraft.room.Constant
import com.example.pbbdraft.room.PBBDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale


class Home : Fragment() {
    val db by lazy { PBBDB(requireContext()) }
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupDashboard()

        binding.searchActivity.setOnClickListener{
            val intent = Intent(context, SearchActivity::class.java)
                .putExtra("intent_type", 2)
            startActivity(intent)
        }
        binding.mapActivity.setOnClickListener {
            val intent = Intent(context, MapActivity::class.java)
            startActivity(intent)
        }

        binding.uploadDatabaseActivity.setOnClickListener{
            val intent = Intent(context, UploadDatabase::class.java)
                .putExtra("intent_type", 0)
            startActivity(intent)
        }

        binding.exportPdfActivity.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.qrActivity.setOnClickListener {
            val intent = Intent(context, CameraActivity::class.java)
            startActivity(intent)
        }

        binding.sejarahActivity.setOnClickListener {
            val intent = Intent(context, SejarahTanahActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun setupDashboard(){
        val localeID = Locale("in", "ID")
        val nf: NumberFormat = NumberFormat.getCurrencyInstance(localeID)

        CoroutineScope(Dispatchers.Main).launch {
            val totalPajakTertanggung = db.PBBDao().updateDashboardPajakTertanggung()?:0
            binding.totalPajakTertanggung.text = nf.format(totalPajakTertanggung)
            val totalPajakTerbayar = db.PBBDao().updateDashboardPajakTerbayar(1)?:0
            binding.textViewTotalPajakTerbayar.text = nf.format(totalPajakTerbayar)
            val totalPajakBelumTerbayar = db.PBBDao().updateDashboardPajakTerbayar(0)?:0
            binding.textViewTotalPajakBelumTerbayar.text = nf.format(totalPajakBelumTerbayar)

            val totalPetakTertanggung = db.PBBDao().updateDashboardPetakTertanggung()?:0
            binding.textViewPetakTertanggung.text = "${totalPetakTertanggung} Petak"

            val totalPetakTerbayar = db.PBBDao().updateDashboardPetakTerbayar(1)?:0
            binding.textViewTotalPetakTerbayar.text = "${totalPetakTerbayar} Petak"
            binding.textViewTotalPetakTerbayar.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.textViewTotalPetakTerbayar.setOnClickListener {
                val intent = Intent(context, SearchActivity::class.java)
                    .putExtra("intent_type", Constant.TYPE_CHECK_IS_PAJAK_TERBAYAR)
                startActivity(intent)
            }

            val totalPetakBelumTerbayar = db.PBBDao().updateDashboardPetakTerbayar(0)?:0
            binding.textViewTotalPetakBelumTerbayar.text = "${totalPetakBelumTerbayar} Petak"
            binding.textViewTotalPetakBelumTerbayar.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            binding.textViewTotalPetakBelumTerbayar.setOnClickListener {
                val intent = Intent(context, SearchActivity::class.java)
                    .putExtra("intent_type", Constant.TYPE_CHECK_IS_PAJAK_BELUM_TERBAYAR)
                startActivity(intent)
            }
            //Toast.makeText(requireContext(), "Dashboard Updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        setupDashboard()
    }
}