package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize

/**
 * The type Cuantas asignaturas.
 */
class CuantasAsignaturas : AppCompatActivity() {
    /**
     * The Asignaturas.
     */
    var asignaturas: ArrayList<String>? = null

    /**
     * The Edit text.
     */
    var editText: EditText? = null

    /**
     * The Button.
     */
    var button: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuantas_asignaturas)
        editText = findViewById<View>(R.id.editText) as EditText


        //Guardar arrays desde 1 hasta 5 asignaturas de el orden que seguirán en cada estrategia
    }

    var doubleBackToExitPressedOnce = false
    var siguiente: Intent? = null
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            siguiente = Intent(baseContext, MainActivity::class.java)
            startActivity(siguiente)
            return
        }
        doubleBackToExitPressedOnce = true
        showSnackbarWithCustomTextSize(
            this,
            "Presiona de nuevo para salir",
        )
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    /**
     * Dependiendo de cuantas asignaturas se hayan elegido, se cogerá una ruta u otra
     *
     * @param view the view
     */
    fun Confirmar(view: View?) {
        val siguiente: Intent
        //String input =editText.getText().toString();
        val bundle = Bundle()
        when (editText!!.text.toString()) {
            "1" -> {
                bundle.putString("actAsig", "1")
                bundle.putString("numAsig", "1")
                siguiente = Intent(view!!.context, AsignaturaUnica::class.java)
                siguiente.putExtras(bundle)
                startActivity(siguiente)
            }

            "2" -> {
                bundle.putString("actAsig", "1")
                bundle.putString("numAsig", "2")
                siguiente = Intent(this, AsignaturaFacil::class.java)
                siguiente.putExtras(bundle)
                startActivity(siguiente)
            }

            "3" -> {
                bundle.putString("actAsig", "1")
                bundle.putString("numAsig", "3")
                siguiente = Intent(this, AsignaturaFacil::class.java)
                siguiente.putExtras(bundle)
                startActivity(siguiente)
            }

            "4" -> {
                bundle.putString("actAsig", "1")
                bundle.putString("numAsig", "4")
                siguiente = Intent(this, AsignaturaFacil::class.java)
                siguiente.putExtras(bundle)
                startActivity(siguiente)
            }

            "5" -> {
                bundle.putString("actAsig", "1")
                bundle.putString("numAsig", "5")
                siguiente = Intent(this, AsignaturaFacil::class.java)
                siguiente.putExtras(bundle)
                startActivity(siguiente)
            }

            "0" -> showSnackbarWithCustomTextSize(this, "No se permiten cero asignaturas")
            "" ->showSnackbarWithCustomTextSize(this, "Cantidad inválida")
            else -> showSnackbarWithCustomTextSize(this, "El número de asignaturas es muy grande")
        }
    }
}