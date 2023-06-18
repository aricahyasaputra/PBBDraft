package com.example.pbbdraft.ui

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

        val ims:InputStream = requireContext().assets.open("javascriptMap/res/peta-blok-lengkap.jpg")
        val drawwable: Drawable? = Drawable.createFromStream(ims, null)

        binding.photoViewPetabloklengkap.setImageDrawable(drawwable)


        return binding.root
    }
}