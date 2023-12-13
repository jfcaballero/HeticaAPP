package com.hetica.AutismoCordoba


import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.HandlerThread
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
 * The type Timer simple.
 */
class TimerSimple : AppCompatActivity() {
    //Bundle bundle = getIntent().getExtras();
    /**
     * The Time string.
     */
    //String timeString = bundle.getString("time");
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

    /**
     * The Booool.
     */
    var booool = 0
    private var r: Ringtone? = null

    /**
     * The Db.
     */
    var db: AdminSQLiteOpenHelperStats? = null
    private var mTextViewCountDown: TextView? = null
    private var mButtonSi: Button? = null
    private var mButtonNo: Button? = null
    private var Pausa: Button? = null
    private var Fin: Button? = null
    private var Fin2: Button? = null
    private var Main: Button? = null
    private var Comentarios: Button? = null

    //private long mStartTime = Long.parseLong(timeString) * 60000;
    private var mStartTime: Long = 0
    var siguiente: Intent? = null
    private var then: Long = 0
    private val longClickDuration = 3000
    var finFlag = 0
    private var mCountDownTimer: CountDownTimer? = null
    private var mTimerRunning = false
    private var mTimeLeftInMillis: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_simple)
        bundle = intent.extras
        textView = findViewById<View>(R.id.textView49) as TextView
        db = AdminSQLiteOpenHelperStats(this)
        finFlag = 0
        timeString = bundle!!.getString("time")
        asig = bundle!!.getString("asig")
        actual = bundle!!.getString("actAsig")
        cuantas = bundle!!.getString("numAsig")
        textView!!.text = asig
        mStartTime = timeString!!.toLong() * 60000
        mTimeLeftInMillis = mStartTime
        mTextViewCountDown = findViewById(R.id.text_view_count)
        Main = findViewById<View>(R.id.button13) as Button
        Comentarios = findViewById<View>(R.id.comentariosBoton) as Button
        Fin = findViewById<View>(R.id.buttonFin) as Button
        Fin2 = findViewById<View>(R.id.buttonFin2) as Button
        Fin2!!.visibility = View.INVISIBLE
        mButtonSi = findViewById<View>(R.id.button38) as Button
        mButtonSi!!.visibility = View.INVISIBLE
        mButtonNo = findViewById<View>(R.id.button39) as Button
        mButtonNo!!.visibility = View.INVISIBLE
        Pausa = findViewById<View>(R.id.button41) as Button
        readParams()
        mTextViewCountDown?.visibility = View.VISIBLE
        startTimer()
        Main!!.setOnTouchListener(OnTouchListener { _, event ->
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

        Comentarios!!.visibility = View.INVISIBLE
        Comentarios!!.setOnLongClickListener {
            val dialog = EnviarComentarios()
            dialog.show(supportFragmentManager, "enviarComentarios")

            true
        }
        Pausa!!.setOnClickListener {
            if (mTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }
    }

    override fun onBackPressed() {}
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
    fun startTimer() {
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                if (!cuantas.equals(actual, ignoreCase = true)) {
                    mTimerRunning = false
                    mButtonSi!!.visibility = View.VISIBLE
                    mButtonNo!!.visibility = View.VISIBLE
                    mTextViewCountDown!!.text = "¿Necesitas un descanso?"
                    mTextViewCountDown!!.textSize = 40f
                    val sdf = SimpleDateFormat("MMddyyyy")
                    db!!.insertData(asig, sdf.format(Date()),
                        timeString?.let { Integer.valueOf(it) })

                    Pausa!!.visibility = View.INVISIBLE
                    Fin!!.visibility = View.INVISIBLE
                    if (!cuantas.equals(actual, ignoreCase = true) && finFlag == 0) {
                        showNotification()
                    }
                } else {
                    //las dos lineas siguientes son para que inserte la última asignatura porque se la salta al final
                    val sdf = SimpleDateFormat("MMddyyyy")
                    db!!.insertData(asig, sdf.format(Date()),
                        timeString?.let { Integer.valueOf(it) })

                    Pausa!!.visibility = View.INVISIBLE
                    Fin!!.visibility = View.INVISIBLE
                    Fin2!!.visibility = View.VISIBLE
                    showNotification()
                }
                //final MediaPlayer alarmSound = MediaPlayer.create(this, R.raw.algo);

                // alarmSound.start();
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

    fun finEstudio(view: View?) {
        r!!.stop()
        val siguiente1 = Intent(view!!.context, Recompensa::class.java)
        startActivity(siguiente1)
    }

    fun finTimer(view: View?) {
        timeString?.let {
            val intValue = Integer.valueOf(it) - (mTimeLeftInMillis / 1000).toInt() / 60
            timeString = intValue.toString()
        }

        mCountDownTimer?.cancel()
        mTimerRunning = false
        Log.d("finTimer", "usando $view")
        finFlag = 1
        mTimeLeftInMillis = 1000
        startTimer()
        Comentarios?.visibility = View.VISIBLE
    }


    /**
     *
     * Función que controla el temporizador
     */
    private fun updateCountDownText() {
        val minutes = (mTimeLeftInMillis / 1000).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        mTextViewCountDown!!.text = timeLeftFormatted
        if (cuantas.equals(actual, ignoreCase = true) && seconds == 1 && minutes == 0) {
            val siguiente1 = Intent(this, Recompensa::class.java)
            startActivity(siguiente1)
        }
    }

    /**
     * Función que pasa a la siguiente asignatura ya que no se ha elegido descanso
     *
     * @param view the view
     */
    fun pasarNo(view: View?) {
        val bundle2 = Bundle()
        if (cuantas.equals("2", ignoreCase = true) && actual.equals("1", ignoreCase = true)) {
            val siguiente1 = Intent(view!!.context, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "2")
            bundle2.putString("numAsig", cuantas)
            siguiente1.putExtras(bundle2)
            if (finFlag == 0) {
                r!!.stop()
            }
            startActivity(siguiente1)
            finish()
        }
        if ((cuantas.equals("3", ignoreCase = true) || cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("1", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaDificil::class.java)
            bundle2.putString("actAsig", "2")
            bundle2.putString("numAsig", cuantas)
            siguiente1.putExtras(bundle2)
            if (finFlag == 0) {
                r!!.stop()
            }
            startActivity(siguiente1)
            finish()
        }
        if (cuantas.equals("3", ignoreCase = true) && actual.equals("2", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "3")
            bundle2.putString("numAsig", cuantas)
            if (finFlag == 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
            finish()
        }
        if ((cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("2", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaGustar::class.java)
            bundle2.putString("actAsig", "3")
            bundle2.putString("numAsig", cuantas)
            if (finFlag == 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
            finish()
        }
        if ((cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("3", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "4")
            bundle2.putString("numAsig", cuantas)
            if (finFlag == 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
            finish()
        }
        if (cuantas.equals("5", ignoreCase = true) && actual.equals("4", ignoreCase = true)) {
            val siguiente1 = Intent(this, AsignaturaSiguiente::class.java)
            bundle2.putString("actAsig", "5")
            bundle2.putString("numAsig", cuantas)
            if (finFlag == 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
            finish()
        }
    }

    /**
     * Función que pasa al descanso
     *
     * @param view the view
     */
    fun pasarSi(view: View?) {
        val bundle2 = Bundle()
        if ((cuantas.equals("2", ignoreCase = true) || cuantas.equals("3", ignoreCase = true) || cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("1", ignoreCase = true)) {
            val siguiente1 = Intent(view!!.context, TimerDescanso::class.java)
            bundle2.putString("actAsig", "1")
            bundle2.putString("numAsig", cuantas)
            if (finFlag == 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
            finish()
        }
        if ((cuantas.equals("3", ignoreCase = true) || cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("2", ignoreCase = true)) {
            val siguiente1 = Intent(this, TimerDescanso::class.java)
            bundle2.putString("actAsig", "2")
            bundle2.putString("numAsig", cuantas)
            if (finFlag == 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
            finish()
        }
        if ((cuantas.equals("4", ignoreCase = true) || cuantas.equals("5", ignoreCase = true)) && actual.equals("3", ignoreCase = true)) {
            val siguiente1 = Intent(this, TimerDescanso::class.java)
            bundle2.putString("actAsig", "3")
            bundle2.putString("numAsig", cuantas)
            if (finFlag == 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
            finish()
        }
        if (cuantas.equals("5", ignoreCase = true) && actual.equals("4", ignoreCase = true)) {
            val siguiente1 = Intent(this, TimerDescanso::class.java)
            bundle2.putString("actAsig", "4")
            bundle2.putString("numAsig", cuantas)
            if (finFlag == 0) {
                r!!.stop()
            }
            siguiente1.putExtras(bundle2)
            startActivity(siguiente1)
            finish()
        }
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


    companion object {
        private const val START_TIME_IN_MILLIS: Long = 600000
        private var textView: TextView? = null
    }
}