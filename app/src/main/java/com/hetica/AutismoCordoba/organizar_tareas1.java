package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * The type Organizar tareas 1.
 */
public class organizar_tareas1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_tareas1);
    }

    @Override
    public void onBackPressed(){
        Intent siguiente =new Intent(this, MainActivity.class);
        startActivity(siguiente);
    }

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */
    public void pasar3(View view)
    {
        Intent siguiente =new Intent(this, organizar_tareas6.class);
        startActivity(siguiente);
    }

    /**
     * Pasa al menú principal
     *
     * @param view the view
     */
    public void pasaratras(View view)
    {
        Intent siguiente =new Intent(this, MainActivity.class);
        startActivity(siguiente);
    }
}
