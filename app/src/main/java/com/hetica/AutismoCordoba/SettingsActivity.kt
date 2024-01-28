package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.Calendar

/**
 * The type Settings activity.
 */

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        seekBar = findViewById<View>(R.id.seekBar4) as SeekBar
        textView = findViewById<View>(R.id.textView46) as TextView
        ayudaOpciones=findViewById(R.id.ayudaOpciones)
        Switch = findViewById<View>(R.id.switch3) as Switch
        SwitchTemp = findViewById<View>(R.id.switch1) as Switch
        SwitchPausa = findViewById<View>(R.id.switch2) as Switch
        SwitchFin = findViewById<View>(R.id.switchFin) as Switch
        SwitchConcentracion = findViewById<View>(R.id.switchConcentracion) as Switch
        seebbarr()
        if (getCurrentInterruptionFilter() == NotificationManager.INTERRUPTION_FILTER_NONE) {
            SwitchConcentracion!!.isChecked = true
        } else {
            SwitchConcentracion!!.isChecked = false
        }
        read()

        // Mostrar el cuadro de diálogo flotante de ayuda
        val textoAyuda = resources.getString(R.string.texto_ayuda)
        ayudaOpciones?.setOnClickListener {

            mostrarCuadroFlotante(textoAyuda)
        }

        if (Switch!!.isChecked) {
            seekBar!!.isEnabled = true
            textView!!.setTextColor(Color.parseColor("#2f2f2f"))
        } else {
            seekBar!!.isEnabled = false
            textView!!.setTextColor(Color.parseColor("#b6b6b6"))
        }
        Switch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                seekBar!!.isEnabled = true
                textView!!.setTextColor(Color.parseColor("#2f2f2f"))
            } else {
                seekBar!!.isEnabled = false
                textView!!.setTextColor(Color.parseColor("#b6b6b6"))
            }
        }
        SwitchTemp!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var fos: FileOutputStream? = null
                try {
                    fos = openFileOutput("temporizador.txt", MODE_PRIVATE)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.write(Integer.toString(1).toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                var fos: FileOutputStream? = null
                try {
                    fos = openFileOutput("temporizador.txt", MODE_PRIVATE)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.write(Integer.toString(0).toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        SwitchPausa!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var fos: FileOutputStream? = null
                try {
                    fos = openFileOutput("pausa.txt", MODE_PRIVATE)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.write(Integer.toString(1).toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
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
            }
        }
        SwitchFin!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var fos: FileOutputStream? = null
                try {
                    fos = openFileOutput("fin.txt", MODE_PRIVATE)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.write(Integer.toString(1).toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                var fos: FileOutputStream? = null
                try {
                    fos = openFileOutput("fin.txt", MODE_PRIVATE)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.write(Integer.toString(0).toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        SwitchConcentracion!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                var fos: FileOutputStream? = null
                try {
                    fos = openFileOutput("concentracion.txt", MODE_PRIVATE)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.write(Integer.toString(1).toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                var fos: FileOutputStream? = null
                try {
                    fos = openFileOutput("concentracion.txt", MODE_PRIVATE)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                try {
                    fos!!.write(Integer.toString(0).toByteArray())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        setupSwitchConcentracionListener()

    }
    private fun mostrarCuadroFlotante(texto: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(texto)
            .setTitle("Ayuda de opciones") // Título del cuadro de diálogo
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Función para obtener el filtro de interrupción actual.
     */
    private fun getCurrentInterruptionFilter(): Int {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.currentInterruptionFilter
    }

    /**
     * Función para manejar el switch del modo de Concentración
     */
    private fun setupSwitchConcentracionListener() {
        SwitchConcentracion!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!notificationPolicyAccessGranted()) {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                    startActivity(intent)
                    Toast.makeText(this, "Habilite el permiso de acceso a la política de notificaciones.", Toast.LENGTH_LONG).show()
                }
            }
            if (isChecked && notificationPolicyAccessGranted()) {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
            } else {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            }
        }
    }
    /**
     * Función para saber si se ha habilitado la excepción de No molestar para la app
     */
    private fun notificationPolicyAccessGranted(): Boolean {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notificationManager.isNotificationPolicyAccessGranted
    }

    override fun onBackPressed() {
        val siguiente = Intent(this, MainActivity::class.java)
        startActivity(siguiente)
        finish()
    }


    /**
     * Función que mide el valor del seekbar
     */
    fun seebbarr() {
        //textView.setText("Covered : " + seekBar.getProgress() + " / " +seekBar.getMax());
        seekBar!!.max = 30
        seekBar!!.setOnSeekBarChangeListener(
                object : OnSeekBarChangeListener {
                    var progress_value = 0
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        progress_value = progress + 30
                        textView!!.text = "$progress_value minutos "
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        textView!!.text = "$progress_value minutos "
                    }
                }
        )
    }

    /**
     * Función que guarda las opciones relacionadas con el tiempo a trabajar
     *
     * @param view the view
     */
    fun guardar(view: View?) {
        Log.d("Guardando", "Guardando usando $view")

        var fos: FileOutputStream? = null
        try {
            fos = openFileOutput("tiempo_trabajar.txt", MODE_PRIVATE)
            val resul = seekBar!!.progress + 30
            fos.write(Integer.toString(resul).toByteArray())
            fos.write("\n".toByteArray())
            if (Switch!!.isChecked) {
                fos.write("1".toByteArray())
            } else {
                fos.write("0".toByteArray())
            }
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


    /**
     * Función que lleva a las opciones de las asignaturas
     *
     * @param view the view
     */
    fun pasar_asig(view: View?) {
        val siguiente = Intent(view!!.context, ElegirAsignaturas::class.java)
        startActivity(siguiente)
    }

    /**
     * Establecer alarma mochila.
     *
     * @param mensaje the mensaje
     * @param hora    the hora
     * @param minutos the minutos
     */
    fun establecerAlarmaMochila(mensaje: String?, hora: Int, minutos: Int) {
        val alarmDays = ArrayList<Int>()
        alarmDays.add(Calendar.MONDAY)
        alarmDays.add(Calendar.TUESDAY)
        alarmDays.add(Calendar.WEDNESDAY)
        alarmDays.add(Calendar.THURSDAY)
        alarmDays.add(Calendar.SUNDAY)
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).putExtra(AlarmClock.EXTRA_MESSAGE, mensaje).putExtra(AlarmClock.EXTRA_HOUR, hora).putExtra(AlarmClock.EXTRA_MINUTES, minutos)
                .putExtra(AlarmClock.EXTRA_DAYS, alarmDays)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    /**
     * Establecer alarma tareas.
     *
     * @param mensaje the mensaje
     * @param hora    the hora
     * @param minutos the minutos
     */
    fun establecerAlarmaTareas(mensaje: String?, hora: Int, minutos: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).putExtra(AlarmClock.EXTRA_MESSAGE, mensaje).putExtra(AlarmClock.EXTRA_HOUR, hora).putExtra(AlarmClock.EXTRA_MINUTES, minutos)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    /**
     * Inicializar las opciones como se dejaron la ultima vez
     */
    fun read() {
        var fis: FileInputStream? = null
        try {
            fis = openFileInput("tiempo_trabajar.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            var text: String
            text = br.readLine()
            seekBar!!.progress = Integer.valueOf(text) - 30
            text = br.readLine()
            if (text.equals("1", ignoreCase = true)) {
                Switch!!.isChecked = true
            } else {
                Switch!!.isChecked = false
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
        try {
            fis = openFileInput("temporizador.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            val text: String
            text = br.readLine()
            if (text.equals("1", ignoreCase = true)) {
                SwitchTemp!!.isChecked = true
            } else {
                SwitchTemp!!.isChecked = false
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
        try {
            fis = openFileInput("pausa.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            val text: String
            text = br.readLine()
            if (text.equals("1", ignoreCase = true)) {
                SwitchPausa!!.isChecked = true
            } else {
                SwitchPausa!!.isChecked = false
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
        try {
            fis = openFileInput("fin.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            val text: String
            text = br.readLine()
            if (text.equals("1", ignoreCase = true)) {
                SwitchFin!!.isChecked = true
            } else {
                SwitchFin!!.isChecked = false
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
        try {
            fis = openFileInput("concentracion.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            val text: String
            text = br.readLine()
            if (text.equals("1", ignoreCase = true)) {
                SwitchConcentracion!!.isChecked = true

            } else {
                SwitchConcentracion!!.isChecked = false

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
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Función para volver al menú principal
     *
     * @param view the view
     */
    fun pasar3(view: View?) {
        val siguiente = Intent(view!!.context, MainActivity::class.java)
        startActivity(siguiente)
    }

    companion object {
        private var seekBar: SeekBar? = null

        /**
         * The constant textView.
         */
        var textView: TextView? = null
        /**
         * El texto de ayuda con la leyenda
         */
        var ayudaOpciones: TextView?=null

        /**
         * The constant Switch.
         */
        var Switch: Switch? = null

        /**
         * The constant SwitchTemp.
         */
        var SwitchTemp: Switch? = null
        var SwitchPausa: Switch? = null
        var SwitchFin: Switch? = null
        var SwitchConcentracion: Switch? = null
    }
}