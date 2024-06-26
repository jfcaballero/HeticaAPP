package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import AdminSQLiteOpenHelperCalificaciones
import AdminSQLiteOpenHelperComentarios
import PrefManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class Bienvenida : AppCompatActivity() {
    private lateinit var prefManager: PrefManager
    private lateinit var db: AdminSQLiteOpenHelperAsig
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bienvenida)
        resetConcentrationState()
        eliminarDatosVersionAnterior()
        prefManager = PrefManager(this)
        db = AdminSQLiteOpenHelperAsig(this)

        if (countData() == 0) {
            // Si no hay asignaturas en la base de datos, mostrar la actividad de bienvenida
            val btnEntendido: Button = findViewById(R.id.btn_entendido)
            btnEntendido.setOnClickListener {
                prefManager.setFirstTimeLaunch(false)
                launchHomeScreen()
            }
        } else {
            // Si hay asignaturas en la base de datos, ir directamente a la actividad principal
            launchHomeScreen()
        }
    }
    /**
     * Nos lleva a la actividad principal
     *
     */
    private fun launchHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Devuelve el numero de asignaturas en la base de datos
     *
     * @return
     */
    private fun countData(): Int {
        val cursor = db.viewData()
        return cursor.count
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
        Log.d("currentVersionCode=","$currentVersionCode")
        Log.d("lastVersionCode=","$lastVersionCode")
        result =
            if (lastVersionCode == -1) 0 else if (lastVersionCode == currentVersionCode) 1 else 2
        Log.d("resultcode=","$result")
        sp.edit().putInt("FIRSTTIMERUN", currentVersionCode).apply()

        return result
    }
    private fun isFirstRun(): Boolean {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }
        return isFirstRun
    }

    /**
     * Función para eliminar los archivos de la versión anterior de la aplicación
     * cuando se instale una nueva version o se instale por primera vez.
     */
    fun eliminarDatosVersionAnterior() {
        if (getFirstTimeRun()==2 || getFirstTimeRun()==0 || isFirstRun()) { //si no estamos en la misma version, ponemos a default la configuracion
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
            //reiniciar la configuracion del Modo concentracion
            resetConcentrationState()
            setDefaulSettings()

        }
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

}