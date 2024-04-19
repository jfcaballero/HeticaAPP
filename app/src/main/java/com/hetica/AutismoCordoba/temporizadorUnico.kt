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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * The type Temporizador unico.
 */
class temporizadorUnico : AppCompatActivity() {
    private var mStartTime: Long = 0
    private var mCountDownTimer: CountDownTimer? = null
    private var mTextViewCountDown: TextView? = null
    private var textAsig: TextView? = null
    private var Pausa: Button? = null

    /**
     * The Db.
     */
    var db: AdminSQLiteOpenHelperStats? = null

    /**
     * The Bundle.
     */
    var bundle: Bundle? = null

    /**
     * The Time string.
     */
    var timeString: String? = null

    /**
     * The Asig.
     */
    var asig: String? = null
    private var r: Ringtone? = null
    var finFlag = 0

    /**
     * The Boton fin.
     */
    var botonFin: Button? = null
    private var Fin: Button? = null
    private var mTimeLeftInMillis: Long = 0
    private var mTimerRunning = false
    private var Main: Button? = null
    var siguiente: Intent? = null
    private var then: Long = 0
    var doubleBackToExitPressedOnce = false


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temporizador_unico)
        bundle = intent.extras
        db = AdminSQLiteOpenHelperStats(this)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        mTextViewCountDown = findViewById<View>(R.id.textViewCountDown2) as TextView
        textAsig = findViewById<View>(R.id.textView56) as TextView
        botonFin = findViewById<View>(R.id.button48) as Button //el ultimo en aparecer y el que corta la alarma y sale de la actividad
        botonFin!!.visibility = View.INVISIBLE


        finFlag = 1
        timeString = bundle!!.getString("time")
        asig = bundle!!.getString("asig")
        textAsig!!.text = asig
        Log.e("Minutes Selected", timeString!!)
        Pausa = findViewById<View>(R.id.buttonPausa) as Button
        Fin = findViewById<View>(R.id.buttonFin2) as Button //el que es visible de primeras y lleva el marcador al 0, activando la alarma
        readParams()
        mStartTime = timeString!!.toLong() * 60000
        mTimeLeftInMillis = mStartTime
        Main = findViewById<View>(R.id.button15) as Button
        Pausa!!.setOnClickListener {
            if (mTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        GoToMain()
        startTimer()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun GoToMain() {
        var isLongPressFired = false
        val handler = Handler(Looper.getMainLooper())
        val delayMillis = 3000L // 3 segundos
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                if (isLongPressFired) {
                    return
                }
                isLongPressFired = true

                // Usamos un Handler para retrasar la apertura de la actividad EditarCalendario
                handler.postDelayed({
                    val intent = Intent(this@temporizadorUnico, MainActivity::class.java)
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
                mCountDownTimer?.cancel()
            }
            true
        }
    }


    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                val siguiente = Intent(baseContext, MainActivity::class.java)
                startActivity(siguiente)
                mCountDownTimer?.cancel()
                finish()
                return
            }
            doubleBackToExitPressedOnce = true
            FuncionesComunes.showSnackbarWithCustomTextSize(
                this@temporizadorUnico,
                "Presiona de nuevo para salir.",
            )
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
            // Override if needed

        }
    }
    override fun onPause() {
        super.onPause()
        // Detener la alarma si está sonando
        r?.stop()
    }
    /**
     * Función que maneja el temporizador
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
                Main!!.visibility=View.INVISIBLE
                botonFin!!.visibility = View.VISIBLE
                Fin!!.visibility = View.INVISIBLE
                showNotification()
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                db!!.insertData(asig!!, sdf.format(Date()), Integer.valueOf(timeString!!))
                mTextViewCountDown!!.text = "00:00"
                //final MediaPlayer alarmSound = MediaPlayer.create(this, R.raw.algo);
                Pausa!!.visibility = View.INVISIBLE
                //alarmSound.start();
            }
        }.start()
        mTimerRunning = true
        Pausa!!.text = "Pausa"
    }

    fun pauseTimer() {
        mCountDownTimer!!.cancel()
        mTimerRunning = false
        Pausa!!.text = "Seguir"
    }

    fun finTimer(view: View?) {
        // Deshabilita el segundo botón Finalizar y Pausa para evitar múltiples clics
        Fin!!.isEnabled = false
        Pausa!!.isEnabled=false
        timeString?.let {
            val intValue = Integer.valueOf(it) - (mTimeLeftInMillis / 1000).toInt() / 60
            timeString = intValue.toString()
        }
        mCountDownTimer!!.cancel()
        mTimerRunning = false
        Main!!.visibility=View.INVISIBLE
        botonFin!!.visibility = View.VISIBLE
        Fin!!.visibility = View.INVISIBLE
        Pausa!!.visibility=View.INVISIBLE
        showNotification()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        db!!.insertData(asig!!, sdf.format(Date()), Integer.valueOf(timeString!!))
        mTextViewCountDown!!.text = "00:00"
        mTimeLeftInMillis = 0
        updateCountDownText()

        //basura para que no me de error con por no usar view
        val siguiente2 = Intent(view!!.context, Recompensa::class.java)
        Log.d("hola","$siguiente2")
        //



    }



    /**
     * Función que escribe el tiempo restante
     *
     */
    private fun updateCountDownText() {
        val minutes = (mTimeLeftInMillis / 1000).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        mTextViewCountDown!!.text = timeLeftFormatted
    }

    /**
     * Función que regresa al menú principal
     *
     * @param view the view
     */
    fun pasarInit(view: View?) {
        val siguiente = Intent(view!!.context, Recompensa::class.java)
        r!!.stop()
        startActivity(siguiente)
    }

    fun readParams() {
        var fis: FileInputStream? = null
        val pausa: String
        try {
            fis = openFileInput("pausa.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            pausa = br.readLine()
            if (pausa.equals("0", ignoreCase = true)) {
                Pausa!!.visibility = View.INVISIBLE
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        val fin: String
        try {
            fis = openFileInput("fin.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            fin = br.readLine()
            if (fin.equals("0", ignoreCase = true)) {
                Fin!!.visibility = View.INVISIBLE
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
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

}