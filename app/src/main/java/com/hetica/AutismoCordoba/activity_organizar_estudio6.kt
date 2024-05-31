package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Activity organizar estudio 6.
 */
class activity_organizar_estudio6 : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_estudio6)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
        val siguiente = Intent(this@activity_organizar_estudio6, activity_organizar_estudio5::class.java)
        startActivity(siguiente)
    }}

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