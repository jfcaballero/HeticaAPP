package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * The type Organizar tareas 6.
 */
public class organizar_tareas6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_tareas6);
    }

    public void onBackPressed(){
        Intent siguiente =new Intent(this, organizar_tareas1.class);
        startActivity(siguiente);
    }

    /**
     * Pasa a la pantalla de elegir cuantas asignaturas vas a estudiar
     *
     * @param view the view
     */
    public void pasar3(View view)
    {
        Intent siguiente =new Intent(this, CuantasAsignaturas.class);
        startActivity(siguiente);
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    public void pasaratras(View view)
    {
        Intent siguiente =new Intent(this, organizar_tareas1.class);
        startActivity(siguiente);
    }
}
