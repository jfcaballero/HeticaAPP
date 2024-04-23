package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Organizar tareas 2.
 */
class organizar_tareas2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_tareas2)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            val siguiente = Intent(this@organizar_tareas2, activity_organizar_estudio6::class.java)
            startActivity(siguiente)
        }}

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    fun pasar3(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_estudio7::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    fun pasaratras(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_estudio6::class.java)
        startActivity(siguiente)
    }
}