package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * The type Activity organizar estudio 7.
 */
public class activity_organizar_estudio7 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_estudio7);
    }

    public void onBackPressed(){
        Intent siguiente =new Intent(this, activity_organizar_estudio6.class);
        startActivity(siguiente);
    }

    /**
     * Pasa a elegir cuantas asignaturas quieres estudiar
     *
     * @param view the view
     */
    public void pasarInit(View view)
    {
        Intent siguiente = new Intent(this, MainActivity.class);
        startActivity(siguiente);
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    public void pasaratras(View view)
    {
        Intent siguiente =new Intent(this, activity_organizar_estudio6.class);
        startActivity(siguiente);
    }
}
