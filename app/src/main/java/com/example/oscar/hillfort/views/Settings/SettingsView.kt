package com.example.oscar.hillfort.views.Settings

import android.os.Bundle
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
        super.init(toolbarSettings, upEnabled = true)

        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        async(UI) {
            txtUsername.text = presenter.doGetUsername()
            txtNumberOfSites.text = presenter.doGetTotalNumberOfSites().toString()
            txtNumberSitesVisited.text = presenter.doGetTotalNumberOfVisitedSites().toString()
        }

        btnUpdatePassword.setOnClickListener {
            if (txtPassword.length() > 5) {
                async(UI) {
                    presenter.doChangePassword(txtPassword.text.toString())
                    txtPassword.setText("")
                }
            }
        }
    }
}
