package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Activity organizar estudio 2.
 */
class activity_organizar_estudio2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_estudio2)
    }

    override fun onBackPressed() {
        val siguiente = Intent(this, OrganizarEstudio1::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasar 3.
     *
     * @param view the view
     */
    fun pasar3(view: View?) {
        val siguiente = Intent(view!!.context, organizar_tareas5::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasaratras.
     *
     * @param view the view
     */
    fun pasaratras(view: View?) {
        val siguiente = Intent(view!!.context, OrganizarEstudio1::class.java)
        startActivity(siguiente)
    }
}