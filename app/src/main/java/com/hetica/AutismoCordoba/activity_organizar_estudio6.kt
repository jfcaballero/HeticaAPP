package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Activity organizar estudio 6.
 */
class activity_organizar_estudio6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_estudio6)
    }

    override fun onBackPressed() {
        val siguiente = Intent(this, activity_organizar_estudio5::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    fun pasarInit(view: View?) {
        val siguiente = Intent(view!!.context, organizar_tareas2::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    fun pasaratras(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_estudio5::class.java)
        startActivity(siguiente)
    }
}