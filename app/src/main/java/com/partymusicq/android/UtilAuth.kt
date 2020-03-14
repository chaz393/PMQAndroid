package com.partymusicq.android

import android.content.Context
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.partymusicq.android.intent.IntentExtra
import com.partymusicq.android.ui.SignInActivity

class UtilAuth() {

    companion object {
        fun signOut(context: Context) {
            AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener {
                    val intent = Intent(context, SignInActivity::class.java)
                    intent.putExtra(IntentExtra.SIGNED_OUT, true)
                    context.startActivity(intent)
                }
        }
    }
}