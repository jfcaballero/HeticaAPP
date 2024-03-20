package com.hetica.AutismoCordoba

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

class TimerSimple : AppCompatActivity() {

    private val START_TIME_IN_MILLIS: Long = 600000
    private lateinit var timeString: String
    private lateinit var actual: String
    private lateinit var cuantas: String
    private lateinit var bundle: Bundle
    private lateinit var asig: String
    private var booool: Int = 0
    private lateinit var r: Ringtone
    private lateinit var db: AdminSQLiteOpenHelperStats

    private lateinit var mTextViewCountDown: TextView
    private lateinit var mButtonSi: Button
    private lateinit var mButtonNo: Button
    private lateinit var Pausa: Button
    private lateinit var Fin: Button
    private lateinit var Fin2: Button
    private lateinit var Main: Button
    private var mStartTime: Long = 0
    private lateinit var siguiente: Intent
    private var then: Long = 0
    private val longClickDuration = 3000
    private var finFlag: Int = 0
    private lateinit var mCountDownTimer: CountDownTimer
    private var mTimerRunning: Boolean = false
    private var mTimeLeftInMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_simple)

        bundle = intent.extras!!
        val textView: TextView = findViewById(R.id.textView49)

        db = AdminSQLiteOpenHelperStats(this)

        finFlag = 0

        timeString = bundle.getString("time")!!
        asig = bundle.getString("asig")!!
        actual = bundle.getString("actAsig")!!
        cuantas = bundle.getString("numAsig")!!
        textView.text = asig

        mStartTime = timeString.toLong() * 60000
        mTimeLeftInMillis = mStartTime

        mTextViewCountDown = findViewById(R.id.text_view_count)
        Main = findViewById(R.id.button13)
        Fin = findViewById(R.id.buttonFin)
        Fin2 = findViewById(R.id.buttonFin2)
        Fin2.visibility = View.INVISIBLE
        mButtonSi = findViewById(R.id.button38)
        mButtonSi.visibility = View.INVISIBLE
        mButtonNo = findViewById(R.id.button39)
        mButtonNo.visibility = View.INVISIBLE
        Pausa = findViewById(R.id.button41)
        readParams()
        mTextViewCountDown.visibility = View.VISIBLE
        startTimer()

        Main.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                then = System.currentTimeMillis()
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (System.currentTimeMillis() - then > longClickDuration) {
                    siguiente = Intent(baseContext, MainActivity::class.java)
                    mCountDownTimer.cancel()
                    mTimerRunning = false
                    startActivity(siguiente)
                    println("Long Click has happened!")
                    return@setOnTouchListener false
                } else {
                    /* Implement short click behavior here or do nothing */
                    println("Short Click has happened...")
                    return@setOnTouchListener false
                }
            }
            true
        }

        Pausa.setOnClickListener {
            if (mTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }
    }

    override fun onBackPressed() {
        // Override if needed
    }

    /**
     * Función que inicia y finaliza el temporizador
     *
     */
    private fun startTimer() {
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                if (!cuantas.equals(actual, ignoreCase = true)) {

                    mTimerRunning = false
                    mButtonSi.visibility = View.VISIBLE
                    mButtonNo.visibility = View.VISIBLE
                    mTextViewCountDown.text = "¿Necesitas un descanso?"
                    mTextViewCountDown.textSize = (mTextViewCountDown.textSize*0.2).toFloat()
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    db.insertData(asig, sdf.format(Date()),
                        timeString.let { Integer.valueOf(it) })
                    Pausa.visibility = View.INVISIBLE
                    Fin.visibility = View.INVISIBLE
                    if (!cuantas.equals(actual, ignoreCase = true) && finFlag == 0) {
                        showNotification()
                    }
                } else {
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    db.insertData(asig, sdf.format(Date()),
                        timeString.let { Integer.valueOf(it) })
                    Main.visibility=View.INVISIBLE
                    Pausa.visibility = View.INVISIBLE
                    Fin.visibility = View.INVISIBLE
                    Fin2.visibility = View.VISIBLE
                    showNotification()
                }
            }
        }.start()

        mTimerRunning = true
        Pausa.text = "Pausa"
    }

    private fun pauseTimer() {
        mCountDownTimer.cancel()
        mTimerRunning = false
        Pausa.text = "Seguir"
    }

    fun finEstudio(view: View?) {
        r.stop()
        val siguiente1 = Intent(view!!.context, Recompensa::class.java)
        startActivity(siguiente1)
    }

    fun finTimer(view: View?) {
        Fin.isEnabled = false
        Pausa.isEnabled=false

        timeString.let {
            val intValue = Integer.valueOf(it) - (mTimeLeftInMillis / 1000).toInt() / 60
            timeString = intValue.toString()
        }

        mCountDownTimer.cancel()
        mTimerRunning = false
        Log.d("finTimer", "usando $view")
        finFlag = 1
        mTimeLeftInMillis = 1000
        startTimer()
    }
    /**
     *
     * Función que controla el temporizador
     */
    private fun updateCountDownText() {
        val minutes = (mTimeLeftInMillis / 1000).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        mTextViewCountDown.text = timeLeftFormatted
        //if (cuantas.equals(actual, ignoreCase = true) && seconds == 1 && minutes == 0) {
        //    val siguiente1 = Intent(this, Recompensa::class.java)
        //    startActivity(siguiente1)
        //}
    }
    /**
     * Función que pasa a la siguiente asignatura ya que no se ha elegido descanso
     *
     * @param view the view
     */
    fun pasarNo(view: View?) {
        val bundle2 = Bundle()
        when {
            cuantas.equals("2", ignoreCase = true) && actual.equals("1", ignoreCase = true) -> {
                val siguiente1 = Intent(view!!.context, AsignaturaSiguiente::class.java)
                bundle2.putString("actAsig", "2")
                bundle2.putString("numAsig", cuantas)

                siguiente1.putExtras(bundle2)
                if (finFlag == 0) {
                    r.stop()
                }
                startActivity(siguiente1)
                finish()
            }
            (cuantas.equals("3", ignoreCase = true) || cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("1", ignoreCase = true) -> {
                val siguiente1 = Intent(this, AsignaturaDificil::class.java)
                bundle2.putString("actAsig", "2")
                bundle2.putString("numAsig", cuantas)
                siguiente1.putExtras(bundle2)
                if (finFlag == 0) {
                    r.stop()
                }
                startActivity(siguiente1)
                finish()
            }
            cuantas.equals("3", ignoreCase = true) && actual.equals("2", ignoreCase = true) -> {
                val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
                bundle2.putString("actAsig", "3")
                bundle2.putString("numAsig", cuantas)
                if (finFlag == 0) {
                    r.stop()
                }
                siguiente1.putExtras(bundle2)
                startActivity(siguiente1)
                finish()
            }
            (cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("2", ignoreCase = true) -> {
                val siguiente1 = Intent(this, AsignaturaGustar::class.java)
                bundle2.putString("actAsig", "3")
                bundle2.putString("numAsig", cuantas)
                if (finFlag == 0) {
                    r.stop()
                }
                siguiente1.putExtras(bundle2)
                startActivity(siguiente1)
                finish()
            }
            (cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("3", ignoreCase = true) -> {
                val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
                bundle2.putString("actAsig", "4")
                bundle2.putString("numAsig", cuantas)
                if (finFlag == 0) {
                    r.stop()
                }
                siguiente1.putExtras(bundle2)
                startActivity(siguiente1)
                finish()
            }
            cuantas.equals("5", ignoreCase = true) && actual.equals("4", ignoreCase = true) -> {
                val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
                bundle2.putString("actAsig", "5")
                bundle2.putString("numAsig", cuantas)
                if (finFlag == 0) {
                    r.stop()
                }
                siguiente1.putExtras(bundle2)
                startActivity(siguiente1)
                finish()
            }
        }
    }
    /**
     * Función que pasa al descanso
     *
     * @param view the view
     */
    fun pasarSi(view: View?) {
        val bundle2 = Bundle()
        when {
            (cuantas.equals("2", ignoreCase = true) || cuantas.equals("3", ignoreCase = true) || cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("1", ignoreCase = true) -> {
                val siguiente1 = Intent(view!!.context, TimerDescanso::class.java)
                bundle2.putString("actAsig", "1")
                bundle2.putString("numAsig", cuantas)
                if (finFlag == 0) {
                    r.stop()
                }
                siguiente1.putExtras(bundle2)
                startActivity(siguiente1)
                finish()
            }
            (cuantas.equals("3", ignoreCase = true) || cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("2", ignoreCase = true) -> {
                val siguiente1 = Intent(this, TimerDescanso::class.java)
                bundle2.putString("actAsig", "2")
                bundle2.putString("numAsig", cuantas)
                if (finFlag == 0) {
                    r.stop()
                }
                siguiente1.putExtras(bundle2)
                startActivity(siguiente1)
                finish()
            }
            (cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("3", ignoreCase = true) -> {
                val siguiente1 = Intent(this, TimerDescanso::class.java)
                bundle2.putString("actAsig", "3")
                bundle2.putString("numAsig", cuantas)
                if (finFlag == 0) {
                    r.stop()
                }
                siguiente1.putExtras(bundle2)
                startActivity(siguiente1)
                finish()
            }
            cuantas.equals("5", ignoreCase = true) && actual.equals("4", ignoreCase = true) -> {
                val siguiente1 = Intent(this, TimerDescanso::class.java)
                bundle2.putString("actAsig", "4")
                bundle2.putString("numAsig", cuantas)
                if (finFlag == 0) {
                    r.stop()
                }
                siguiente1.putExtras(bundle2)
                startActivity(siguiente1)
                finish()
            }
        }
    }

    private fun readParams() {
        var fis: FileInputStream? = null
        var pausa: String
        try {
            fis = openFileInput("pausa.txt")
            val isr = InputStreamReader(fis!!)
            val br = BufferedReader(isr)


            pausa = br.readLine()

            if (pausa.equals("0", ignoreCase = true)) {
                Pausa.visibility = View.INVISIBLE
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

        var fin: String
        try {
            fis = openFileInput("fin.txt")
            val isr = InputStreamReader(fis!!)
            val br = BufferedReader(isr)


            fin = br.readLine()

            if (fin.equals("0", ignoreCase = true)) {
                Fin.visibility = View.INVISIBLE
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
            r.play()

            val handlerThread = HandlerThread("StopRingtoneThread")
            handlerThread.start()

            val handler = Handler(handlerThread.looper)
            handler.postDelayed({
                r.stop()
                handlerThread.quitSafely()
            }, 60000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
