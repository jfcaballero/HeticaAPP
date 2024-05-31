package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Activity organizar mochila 2.
 */
class activity_organizar_mochila2 : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_mochila2)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
        val siguiente = Intent(this@activity_organizar_mochila2, OrganizarMochila1::class.java)
        startActivity(siguiente)
    }}

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    fun pasar3(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_mochila3::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    fun pasaratras(view: View?) {
        val siguiente = Intent(view!!.context, OrganizarMochila1::class.java)
        startActivity(siguiente)
    }
}