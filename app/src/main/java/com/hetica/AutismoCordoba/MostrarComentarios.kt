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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import java.util.Calendar

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
var imageMain2: ImageView?=null



class MostrarComentarios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_comentarios)
        val editText = findViewById<View>(R.id.fechacomentarios) as EditText
        dbAsig = AdminSQLiteOpenHelperAsig(this)
        listViewComentarios = findViewById(R.id.listViewComentarios)
        imageMain2=findViewById(R.id.botonMain2)
        GoToMain()

        // Llamar a viewData con los valores iniciales del Spinner y la fecha
        val fechaActual = obtenerFechaActual()
        if (asignaturaSeleccionada != null) {
            viewData(asignaturaSeleccionada!!, fechaActual)
        }


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationViewestadisticas)
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.selectedItemId = R.id.action_comentarios
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
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
                    //Toast.makeText(applicationContext, "Asignatura seleccionada: $asignaturaSeleccionada, Fecha seleccionada: $fechaSeleccionada", Toast.LENGTH_SHORT).show()
                    viewData(asignaturaSeleccionada, yearFinal)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se realiza ninguna acción si no se selecciona nada
                }
            }

            // Llamar a viewData con los valores iniciales del Spinner y la fecha
            if (asignaturaSeleccionada != null) {
                viewData(asignaturaSeleccionada, yearFinal)
            }

        }


        val mcurrentDate = Calendar.getInstance()
        val yearAux = mcurrentDate[Calendar.YEAR]
        var monthAux = mcurrentDate[Calendar.MONTH]
        val dayAux = mcurrentDate[Calendar.DAY_OF_MONTH]
        monthAux = monthAux + 1
        yearFinal = if (monthAux < 10) {
            "0" + Integer.toString(monthAux)
        } else {
            Integer.toString(monthAux)
        }
        if (dayAux < 10) {
            yearFinal = yearFinal + "0"
        }
        yearFinal = yearFinal + Integer.toString(dayAux) + Integer.toString(yearAux)
        editText.setText("$dayAux/$monthAux/$yearAux")
        //viewData()
        //asignaturaSeleccionada?.let { viewData(it, yearFinal!!) }
        viewData(asignaturaSeleccionada, yearFinal)
        editText.setOnClickListener { // TODO Auto-generated method stub
            //To show current date in the datepicker
            val mcurrentDate = Calendar.getInstance()
            val year = mcurrentDate[Calendar.YEAR]
            val month = mcurrentDate[Calendar.MONTH]
            val day = mcurrentDate[Calendar.DAY_OF_MONTH]
            //month=month +1;
            //yearFinal = Integer.toString(month) + Integer.toString(day) + Integer.toString(year);
            yearFinal = if (month < 10) {
                "0" + Integer.toString(month)
            } else {
                Integer.toString(month)
            }
            if (day < 10) {
                yearFinal = yearFinal + "0"
            }
            yearFinal = yearFinal + Integer.toString(day) + Integer.toString(year)
            val mDatePicker = DatePickerDialog(this@MostrarComentarios, { datepicker, selectedYear, selectedMonth, selectedDay ->
                var selectedMonth = selectedMonth
                Log.e("Date Selected", "Month: $selectedMonth Day: $selectedDay Year: $selectedYear")
                selectedMonth = selectedMonth + 1
                editText.setText("$selectedDay/$selectedMonth/$selectedYear")
                yearFinal = if (selectedMonth < 10) {
                    "0" + Integer.toString(selectedMonth)
                } else {
                    Integer.toString(selectedMonth)
                }
                if (selectedDay < 10) {
                    yearFinal = yearFinal + "0"
                }
                yearFinal = yearFinal + Integer.toString(selectedDay) + Integer.toString(selectedYear)

                // Llamar a viewData después de seleccionar la fecha
                viewData(asignaturaSeleccionada, yearFinal)
                //asignaturaSeleccionada?.let { viewData(it, yearFinal!!) }
            }, year, month, day)
            mDatePicker.setTitle("Select date")
            mDatePicker.show()
        }


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
    private fun viewData(asignaturaSeleccionada: String?, fechaSeleccionada: String?) {
        dbComentarios = AdminSQLiteOpenHelperComentarios(this, "Comentarios.db", null, 1)
        //dbComentarios!!.borrarTabla()
        //val comentarioAgregado = dbComentarios?.insertData("10172023", "mates", "esto es un comentario de mates MUY ANTIGUO")

        /*if (comentarioAgregado == true) {
            Log.d("MostrarComentarios", "Comentario agregado con éxito")
        } else {
            Log.e("MostrarComentarios", "Error al agregar el comentario")
        }*/
        val cursor = dbComentarios?.viewData()
        val comentariosList: ArrayList<String> = ArrayList()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex("NAME"))
                val date = cursor.getString(cursor.getColumnIndex("DATE"))
                val comments = cursor.getString(cursor.getColumnIndex("COMMENTS"))
                if (name == asignaturaSeleccionada && date == fechaSeleccionada) {
                    comentariosList.add("$comments")
                }
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, comentariosList)
        listViewComentarios?.adapter = adapter
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



}