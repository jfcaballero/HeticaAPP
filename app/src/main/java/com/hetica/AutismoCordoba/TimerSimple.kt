package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import android.annotation.SuppressLint
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
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
    private var vengo_de_asignaturas_de_hoy: Boolean = false
    private lateinit var r: Ringtone
    private lateinit var db: AdminSQLiteOpenHelperStats
    private var dbCalendario: AdminSQLiteOpenHelperCalendario? = null
    private lateinit var mTextViewCountDown: TextView
    private lateinit var mButtonSi: Button
    private lateinit var mButtonNo: Button
    private lateinit var Pausa: Button
    private lateinit var Fin: Button
    private lateinit var Fin2: Button
    private var isLongPressFired = false
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 3000L // 3 segundos
    private lateinit var Main: Button
    private var mStartTime: Long = 0
    private var finFlag: Int = 0
    private lateinit var mCountDownTimer: CountDownTimer
    private var mTimerRunning: Boolean = false
    private var mTimeLeftInMillis: Long = 0

    //estas dos solo las pasa AsignaturaDeHoy
    private var posicionDeLaAsignaturaCalendario: String=""
    private var fechaDelEstudioCalendario:String=""
    var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_simple)

        bundle = intent.extras!!
        val textView: TextView = findViewById(R.id.textView49)

        db = AdminSQLiteOpenHelperStats(this)
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        finFlag = 0
        dbCalendario = AdminSQLiteOpenHelperCalendario(this)
        timeString = bundle.getString("time")!!
        asig = bundle.getString("asig")!!
        actual = bundle.getString("actAsig")!!
        cuantas = bundle.getString("numAsig")!!

        //estas dos siguientes SÓLO las pasa la actividad AsignaturaDeHoy para marcarlas como estudiadas aqui
        // Obtén los extras del intento
        val extras = intent.extras

        // Verifica si hay extras y si los extras específicos están presentes
        if (extras != null && extras.containsKey("posicion_de_la_asignatura") && extras.containsKey("fecha_del_estudio")) {
            vengo_de_asignaturas_de_hoy=true
            val posicion = extras.getString("posicion_de_la_asignatura")
            val fechaCalendario = extras.getString("fecha_del_estudio")

            if (posicion != null && fechaCalendario != null) {
                // Utiliza los valores obtenidos
                posicionDeLaAsignaturaCalendario = posicion
                fechaDelEstudioCalendario = fechaCalendario

                Log.d("Posicion en TIMER", posicionDeLaAsignaturaCalendario)
                Log.d("Fecha en TIMER", fechaDelEstudioCalendario)
            }else{
                posicionDeLaAsignaturaCalendario=""
                fechaDelEstudioCalendario=""
            }
        } else {
            // Si los extras específicos no están presentes, simplemente no hagas nada o maneja la situación según lo necesites
            Log.d("TimerSimple", "Los extras 'posicion_de_la_asignatura' y 'fecha_del_estudio' no están presentes en el intento.")
        }

        Log.d("Posicion en TIMER",posicionDeLaAsignaturaCalendario)
        Log.d("Fecha en TIMER",fechaDelEstudioCalendario)


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
        GoToMain()


        Pausa.setOnClickListener {
            if (mTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }
    }
    private fun marcarComoEstudiada(){
        if(posicionDeLaAsignaturaCalendario.isNotEmpty() && fechaDelEstudioCalendario.isNotEmpty()){
            Log.d("Posicion en TIMER ANTES DE INSERTAR",posicionDeLaAsignaturaCalendario)
            Log.d("Fecha en TIMER ANTES DE INSERTAR",fechaDelEstudioCalendario)
            dbCalendario?.marcarComoEstudiado(posicionDeLaAsignaturaCalendario.toInt(), fechaDelEstudioCalendario)
        }
    }
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (doubleBackToExitPressedOnce) {
                val siguiente = Intent(baseContext, MainActivity::class.java)
                startActivity(siguiente)
                mCountDownTimer.cancel()
                return
            }
            doubleBackToExitPressedOnce = true
            FuncionesComunes.showSnackbarWithCustomTextSize(
                this@TimerSimple,
                "Presiona de nuevo para salir",
            )
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
            // Override if needed

        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun GoToMain() {
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                if (isLongPressFired) {
                    return
                }
                isLongPressFired = true

                // Usamos un Handler para retrasar la apertura de la actividad EditarCalendario
                handler.postDelayed({
                    val intent = Intent(this@TimerSimple, MainActivity::class.java)
                    mCountDownTimer.cancel()
                    startActivity(intent)
                }, delayMillis)
            }
        })

        Main.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                isLongPressFired = false
                // Si se libera el botón antes del tiempo de espera, cancelamos el Handler
                handler.removeCallbacksAndMessages(null)
                //mCountDownTimer.cancel()
            }
            true
        }
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
                    marcarComoEstudiada()
                    db.insertData(asig, sdf.format(Date()),
                        timeString.let { Integer.valueOf(it) })
                    Pausa.visibility = View.INVISIBLE
                    Fin.visibility = View.INVISIBLE
                    if (!cuantas.equals(actual, ignoreCase = true) && finFlag == 0) {
                        showNotification()
                    }
                } else {
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    marcarComoEstudiada()
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
        // Detener el tono de notificación
        r.stop()

        // Crear el intent para la próxima actividad
        val siguiente1 = Intent(view!!.context, Recompensa::class.java)

        // Pasar la bandera "vengo_de_asignaturas_de_hoy"
        siguiente1.putExtra("vengo_de_asignaturas_de_hoy", vengo_de_asignaturas_de_hoy)

        // Iniciar la próxima actividad
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

        // Que suene la alarma cuando haya más de una asignatura y no sea la última
        if (!cuantas.equals(actual, ignoreCase = true)) {
            showNotification()
        }
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
        r.stop()
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
        r.stop()
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
            // Detener cualquier instancia de Ringtone que se esté reproduciendo actualmente
            if (::r.isInitialized && r.isPlaying) {
                r.stop()
            }

            // Reproducir el tono de notificación
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()

            // Detener el tono después de un minuto
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