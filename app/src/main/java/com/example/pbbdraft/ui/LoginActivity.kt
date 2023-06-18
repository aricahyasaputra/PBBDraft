package com.example.pbbdraft.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pbbdraft.databinding.ActivityLoginBinding
import com.example.pbbdraft.room.PBBDB
import com.example.pbbdraft.room.Profile
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


const val RC_SIGN_IN = 123

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    val db by lazy { PBBDB(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.buttonLogin.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        binding.buttonLogout.setOnClickListener{
            mGoogleSignInClient.signOut().addOnCompleteListener{
                    binding.textHasil.text = "Berhasil Logout"
                CoroutineScope(Dispatchers.IO).launch {
                    db.PBBDao().updateProfile(
                        Profile(1, "Guest", "Guest", "Guest")
                    )
                }
            }
        }
        binding.buttonCheck.setOnClickListener {
            startActivity(
                Intent(applicationContext, MainActivity::class.java)
            )
        /*
            val profile = db.PBBDao().getProfile()
            Log.i("Isi Profile", profile.toString())
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                Toast.makeText(this, account.displayName, Toast.LENGTH_LONG).show()
            }*/
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            binding.textHasil.text = account.displayName
            CoroutineScope(Dispatchers.IO).launch {
                db.PBBDao().updateProfile(
                    Profile(1, account.displayName?:"Guest", account.email?:"Guest", account.photoUrl.toString())
                )
            }
            startActivity(
                Intent(applicationContext, MainActivity::class.java)
            )
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Gagal", "signInResult:failed code=" + e.getStatusCode())
            binding.textHasil.text = "gagal"
        }
    }
}