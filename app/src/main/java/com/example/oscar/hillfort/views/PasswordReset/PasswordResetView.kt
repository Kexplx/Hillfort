package com.example.oscar.hillfort.views.PasswordReset

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.views.BaseView
import com.example.oscar.hillfort.views.VIEW
import kotlinx.android.synthetic.main.activity_user_forgot_password.*

class PasswordResetView : BaseView() {
    lateinit var presenter: PasswordResetPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_forgot_password)
        init(toolbarPasswordReset, true)
        presenter = initPresenter(PasswordResetPresenter(this)) as PasswordResetPresenter

        btnSendMail.setOnClickListener {
            this.runOnUiThread { progressBar.visibility = View.VISIBLE }
            Thread(Runnable {
                try {
                    Thread.sleep(1000)
                    presenter.doSendPasswordResetEmail(txtSendMail.text.toString())
                    this.runOnUiThread {
                        progressBar.visibility = View.GONE
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }).start()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                navigateTo(VIEW.LOGIN)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}