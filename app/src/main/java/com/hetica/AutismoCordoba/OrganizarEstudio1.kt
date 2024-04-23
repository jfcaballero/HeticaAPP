package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Organizar estudio 1.
 */
class OrganizarEstudio1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_estudio1)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        val siguiente = Intent(this@OrganizarEstudio1, MainActivity::class.java)
        startActivity(siguiente)
    }}

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    fun pasar2(view: View?) {
        val siguente = Intent(view!!.context, activity_organizar_estudio2::class.java)
        startActivity(siguente)
    }

    /**
     * Pasa al menú principal
     *
     * @param view the view
     */
    fun pasaratras(view: View?) {
        val siguiente = Intent(view!!.context, MainActivity::class.java)
        startActivity(siguiente)
    }
}