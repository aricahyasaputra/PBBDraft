package com.example.pbbdraft.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.pbbdraft.databinding.FragmentProfileBinding
import com.example.pbbdraft.room.PBBDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Profile : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    val db by lazy { PBBDB(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val profile = db.PBBDao().getProfile()
        binding.namaLengkap.text = profile[0].nama
        binding.email.text = profile[0].email
        binding.profilePicture.load(profile[0].url)


        return binding.root
    }
}