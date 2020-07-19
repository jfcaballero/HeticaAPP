package com.hetica.AutismoCordoba;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The type Activity organizar estudio 3.
 */
public class activity_organizar_estudio3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_estudio3);
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
    public void pasar4(View view)
    {
        Intent siguiente = new Intent(this, activity_organizar_estudio4.class);
        startActivity(siguiente);
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    public void pasaratras(View view)
    {
        Intent siguiente =new Intent(this, activity_organizar_estudio2.class);
        startActivity(siguiente);
    }
}
