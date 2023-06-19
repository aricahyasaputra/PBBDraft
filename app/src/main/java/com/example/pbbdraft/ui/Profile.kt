package com.example.pbbdraft.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.pbbdraft.databinding.FragmentProfileBinding
import com.example.pbbdraft.room.PBBDB
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.pbbdraft.room.Profile

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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.buttonLogout.setOnClickListener{
            mGoogleSignInClient.signOut().addOnCompleteListener{
                CoroutineScope(Dispatchers.IO).launch {
                    db.PBBDao().updateProfile(
                        Profile(1, "Guest", "Guest", "Guest")
                    )
                }
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }
}