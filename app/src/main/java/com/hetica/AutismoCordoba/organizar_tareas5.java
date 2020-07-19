package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * The type Organizar tareas 5.
 */
public class organizar_tareas5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_tareas5);
    }

    public void onBackPressed(){
        Intent siguiente =new Intent(this, activity_organizar_estudio2.class);
        startActivity(siguiente);
    }

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    public void pasar3(View view)
    {
        Intent siguiente =new Intent(this, activity_organizar_estudio3.class);
        startActivity(siguiente);
    }

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    public void pasaratras(View view)
    {
        Intent siguiente =new Intent(this, activity_organizar_estudio2.class);
        startActivity(siguiente);
    }
}
