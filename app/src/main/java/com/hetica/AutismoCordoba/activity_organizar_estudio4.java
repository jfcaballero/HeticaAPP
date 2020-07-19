package com.hetica.AutismoCordoba;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * The type Activity organizar estudio 4.
 */
public class activity_organizar_estudio4 extends AppCompatActivity {

    public static TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizar_estudio4);

        text = (TextView)findViewById(R.id.textView8);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.x;

        if (height<1300) {
            text.setTextSize(25);
        }
    }

    /**
     * Pasa al siguiente consejo
     *
     * @param view the view
     */

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
