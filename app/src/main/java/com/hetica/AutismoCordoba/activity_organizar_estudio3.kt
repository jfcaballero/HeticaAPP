package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Activity organizar estudio 3.
 */
class activity_organizar_estudio3 : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_estudio3)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val siguiente = Intent(this@activity_organizar_estudio3, activity_organizar_estudio2::class.java)
            startActivity(siguiente)
    }}

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    fun pasar4(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_estudio4::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    fun pasaratras(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_estudio2::class.java)
        startActivity(siguiente)
    }
}