package com.hetica.AutismoCordoba
import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
class FuncionesComunes {
    companion object {
        fun showSnackbarWithCustomTextSize(context: Context, message: String) {
            val view: View = (context as Activity).findViewById(android.R.id.content)
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val layout = snackbar.view as Snackbar.SnackbarLayout
            val textView = layout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))

            // Determinar el tamaño del texto según los rangos de pantalla
            val screenWidthDp = context.resources.configuration.screenWidthDp
            val textSizeSp = when {
                screenWidthDp >= 720 -> context.resources.getDimension(R.dimen.text_size_medium_720dp)
                screenWidthDp >= 480 -> context.resources.getDimension(R.dimen.text_size_large_480dp)
                else -> context.resources.getDimension(R.dimen.text_size_large_less_than_480dp)
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeSp)  // Establecer el tamaño del texto
            snackbar.show()
        }
    }
}