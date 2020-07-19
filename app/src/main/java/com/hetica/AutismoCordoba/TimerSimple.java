package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Handler;
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
 * The type Timer simple.
 */
public class TimerSimple extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 600000;

    //Bundle bundle = getIntent().getExtras();

    /**
     * The Time string.
     */
//String timeString = bundle.getString("time");
    String timeString;
    /**
     * The Actual.
     */
    String actual;
    /**
     * The Cuantas.
     */
    String cuantas;
    /**
     * The Bundle.
     */
    Bundle bundle;
    /**
     * The Asig.
     */
    String asig;
    /**
     * The Booool.
     */
    int booool;
    private Ringtone r;
    /**
     * The Db.
     */
    AdminSQLiteOpenHelperStats db;

    private TextView mTextViewCountDown;
    private Button mButtonSi;
    private Button mButtonNo;
    private Button Pausa;
    private Button Fin;
    private Button Fin2;
    private Button Main;
    //private long mStartTime = Long.parseLong(timeString) * 60000;
    private long mStartTime;

    Intent siguiente;
    private long then;
    private int longClickDuration = 3000;

    int finFlag;

    private CountDownTimer mCountDownTimer;
    private static TextView textView;
    private boolean mTimerRunning;

    private long mTimeLeftInMillis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_simple);

        bundle = getIntent().getExtras();
        textView=(TextView) findViewById(R.id.textView49);

        db = new AdminSQLiteOpenHelperStats(this, "Stats.db", null, 1);

        finFlag=0;

        timeString = bundle.getString("time");
        asig=bundle.getString("asig");
        actual = bundle.getString("actAsig");
        cuantas = bundle.getString("numAsig");
        textView.setText(asig);

        mStartTime=Long.parseLong(timeString)*60000;

        mTimeLeftInMillis=mStartTime;

        mTextViewCountDown = findViewById(R.id.text_view_count);
        Main=(Button)findViewById(R.id.button13);
        Fin=(Button)findViewById(R.id.buttonFin);
        Fin2=(Button)findViewById(R.id.buttonFin2);
        Fin2.setVisibility(View.INVISIBLE);
        mButtonSi = (Button)findViewById(R.id.button38);
        mButtonSi.setVisibility(View.INVISIBLE);
        mButtonNo = (Button)findViewById(R.id.button39);
        mButtonNo.setVisibility(View.INVISIBLE);
        Pausa = (Button)findViewById(R.id.button41);
        readParams ();
        mTextViewCountDown.setVisibility(View.VISIBLE);
        startTimer();

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

        }

    @Override
    public void onBackPressed() {

    }


        /*public void ocultar(View view){
            if (booool==1) {
                mTextViewCountDown.setVisibility(View.INVISIBLE);
                booool=0;
            }else{
                mTextViewCountDown.setVisibility(View.VISIBLE);
                booool=1;
            }

        }*/

    /**
     * Función que inicia y finaliza el temporizador
     *
     */
    public void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {


                if (!cuantas.equalsIgnoreCase(actual)) {
                    mTimerRunning = false;

                    mButtonSi.setVisibility(View.VISIBLE);
                    mButtonNo.setVisibility(View.VISIBLE);
                    mTextViewCountDown.setText("¿Necesitas un descanso?");
                    mTextViewCountDown.setTextSize(40);

                    SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
                    db.insertData(asig, sdf.format(new Date()), Integer.valueOf(timeString));
                    Pausa.setVisibility(View.INVISIBLE);
                    Fin.setVisibility(View.INVISIBLE);
                    if (!cuantas.equalsIgnoreCase(actual) && finFlag == 0) {
                        showNotification();
                    }
                }
                else{
                    Pausa.setVisibility(View.INVISIBLE);
                    Fin.setVisibility(View.INVISIBLE);
                    Fin2.setVisibility(View.VISIBLE);
                    showNotification();
                }
                //final MediaPlayer alarmSound = MediaPlayer.create(this, R.raw.algo);

               // alarmSound.start();
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

    public void finEstudio(View view) {
        r.stop();
        Intent siguiente1 = new Intent(this, MainActivity.class);


        startActivity(siguiente1);
    }

    public void finTimer(View view) {
        timeString=String.valueOf( Integer.valueOf(timeString) - (int) (mTimeLeftInMillis / 1000) / 60);
        mCountDownTimer.cancel();
        mTimerRunning = false;
        finFlag=1;
        mTimeLeftInMillis=1000;
        startTimer();

    }



    /**
     *
     * Función que controla el temporizador
     */

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);

        if (cuantas.equalsIgnoreCase(actual) && seconds==1 && minutes==0){
            Intent siguiente1 = new Intent(this, MainActivity.class);

            startActivity(siguiente1);
        }

    }

    /**
     * Función que pasa a la siguiente asignatura ya que no se ha elegido descanso
     *
     * @param view the view
     */
    public void pasarNo(View view){
        Bundle bundle2 = new Bundle();
        if ((cuantas.equalsIgnoreCase("2")) && actual.equalsIgnoreCase("1")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "2");
            bundle2.putString("numAsig", cuantas);

            siguiente1.putExtras(bundle2);
            if (finFlag==0){
                r.stop();
            }

            startActivity(siguiente1);
            this.finish();
        }
        if ((cuantas.equalsIgnoreCase("3") || cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("1")) {
            Intent siguiente1 = new Intent(this, AsignaturaDificil.class);
            bundle2.putString("actAsig", "2");
            bundle2.putString("numAsig", cuantas);

            siguiente1.putExtras(bundle2);
            if (finFlag==0){
                r.stop();
            }

            startActivity(siguiente1);
            this.finish();
        }
        if ((cuantas.equalsIgnoreCase("3")) && actual.equalsIgnoreCase("2")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "3");
            bundle2.putString("numAsig", cuantas);
            if (finFlag==0){
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
            this.finish();
        }
        if ((cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("2")) {
            Intent siguiente1 = new Intent(this, AsignaturaGustar.class);
            bundle2.putString("actAsig", "3");
            bundle2.putString("numAsig", cuantas);
            if (finFlag==0){
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
            this.finish();
        }
        if ((cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("3")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "4");
            bundle2.putString("numAsig", cuantas);
            if (finFlag==0){
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
            this.finish();
        }
        if (cuantas.equalsIgnoreCase("5") && actual.equalsIgnoreCase("4")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "5");
            bundle2.putString("numAsig", cuantas);
            if (finFlag==0){
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
            this.finish();
        }

    }

    /**
     * Función que pasa al descanso
     *
     * @param view the view
     */
    public void pasarSi (View view){
        Bundle bundle2 = new Bundle();
        if ((cuantas.equalsIgnoreCase("2") || cuantas.equalsIgnoreCase("3") || cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("1")) {
            Intent siguiente1 = new Intent(this, TimerDescanso.class);
            bundle2.putString("actAsig", "1");
            bundle2.putString("numAsig", cuantas);
            if (finFlag==0){
                r.stop();
            }
            siguiente1.putExtras(bundle2);

            startActivity(siguiente1);
            this.finish();
        }
        if ((cuantas.equalsIgnoreCase("3") || cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("2")) {
            Intent siguiente1 = new Intent(this, TimerDescanso.class);
            bundle2.putString("actAsig", "2");
            bundle2.putString("numAsig", cuantas);
            if (finFlag==0){
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
            this.finish();
        }
        if ((cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("3")) {
            Intent siguiente1 = new Intent(this, TimerDescanso.class);
            bundle2.putString("actAsig", "3");
            bundle2.putString("numAsig", cuantas);
            if (finFlag==0){
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
            this.finish();
        }
        if (cuantas.equalsIgnoreCase("5") && actual.equalsIgnoreCase("4")) {
            Intent siguiente1 = new Intent(this, TimerDescanso.class);
            bundle2.putString("actAsig", "4");
            bundle2.putString("numAsig", cuantas);

            if (finFlag==0){
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);

            this.finish();
        }


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

