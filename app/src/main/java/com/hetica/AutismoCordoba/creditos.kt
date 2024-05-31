package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private var btnMenu: Button?=null

class creditos : AppCompatActivity() {
    var tv: TextView? = null
    var text: String? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creditos)
        btnMenu=findViewById(R.id.volverAlMenu)

        //final SpannableStringBuilder sb = new SpannableStringBuilder("Este proyecto ha sido desarrollado bajo el V Plan Propio Galileo de Innovación y Transferencia de la Universidad de Córdoba, Modalidad IV, proyectos UCO-SOCIAL-INNOVA.\n\nCoordinador principal:\nJuan Carlos Fernández Caballero, jfcaballero@uco.es. Universidad de Córdoba.\n\nEquipo de investigación:\nCésar Hervás Martínez, chervas@uco.es. Universidad de Córdoba.\nPedro Antonio Gutiérrez Peña, pagutierrez@uco.es. Universidad de Córdoba.\n\nEquipo de trabajo:\nMiguel Ángel Borreguero Aparicio, i52boapm@uco.es. Universidad de Córdoba. Desarrollador principal.\nMaría Muñoz Reyes, direccion@autismocordoba.org. Asociación Autismo Córdoba (Córdoba).\nCarmen Moscoso Galisteo, XXXXXXXXXX. Asociación Autismo Córdoba (Córdoba).\nJulio Camacho Cañamón, julio.camacho@uco.es. Universidad de Córdoba.\nAntonio Manuel Gómez Orellana, am.gomez@uco.es. Universidad de Córdoba.\nDavid Guijo Rubio, dguijo@uco.es. Universidad de Córdoba.");

        //final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        tv = findViewById<View>(R.id.textView57) as TextView
        tv!!.movementMethod = ScrollingMovementMethod()
        //sb.setSpan(bss, 169, 191, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //sb.setSpan(bss, 276, 300, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //sb.setSpan(bss, 433, 451, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        //tv.setText(sb);
        btnMenu?.setOnClickListener {
            volverAlMenu()
        }
    }

    private fun volverAlMenu(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}