package com.example.oscar.hillfort.views.Settings

import android.os.Bundle
import com.example.oscar.hillfort.R
import com.example.oscar.hillfort.views.BaseView

class SettingsView : BaseView() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_view)
    }
}
