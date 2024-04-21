package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import AdminSQLiteOpenHelperCalificaciones
import AdminSQLiteOpenHelperComentarios
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader


/**
 * The type Main activity.
 *
 * @author Miguel Ángel Borreguero Aparicio
 */
class MainActivity : AppCompatActivity() {
    var siguiente: Intent? = null
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 3000L // 3 segundos
    private var isLongPressFired = false
    private lateinit var sharedPreferences: SharedPreferences
    /**
     * The Db.
     */
    var dbAsignaturas: AdminSQLiteOpenHelperAsig? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serviceIntent = Intent(this, ConcentrationModeService::class.java)
        startService(serviceIntent)

        setContentView(R.layout.activity_main)
        dbAsignaturas = AdminSQLiteOpenHelperAsig(this)
        botonTemp = findViewById<View>(R.id.button45) as Button
        botonOpc = findViewById<View>(R.id.button4) as Button
        botonStat = findViewById<View>(R.id.button5) as Button
        botonCred = findViewById<View>(R.id.button6) as Button
        botonCalendario = findViewById<View>(R.id.button16) as? Button ?: return
        botonTemp!!.visibility = View.INVISIBLE
        boton = findViewById<View>(R.id.button) as Button
        boton1 = findViewById<View>(R.id.button2) as Button
        boton2 = findViewById<View>(R.id.button3) as Button
        text = findViewById<View>(R.id.textView59) as TextView
        text1 = findViewById<View>(R.id.textView60) as TextView
        text2 = findViewById<View>(R.id.textView61) as TextView

        eliminarDatosVersionAnterior()
        leerTemp()

        /* Nuevo código para obtener height and widht
        val windowMetrics = windowManager.currentWindowMetrics
        val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        val height = windowMetrics.bounds.height() - insets.bottom - insets.top
        val weight = windowMetrics.bounds.width() - insets.right - insets.left


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int weight = size.x;
        if (height < 721) {
            boton!!.layoutParams.width = 250
            boton!!.layoutParams.height = 250
            boton1!!.layoutParams.width = 250
            boton1!!.layoutParams.height = 250
            boton2!!.layoutParams.width = 250
            boton2!!.layoutParams.height = 250
        }
        if (weight < 1300) {
            text!!.textSize = 11f
            text1!!.textSize = 11f
            text2!!.textSize = 11f
        }*/

        pasarOpciones()
        pasarEstadisticas()
        pasarCreditos()

        var fos: FileOutputStream? = null
        var filename = "tiempo_trabajar.txt"
        var file = File(applicationContext.filesDir, filename)
        if (file.exists()) {
        } else {
            try {
                fos = openFileOutput(filename, MODE_PRIVATE)
                fos.write("30".toByteArray())
                fos.write("\n".toByteArray())
                fos.write("0".toByteArray())
                fos.write("\n".toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        filename = "pausa.txt"
        file = File(applicationContext.filesDir, filename)
        if (file.exists()) {
        } else {
            try {
                fos = openFileOutput(filename, MODE_PRIVATE)
                fos.write("1".toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        filename = "fin.txt"
        file = File(applicationContext.filesDir, filename)
        if (file.exists()) {
        } else {
            try {
                fos = openFileOutput(filename, MODE_PRIVATE)
                fos.write("1".toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    /**
     * Función para eliminar los archivos de la versión anterior de la aplicación.
     */
    fun eliminarDatosVersionAnterior() {
        if (getFirstTimeRun()==2 || getFirstTimeRun()==0) {

            val dbAsignaturas = AdminSQLiteOpenHelperAsig(this)
            val dbCalificaciones = AdminSQLiteOpenHelperCalificaciones(this, null, 3)
            val dbStats = AdminSQLiteOpenHelperStats(this)
            val dbComentarios=AdminSQLiteOpenHelperComentarios(this)
            val dbCalendario=AdminSQLiteOpenHelperCalendario(this)

            dbAsignaturas.clearData()
            dbCalificaciones.clearData()
            dbStats.clearData()
            dbComentarios.clearData()
            dbCalendario.clearData()
            //para reiniciar la configuracion del boton de temporizador unico
            escribirValorCeroEnTemporizador()
            //reiniciar el modo concentracion
            resetConcentrationState()
            //poner el resto de configuracion por defecto
            setDefaulSettings()


        }
    }

    /**
     * Escribir el valor "0" en el archivo temporizador.txt
     */
    fun escribirValorCeroEnTemporizador() {
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput("temporizador.txt", MODE_PRIVATE)
            Log.d("pudimos leer","ole")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.d("no pudimos leer","lo siento")
        }
        try {
            fos!!.write(Integer.toString(0).toByteArray())
            Log.d("pudimos escribir","ole")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("no pudimos escribir","lo siento")
        }
    }


    override fun onBackPressed() {
        //new AlertDialog.Builder(this)
        //        .setTitle("Really Exit?")
        //        .setMessage("Are you sure you want to exit?")
        //        .setNegativeButton(android.R.string.no, null)
        //        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

        //                   public void onClick(DialogInterface arg0, int arg1) {
        //                MainActivity.super.onBackPressed();
        //            }
        //        }).create().show();
    }
    /**
     * 0: La aplicación se instalo por primera vez
     * 1: No ha cambiado nada
     * 2: La aplicación se acaba de actualizar
     */
    private fun getFirstTimeRun(): Int {
        val sp = getSharedPreferences("MYAPP", 0)
        val result: Int
        val currentVersionCode = BuildConfig.VERSION_CODE
        val lastVersionCode = sp.getInt("FIRSTTIMERUN", -1)
        result =
            if (lastVersionCode == -1) 0 else if (lastVersionCode == currentVersionCode) 1 else 2
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply()
        return result
    }

    /**Establecer el resto de configuracion a su estado inicial
     */
    private fun setDefaulSettings(){
        //PAUSA
        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput("pausa.txt", MODE_PRIVATE)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos!!.write(Integer.toString(0).toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //FIN
        var fos2: FileOutputStream? = null
        try {
            fos2 = openFileOutput("fin.txt", MODE_PRIVATE)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos2!!.write(Integer.toString(0).toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //Tiempo de trabajo fijo
        fos = openFileOutput("tiempo_trabajar.txt", MODE_PRIVATE)
        val resul = 30
        fos.write(Integer.toString(resul).toByteArray())
        fos.write("\n".toByteArray())
        fos.write("0".toByteArray())
        fos.write("\n".toByteArray())

    }

    private fun resetConcentrationState() {
        sharedPreferences = getSharedPreferences("settings_preferences", Context.MODE_PRIVATE)
        val HeticaActivatedConcentrationMode = sharedPreferences.getBoolean("concentration_mode", false)
        if(HeticaActivatedConcentrationMode){
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)

        }
         try {
            val fos = openFileOutput("concentracion.txt", MODE_PRIVATE)
            fos.write("0".toByteArray())
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun pasarOpciones() {
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                if (isLongPressFired) {
                    return
                }
                isLongPressFired = true

                // Usamos un Handler para retrasar la apertura de la actividad EditarCalendario
                handler.postDelayed({

                    val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                    startActivity(intent)


                }, delayMillis)
            }
        })
        val boton = findViewById<Button>(R.id.button4)
        boton?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                isLongPressFired = false
                // Si se libera el botón antes del tiempo de espera, cancelamos el Handler
                handler.removeCallbacksAndMessages(null)
            }
            true
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun pasarEstadisticas() {
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                if (isLongPressFired) {
                    return
                }
                isLongPressFired = true

                // Usamos un Handler para retrasar la apertura de la actividad EditarCalendario
                handler.postDelayed({
                if(countData()!=0){
                    val intent = Intent(this@MainActivity, estadisticasDias::class.java)
                    startActivity(intent)
                }else{
                    showSnackbarWithCustomTextSize(this@MainActivity, "Introduce primero una asignatura")

                }

                }, delayMillis)
            }
        })
        val boton = findViewById<Button>(R.id.button5)
        boton?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                isLongPressFired = false
                // Si se libera el botón antes del tiempo de espera, cancelamos el Handler
                handler.removeCallbacksAndMessages(null)
            }
            true
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun pasarCreditos() {
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                if (isLongPressFired) {
                    return
                }
                isLongPressFired = true

                // Usamos un Handler para retrasar la apertura de la actividad EditarCalendario
                handler.postDelayed({

                    val intent = Intent(this@MainActivity, creditos::class.java)
                    startActivity(intent)


                }, delayMillis)
            }
        })
        val boton = findViewById<Button>(R.id.button6)
        boton?.setOnTouchListener { _, event ->
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
     * Pasar a la pantalla de "Asignatura de hoy"
     *
     * @param view the view
     */
    fun pasarAsignaturaDeHoy(view: View) {
        if (countData() == 0) {
            showSnackbarWithCustomTextSize(view.context, "Introduce primero una asignatura")
        } else {
            val intent = Intent(this, AsignaturaDeHoy::class.java)
            startActivity(intent)
        }
    }

    /**
     * Pasar a la pantalla de "Organizar el Estudio"
     *
     * @param view the view
     */
    fun pasarOrgEst(view: View?) {
        if (countData() == 0) {
            showSnackbarWithCustomTextSize(view!!.context, "Introduce primero una asignatura")
        } else {
            val siguiente = Intent(this, organizar_tareas1::class.java)
            startActivity(siguiente)
        }
    }

    /**
     * Pasar org tar.
     *
     * @param view the view
     */
    fun pasarOrgTar(view: View?) {
        if (countData() == 0) {
            showSnackbarWithCustomTextSize(view!!.context, "Introduce primero una asignatura")
        } else {
            val siguiente = Intent(this, OrganizarEstudio1::class.java)
            startActivity(siguiente)
        }
    }

    /**
     * Pasar a la pantalla de "Organizar la mochila"
     *
     * @param view the view
     */
    fun pasarOrgMoch(view: View?) {
        val siguiente = Intent(view!!.context, OrganizarMochila1::class.java)
        startActivity(siguiente)
    }



    /**
     * Comprobar que la base de datos no este vacia
     *
     * @param view the view
     */
    fun pasarTemporizador(view: View?) {
        if (countData() == 0) {
            showSnackbarWithCustomTextSize(view!!.context, "Introduce primero una asignatura" )
        } else {
            val siguiente = Intent(this, tiempoEstudio::class.java)
            startActivity(siguiente)
        }
    }

    /**
     * Mostrar o no la función del temporizador
     */
    fun leerTemp() {
        var fis: FileInputStream? = null
        try {
            fis = openFileInput("temporizador.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            val text: String
            text = br.readLine()
            Log.d("MainActivity", "Contenido de temporizador.txt:\n${text}")

            if (text.equals("1", ignoreCase = true)) {
                botonTemp!!.visibility = View.VISIBLE
            } else {
                botonTemp!!.visibility = View.INVISIBLE
            }
            try {
                fis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
            }
        }
    }

    /**
     * Devuelve el numero de asignaturas en la base de datos
     *
     * @return
     */
    private fun countData(): Int {
        val cursor = dbAsignaturas!!.viewData()
        return cursor.count
    }

    companion object {
        /**
         * The constant botonTemp.
         */
        var botonTemp: Button? = null
        var botonOpc: Button? = null
        var botonStat: Button? = null
        var botonCred: Button? = null
        var text: TextView? = null
        var text1: TextView? = null
        var text2: TextView? = null
        var boton: Button? = null
        var boton1: Button? = null
        var boton2: Button? = null
        var botonCalendario:Button? = null
    }
}