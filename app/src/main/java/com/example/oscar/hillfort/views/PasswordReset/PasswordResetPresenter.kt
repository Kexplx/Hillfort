package com.example.oscar.hillfort.views.PasswordReset

import android.content.ContentValues.TAG
import android.text.TextUtils
import android.util.Log
import com.example.oscar.hillfort.views.BasePresenter
import com.example.oscar.hillfort.views.VIEW
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast

class PasswordResetPresenter(view: PasswordResetView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doSendPasswordResetEmail(email: String) {
        if (!TextUtils.isEmpty(email)) {
            auth
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val message = "Email sent."
                        Log.d(TAG, message)
                        view?.toast(message)
                        view?.navigateTo(VIEW.LOGIN)
                    } else {
                        Log.w(TAG, task.exception!!.message)
                        view?.toast("No user found with this email.")
                    }
                }
        } else {
            view?.toast("Enter Email")
        }
    }

}