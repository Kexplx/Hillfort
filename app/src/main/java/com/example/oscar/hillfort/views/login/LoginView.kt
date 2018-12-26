package com.example.oscar.hillfort.views.login

import android.os.Bundle
import android.view.View
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.views.BaseView
import com.example.oscar.hillfort.views.VIEW
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginView : BaseView() {

    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init(toolbar, false)

        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

        signUp.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            } else {
                presenter.doSignUp(email, password)
            }
        }

        txtForgotPassword.setOnClickListener {
            navigateTo(VIEW.RESETPASSWORD)
        }

        logIn.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            if (email == "" || password == "") {
                toast("Please provide email + password")
            } else {
                presenter.doLogin(email, password)
            }
        }
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}