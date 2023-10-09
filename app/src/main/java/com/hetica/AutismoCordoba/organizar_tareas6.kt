package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Organizar tareas 6.
 */
class organizar_tareas6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_tareas6)
    }

    override fun onBackPressed() {
        val siguiente = Intent(this, organizar_tareas1::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa a la pantalla de elegir cuantas asignaturas vas a estudiar
     *
     * @param view the view
     */
    fun pasar3(view: View?) {
        val siguiente = Intent(this, CuantasAsignaturas::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    fun pasaratras(view: View?) {
        val siguiente = Intent(this, organizar_tareas1::class.java)
        startActivity(siguiente)
    }
}