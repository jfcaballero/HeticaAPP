package com.hetica.AutismoCordoba;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Insets;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


/**
 * The type Activity organizar estudio 4.
 */
public class activity_organizar_estudio4 extends AppCompatActivity {

    public static TextView text;

    /*
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Your business logic to handle the back pressed event
            Intent siguiente =new Intent(this, activity_organizar_estudio3.class);
            startActivity(siguiente);
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_estudio4);
        text = (TextView)findViewById(R.id.textView8);

        //onBackPressed() is deprecated
        /* adding onbackpressed callback listener.
        this.getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
        */




        /* //getDefaultDisplay() is deprecated
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.x;
        */

        // Nuevo código para obtener height
        WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
        Insets insets = windowMetrics.getWindowInsets()
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
        int height = windowMetrics.getBounds().height() - insets.bottom - insets.top;


        if (height<1300) {
            text.setTextSize(25);
        }
    }

    /*
    override fun onBackPressed(){
       super.onBackPressed();
    }*/

    /**
     * Pasa al siguiente consejo
     *
     */

    @Override
    public void onBackPressed(){
        Intent siguiente =new Intent(this, activity_organizar_estudio3.class);
        startActivity(siguiente);
    }


    public void pasar5(View view)
    {
        Intent siguiente = new Intent(this, activity_organizar_estudio5.class);
        startActivity(siguiente);
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    public void pasaratras(View view)
    {
        Intent siguiente =new Intent(this, activity_organizar_estudio3.class);
        startActivity(siguiente);
    }


}
