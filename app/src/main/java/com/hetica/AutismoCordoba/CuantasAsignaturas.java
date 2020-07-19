package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The type Cuantas asignaturas.
 */
public class CuantasAsignaturas extends AppCompatActivity {
    /**
     * The Asignaturas.
     */
    ArrayList<String> asignaturas;
    /**
     * The Edit text.
     */
    EditText editText;
    /**
     * The Button.
     */
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuantas_asignaturas);

        editText = (EditText) findViewById(R.id.editText);


        //Guardar arrays desde 1 hasta 5 asignaturas de el orden que seguirán en cada estrategia
    }

    boolean doubleBackToExitPressedOnce = false;
    Intent siguiente;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            siguiente = new Intent(getBaseContext(), MainActivity.class);
            startActivity(siguiente);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presiona de nuevo para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    /**
     * Dependiendo de cuantas asignaturas se hayan elegido, se cogerá una ruta u otra
     *
     * @param view the view
     */
    public void Confirmar(View view){
        Intent siguiente;
        //String input =editText.getText().toString();
        Bundle bundle = new Bundle();




        switch (editText.getText().toString()) {
            case "1":
                bundle.putString("actAsig", "1");
                bundle.putString("numAsig", "1");
                siguiente = new Intent(this, AsignaturaUnica.class);
                siguiente.putExtras(bundle);
                startActivity(siguiente);
                break;
            case "2":
                bundle.putString("actAsig", "1");
                bundle.putString("numAsig", "2");
                siguiente = new Intent(this, AsignaturaFacil.class);
                siguiente.putExtras(bundle);
                startActivity(siguiente);
                break;
            case "3":
                bundle.putString("actAsig", "1");
                bundle.putString("numAsig", "3");
                siguiente = new Intent(this, AsignaturaFacil.class);
                siguiente.putExtras(bundle);
                startActivity(siguiente);
                break;
            case "4":
                bundle.putString("actAsig", "1");
                bundle.putString("numAsig", "4");
                siguiente = new Intent(this, AsignaturaFacil.class);
                siguiente.putExtras(bundle);
                startActivity(siguiente);
                break;
            case "5":
                bundle.putString("actAsig", "1");
                bundle.putString("numAsig", "5");
                siguiente = new Intent(this, AsignaturaFacil.class);
                siguiente.putExtras(bundle);
                startActivity(siguiente);
                break;
            case "0":
                Toast.makeText(this, "No se permiten cero asignaturas", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "El número de asignaturas es muy grande", Toast.LENGTH_LONG).show();
                break;
        }



    }
}
