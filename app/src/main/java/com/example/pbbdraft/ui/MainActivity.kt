package com.example.pbbdraft.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pbbdraft.databinding.ActivityMainBinding
import com.example.pbbdraft.mapdata.MapAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.searchActivity.setOnClickListener {
            startActivity(
                Intent(applicationContext, SearchActivity::class.java)
            )
        }
        binding.mapActivity.setOnClickListener {
            startActivity(
                Intent(applicationContext, MapActivity::class.java)
            )
            /*startActivity(
                Intent(applicationContext, MapAdapter::class.java)
            )*/
        }

    }


}