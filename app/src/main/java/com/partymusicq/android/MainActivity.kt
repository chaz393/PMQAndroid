package com.partymusicq.android

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val SIGNIN = 123
    private lateinit var logOutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logOutButton = findViewById(R.id.log_out_button)
        logOutButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                doSignOut()
            }
        })
        doSignIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGNIN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                //TODO: logged in, do things now
            } else {
                // TODO: sign in failed, probably just crash the app for fun here
            }
        }
    }

    private fun doSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            SIGNIN)
    }

    private fun doSignOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                doSignIn()
            }
    }
}
