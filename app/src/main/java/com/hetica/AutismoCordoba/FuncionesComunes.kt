package com.hetica.AutismoCordoba
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
class FuncionesComunes {
    companion object {
        @RequiresApi(Build.VERSION_CODES.R)
        fun showSnackbarWithCustomTextSize(context: Context, message: String) {
            val view: View = (context as Activity).findViewById(android.R.id.content)
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val layout = snackbar.view as Snackbar.SnackbarLayout
            val textView = layout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))

            // Obtener la rotación de la pantalla según el nivel de API
            val rotation = context.display?.rotation ?: android.view.Surface.ROTATION_0

            val isPortrait = rotation == android.view.Surface.ROTATION_0 || rotation == android.view.Surface.ROTATION_180

            // Determinar el tamaño del texto según los rangos de pantalla
            val screenWidthDp = context.resources.configuration.screenWidthDp
            val screenHeightDp = context.resources.configuration.screenHeightDp
            val textSizeSp = when {
                isPortrait -> {
                    when {
                        screenWidthDp >= 720 -> context.resources.getDimension(R.dimen.snackbar_portrait_720dp)
                        screenWidthDp >= 480 -> context.resources.getDimension(R.dimen.snackbar_portrait_480dp)
                        else -> context.resources.getDimension(R.dimen.snackbar_portrait_320dp)
                    }
                }
                else -> {
                    when {
                        screenHeightDp >= 600 -> context.resources.getDimension(R.dimen.snackbar_landscape_720dp)
                        screenHeightDp >= 400 -> context.resources.getDimension(R.dimen.snackbar_landscape_480dp)
                        else -> context.resources.getDimension(R.dimen.snackbar_landscape_320dp)
                    }
                }
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeSp)  // Establecer el tamaño del texto
            snackbar.show()
        }

        fun listaBasura(): List<Pair<String, Float>> {
            val datosEjemplo = listOf(
                "2024-01-01" to 30f,
                "2024-01-02" to 40f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f, "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f, "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-01" to 30f,
                "2024-01-02" to 40f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f, "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f, "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-01" to 30f,
                "2024-01-02" to 40f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f, "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f, "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-01" to 30f,
                "2024-01-02" to 40f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f, "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f, "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-01" to 30f,
                "2024-01-02" to 40f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,
                "2024-01-03" to 50f,

                )
            return datosEjemplo

        }
    }
}