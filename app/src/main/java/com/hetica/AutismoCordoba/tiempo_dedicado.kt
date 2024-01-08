package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.hetica.AutismoCordoba.databinding.TiempoDedicadoBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * The type tiempo_dedicado.
 *
 * @author Álvaro Berjillos Roldán
 */

class tiempo_dedicado: AppCompatActivity()  {
    /**
     * The Stats Db.
     */
    var dbStats: AdminSQLiteOpenHelperStats? = null
    /**
     * The Stats Db.
     */
    var dbAsig: AdminSQLiteOpenHelperAsig? = null

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
    var lv: ListView? = null

    /**
     * The Year final.
     */
    var yearFinal: String? = null
    /**
     * Imagen para irnos al Main
     */
    var imageMain: ImageView?=null

    var fechaInicio: EditText?=null

    var fechaFin: EditText?=null

    var asignaturaSeleccionada: String?=null

    var ListViewDias: ListView?=null

    var MinutosEnTotal: TextView?=null

    var totalMinutos: Int = 0

    var tiempoOpciones: Spinner? = null

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tiempo_dedicado)


        imageMain=findViewById(R.id.botonMain4)
        GoToMain()

        fechaInicio = findViewById<View>(R.id.tiempoInicio) as EditText?
        fechaFin = findViewById<View>(R.id.tiempoFin) as EditText?
        dbStats = AdminSQLiteOpenHelperStats(this)
        dbAsig = AdminSQLiteOpenHelperAsig(this)
        ListViewDias = findViewById(R.id.tiempoLista)
        MinutosEnTotal=findViewById(R.id.tiempoMinutosTotales)

        // Configuración inicial de fechas
        val fechaHoy = obtenerFechaActual()
        val fechaManana = obtenerFechaManana()

        fechaInicio?.setText(fechaHoy)
        fechaFin?.setText(fechaManana)

        // Asigna el OnClickListener a los EditText de fecha
        fechaInicio?.setOnClickListener {
            showDatePickerDialog(fechaInicio)
        }

        fechaFin?.setOnClickListener {
            showDatePickerDialog(fechaFin)
        }

        fechaInicio?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Actualiza los datos al cambiar la fecha de inicio
                mostrarDatosEnTiempoReal()
            }
        })

        fechaFin?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Actualiza los datos al cambiar la fecha de fin
                mostrarDatosEnTiempoReal()
            }
        })



        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationViewestadisticas)
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.selectedItemId = R.id.action_tiempo_dedicado

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
                R.id.action_comentarios -> {
                    val intent = Intent(this, MostrarComentarios::class.java)
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
                R.id.action_tiempo_dedicado -> {
                    true
                }
                else -> false
            }
        }

        mostrarAsignaturas()
        mostrarOpcionesSpinner()


    }


    private fun mostrarDatosEnTiempoReal() {

        val spinnerOpciones: Spinner = findViewById(R.id.tiempoOpciones)
        when (spinnerOpciones.selectedItem.toString()) {
            //"Rango" -> mostrarDatosRango()
            "Histórico" -> mostrarDatosHistoricos()
            "Día" -> mostrarDatosDelDia()
        }

    }


    private fun mostrarOpcionesSpinner() {
        val spinnerOpciones: Spinner = findViewById(R.id.tiempoOpciones)

        val opciones = listOf("Rango", "Histórico", "Día")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerOpciones.adapter = adapter

        spinnerOpciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val opcionSeleccionada = opciones[position]

                when (opcionSeleccionada) {
                    "Rango" -> {
                        fechaInicio?.visibility = View.VISIBLE
                        fechaFin?.visibility = View.VISIBLE
                       // mostrarDatosRango()
                    }
                    "Histórico" -> {
                        fechaInicio?.visibility = View.GONE
                        fechaFin?.visibility = View.GONE
                        mostrarDatosHistoricos()
                    }
                    "Día" -> {
                        fechaInicio?.visibility = View.VISIBLE
                        fechaFin?.visibility = View.GONE
                        mostrarDatosDelDia()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se realiza ninguna acción si no se selecciona nada
            }
        }
    }

    @SuppressLint("Range")
    private fun mostrarDatosDelDia() {
        try {
            totalMinutos=0
            // Obtén la asignatura seleccionada del Spinner
            val asignaturaSeleccionada = asignaturaSeleccionada ?: return
            val fechaSeleccionada = fechaInicio?.text.toString() ?: return

            // Obtén el cursor con los datos de un solo dia
            val cursor = dbStats?.viewDataDiaAsignatura(fechaSeleccionada,asignaturaSeleccionada) ?: return

            // Lista para almacenar pares de fecha y minutos totales
            val datosDia = mutableListOf<Pair<String, Int>>()


            // Itera sobre el cursor y agrega los datos a la lista
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex("NAME"))
                    Log.d("Dia asig",name)
                    val fecha = cursor.getString(cursor.getColumnIndex("DATE"))
                    val minutosTotales = cursor.getInt(cursor.getColumnIndex("TIME"))
                    totalMinutos += minutosTotales
                    datosDia.add(fecha to minutosTotales)
                } while (cursor.moveToNext())
            }
            MinutosEnTotal?.text = "$totalMinutos"

            // Crea un adaptador para el ListView
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                datosDia.map { "${it.first}: ${it.second} minutos" }
            )

            // Asigna el adaptador al ListView
            ListViewDias?.adapter = adapter
        } catch (e: Exception) {
            Log.e("MostrarDatosHistoricos", "Error: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun mostrarDatosRango() {
        TODO("Not yet implemented")
    }

    @SuppressLint("Range")
    private fun mostrarDatosHistoricos() {
        try {
            totalMinutos=0
            // Obtén la asignatura seleccionada del Spinner
            val asignaturaSeleccionada = asignaturaSeleccionada ?: return

            // Obtén el cursor con los datos históricos
            val cursor = dbStats?.viewDataHistorico(asignaturaSeleccionada) ?: return

            // Lista para almacenar pares de fecha y minutos totales
            val datosHistoricos = mutableListOf<Pair<String, Int>>()


            // Itera sobre el cursor y agrega los datos a la lista
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex("NAME"))
                    Log.d("Historico asig",name)
                    val fecha = cursor.getString(cursor.getColumnIndex("DATE"))
                    val minutosTotales = cursor.getInt(cursor.getColumnIndex("TIME"))
                    totalMinutos += minutosTotales
                    datosHistoricos.add(fecha to minutosTotales)
                } while (cursor.moveToNext())
            }
            MinutosEnTotal?.text = "$totalMinutos"

            // Crea un adaptador para el ListView
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                datosHistoricos.map { "${it.first}: ${it.second} minutos" }
            )

            // Asigna el adaptador al ListView
            ListViewDias?.adapter = adapter
        } catch (e: Exception) {
            Log.e("MostrarDatosHistoricos", "Error: ${e.message}")
            e.printStackTrace()
        }
    }






    private fun mostrarAsignaturas() {
        val spinner: Spinner = findViewById(R.id.tiempoAsignatura)
        val spinnerOpciones: Spinner = findViewById(R.id.tiempoOpciones)

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
                    // Llamar a la función correspondiente según la opción seleccionada en el Spinner de opciones
                    when (spinnerOpciones.selectedItem.toString()) {
                        //"Rango" -> mostrarDatosRango()
                        "Histórico" -> mostrarDatosHistoricos()
                        "Día" -> mostrarDatosDelDia()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se realiza ninguna acción si no se selecciona nada
                }
            }

            // Llamar a viewData con los valores iniciales del Spinner y la fecha
            if (asignaturaSeleccionada != null) {
                //viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())
            }
        }
    }


    /**
     * Función para irnos al Main
     *
     */
    fun GoToMain(){
        imageMain?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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


    // Función para mostrar el DatePickerDialog
    private fun showDatePickerDialog(editText: EditText?) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Actualiza el calendario con la nueva fecha seleccionada
                calendar.set(selectedYear, selectedMonth, selectedDay)

                // Formatea la fecha y actualiza el texto del EditText
                val formattedDate = formatDate(selectedYear, selectedMonth + 1, selectedDay)
                editText?.setText(formattedDate)

                // Puedes realizar acciones adicionales aquí si es necesario
            },
            year,
            month,
            day
        )

        // Muestra el DatePickerDialog
        datePickerDialog.show()
    }

    // Función para formatear la fecha
    private fun formatDate(year: Int, month: Int, day: Int): String {
        calendar.set(year, month - 1, day)
        return dateFormat.format(calendar.time)
    }


    /*
     * Función que devuelve las estadísticas de una fecha en concreto
     *

    @SuppressLint("Range")
    private fun viewData() {
        Log.e("Date Selected", yearFinal!!)
        val cursor = db!!.viewDataDias(yearFinal)
        if (cursor.count == 0) {
            Toast.makeText(this, "No se trabajó este día", Toast.LENGTH_LONG).show()
            binding.barChartHorizontal.visibility = View.INVISIBLE  // Ocultar la gráfica cuando no hay datos

        } else {
            obtenerTiempoTotalAsignaturas(yearFinal)
        }
    }*/
    /*
     * Función para convertir map a Lista de asignaturas y minutos
     * @param mapAsignaturas La lista mutable de asignaturas y minutos
     * @return list
     *
    private fun convertMapToList(mapAsignaturas: HashMap<String, Int>): List<Pair<String, Float>> {
        val list = mutableListOf<Pair<String, Float>>()
        for ((asignatura, tiempoTotal) in mapAsignaturas) {
            list.add(asignatura to tiempoTotal.toFloat())
        }
        return list
    }
    /**
     * Función para generar una lista de pares mapeados de asignatura y minutos dada una lista
     * @param data La lista de asignatura y minutos
     * @return mappedData
     */
    private fun generateHorizontalBarData(data: List<Pair<String, Float>>): List<Pair<String, Float>> {
        val mappedData = mutableListOf<Pair<String, Float>>()
        "Asignatura: ${data[0].first} - Tiempo Total: ${data[0].second} minutos"
        //Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()

        for (i in 0 until data.size) {
            mappedData.add(data[i])
        }

        if (data.size == 1) {
            mappedData.add(data[0])

        }else{
            val rectangle = findViewById<View>(R.id.rectangle)
            rectangle.visibility = View.INVISIBLE

        }

        return mappedData
    }





    /**
     * Función para mostrar en la grafica el tiempo dedicado a cada asignatura dada una fecha
     * @param Fecha
     */
    @SuppressLint("Range")
    private fun obtenerTiempoTotalAsignaturas(date: String?) {
        val cursor = db!!.viewDataDias(date)
        val mapAsignaturas = HashMap<String, Int>()

        if (cursor.count == 0) {
            Log.e("Obtener Tiempo Total", "No hay datos para la fecha especificada")
            binding.barChartHorizontal.visibility = View.INVISIBLE  // Ocultar la gráfica cuando no hay datos

        } else {
            while (cursor.moveToNext()) {
                val nombreAsignatura = cursor.getString(cursor.getColumnIndex("NAME"))
                val tiempo = cursor.getInt(cursor.getColumnIndex("TIME"))

                if (mapAsignaturas.containsKey(nombreAsignatura)) {
                    val tiempoTotal = mapAsignaturas[nombreAsignatura] ?: 0
                    mapAsignaturas[nombreAsignatura] = tiempoTotal + tiempo
                } else {
                    mapAsignaturas[nombreAsignatura] = tiempo
                }
            }

            binding.barChartHorizontal.visibility = View.VISIBLE  // Mostrar la gráfica cuando hay datos disponibles

            binding.barChartHorizontal.animation.duration = animationDuration
            val dataList = convertMapToList(mapAsignaturas)
            val data = generateHorizontalBarData(dataList)
            binding.barChartHorizontal.animate(data)
            binding.barChartHorizontal.invalidate()

        }
    }

     */

}