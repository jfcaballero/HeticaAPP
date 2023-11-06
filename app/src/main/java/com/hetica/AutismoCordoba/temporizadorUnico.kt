package com.hetica.AutismoCordoba


import android.annotation.SuppressLint
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.TextView
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
    private var Comentarios: Button? = null

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
    private val longClickDuration = 3000

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temporizador_unico)
        bundle = intent.extras
        db = AdminSQLiteOpenHelperStats(this, "Stats.db", null, 1)
        mTextViewCountDown = findViewById<View>(R.id.textViewCountDown2) as TextView
        textAsig = findViewById<View>(R.id.textView56) as TextView
        botonFin = findViewById<View>(R.id.button48) as Button
        botonFin!!.visibility = View.INVISIBLE
        Comentarios = findViewById<View>(R.id.comentariosBoton2) as Button

        finFlag = 1
        timeString = bundle!!.getString("time")
        asig = bundle!!.getString("asig")
        textAsig!!.text = asig
        Log.e("Date Selected", timeString!!)
        Pausa = findViewById<View>(R.id.buttonPausa) as Button
        Fin = findViewById<View>(R.id.buttonFin2) as Button
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
        Comentarios!!.setOnLongClickListener {
            val dialog = EnviarComentarios()
            dialog.show(supportFragmentManager, "enviarComentarios")

            true
        }
        Main!!.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                then = System.currentTimeMillis()
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (System.currentTimeMillis() - then > longClickDuration) {
                    siguiente = Intent(baseContext, MainActivity::class.java)
                    mCountDownTimer!!.cancel()
                    mTimerRunning = false
                    startActivity(siguiente)
                    println("Long Click has happened!")
                    return@OnTouchListener false
                } else {
                    /* Implement short click behavior here or do nothing */
                    println("Short Click has happened...")
                    return@OnTouchListener false
                }
            }
            true
        })
        startTimer()
    }

    override fun onBackPressed() {
        super.onBackPressed()
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
                botonFin!!.visibility = View.VISIBLE
                Fin!!.visibility = View.INVISIBLE
                showNotification()
                val sdf = SimpleDateFormat("MMddyyyy")
                db!!.insertData(asig, sdf.format(Date()), Integer.valueOf(timeString))
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
        timeString = (Integer.valueOf(timeString) - (mTimeLeftInMillis / 1000).toInt() / 60).toString()
        mCountDownTimer!!.cancel()
        mTimerRunning = false
        finFlag = 1
        val sdf = SimpleDateFormat("MMddyyyy")
        db!!.insertData(asig, sdf.format(Date()), Integer.valueOf(timeString))
        val siguiente1 = Intent(this, Recompensa::class.java)
        startActivity(siguiente1)
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
        val siguiente = Intent(this, Recompensa::class.java)
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
            val sb = StringBuilder()
            var text: String
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
            val sb = StringBuilder()
            var text: String
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
            val handler = Handler()
            handler.postDelayed({ r?.stop() }, 60000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}