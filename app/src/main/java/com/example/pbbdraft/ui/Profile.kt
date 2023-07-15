package com.example.pbbdraft.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import coil.load
import com.example.pbbdraft.R
import com.example.pbbdraft.databinding.FragmentProfileBinding
import com.example.pbbdraft.room.PBBDB
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.pbbdraft.room.Profile
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes

class Profile : Fragment() {
    private val STORAGE_CODE = 1002
    private lateinit var binding: FragmentProfileBinding
    val db by lazy { PBBDB(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupView()
        setupListener()

        return binding.root
    }


    private fun setupView(){
        val profile = db.PBBDao().getProfile()
        binding.namaLengkap.text = profile[0].nama
        binding.email.text = profile[0].email
        binding.profilePicture.load(profile[0].url)

        if (profile[0].nama == "Guest"){
            binding.buttonLogout.visibility = View.GONE
            binding.buttonBackup.isEnabled = false
        }else{

            binding.buttonLogin.visibility = View.GONE
        }
    }

    private fun View.setMargins(
        left: Int = this.marginLeft,
        top: Int = this.marginTop,
        right: Int = this.marginRight,
        bottom: Int = this.marginBottom,
    ) {
        layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            setMargins(left, top, right, bottom)
        }
    }

    private fun setupListener(){
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

        binding.buttonBackup.setOnClickListener {
            val intent = Intent(context, UploadDatabase::class.java)
            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}