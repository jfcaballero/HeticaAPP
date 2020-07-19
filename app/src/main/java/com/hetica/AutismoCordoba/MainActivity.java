package com.hetica.AutismoCordoba;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The type Main activity.
 *
 * @author Miguel Ángel Borreguero Aparicio
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The constant botonTemp.
     */
    public static Button botonTemp;
    public static Button botonOpc;
    public static Button botonStat;
    public static Button botonCred;

    public static TextView text;
    public static TextView text1;
    public static TextView text2;

    public static Button boton;
    public static Button boton1;
    public static Button boton2;

    Intent siguiente;
    private int longClickDuration = 3000;
    private long then;
    /**
     * The Db.
     */
    AdminSQLiteOpenHelperAsig db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new AdminSQLiteOpenHelperAsig(this, "Asig.db", null, 1);
        botonTemp = (Button)findViewById(R.id.button45);
        botonOpc = (Button)findViewById(R.id.button4);
        botonStat = (Button)findViewById(R.id.button5);
        botonCred = (Button)findViewById(R.id.button6);
        botonTemp.setVisibility(View.INVISIBLE);

        boton = (Button)findViewById(R.id.button);
        boton1 = (Button)findViewById(R.id.button2);
        boton2 = (Button)findViewById(R.id.button3);

        text=(TextView)findViewById(R.id.textView59);
        text1=(TextView)findViewById(R.id.textView60);
        text2=(TextView)findViewById(R.id.textView61);

        leerTemp();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int weight = size.x;

        if (height<721)
        {
            boton.getLayoutParams().width= 250;
            boton.getLayoutParams().height= 250;

            boton1.getLayoutParams().width= 250;
            boton1.getLayoutParams().height= 250;

            boton2.getLayoutParams().width= 250;
            boton2.getLayoutParams().height= 250;



        }

        if (weight<1300){
            text.setTextSize(11);
            text1.setTextSize(11);
            text2.setTextSize(11);
        }

        botonOpc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    then = (long) System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - then) > longClickDuration) {
                        siguiente = new Intent(getBaseContext(), SettingsActivity.class);
                        startActivity(siguiente);
                        System.out.println("Long Click has happened!");
                        return false;
                    } else {
                        /* Implement short click behavior here or do nothing */
                        System.out.println("Short Click has happened...");
                        return false;
                    }
                }
                return true;
            }
        });

        botonStat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    then = (long) System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - then) > longClickDuration) {
                        siguiente = new Intent(getBaseContext(), estadisticasDias.class);
                        startActivity(siguiente);
                        System.out.println("Long Click has happened!");
                        return false;
                    } else {
                        /* Implement short click behavior here or do nothing */
                        System.out.println("Short Click has happened...");
                        return false;
                    }
                }
                return true;
            }
        });

        botonCred.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    then = (long) System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - then) > longClickDuration) {
                        siguiente = new Intent(getBaseContext(), creditos.class);
                        startActivity(siguiente);
                        System.out.println("Long Click has happened!");
                        return false;
                    } else {
                        /* Implement short click behavior here or do nothing */
                        System.out.println("Short Click has happened...");
                        return false;
                    }
                }
                return true;
            }
        });
        /*botonCred.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                siguiente = new Intent(getBaseContext(), creditos.class);
                startActivity(siguiente);
                return true;
            }
        });*/

        FileOutputStream fos = null;
        String filename="tiempo_trabajar.txt";
        File file = new File(getApplicationContext().getFilesDir(),filename);

        if(file.exists()) {

        }else{

            try {
                fos = openFileOutput(filename, MODE_PRIVATE);

                fos.write("30".getBytes());
                fos.write("\n".getBytes());
                fos.write("0".getBytes());
                fos.write("\n".getBytes());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        filename="pausa.txt";
        file = new File(getApplicationContext().getFilesDir(),filename);

        if(file.exists()) {

        }else{

            try {
                fos = openFileOutput(filename, MODE_PRIVATE);

                fos.write("1".getBytes());


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        filename="fin.txt";
        file = new File(getApplicationContext().getFilesDir(),filename);

        if(file.exists()) {

        }else{

            try {
                fos = openFileOutput(filename, MODE_PRIVATE);

                fos.write("1".getBytes());


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }



    @Override
    public void onBackPressed() {
        //new AlertDialog.Builder(this)
        //        .setTitle("Really Exit?")
        //        .setMessage("Are you sure you want to exit?")
        //        .setNegativeButton(android.R.string.no, null)
        //        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

        //                   public void onClick(DialogInterface arg0, int arg1) {
        //                MainActivity.super.onBackPressed();
        //            }
    //        }).create().show();
    }

    /**
     * Pasar a la pantalla de "Organizar el Estudio"
     *
     * @param view the view
     */
    public void pasarOrgEst(View view)
    {
        if(countData()==0){
            Toast.makeText(this, "Introduce alguna asignatura", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent siguiente = new Intent(this, organizar_tareas1.class);
            startActivity(siguiente);
        }
    }


    /**
     * Pasar org tar.
     *
     * @param view the view
     */
    public void pasarOrgTar(View view)
    {
        if(countData()==0){
            Toast.makeText(this, "Introduce alguna asignatura", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent siguiente = new Intent(this, OrganizarEstudio1.class);

            startActivity(siguiente);
        }
    }

    /**
     * Pasar a la pantalla de "Organizar la mochila"
     *
     * @param view the view
     */
    public void pasarOrgMoch(View view)
    {
        Intent siguiente = new Intent(this, OrganizarMochila1.class);
        startActivity(siguiente);
    }

    /**
     * Pasar a la pantalla de "Opciones"
     *
     * @param view the view
     */
    public void pasarOpciones(View view)
    {
        Intent siguiente = new Intent(this, SettingsActivity.class);
        startActivity(siguiente);
    }

    /**
     * Pasar a la pantalla de "Estadisticas"
     *
     * @param view the view
     */
    public void pasarEstadisticas(View view)
    {
        Intent siguiente = new Intent(this, estadisticasDias.class);
        startActivity(siguiente);
    }

    public void pasarCreditos(View view)
    {
        Intent siguiente = new Intent(this, creditos.class);
        startActivity(siguiente);
    }

    /**
     * Comprobar que la base de datos no este vacia
     *
     * @param view the view
     */
    public void pasarTemporizador(View view)
    {
        if(countData()==0){
            Toast.makeText(this, "Introduce alguna asignatura", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent siguiente = new Intent(this, tiempoEstudio.class);
            startActivity(siguiente);
        }
    }

    /**
     * Mostrar o no la función del temporizador
     */
    public void leerTemp(){
        FileInputStream fis = null;
        try {
            fis = openFileInput("temporizador.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            text = br.readLine();



            if (text.equalsIgnoreCase("1")) {
                botonTemp.setVisibility(View.VISIBLE);
            }else{
                botonTemp.setVisibility(View.INVISIBLE);
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {

            }
        }
    }

    /**
     * Devuelve el numero de asignaturas en la base de datos
     *
     * @return
     */
    private int countData(){
        Cursor cursor = db.viewData();

        return cursor.getCount();
    }
}
