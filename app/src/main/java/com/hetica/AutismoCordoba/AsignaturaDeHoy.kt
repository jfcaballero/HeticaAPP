package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.util.SparseBooleanArray
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize
import com.hetica.AutismoCordoba.MainActivity
import java.util.ArrayList
import java.util.Calendar


class AsignaturaDeHoy : AppCompatActivity() {

    private var dbCalendario: AdminSQLiteOpenHelperCalendario? = null
    private var calendarioListView: ListView? = null
    private var isLongPressFired = false
    private var ComenzarSesion: Button? = null
    private var SalirCalendario: Button? = null
    private var ayuda: Button? = null
    private var limpiar:Button?=null
    private lateinit var adapter: CalendarioArrayAdapter
    private var todasMarcadas=true
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis = 3000L // 3 segundos

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignatura_de_hoy)

        dbCalendario = AdminSQLiteOpenHelperCalendario(this)
        calendarioListView = findViewById(R.id.asignaturasdehoy)
        ComenzarSesion = findViewById(R.id.asignaturaDeHoyComenzar)
        SalirCalendario = findViewById(R.id.asignaturaDeHoySalir)
        ayuda=findViewById(R.id.asignaturaDeHoyAyuda)
        limpiar=findViewById(R.id.asignaturaDeHoyLimpiar)

        // Configurar el adaptador personalizado
        adapter = CalendarioArrayAdapter(this, R.layout.list_item_checkbox, ArrayList())
        calendarioListView?.adapter = adapter
        calendarioListView?.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Manejar eventos de clic en elementos de la lista
        calendarioListView?.setOnItemClickListener { _, _, position, _ ->
            val isChecked = !calendarioListView?.isItemChecked(position)!!
            Log.d("ItemClicked", "Position: $position, Checked: $isChecked")
            calendarioListView?.setItemChecked(position, isChecked)
            val checkedItemPositions = calendarioListView?.checkedItemPositions
            Log.d("Num items:", checkedItemPositions?.size().toString())
        }
        salir()
        limpiar()
        viewData()
        pasarEditarCalendario()
        comenzarSesion()
        ayuda()

    }

    /**
     * Función para mostrar la lista de asignaturas programadas para el día de hoy
     */
    private fun viewData() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateString = String.format("%02d%02d%d", month, day, year)
        var nohaytareas: TextView?=findViewById(R.id.textoNoHayTareasHoy)
        val asignaturasData = dbCalendario?.getAsignaturasForDayWithMinutos(dateString)
        var hayAsignaturasEstudiadas = false
        adapter.clear()
        ComenzarSesion?.isEnabled = true



        if (!asignaturasData.isNullOrEmpty()) {
            val displayList = mutableListOf<String>()
            for (asignaturaData in asignaturasData) {
                val displayText = "${asignaturaData.first} - ${asignaturaData.second} minutos - Estudiado: ${if (asignaturaData.third == 1) "Sí" else "No"}"
                displayList.add(displayText)
                if (asignaturaData.third == 1) {// Si la asignatura está estudiada, desactivar la interacción
                    hayAsignaturasEstudiadas = true
                    val position = displayList.size - 1
                    adapter.checkedPositions.put(position, true)
                }else{
                    todasMarcadas=false
                }
            }
            adapter.addAll(displayList)
            if (nohaytareas != null) {
                nohaytareas.visibility=View.INVISIBLE
            }
            if(todasMarcadas){ //esto es para que cuando esten todas marcadas no deje pulsar el boton
                ComenzarSesion?.isEnabled = false

            }
        } else {

            if (nohaytareas != null) {
                nohaytareas.visibility=View.VISIBLE
                ComenzarSesion?.isEnabled = false //para que no deje pulsar el boton tampoco cuando no haya sesiones programadas

            }
            calendarioListView?.adapter = null
        }
        limpiar?.isEnabled = hayAsignaturasEstudiadas
        adapter.notifyDataSetChanged()
        for (i in 0 until calendarioListView?.count!!) {
            calendarioListView?.setItemChecked(i, false)
        }
        Log.d("Asignaturas", asignaturasData.toString())
    }



    private fun limpiar(){
        limpiar?.setOnClickListener {
            dbCalendario?.limpiarEstudiadas(getDateAsString())
            viewData()
            // Desmarcar todas las casillas de verificación en el adaptador
            adapter.uncheckAll()
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun comenzarSesion() {
        ComenzarSesion?.setOnClickListener {
            val position = adapter.lastCheckedPosition
            if (position != -1) {
                val asignaturaData = adapter.getItem(position)
                val nombreAsignatura = asignaturaData?.substringBefore(" - ")
                val minutosIndex = asignaturaData?.indexOf("minutos") ?: -1

                if (minutosIndex != -1 && nombreAsignatura != null) {
                    val minutosString = asignaturaData.substring(minutosIndex - 3, minutosIndex).trim()
                    val minutos = minutosString.toIntOrNull()

                    if (minutos != null && minutos != 0) {
                        if (asignaturaData.endsWith("No")) {
                            val intent = Intent(this, TimerSimple::class.java).apply {
                                putExtra("time", minutos.toString())
                                putExtra("asig", nombreAsignatura)
                                putExtra("actAsig", "1")
                                putExtra("numAsig", "1")
                            }

                            startActivity(intent)
                            dbCalendario?.marcarComoEstudiado(position, getDateAsString())
                        }
                    } else {
                        showSnackbarWithCustomTextSize(
                            this,
                            "Datos de la asignatura inválidos."
                        )
                    }
                } else {
                    showSnackbarWithCustomTextSize(
                        this,
                        "Datos de la asignatura inválidos."
                    )
                }
            } else {

                showSnackbarWithCustomTextSize(
                    this,
                    "Selecciona un único elemento."
                )
            }
        }
    }





    private fun getDateAsString(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format("%02d%02d%d", month, day, year)
    }


    private fun ayuda(){
        ayuda?.setOnClickListener {
            val texto=this.resources.getString(R.string.asignatura_de_hoy_texto_de_ayuda)

            val dialogTextSize = getDialogTextSize()
            val formattedText = getFormattedText(texto)

            val builder = AlertDialog.Builder(this,dialogTextSize)
            builder.setTitle("Asignaturas de hoy")
                .setMessage(formattedText)
                .setPositiveButton("Salir") { dialog, _ ->
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()
        }

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
    fun getFormattedText(texto: String): CharSequence {
        val spannableStringBuilder = SpannableStringBuilder(texto)
        val screenSize = resources.configuration.screenWidthDp

        val textSizeSmall = 19
        val textSizeMedium = this.resources.getDimensionPixelSize(R.dimen.text_size_small_less_than_480dp)
        val textSizeLarge = this.resources.getDimensionPixelSize(R.dimen.text_size_medium_less_than_480dp)

        val textSize = when {
            screenSize >= 720 -> textSizeLarge
            screenSize >= 480 -> textSizeMedium
            else -> textSizeSmall
        }

        spannableStringBuilder.setSpan(
            AbsoluteSizeSpan(textSize, true),
            0,
            spannableStringBuilder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableStringBuilder
    }

    private fun salir() {
        SalirCalendario?.setOnClickListener {
            val intent = Intent(this@AsignaturaDeHoy, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun pasarEditarCalendario() {
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                if (isLongPressFired) {
                    return
                }
                isLongPressFired = true

                // Usamos un Handler para retrasar la apertura de la actividad EditarCalendario
                handler.postDelayed({
                    val intent = Intent(this@AsignaturaDeHoy, EditarCalendario::class.java)
                    startActivity(intent)
                }, delayMillis)
            }
        })
        val editarCalendario = findViewById<ImageView>(R.id.editarCalendarioBoton)
        editarCalendario?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                isLongPressFired = false
                // Si se libera el botón antes del tiempo de espera, cancelamos el Handler
                handler.removeCallbacksAndMessages(null)
            }
            true
        }
    }




    override fun onResume() {
        super.onResume()
        viewData()


    }


}