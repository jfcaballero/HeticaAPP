package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Organizar estudio 1.
 */
class OrganizarEstudio1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_estudio1)
    }

    override fun onBackPressed() {
        val siguiente = Intent(this, MainActivity::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    fun pasar2() {
        val siguente = Intent(this, activity_organizar_estudio2::class.java)
        startActivity(siguente)
    }

    /**
     * Pasa al menú principal
     *
     * @param view the view
     */
    fun pasaratras() {
        val siguiente = Intent(this, MainActivity::class.java)
        startActivity(siguiente)
    }
}