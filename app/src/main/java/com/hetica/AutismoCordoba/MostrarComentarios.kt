package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperComentarios
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
//import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_AUTO
import com.google.android.material.navigation.NavigationBarView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * The Db Asig.
 */
var dbAsig: AdminSQLiteOpenHelperAsig? = null

/**
 * The Db Comentarios.
 */
var dbComentarios: AdminSQLiteOpenHelperComentarios? = null

/**
 * The Array list.
 */
var arrayList: ArrayList<String>? = null

/**
 * The Adapter.
 */
var adapter: ArrayAdapter<String>? = null

/**
 * The Lv.
 */
@SuppressLint("StaticFieldLeak")
var lv: ListView? = null

/**
 * The Year final.
 */
var yearFinal: String? = null

/**
 * The Year final2.
 */
var yearFinal2: String? = null

/**
 * The Month final.
 */
var monthFinal: String? = null

/**
 * The Day final.
 */
var dayFinal: String? = null
/**
 * La lista de comentarios.
 */
@SuppressLint("StaticFieldLeak")
private var listViewComentarios: ListView? = null
/**
 * La asignatura seleccionada.
 */
private var asignaturaSeleccionada: String? = null

/**
 * Imagen para irnos al Main
 */
@SuppressLint("StaticFieldLeak")
var imageMain2: ImageView?=null


@SuppressLint("StaticFieldLeak")
private var fechaInicio: EditText? = null
@SuppressLint("StaticFieldLeak")
private var fechaFin: EditText? = null

class MostrarComentarios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_comentarios)

        fechaInicio = findViewById<View>(R.id.comentariosDesde) as EditText
        fechaFin = findViewById<View>(R.id.comentariosHasta) as EditText
        dbAsig = AdminSQLiteOpenHelperAsig(this)
        dbComentarios = AdminSQLiteOpenHelperComentarios(this)
        listViewComentarios = findViewById(R.id.listViewComentarios)
        imageMain2=findViewById(R.id.botonMain2)
        GoToMain()

        // Configuración inicial de fechas
        val fechaHoy = obtenerFechaActual()
        val fechaManana = obtenerFechaManana()

        fechaInicio?.setText(fechaHoy)
        fechaFin?.setText(fechaManana)
        if (asignaturaSeleccionada != null) {
            viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())
        }


        // Obtener la fecha de mañana
        val calendarTomorrow = Calendar.getInstance()
        calendarTomorrow.add(Calendar.DAY_OF_MONTH, 1)


        fechaInicio?.setOnClickListener {
            showDatePickerDialog(fechaInicio!!, fechaFin!!)
        }

        fechaFin?.setOnClickListener {
            showDatePickerDialog(fechaFin!!, fechaInicio!!)
        }


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationViewestadisticas)
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.selectedItemId = R.id.action_comentarios

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_estadisticas -> {
                    val intent = Intent(this, estadisticasDias::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out  // Exit animation
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_tiempo_dedicado -> {
                    val intent = Intent(this, tiempo_dedicado::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out  // Exit animation
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_actividad -> {
                    val intent = Intent(this, mayormenoractividad::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out  // Exit animation
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_calificaciones -> {
                    val intent = Intent(this, VisualizarCalificaciones::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out  // Exit animation
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_comentarios -> {
                    // Aquí maneja la selección de comentarios
                    true
                }
                else -> false
            }
        }

        val spinner: Spinner = findViewById(R.id.selectorasignaturas)

        // Obtener la lista de asignaturas desde la base de datos
        val asignaturasList = dbAsig?.getAsignaturasList()
        if (asignaturasList != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Establecer el listener para el evento de clic del Spinner
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    asignaturaSeleccionada = parent.getItemAtPosition(position).toString() // Asignar valor a la variable global
                    viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se realiza ninguna acción si no se selecciona nada
                }
            }

            // Llamar a viewData con los valores iniciales del Spinner y la fecha
            if (asignaturaSeleccionada != null) {
                viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())
            }

        }



    }




    // Función para mostrar el DatePickerDialog
    private fun showDatePickerDialog(editText: EditText, otherEditText: EditText) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val adjustedMonth = selectedMonth + 1
                val formattedDate = String.format("%02d/%02d/%d", selectedDay, adjustedMonth, selectedYear)
                editText.setText(formattedDate)

                // Llamar a viewData después de seleccionar la fecha
                viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())

                // Configurar nuevamente el onFocusChangeListener
                editText.onFocusChangeListener?.onFocusChange(editText, false)
                otherEditText.onFocusChangeListener?.onFocusChange(otherEditText, false)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    /**
     * Función para irnos al Main
     *
     */
    fun GoToMain(){
        imageMain2?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
    /**
     * Función que devuelve los comentarios de una fecha y asignatura en concreto
     *
     */
    @SuppressLint("Range")
    private fun viewData(asignaturaSeleccionada: String?, fechaInicio: String?, fechaFin: String?) {

        val cursor = dbComentarios?.viewData()
        //Log.d("Número de filas", "${cursor?.count}")
        val comentariosList: ArrayList<String> = ArrayList()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex("NAME"))
                val date = cursor.getString(cursor.getColumnIndex("DATE"))
                val comments = cursor.getString(cursor.getColumnIndex("COMMENTS"))
                //Log.d("Fecha de item", "La fecha del item es $date")
                // Verificar si la fecha está comprendida entre fechaInicio y fechaFin
                if (name == asignaturaSeleccionada && fechaInicio != null && fechaFin != null) {
                    if (fechaEstaEntre(date,fechaInicio,fechaFin)) {
                        comentariosList.add("$date - $comments")
                        Log.d("Bien", "La fecha $date está comprendida entre $fechaInicio y $fechaFin")
                    } else {
                        Log.d("Mal", "La fecha $date NO está comprendida entre $fechaInicio y $fechaFin")

                    }
                }
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, comentariosList)
        listViewComentarios?.adapter = adapter
    }
    /**
     * Función para comprobar si una fecha está entre dos límites
     *
     */
    fun fechaEstaEntre(fecha: String, fechaInicio: String, fechaFin: String): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy")

        try {
            val dateFecha = formato.parse(fecha)!!
            val dateInicio = formato.parse(fechaInicio)!!
            val dateFin = formato.parse(fechaFin)!!

            // Verificar si la fecha está entre fechaInicio y fechaFin, incluyendo los límites
            if (dateFecha.compareTo(dateInicio) >= 0 && dateFecha.compareTo(dateFin) <= 0) {
                Log.d("Fecha correcta", "La fecha $fecha está entre $fechaInicio y $fechaFin")
                return true
            } else {
                Log.d("Fecha incorrecta", "La fecha $fecha NO está entre $fechaInicio y $fechaFin")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Fecha incorrecta", "Error al procesar fechas")
        }

        return false  // En caso de error
    }




    /**
     * Función para obtener la fecha de hoy
     *
     */
    fun obtenerFechaActual(): String {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH) + 1
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        val mesFormateado = if (month < 10) "0$month" else month.toString()
        val diaFormateado = if (day < 10) "0$day" else day.toString()

        return "$diaFormateado/$mesFormateado/$year"
    }
    /**
     * Función para obtener la fecha de mañana
     *
     */
    fun obtenerFechaManana(): String {
        val calendario = Calendar.getInstance()
        calendario.add(Calendar.DAY_OF_MONTH, 1)  // Añadir un día

        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH) + 1
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        val mesFormateado = if (month < 10) "0$month" else month.toString()
        val diaFormateado = if (day < 10) "0$day" else day.toString()

        return "$diaFormateado/$mesFormateado/$year"
    }




}