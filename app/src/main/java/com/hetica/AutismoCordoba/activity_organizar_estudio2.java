package com.hetica.AutismoCordoba;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The type Activity organizar estudio 2.
 */
public class activity_organizar_estudio2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_estudio2);
    }

    public void onBackPressed(){
        Intent siguiente =new Intent(this, OrganizarEstudio1.class);
        startActivity(siguiente);
    }

    /**
     * Pasar 3.
     *
     * @param view the view
     */
    public void pasar3(View view)
    {
        Intent siguiente =new Intent(this, organizar_tareas5.class);
        startActivity(siguiente);
    }

    /**
     * Pasaratras.
     *
     * @param view the view
     */
    public void pasaratras(View view)
    {
        Intent siguiente =new Intent(this, OrganizarEstudio1.class);
        startActivity(siguiente);
    }
}
