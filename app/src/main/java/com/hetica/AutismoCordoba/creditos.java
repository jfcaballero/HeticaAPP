package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class creditos extends AppCompatActivity {

    TextView tv;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        //final SpannableStringBuilder sb = new SpannableStringBuilder("Este proyecto ha sido desarrollado bajo el V Plan Propio Galileo de Innovación y Transferencia de la Universidad de Córdoba, Modalidad IV, proyectos UCO-SOCIAL-INNOVA.\n\nCoordinador principal:\nJuan Carlos Fernández Caballero, jfcaballero@uco.es. Universidad de Córdoba.\n\nEquipo de investigación:\nCésar Hervás Martínez, chervas@uco.es. Universidad de Córdoba.\nPedro Antonio Gutiérrez Peña, pagutierrez@uco.es. Universidad de Córdoba.\n\nEquipo de trabajo:\nMiguel Ángel Borreguero Aparicio, i52boapm@uco.es. Universidad de Córdoba. Desarrollador principal.\nMaría Muñoz Reyes, direccion@autismocordoba.org. Asociación Autismo Córdoba (Córdoba).\nCarmen Moscoso Galisteo, XXXXXXXXXX. Asociación Autismo Córdoba (Córdoba).\nJulio Camacho Cañamón, julio.camacho@uco.es. Universidad de Córdoba.\nAntonio Manuel Gómez Orellana, am.gomez@uco.es. Universidad de Córdoba.\nDavid Guijo Rubio, dguijo@uco.es. Universidad de Córdoba.");

        //final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        tv = (TextView) findViewById(R.id.textView57);
        tv.setMovementMethod(new ScrollingMovementMethod());
        //sb.setSpan(bss, 169, 191, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //sb.setSpan(bss, 276, 300, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //sb.setSpan(bss, 433, 451, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        //tv.setText(sb);
    }
}
