package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The type Temporizador unico.
 */
public class temporizadorUnico extends AppCompatActivity {

    private long mStartTime;
    private CountDownTimer mCountDownTimer;
    private TextView mTextViewCountDown;
    private TextView textAsig;
    private Button Pausa;
    /**
     * The Db.
     */
    AdminSQLiteOpenHelperStats db;
    /**
     * The Bundle.
     */
    Bundle bundle;
    /**
     * The Time string.
     */
    String timeString;
    /**
     * The Asig.
     */
    String asig;
    private Ringtone r;

    int finFlag;

    /**
     * The Boton fin.
     */
    Button botonFin;
    private Button Fin;

    private long mTimeLeftInMillis;
    private boolean mTimerRunning;

    private Button Main;

    Intent siguiente;
    private long then;
    private int longClickDuration = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporizador_unico);

        bundle = getIntent().getExtras();
        db = new AdminSQLiteOpenHelperStats(this, "Stats.db", null, 1);

        mTextViewCountDown=(TextView)findViewById(R.id.textViewCountDown2);
        textAsig=(TextView)findViewById(R.id.textView56);

        botonFin=(Button)findViewById(R.id.button48);
        botonFin.setVisibility(View.INVISIBLE);
        finFlag=1;
        timeString = bundle.getString("time");
        asig=bundle.getString("asig");
        textAsig.setText(asig);
        Log.e("Date Selected", timeString);
        Pausa = (Button)findViewById(R.id.buttonPausa);
        Fin = (Button)findViewById(R.id.buttonFin2);
        readParams();
        mStartTime=Long.parseLong(timeString)*60000;
        mTimeLeftInMillis=mStartTime;

        Main=(Button)findViewById(R.id.button15);

        Pausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        Main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    then = (long) System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - then) > longClickDuration) {
                        siguiente = new Intent(getBaseContext(), MainActivity.class);
                        mCountDownTimer.cancel();
                        mTimerRunning = false;
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
        startTimer();
    }

    @Override
    public void onBackPressed() {

    }

    /**
     * Función que maneja el temporizador
     *
     */
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                botonFin.setVisibility(View.VISIBLE);
                Fin.setVisibility(View.INVISIBLE);
                showNotification();

                SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
                db.insertData(asig, sdf.format(new Date()), Integer.valueOf(timeString));
                mTextViewCountDown.setText("00:00");
                //final MediaPlayer alarmSound = MediaPlayer.create(this, R.raw.algo);
                Pausa.setVisibility(View.INVISIBLE);
                //alarmSound.start();
            }
        }.start();

        mTimerRunning = true;
        Pausa.setText("Pausa");
    }

    public void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;

        Pausa.setText("Seguir");
    }

    public void finTimer(View view) {
        timeString=String.valueOf( Integer.valueOf(timeString) - (int) (mTimeLeftInMillis / 1000) / 60);
        mCountDownTimer.cancel();
        mTimerRunning = false;
        finFlag=1;
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        db.insertData(asig, sdf.format(new Date()), Integer.valueOf(timeString));
        Intent siguiente1 = new Intent(this, MainActivity.class);

        startActivity(siguiente1);

    }

    /**
     * Función que escribe el tiempo restante
     *
     */
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);

    }

    /**
     * Función que regresa al menú principal
     *
     * @param view the view
     */
    public void pasarInit(View view)
    {
        Intent siguiente = new Intent(this, MainActivity.class);
        r.stop();
        startActivity(siguiente);
    }
    public void readParams (){
        FileInputStream fis = null;
        String pausa;
        try {
            fis = openFileInput("pausa.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            pausa = br.readLine();



            if (pausa.equalsIgnoreCase("0")){
                Pausa.setVisibility(View.INVISIBLE);

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

        String fin;
        try {
            fis = openFileInput("fin.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            fin = br.readLine();



            if (fin.equalsIgnoreCase("0")){
                Fin.setVisibility(View.INVISIBLE);

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

    public void showNotification(){

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    r.stop();
                }
            }, 60000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
