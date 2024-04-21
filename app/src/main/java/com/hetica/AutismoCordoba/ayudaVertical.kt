package com.hetica.AutismoCordoba

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan

class ayudaVertical : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ayuda_vertical)
        ayuda()
    }

    private fun ayuda(){
        val texto=this.resources.getString(R.string.asignatura_de_hoy_texto_de_ayuda)

        val dialogTextSize = getDialogTextSize()
        val formattedText = getFormattedText(texto)

        val builder = AlertDialog.Builder(this,dialogTextSize)
        builder.setTitle("Asignaturas de hoy")
            .setMessage(formattedText)
            .setPositiveButton("Salir") { dialog, _ ->
                val intent = Intent(this@ayudaVertical, AsignaturaDeHoy::class.java)
                startActivity(intent)
            }

        val dialog = builder.create()

        dialog.show()

        }

    private fun getDialogTextSize(): Int {
        val screenWidthDp = resources.configuration.screenWidthDp
        val screenHeightDp = resources.configuration.screenHeightDp
        // Tamaños de titulos y boton del alertdialog para diferentes tamaños de pantalla
        val textSizeSmall = R.style.DialogTextStyleSmall
        val textSizeMedium = R.style.DialogTextStyleMedium
        val textSizeLarge = R.style.DialogTextStyleLarge

        return when {
            screenHeightDp >= 600 && screenWidthDp >= 720 -> textSizeLarge // Pantalla grande (más de 720dp)
            screenWidthDp >= 480 -> textSizeMedium // Pantalla mediana (entre 480dp y 720dp)
            else -> textSizeSmall // Pantalla pequeña (menos de 480dp)
        }
    }
    fun getFormattedText(texto: String): CharSequence {
        val spannableStringBuilder = SpannableStringBuilder(texto)
        val screenWidthDp = resources.configuration.screenWidthDp
        val screenHeightDp = resources.configuration.screenHeightDp

        val textSizeSmall = 19
        val textSizeMedium = this.resources.getDimensionPixelSize(R.dimen.text_size_small_less_than_480dp)
        val textSizeLarge = this.resources.getDimensionPixelSize(R.dimen.text_size_medium_less_than_480dp)
        val textSize = when {
            screenHeightDp >= 600 && screenWidthDp >= 720 -> textSizeLarge
            screenWidthDp >= 480 -> textSizeMedium
            else -> textSizeSmall
        }

        spannableStringBuilder.setSpan(
            AbsoluteSizeSpan(textSize, true),
            0,
            spannableStringBuilder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableStringBuilder
    }

}