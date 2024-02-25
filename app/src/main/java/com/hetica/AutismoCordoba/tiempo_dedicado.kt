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
     * Imagen para irnos al Main
     */
    var imageMain: ImageView?=null

    var fechaInicio: EditText?=null

    var fechaFin: EditText?=null

    var asignaturaSeleccionada: String?=null

    var ListViewDias: ListView?=null

    var MinutosEnTotal: TextView?=null

    var totalMinutos: Int = 0

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

        ListViewDias?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val asignaturaSeleccionada = asignaturaSeleccionada ?: return@OnItemClickListener
            val itemSeleccionado = parent.getItemAtPosition(position).toString()
            val partes = itemSeleccionado.split(": ")
            val fecha = partes[0]

            val intent = Intent(this, InsertarComentarios::class.java)
            intent.putExtra("asignatura", asignaturaSeleccionada)
            intent.putExtra("fecha", fecha)
            startActivity(intent)
        }

    }

    /**
     * Función para mostrar los datos actuales teniendo en cuenta lo que esté configurado en el spinner
     */
    private fun mostrarDatosEnTiempoReal() {

        val spinnerOpciones: Spinner = findViewById(R.id.tiempoOpciones)
        when (spinnerOpciones.selectedItem.toString()) {
            "Rango" -> mostrarDatosRango()
            "Histórico" -> mostrarDatosHistoricos()
            "Día" -> mostrarDatosDelDia()
        }

    }

    /**
     * Función para mostrar las funciones disponibles en el spinner
     */
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
                        mostrarDatosRango()
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

    /**
     * Función para mostrar la fecha y los minutos totales en la lista de un dia concreto
     */
    @SuppressLint("Range")
    private fun mostrarDatosDelDia() {
        try {
            totalMinutos=0

            // Obtenmos la asignatura seleccionada del Spinner
            val asignaturaSeleccionada = asignaturaSeleccionada ?: return
            val fechaSeleccionada = fechaInicio?.text.toString()

            // Cursor con los datos de un solo dia
            val cursor = dbStats?.viewDataDiaAsignatura(fechaSeleccionada,asignaturaSeleccionada) ?: return

            // Lista para almacenar pares de fecha y minutos totales
            val datosDia = mutableListOf<Pair<String, Int>>()


            // Agregamos los datos a la lista
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
            MinutosEnTotal?.text = "Total: $totalMinutos minutos"
            var nodatos: TextView?=findViewById(R.id.textoNoComentarios3)
            if(datosDia.isEmpty()){
                if (nodatos != null) {
                    nodatos.visibility=View.VISIBLE
                }
            }else{
                if (nodatos != null) {
                    nodatos.visibility=View.INVISIBLE
                }
            }
            // Adaptador para el ListView
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                datosDia.map { "${it.first}: ${it.second} minutos" }
            )

            // Asignamos el adaptador al ListView
            ListViewDias?.adapter = adapter
        } catch (e: Exception) {
            Log.e("MostrarDatosHistoricos", "Error: ${e.message}")
            e.printStackTrace()
        }
    }
    /**
     * Función para mostrar la fecha y los minutos totales en la lista en un rango de dos fechas
     */
    @SuppressLint("Range")
    private fun mostrarDatosRango() {
        try {
            totalMinutos = 0

            // Obtemos la asignatura seleccionada del Spinner
            val asignaturaSeleccionada = asignaturaSeleccionada ?: return
            val fechaInicioSeleccionada = fechaInicio?.text.toString()
            val fechaFinSeleccionada = fechaFin?.text.toString()

            // Cursor con los datos en el rango de fechas y para la asignatura seleccionada
            val cursor = dbStats?.viewDataHistorico(asignaturaSeleccionada) ?: return

            // Lista para almacenar pares de fecha y minutos totales
            val datosRango = mutableListOf<Pair<String, Int>>()

            // Iteramos sobre el cursor y agrega los datos a la lista si están dentro del rango de fechas
            if (cursor.moveToFirst()) {
                do {
                    val fecha = cursor.getString(cursor.getColumnIndex("DATE"))
                    val minutosTotales = cursor.getInt(cursor.getColumnIndex("TIME"))

                    // Verificamos si la fecha está en el rango
                    if (fechaEstaEntre(fecha, fechaInicioSeleccionada, fechaFinSeleccionada)) {
                        totalMinutos += minutosTotales
                        datosRango.add(fecha to minutosTotales)
                    }
                } while (cursor.moveToNext())
            }

            MinutosEnTotal?.text = "Total: $totalMinutos minutos"
            var nodatos: TextView? = findViewById(R.id.textoNoComentarios3)
            if (datosRango.isEmpty()) {
                nodatos?.visibility = View.VISIBLE
            } else {
                nodatos?.visibility = View.INVISIBLE
            }
            // Adaptador para el ListView
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                datosRango.map { "${it.first}: ${it.second} minutos" }
            )

            // Asignamos el adaptador al ListView
            ListViewDias?.adapter = adapter
        } catch (e: Exception) {
            Log.e("MostrarDatosRango", "Error: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun fechaEstaEntre(fecha: String, fechaInicio: String?, fechaFin: String?): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy")

        try {
            val dateFecha = formato.parse(fecha)!!
            val dateInicio = fechaInicio?.let { formato.parse(it) }!!
            val dateFin = fechaFin?.let { formato.parse(it) }!!

            return dateFecha.compareTo(dateInicio) >= 0 && dateFecha.compareTo(dateFin) <= 0
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Fecha incorrecta", "Error al procesar fechas")
        }

        return false
    }



    /**
     * Función para mostrar la fecha y los minutos totales en la lista desde siempre
     */
    @SuppressLint("Range")
    private fun mostrarDatosHistoricos() {
        try {
            totalMinutos=0
            // Obtemos la asignatura seleccionada del Spinner
            val asignaturaSeleccionada = asignaturaSeleccionada ?: return

            // Cursor con los datos históricos
            val cursor = dbStats?.viewDataHistorico(asignaturaSeleccionada) ?: return

            // Lista para almacenar pares de fecha y minutos totales
            val datosHistoricos = mutableListOf<Pair<String, Int>>()


            // Iteramos sobre el cursor y agrega los datos a la lista
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
            MinutosEnTotal?.text = "Total: $totalMinutos minutos"

            // Adaptador para el ListView
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                datosHistoricos.map { "${it.first}: ${it.second} minutos" }
            )
            var nodatos: TextView?=findViewById(R.id.textoNoComentarios3)
            if(datosHistoricos.isEmpty()){
                if (nodatos != null) {
                    nodatos.visibility=View.VISIBLE
                }
            }else{
                if (nodatos != null) {
                    nodatos.visibility=View.INVISIBLE
                }
            }
            // Asignamos el adaptador al ListView
            ListViewDias?.adapter = adapter
        } catch (e: Exception) {
            Log.e("MostrarDatosHistoricos", "Error: ${e.message}")
            e.printStackTrace()
        }
    }


    /**
     * Función para mostrar los datos en función del spinner seleccionado
     */
    private fun mostrarAsignaturas() {
        val spinner: Spinner = findViewById(R.id.tiempoAsignatura)
        val spinnerOpciones: Spinner = findViewById(R.id.tiempoOpciones)

        // Obtenemos la lista de asignaturas desde la base de datos
        val asignaturasList = dbAsig?.getAsignaturasList()
        if (asignaturasList != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Establecemos el listener para el evento de clic del Spinner
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    asignaturaSeleccionada = parent.getItemAtPosition(position).toString() // Asignar valor a la variable global

                    // Llamamos a la función correspondiente según la opción seleccionada en el Spinner de opciones
                    when (spinnerOpciones.selectedItem.toString()) {
                        "Rango" -> mostrarDatosRango()
                        "Histórico" -> mostrarDatosHistoricos()
                        "Día" -> mostrarDatosDelDia()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se realiza ninguna acción si no se selecciona nada
                }
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
     * @return Fecha formateada
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
     *@return Fecha formateada
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

    /**
     * Función para mostrar el DatePickerDialog
     * @param editText Edit Text de la fecha
     */

    private fun showDatePickerDialog(editText: EditText?) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Actualizamos el calendario con la nueva fecha seleccionada
                calendar.set(selectedYear, selectedMonth, selectedDay)

                // Formateamos la fecha y actualiza el texto del EditText
                val formattedDate = formatDate(selectedYear, selectedMonth + 1, selectedDay)
                editText?.setText(formattedDate)

            },
            year,
            month,
            day
        )

        // Muestra el DatePickerDialog
        datePickerDialog.show()
    }

    /**
     * Función para formatear la fecha
     * @param year Año como entero
     * @param month Mes como entero
     * @param day Día como entero
     * @return Fecha formateada
     */
    private fun formatDate(year: Int, month: Int, day: Int): String {
        calendar.set(year, month - 1, day)
        return dateFormat.format(calendar.time)
    }




}