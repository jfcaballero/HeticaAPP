package com.hetica.AutismoCordoba;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The type Organizar mochila 1.
 */
public class OrganizarMochila1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_mochila1);
    }

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
        Intent siguiente =new Intent(this, activity_organizar_mochila2.class);
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
