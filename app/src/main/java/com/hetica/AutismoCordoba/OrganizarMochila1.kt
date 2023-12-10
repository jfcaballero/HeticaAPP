package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Organizar mochila 1.
 */
class OrganizarMochila1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_mochila1)
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
    fun pasar3() {
        val siguiente = Intent(this, activity_organizar_mochila2::class.java)
        startActivity(siguiente)
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