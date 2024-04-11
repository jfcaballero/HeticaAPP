package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

/**
 * The type Timer descanso.
 */
class TimerDescanso : AppCompatActivity() {
    /**
     * The Time string.
     */
    var timeString: String? = null

    /**
     * The Actual.
     */
    var actual: String? = null

    /**
     * The Cuantas.
     */
    var cuantas: String? = null

    /**
     * The Bundle.
     */
    var bundle: Bundle? = null

    /**
     * The Asig.
     */
    var asig: String? = null
    private var r: Ringtone? = null
    private var mTextViewCountDown: TextView? = null
    private var mButtonSiguiente: Button? = null
    private var mButtonFin: Button? = null
    private var Main: Button? = null
    private var mStartTime: Long = 0
    private var flag = 1
    private var mTimeLeftInMillis: Long = 0
    private var mTimerRunning = false
    private var mCountDownTimer: CountDownTimer? = null
    var siguiente: Intent? = null
    private var then: Long = 0
    private val longClickDuration = 3000
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 3000L // 3 segundos
    private var isLongPressFired = false
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_descanso)
        bundle = intent.extras
        actual = bundle!!.getString("actAsig")
        cuantas = bundle!!.getString("numAsig")
        mStartTime = "15".toLong() * 60000
        mTimeLeftInMillis = mStartTime
        mTextViewCountDown = findViewById(R.id.text_view_count)
        Main = findViewById<View>(R.id.button14) as Button
        //mButtonSiguiente = findViewById<View>(R.id.button38) as Button
        mButtonFin = findViewById<View>(R.id.buttonFinDesc) as Button
        //mButtonSiguiente!!.visibility = View.INVISIBLE
        mTextViewCountDown?.visibility = View.VISIBLE

        main()
        startTimer()
    }

    override fun onBackPressed() {}
    @SuppressLint("ClickableViewAccessibility")
    private fun main() {
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                if (isLongPressFired) {
                    return
                }
                isLongPressFired = true

                // Usamos un Handler para retrasar la apertura de la actividad EditarCalendario
                handler.postDelayed({
                    val intent = Intent(this@TimerDescanso, MainActivity::class.java)
                    startActivity(intent)
                }, delayMillis)
            }
        })

        Main?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                isLongPressFired = false
                // Si se libera el botón antes del tiempo de espera, cancelamos el Handler
                handler.removeCallbacksAndMessages(null)
            }
            true
        }
    }
    /**
     * Funcion que comienza y finaliza el temporizador del descanso
     *
     */
    private fun startTimer() {
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                mTimerRunning = false
                //mButtonSiguiente!!.visibility = View.VISIBLE
                mButtonFin!!.visibility = View.INVISIBLE
                mTextViewCountDown?.text = "00:00"

                //final MediaPlayer alarmSound = MediaPlayer.create(this, R.raw.algo);
                if (flag == 1) {
                    showNotification()
                } else {
                    pasar2()
                }
                //alarmSound.start();
            }
        }.start()
        mTimerRunning = true
    }

    /**
     * Función que controla el temporizador
     *
     */
    private fun updateCountDownText() {
        val minutes = (mTimeLeftInMillis / 1000).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        mTextViewCountDown!!.text = timeLeftFormatted
    }

    /**
     * Función que pasa a la siguiente actividad depende de la situación
     *
     * @param view the view
     */
    fun pasar2() {
        val bundle2 = Bundle()
        if (cuantas.equals("2", ignoreCase = true) && actual.equals("1", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "2")
            bundle2.putString("numAsig", cuantas)
            siguiente1.putExtras(bundle2)
            if (flag != 0) {
                r!!.stop()
            }
            startActivity(siguiente1)
        }
        if ((cuantas.equals("3", ignoreCase = true) || cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("1", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaDificil::class.java)
            bundle2.putString("actAsig", "2")
            bundle2.putString("numAsig", cuantas)
            siguiente1.putExtras(bundle2)
            if (flag != 0) {
                r!!.stop()
            }
            startActivity(siguiente1)
        }
        if (cuantas.equals("3", ignoreCase = true) && actual.equals("2", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "3")
            bundle2.putString("numAsig", cuantas)
            if (flag != 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
        }
        if ((cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("2", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaGustar::class.java)
            bundle2.putString("actAsig", "3")
            bundle2.putString("numAsig", cuantas)
            if (flag != 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
        }
        if ((cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("3", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "4")
            bundle2.putString("numAsig", cuantas)
            if (flag != 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
        }
        if (cuantas.equals("5", ignoreCase = true) && actual.equals("4", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "5")
            bundle2.putString("numAsig", cuantas)
            if (flag != 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
        }
        if (cuantas.equals(actual, ignoreCase = true)) {
            val siguiente1 = Intent(this, MainActivity::class.java)
            if (flag != 0) {
                r!!.stop()
            }
            startActivity(siguiente1)
        }
    }

    fun pasar(view: View?) {
        val bundle2 = Bundle()
        if (cuantas.equals("2", ignoreCase = true) && actual.equals("1", ignoreCase = true)) {
            val siguiente1 = Intent(view!!.context, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "2")
            bundle2.putString("numAsig", cuantas)
            siguiente1.putExtras(bundle2)
            r!!.stop()
            startActivity(siguiente1)
        }
        if ((cuantas.equals("3", ignoreCase = true) || cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("1", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaDificil::class.java)
            bundle2.putString("actAsig", "2")
            bundle2.putString("numAsig", cuantas)
            siguiente1.putExtras(bundle2)
            r!!.stop()
            startActivity(siguiente1)
        }
        if (cuantas.equals("3", ignoreCase = true) && actual.equals("2", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "3")
            bundle2.putString("numAsig", cuantas)
            r!!.stop()
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
        }
        if ((cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("2", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaGustar::class.java)
            bundle2.putString("actAsig", "3")
            bundle2.putString("numAsig", cuantas)
            r!!.stop()
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
        }
        if ((cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("3", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "4")
            bundle2.putString("numAsig", cuantas)
            r!!.stop()
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
        }
        if (cuantas.equals("5", ignoreCase = true) && actual.equals("4", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "5")
            bundle2.putString("numAsig", cuantas)
            r!!.stop()
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
        }
        if (cuantas.equals(actual, ignoreCase = true)) {
            val siguiente1 = Intent(this, MainActivity::class.java)
            r!!.stop()
            startActivity(siguiente1)
        }
    }

    fun showNotification() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            r = RingtoneManager.getRingtone(applicationContext, notification)
            r?.play()

            val handlerThread = HandlerThread("StopRingtoneThread")
            handlerThread.start()

            val handler = Handler(handlerThread.looper)
            handler.postDelayed({
                r?.stop()
                handlerThread.quitSafely()
            }, 60000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun Finalizar(view: View?) {
        Log.d("Finalizando", "Finalizando usando $view")
        mCountDownTimer!!.cancel()
        mTimerRunning = false
        flag = 0
        mTimeLeftInMillis = 1000
        startTimer()
        mButtonFin!!.visibility = View.INVISIBLE
    }
}