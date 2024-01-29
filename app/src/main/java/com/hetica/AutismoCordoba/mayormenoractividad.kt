package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartZoomType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.android.material.navigation.NavigationBarView
import com.hetica.AutismoCordoba.databinding.ActivityMayormenoractividadBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class mayormenoractividad : AppCompatActivity() {
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

    var textoDeDia:TextView?=null

    private val calendar = Calendar.getInstance()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var _binding: ActivityMayormenoractividadBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMayormenoractividadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageMain=findViewById(R.id.botonMain)
        GoToMain()

        fechaInicio = findViewById<View>(R.id.graficasActividadInicio) as EditText?
        fechaFin = findViewById<View>(R.id.graficasActividadFin) as EditText?
        dbStats = AdminSQLiteOpenHelperStats(this)
        dbAsig = AdminSQLiteOpenHelperAsig(this)
        textoDeDia=findViewById(R.id.textoDeDia)
        //textoDeDia?.visibility=View.VISIBLE

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

        mostrarAsignaturas()
        mostrarOpcionesSpinner()



        val bottomNavigation = binding.bottomNavigationViewestadisticas
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.selectedItemId = R.id.action_actividad

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
                R.id.action_actividad -> {
                    // Nada, ya estamos en esta sección
                    true
                }
                else -> false
            }
        }


    }


    /**
     * Función para mostrar los datos actuales teniendo en cuenta lo que esté configurado en el spinner
     */
    private fun mostrarDatosEnTiempoReal() {

        val spinnerOpciones: Spinner = findViewById(R.id.graficasActividadOpciones)
        when (spinnerOpciones.selectedItem.toString()) {
            "Rango" ->  obtenerGrafica(1)
            "Histórico" -> obtenerGrafica(2)
            "Mes actual" -> obtenerGrafica(3)

        }

    }

    /**
     * Función para mostrar las funciones disponibles en el spinner
     */
    private fun mostrarOpcionesSpinner() {
        val spinnerOpciones: Spinner = findViewById(R.id.graficasActividadOpciones)

        val opciones = listOf("Rango", "Histórico","Mes actual")

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
                        obtenerGrafica(1)
                    }
                    "Histórico" -> {
                        fechaInicio?.visibility = View.GONE
                        fechaFin?.visibility = View.GONE
                        obtenerGrafica(2)
                    }
                    "Mes actual" -> {
                        fechaInicio?.visibility = View.GONE
                        fechaFin?.visibility = View.GONE
                        obtenerGrafica(3)
                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se realiza ninguna acción si no se selecciona nada
            }
        }
    }





    /**
     * Función para mostrar en la gráficas los días con la cantidad de minutos para cada uno (histórico)
     */
    private fun obtenerGrafica(opcion:Int) {
        val db = AdminSQLiteOpenHelperStats(this@mayormenoractividad)
        val asignaturaSeleccionada = asignaturaSeleccionada ?: return
        var fechaInicial = fechaInicio?.text.toString()
        var fechaFinal = fechaFin?.text.toString()
        var nocomentarios: TextView?=findViewById(R.id.textoNoComentarios2)

        if(opcion==3){ //como es la actividad del mes actual lo que hace es cambiar la fecha de inicio y fin
            fechaInicial=obtenerPrimerDiaMesActual()
            fechaFinal=obtenerUltimoDiaMesActual()

        }



        val aaChartViewGrafica = findViewById<AAChartView>(R.id.graficaActividad)

        val listaDias = db.obtenerListaDias(asignaturaSeleccionada,opcion,fechaInicial,fechaFinal)

        if (listaDias.isEmpty()) {
            //val mensaje = "No se encontraron resultados historicos para $asignaturaSeleccionada"
            textoDeDia?.visibility=View.GONE
            //Toast.makeText(this@mayormenoractividad, mensaje, Toast.LENGTH_SHORT).show()
            if (nocomentarios != null) {
                nocomentarios.visibility=View.VISIBLE
            }
            aaChartViewGrafica.aa_drawChartWithChartModel(AAChartModel())  // Dibuja un gráfico vacío
            return
        }else{
            textoDeDia?.visibility=View.VISIBLE
            if (nocomentarios != null) {
                nocomentarios.visibility=View.INVISIBLE
            }
        }

        val listaDiasOrdenados = listaDias.sortedByDescending { it.first } // Ordenar la lista por cantidad de minutos en orden descendente

        val listaDiasFloat = listaDiasOrdenados.map { it.first to it.second.toFloat() }

        val dataGrafica = generateAreaChartData(listaDiasFloat)

        val aaChartModelGrafica : AAChartModel = AAChartModel()
            .chartType(AAChartType.Bar)
            .title("Días de actividad de $asignaturaSeleccionada")
            //.subtitle("Histórico")
            .backgroundColor("#d8fcf2")
            .colorsTheme(arrayOf("#f13e71", "#d8fcf2", "#06caf4", "#7dffc0"))
            .dataLabelsEnabled(true)
            .xAxisReversed(true)
            .zoomType(AAChartZoomType.XY)
            .yAxisTitle("Minutos del día")
            .categories(dataGrafica.map { it.first }.toTypedArray())
            .series(arrayOf(
                AASeriesElement()
                    .name("Minutos")
                    .data(dataGrafica.map { it.second }.toTypedArray())
            )
            )


        // Dibuja el gráfico con el modelo configurado
        aaChartViewGrafica.aa_drawChartWithChartModel(aaChartModelGrafica)

    }




    /**
     * Función para mostrar los datos en función del spinner seleccionado
     */
    private fun mostrarAsignaturas() {
        val spinner: Spinner = findViewById(R.id.graficasActividadAsig)
        val spinnerOpciones: Spinner = findViewById(R.id.graficasActividadOpciones)

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
                        "Rango" ->  obtenerGrafica(1)
                        "Histórico" -> obtenerGrafica(2)
                        "Mes actual" -> obtenerGrafica(3)

                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se realiza ninguna acción si no se selecciona nada
                }
            }

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
     * Función para obtener el primer día del mes actual
     * @return Fecha formateada
     */
    fun obtenerPrimerDiaMesActual(): String {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH) + 1

        val mesFormateado = if (month < 10) "0$month" else month.toString()

        return "01/$mesFormateado/$year"
    }

    /**
     * Función para obtener el último día del mes actual
     * @return Fecha formateada
     */
    fun obtenerUltimoDiaMesActual(): String {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH) + 1
        val ultimoDia = calendario.getActualMaximum(Calendar.DAY_OF_MONTH)

        val mesFormateado = if (month < 10) "0$month" else month.toString()
        val ultimoDiaFormateado = if (ultimoDia < 10) "0$ultimoDia" else ultimoDia.toString()

        return "$ultimoDiaFormateado/$mesFormateado/$year"
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

    /**
     * Función para generar datos para el gráfico de área
     * @param data Lista de día y cantidad de minutos
     */

    private fun generateAreaChartData(data: List<Pair<String, Float>>): List<Pair<String, Float>> {
        return data.map { it.first to it.second }
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




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}





