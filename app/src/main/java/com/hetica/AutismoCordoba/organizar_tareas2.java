package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * The type Organizar tareas 2.
 */
public class organizar_tareas2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_tareas2);
    }

    public void onBackPressed(){
        Intent siguiente =new Intent(this, activity_organizar_estudio6.class);
        startActivity(siguiente);
    }

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    public void pasar3(View view)
    {
        Intent siguiente =new Intent(this, activity_organizar_estudio7.class);
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
