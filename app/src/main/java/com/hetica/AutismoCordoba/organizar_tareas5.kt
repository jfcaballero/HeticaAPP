package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Organizar tareas 5.
 */
class organizar_tareas5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_tareas5)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
        val siguiente = Intent(this@organizar_tareas5, activity_organizar_estudio2::class.java)
        startActivity(siguiente)
    }}

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    fun pasar3(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_estudio3::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    fun pasaratras(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_estudio2::class.java)
        startActivity(siguiente)
    }
}