package com.hetica.AutismoCordoba

import PrefManager
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Bienvenida : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var db: AdminSQLiteOpenHelperAsig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)
        prefManager = PrefManager(this)
        db = AdminSQLiteOpenHelperAsig(this)

        if (countData() == 0) {
            // Si no hay asignaturas en la base de datos, mostrar la actividad de bienvenida
            val btnEntendido: Button = findViewById(R.id.btn_entendido)
            btnEntendido.setOnClickListener {
                prefManager.setFirstTimeLaunch(false)
                launchHomeScreen()
            }
        } else {
            // Si hay asignaturas en la base de datos, ir directamente a la actividad principal
            launchHomeScreen()
        }
    }
    /**
     * Nos lleva a la actividad principal
     *
     */
    private fun launchHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Devuelve el numero de asignaturas en la base de datos
     *
     * @return
     */
    private fun countData(): Int {
        val cursor = db!!.viewData()
        return cursor.count
    }

}