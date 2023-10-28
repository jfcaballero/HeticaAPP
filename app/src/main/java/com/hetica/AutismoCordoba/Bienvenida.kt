package com.hetica.AutismoCordoba

import PrefManager
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Bienvenida : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)
        prefManager = PrefManager(this)
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen()
            finish()
        }
        val btnEntendido: Button = findViewById(R.id.btn_entendido)
        btnEntendido.setOnClickListener {
            prefManager.setFirstTimeLaunch(false)
            launchHomeScreen()
        }
    }
    private fun launchHomeScreen() {
        val intent: Intent = if (prefManager.isFirstTimeLaunch()) {
            Intent(this, Bienvenida::class.java)
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}