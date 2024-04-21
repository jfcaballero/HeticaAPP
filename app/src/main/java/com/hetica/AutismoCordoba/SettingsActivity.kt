package com.hetica.AutismoCordoba

import CustomToolbarAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils.replace
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize
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
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var toolbar: Toolbar
    private lateinit var customToolbarAdapter: CustomToolbarAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
        customToolbarAdapter = CustomToolbarAdapter(this, toolbar)
        sharedPreferences = getSharedPreferences("settings_preferences", Context.MODE_PRIVATE)
        seekBar = findViewById<View>(R.id.seekBar4) as SeekBar
        textView = findViewById<View>(R.id.textView46) as TextView
        ayudaOpciones=findViewById(R.id.ayudaOpciones)
        Switch = findViewById<View>(R.id.switch3) as Switch
        gestionarAsignaturasBoton = findViewById(R.id.button35)
        SwitchTemp = findViewById<View>(R.id.switch1) as Switch
        SwitchPausa = findViewById<View>(R.id.switch2) as Switch
        SwitchFin = findViewById<View>(R.id.switchFin) as Switch
        SwitchConcentracion = findViewById<View>(R.id.switchConcentracion) as Switch
        seebbarr()

        toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
        customToolbarAdapter = CustomToolbarAdapter(this, toolbar)
        customToolbarAdapter.setTextSizeBasedOnScreenWidth()

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
    // Métodos para guardar y obtener el estado del modo de concentración en SharedPreferences
    private fun saveConcentrationModeState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("concentration_mode", state)
        editor.apply()
    }

    /**
     * Función para mostrar el cuadro flotante de ayuda al pulsarse
     * @param texto Texto de ayuda
     */
    private fun mostrarCuadroFlotante(texto: String) {
        val dialogTextSize = getDialogTextSize()
        val formattedText = getFormattedText(texto)


        val builder = AlertDialog.Builder(this, dialogTextSize)
        builder.setMessage(formattedText)
            .setTitle("Ayuda de opciones") // Título del cuadro de diálogo
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }
     private fun getDialogTextSize(): Int {
        val screenSize = resources.configuration.screenWidthDp
        // Tamaños de titulos y boton del alertdialog para diferentes tamaños de pantalla
        val textSizeSmall = R.style.DialogTextStyleSmall
        val textSizeMedium = R.style.DialogTextStyleMedium
        val textSizeLarge = R.style.DialogTextStyleLarge

        return when {
            screenSize >= 720 -> textSizeLarge // Pantalla grande (más de 720dp)
            screenSize >= 480 -> textSizeMedium // Pantalla mediana (entre 480dp y 720dp)
            else -> textSizeSmall // Pantalla pequeña (menos de 480dp)
        }
    }

    /**
     * Función para formatear el texto con saltos de línea y negritas
     */
    private fun getFormattedText(texto: String): CharSequence {
        val spannableStringBuilder = SpannableStringBuilder()
        val lines = texto.split("\n")
        val screenSize = resources.configuration.screenWidthDp
        // Tamaños de contenido del alertdialog para diferentes tamaños de pantalla
        val textSizeSmall = 19
        val textSizeMedium = this.resources.getDimensionPixelSize(R.dimen.text_size_small_less_than_480dp)
        val textSizeLarge = this.resources.getDimensionPixelSize(R.dimen.text_size_medium_less_than_480dp)
        for (line in lines) {
            val formattedLine = SpannableStringBuilder(line)

            // Detectar y aplicar negrita a partes del texto entre ** **
            val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
            val boldMatches = boldRegex.findAll(line)
            for (match in boldMatches) {
                val startIndex = match.range.start + 2 // Agregar 2 para omitir los dos asteriscos de inicio
                val endIndex = match.range.endInclusive - 1 // Restar 1 para omitir los dos asteriscos de fin
                formattedLine.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                formattedLine.delete(endIndex, endIndex + 2) // Eliminar los dos asteriscos de fin
                formattedLine.delete(startIndex - 2, startIndex) // Eliminar los dos asteriscos de inicio
            }
            //Adaptar el tamaño a la pantalla
            val textSize = when {
                screenSize >= 720 -> textSizeLarge // Pantalla grande (más de 720dp)
                screenSize >= 480 -> textSizeMedium // Pantalla mediana (entre 480dp y 720dp)
                else -> textSizeSmall // Pantalla pequeña (menos de 480dp)
            }
            formattedLine.setSpan(AbsoluteSizeSpan(textSize, true), 0, formattedLine.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableStringBuilder.append(formattedLine)
            spannableStringBuilder.append("\n")
        }
        // Eliminar el último carácter de salto de línea adicional
        if (spannableStringBuilder.endsWith("\n")) {
            spannableStringBuilder.delete(spannableStringBuilder.length - 1, spannableStringBuilder.length)
        }
        return spannableStringBuilder
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
                    showSnackbarWithCustomTextSize(this, "Habilite el permiso de acceso a la política de notificaciones." )
                }
            }
            if (isChecked && notificationPolicyAccessGranted()) {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
                saveConcentrationModeState(true) // Guardar estado en SharedPreferences
            } else {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                saveConcentrationModeState(false) // Guardar estado en SharedPreferences
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
        seekBar!!.max = 30
        seekBar!!.progress = 0 // Establecer el progreso inicial a 0

        // Actualizar el texto de acuerdo al valor inicial
        textView!!.text = "30 minutos"

        seekBar!!.setOnSeekBarChangeListener(
            object : OnSeekBarChangeListener {
                var progressValue = 30 // Valor predeterminado de minutos

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    // Asegurar que el valor mínimo sea siempre 30 minutos
                    if (progress < 0) {
                        seekBar.progress = 0
                        progressValue = 30
                    } else {
                        progressValue = progress + 30
                    }
                    textView!!.text = "$progressValue minutos"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    // No es necesario realizar ninguna acción aquí
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

        var gestionarAsignaturasBoton:Button?=null
    }
}