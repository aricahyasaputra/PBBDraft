package com.example.pbbdraft.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pbbdraft.databinding.ActivityMainBinding
import com.example.pbbdraft.databinding.FragmentHomeBinding


class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)


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
            Toast.makeText(context, "QR Gagal Eror Camera API", Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

}