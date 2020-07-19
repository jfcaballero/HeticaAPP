package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * The type Asignatura gustar.
 */
public class AsignaturaGustar extends AppCompatActivity {

    private static SeekBar seekBar;
    private static TextView textView;
    private static TextView textView2;

    /**
     * The Bundle.
     */
    Bundle bundle;
    /**
     * The Tiempo constante.
     */
    String tiempoConstante;
    /**
     * The Tiempo.
     */
    String tiempo;

    /**
     * The Cuantas.
     */
    String cuantas;
    /**
     * The Actual.
     */
    String actual;

    /**
     * The Db.
     */
    AdminSQLiteOpenHelperAsig db;

    /**
     * The Array list.
     */
    ArrayList<String> arrayList;
    /**
     * The Adapter.
     */
    ArrayAdapter<String> adapter;

    /**
     * The Lv.
     */
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignatura_gustar);


        db = new AdminSQLiteOpenHelperAsig(this, "Asig.db", null, 1);
        textView2 = (TextView) findViewById(R.id.textView47);



        if (tiempoConstante=="1"){
            seekBar.setVisibility(View.INVISIBLE);
        }

        bundle = getIntent().getExtras();

        cuantas = bundle.getString("numAsig");
        actual = bundle.getString("actAsig");


        seebbarr();

        lv = (ListView) findViewById(R.id.listViewDificil);

        arrayList = new ArrayList<String>();
        viewData();
        readParams();
        read();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView2.setText(arrayList.get(position));
            }
        });
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
     * Función que mide el valor del seekbar
     */
    public void seebbarr() {
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.textView45);

        //textView.setText("Covered : " + seekBar.getProgress() + " / " +seekBar.getMax());
        seekBar.setMax(30);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    int progress_value;

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress + 30;
                        textView.setText(progress_value + " minutos ");

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        textView.setText(progress_value + " minutos ");

                    }
                }
        );
    }

    /**
     * Función que lee opciones del sistema
     */
    public void read() {
        FileInputStream fis = null;

        try {
            fis = openFileInput("asignaturas_listado.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                arrayList.add(text);
                adapter.notifyDataSetChanged();

            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Función que se encarga de pasar al temporizador todos los datos necesarios
     *
     * @param view the view
     */
    public void pasar(View view)
    {
        Intent siguiente =new Intent(this, TimerSimple.class);
        bundle.putString("actAsig", "3");
        bundle.putString("numAsig", cuantas);
        bundle.putString("asig", textView2.getText().toString());

        //if(tiempoConstante.equalsIgnoreCase("0")){
            bundle.putString("time", Integer.toString(seekBar.getProgress()+30));
        //bundle.putString("time", Integer.toString(1));
        //}else{
           // bundle.putString("time", tiempo);
       // }

        siguiente.putExtras(bundle);
        if(textView2.getText().toString().equalsIgnoreCase("Clica una asignatura")){
            Toast.makeText(getApplicationContext(),
                    "Tienes que clicar una asignatura", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(siguiente);
        }
    }

    /**
     * Función que aplica la opcion de visualizar o no el SeekBar
     */
    public void readParams (){
        FileInputStream fis = null;

        try {
            fis = openFileInput("tiempo_trabajar.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            tiempo = br.readLine();

            tiempoConstante = br.readLine();

            if (tiempoConstante.equalsIgnoreCase("1")){
                seekBar.setVisibility(View.INVISIBLE);
                textView.setText(tiempo+" minutos");
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Función que añade las asignaturas a la pantalla
     *
     */
    private void viewData(){
        Cursor cursor = db.viewData();

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No hay ninguna asignatura", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                arrayList.add(cursor.getString(1));
            }
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                adapter = new ArrayAdapter<String>(AsignaturaGustar.this, android.R.layout.simple_list_item_1, arrayList){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        /// Get the Item from ListView
                        View view = super.getView(position, convertView, parent);

                        TextView tv = (TextView) view.findViewById(android.R.id.text1);

                        // Set the text size 25 dip for ListView each item
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);

                        // Return the view
                        return view;
                    }

                };

            }else
            {
                adapter = new ArrayAdapter<String>(AsignaturaGustar.this, android.R.layout.simple_list_item_1, arrayList);
            }
            lv.setAdapter(adapter);
        }

    }
}

