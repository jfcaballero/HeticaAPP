package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Organizar mochila 1.
 */
class OrganizarMochila1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_mochila1)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            val siguiente = Intent(this@OrganizarMochila1, MainActivity::class.java)
            startActivity(siguiente)
        }}

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    fun pasar3(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_mochila2::class.java)
        startActivity(siguiente)
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