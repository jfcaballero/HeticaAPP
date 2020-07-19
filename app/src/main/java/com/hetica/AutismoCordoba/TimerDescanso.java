package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

/**
 * The type Timer descanso.
 */
public class TimerDescanso extends AppCompatActivity {
    /**
     * The Time string.
     */
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

    private Ringtone r;

    private TextView mTextViewCountDown;
    private Button mButtonSiguiente;
    private Button mButtonFin;
    private Button Main;
    private long mStartTime;

    private int flag=1;

    private long mTimeLeftInMillis;
    private boolean mTimerRunning;
    private CountDownTimer mCountDownTimer;

    Intent siguiente;
    private long then;
    private int longClickDuration = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_descanso);

        bundle = getIntent().getExtras();

        actual = bundle.getString("actAsig");
        cuantas = bundle.getString("numAsig");


        mStartTime=Long.parseLong("15")*60000;

        mTimeLeftInMillis=mStartTime;

        mTextViewCountDown = findViewById(R.id.text_view_count);

        Main=(Button)findViewById(R.id.button14);
        mButtonSiguiente = (Button)findViewById(R.id.button38);
        mButtonFin = (Button)findViewById(R.id.buttonFinDesc);
        mButtonSiguiente.setVisibility(View.INVISIBLE);
        mTextViewCountDown.setVisibility(View.VISIBLE);

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
     * Funcion que comienza y finaliza el temporizador del descanso
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
                mButtonSiguiente.setVisibility(View.VISIBLE);
                mButtonFin.setVisibility(View.INVISIBLE);
                mTextViewCountDown.setText("00:00");
                //final MediaPlayer alarmSound = MediaPlayer.create(this, R.raw.algo);
                if (flag == 1) {
                    showNotification();
                }
                else{
                    pasar2();
                }
                //alarmSound.start();


            }
        }.start();

        mTimerRunning = true;

    }

    /**
     * Función que controla el temporizador
     *
     */
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);



    }

    /**
     * Función que pasa a la siguiente actividad depende de la situación
     *
     * @param view the view
     */
    public void pasar2(){
        Bundle bundle2 = new Bundle();
        if ((cuantas.equalsIgnoreCase("2")) && actual.equalsIgnoreCase("1")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "2");
            bundle2.putString("numAsig", cuantas);

            siguiente1.putExtras(bundle2);
            if(flag!=0) {
                r.stop();
            }
            startActivity(siguiente1);
        }
        if ((cuantas.equalsIgnoreCase("3") || cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("1")) {
            Intent siguiente1 = new Intent(this, AsignaturaDificil.class);
            bundle2.putString("actAsig", "2");
            bundle2.putString("numAsig", cuantas);

            siguiente1.putExtras(bundle2);
            if(flag!=0) {
                r.stop();
            }
            startActivity(siguiente1);
        }
        if ((cuantas.equalsIgnoreCase("3")) && actual.equalsIgnoreCase("2")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "3");
            bundle2.putString("numAsig", cuantas);
            if(flag!=0) {
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
        }
        if ((cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("2")) {
            Intent siguiente1 = new Intent(this, AsignaturaGustar.class);
            bundle2.putString("actAsig", "3");
            bundle2.putString("numAsig", cuantas);
            if(flag!=0) {
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
        }
        if ((cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("3")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "4");
            bundle2.putString("numAsig", cuantas);
            if(flag!=0) {
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
        }
        if (cuantas.equalsIgnoreCase("5") && actual.equalsIgnoreCase("4")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "5");
            bundle2.putString("numAsig", cuantas);
            if(flag!=0) {
                r.stop();
            }
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
        }
        if (cuantas.equalsIgnoreCase(actual)){
            Intent siguiente1 = new Intent(this, MainActivity.class);
            if(flag!=0) {
                r.stop();
            }
            startActivity(siguiente1);
        }
    }

    public void pasar(View view){
        Bundle bundle2 = new Bundle();
        if ((cuantas.equalsIgnoreCase("2")) && actual.equalsIgnoreCase("1")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "2");
            bundle2.putString("numAsig", cuantas);

            siguiente1.putExtras(bundle2);
            r.stop();
            startActivity(siguiente1);
        }
        if ((cuantas.equalsIgnoreCase("3") || cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("1")) {
            Intent siguiente1 = new Intent(this, AsignaturaDificil.class);
            bundle2.putString("actAsig", "2");
            bundle2.putString("numAsig", cuantas);

            siguiente1.putExtras(bundle2);
            r.stop();
            startActivity(siguiente1);
        }
        if ((cuantas.equalsIgnoreCase("3")) && actual.equalsIgnoreCase("2")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "3");
            bundle2.putString("numAsig", cuantas);
            r.stop();
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
        }
        if ((cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("2")) {
            Intent siguiente1 = new Intent(this, AsignaturaGustar.class);
            bundle2.putString("actAsig", "3");
            bundle2.putString("numAsig", cuantas);
            r.stop();
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
        }
        if ((cuantas.equalsIgnoreCase("4") || cuantas.equalsIgnoreCase("5")) && actual.equalsIgnoreCase("3")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "4");
            bundle2.putString("numAsig", cuantas);
            r.stop();
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
        }
        if (cuantas.equalsIgnoreCase("5") && actual.equalsIgnoreCase("4")) {
            Intent siguiente1 = new Intent(this, AsignaturaSiguiente.class);
            bundle2.putString("actAsig", "5");
            bundle2.putString("numAsig", cuantas);
            r.stop();
            siguiente1.putExtras(bundle2);
            startActivity(siguiente1);
        }
        if (cuantas.equalsIgnoreCase(actual)){
            Intent siguiente1 = new Intent(this, MainActivity.class);
            r.stop();
            startActivity(siguiente1);
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

    public void Finalizar(View view){

        mCountDownTimer.cancel();
        mTimerRunning = false;
        flag = 0;
        mTimeLeftInMillis=1000;
        startTimer();
        mButtonFin.setVisibility(View.INVISIBLE);

    }
}
