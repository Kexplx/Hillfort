package com.example.oscar.hillfort.views.Settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.views.BaseView
import kotlinx.android.synthetic.main.activity_settings_view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async


class SettingsView : BaseView() {

    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_view)
        init(toolbarSettings, upEnabled = true)

        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        async(UI) {
            txtUsername.text = presenter.doGetUsername()
            txtNumberOfSites.text = presenter.doGetTotalNumberOfSites().toString()
            txtNumberSitesVisited.text = presenter.doGetTotalNumberOfVisitedSites().toString()
            txtNumberFavSites.text = presenter.doGetNumberOfFavourites().toString()
        }

        txtPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btnUpdatePassword.isEnabled = s.toString().length > 7
            }
        })

        btnUpdatePassword.setOnClickListener {
            this.runOnUiThread { progressBar.visibility = View.VISIBLE }

            Thread(Runnable {
                try {
                    Thread.sleep(1000)
                    presenter.doChangePassword(txtPassword.text.toString())
                    this.runOnUiThread {
                        progressBar.visibility = View.GONE
                        txtPassword.setText("")
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }).start()
        }
    }
}
