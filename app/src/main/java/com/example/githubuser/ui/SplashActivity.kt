package com.example.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.R
import com.example.githubuser.ui.settings.SettingModelFactory
import com.example.githubuser.ui.settings.SettingPreferences
import com.example.githubuser.ui.settings.SettingViewModel
import com.example.githubuser.ui.settings.dataStore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, SettingModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        Handler().postDelayed({
            val intentMove = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intentMove)
            finish()
        }, SPLASH_TIMES)
    }

    companion object {
        private const val SPLASH_TIMES: Long = 3000
    }
}