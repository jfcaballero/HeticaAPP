package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The type Settings activity.
 */
public class SettingsActivity extends AppCompatActivity {

    private static SeekBar seekBar;
    /**
     * The constant textView.
     */
    public static TextView textView;

    /**
     * The constant Switch.
     */
    public static Switch Switch;
    /**
     * The constant SwitchTemp.
     */
    public static Switch SwitchTemp;

    public static Switch SwitchPausa;

    public static Switch SwitchFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        seekBar = (SeekBar)findViewById(R.id.seekBar4);
        textView =(TextView)findViewById(R.id.textView46);
        Switch = (Switch)findViewById(R.id.switch3);

        SwitchTemp = (Switch)findViewById(R.id.switch1) ;
        SwitchPausa = (Switch)findViewById(R.id.switch2) ;
        SwitchFin = (Switch)findViewById(R.id.switchFin) ;
        seebbarr();
        read();
        if(Switch.isChecked()) {
            seekBar.setEnabled(true);

            textView.setTextColor(Color.parseColor("#2f2f2f"));
        }
        else {
            seekBar.setEnabled(false);
            textView.setTextColor(Color.parseColor("#b6b6b6"));
        }

        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    seekBar.setEnabled(true);
                    textView.setTextColor(Color.parseColor("#2f2f2f"));
                }
                else {
                    seekBar.setEnabled(false);
                    textView.setTextColor(Color.parseColor("#b6b6b6"));
                }
            }
        });



        SwitchTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("temporizador.txt", MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(Integer.toString(1).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("temporizador.txt", MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(Integer.toString(0).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        SwitchPausa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("pausa.txt", MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(Integer.toString(1).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("pausa.txt", MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(Integer.toString(0).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        SwitchFin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("fin.txt", MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(Integer.toString(1).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput("fin.txt", MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(Integer.toString(0).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent siguiente =new Intent(this, MainActivity.class);
        startActivity(siguiente);
    }

    /**
     * Función que mide el valor del seekbar
     */
    public void seebbarr( ){


        //textView.setText("Covered : " + seekBar.getProgress() + " / " +seekBar.getMax());
        seekBar.setMax(30);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    int progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value=progress+30;
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
     * Función que guarda las opciones relacionadas con el tiempo a trabajar
     *
     * @param view the view
     */
    public void guardar(View view){
        FileOutputStream fos = null;

        try {
            fos = openFileOutput("tiempo_trabajar.txt", MODE_PRIVATE);
            int resul = seekBar.getProgress()+30;
            fos.write(Integer.toString(resul).getBytes());
            fos.write("\n".getBytes());

            if (Switch.isChecked()) {
                fos.write("1".getBytes());
            }else{
                fos.write("0".getBytes());
            }

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

    /**
     * Función que lleva a las opciones de las asignaturas
     *
     * @param view the view
     */
    public void pasar_asig(View view)
    {
        Intent siguiente =new Intent(this, ElegirAsignaturas.class);
        startActivity(siguiente);
    }

    /**
     * Establecer alarma mochila.
     *
     * @param mensaje the mensaje
     * @param hora    the hora
     * @param minutos the minutos
     */
    public void establecerAlarmaMochila(String mensaje, int hora, int minutos){
        ArrayList<Integer> alarmDays= new ArrayList<Integer>();
        alarmDays.add(Calendar.MONDAY);
        alarmDays.add(Calendar.TUESDAY);
        alarmDays.add(Calendar.WEDNESDAY);
        alarmDays.add(Calendar.THURSDAY);
        alarmDays.add(Calendar.SUNDAY);
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM).putExtra(AlarmClock.EXTRA_MESSAGE, mensaje).putExtra(AlarmClock.EXTRA_HOUR, hora).putExtra(AlarmClock.EXTRA_MINUTES, minutos)
                .putExtra(AlarmClock.EXTRA_DAYS, alarmDays);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    /**
     * Establecer alarma tareas.
     *
     * @param mensaje the mensaje
     * @param hora    the hora
     * @param minutos the minutos
     */
    public void establecerAlarmaTareas(String mensaje, int hora, int minutos){
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM).putExtra(AlarmClock.EXTRA_MESSAGE, mensaje).putExtra(AlarmClock.EXTRA_HOUR, hora).putExtra(AlarmClock.EXTRA_MINUTES, minutos);

        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    /**
     * Inicializar las opciones como se dejaron la ultima vez
     */
    public void read (){
        FileInputStream fis = null;

        try {
            fis = openFileInput("tiempo_trabajar.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            text = br.readLine();

            seekBar.setProgress(Integer.valueOf(text)-30);

            text = br.readLine();

            if (text.equalsIgnoreCase("1")) {
                Switch.setChecked(true);
            }else{
                Switch.setChecked(false);
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
        try {
            fis = openFileInput("temporizador.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            text = br.readLine();



            if (text.equalsIgnoreCase("1")) {
                SwitchTemp.setChecked(true);
            }else{
                SwitchTemp.setChecked(false);
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
        try {
            fis = openFileInput("pausa.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            text = br.readLine();



            if (text.equalsIgnoreCase("1")) {
                SwitchPausa.setChecked(true);
            }else{
                SwitchPausa.setChecked(false);
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


        try {
            fis = openFileInput("fin.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            text = br.readLine();



            if (text.equalsIgnoreCase("1")) {
                SwitchFin.setChecked(true);
            }else{
                SwitchFin.setChecked(false);
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
     * Función para volver al menú principal
     *
     * @param view the view
     */
    public void pasar3(View view)
    {
        Intent siguiente =new Intent(this, MainActivity.class);
        startActivity(siguiente);
    }


}
