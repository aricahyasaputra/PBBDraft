package com.example.pbbdraft.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.pbbdraft.databinding.FragmentArchiveBinding
import java.io.InputStream


class Archive : Fragment() {
    private lateinit var binding: FragmentArchiveBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentArchiveBinding.inflate(inflater, container, false)

        setupPetaBlok()
        setupPetaPersil()


        return binding.root
    }
    private fun setupPetaBlok(){
        val ims:InputStream = requireContext().assets.open("javascriptMap/res/peta-blok-lengkap.jpg")
        val drawwable: Drawable? = Drawable.createFromStream(ims, null)

        binding.photoViewPetabloklengkap.setImageDrawable(drawwable)
        binding.fullscreenPetabloklengkap.setOnClickListener {
            val intent = Intent(context, PetaBlokFullscreenActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupPetaPersil(){
        val ims:InputStream = requireContext().assets.open("javascriptMap/res/peta-persil-lengkap.jpg")
        val drawwable: Drawable? = Drawable.createFromStream(ims, null)

        binding.photoViewPetapersillengkap.setImageDrawable(drawwable)
        binding.fullscreenPetapersillengkap.setOnClickListener {
            val intent = Intent(context, PetaPersilFullscreenActivity::class.java)
            startActivity(intent)
        }
    }
}