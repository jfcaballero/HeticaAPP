package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalificaciones
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.hetica.AutismoCordoba.databinding.ActivityVisualizarCalificacionesBinding


import android.graphics.Color
import com.androidplot.xy.*
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*




/**
 * The type VisualizarCalificaciones
 *
 * @author Álvaro Berjillos Roldán
 *
 */


/**
 * La asignatura seleccionada.
 */
private var asignaturaSeleccionada: String? = null
/**
 * El tipo de examen seleccionado.
 */
private var tipoSeleccionado: String? = null
/**
 * La base de datos de las calificaciones
 */
var dbCalificaciones: AdminSQLiteOpenHelperCalificaciones? =null
/**
 * Imagen para irnos al Main
 */
@SuppressLint("StaticFieldLeak")
var imageMain: ImageView?=null

@SuppressLint("StaticFieldLeak")
private lateinit var spinnerAsignaturas: Spinner
@SuppressLint("StaticFieldLeak")
private lateinit var spinnerTipos: Spinner
@SuppressLint("StaticFieldLeak")
private var rectangle: View? = null

@SuppressLint("StaticFieldLeak")
private var _binding: ActivityVisualizarCalificacionesBinding? = null
private val binding get() = _binding!!
class VisualizarCalificaciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setInitialValues()
        dbAsig = AdminSQLiteOpenHelperAsig(this)
        dbCalificaciones = AdminSQLiteOpenHelperCalificaciones(this, null, 3)
        _binding = ActivityVisualizarCalificacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageMain=findViewById(R.id.botonMain3)
        GoToMain()

        val asignaturasList = dbAsig?.getAsignaturasList()
        val tipoExamenList = listOf("Parcial", "Final")

        asignaturaSeleccionada = asignaturasList?.get(0)
        tipoSeleccionado = tipoExamenList[0]

        val buttonActividadCalificacion: Button = binding.buttonActividadCalificacion
        buttonActividadCalificacion.setOnClickListener {
            val intent = Intent(this, AddCalificaciones::class.java)
            startActivity(intent)
        }

        val buttonActividadEliminarCalificaciones: Button = binding.buttonActividadEliminarCalificacion
        buttonActividadEliminarCalificaciones.setOnClickListener {
            val intent = Intent(this, EliminarCalificaciones::class.java)
            startActivity(intent)
        }


        binding.apply {

            barChartCalificaciones.animation.duration = animationDuration
            barChartCalificaciones.animate(horizontalBarSet)
            barChartCalificaciones.labelsFormatter = { "%.2f".format(it) }


        }
        //dbCalificaciones!!.clearData()
        //dbCalificaciones!!.insertData("lengua", 8.0F,"Parcial")

        spinnerAsignaturas = binding.spinnerCalificacionAsig
        spinnerTipos = binding.spinnerParcialFinal
        rectangle = binding.rectangleVisualizarCalificaciones
        rectangle!!.visibility = View.INVISIBLE

        //Spinner de asignaturas
        if (asignaturasList != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAsignaturas.adapter = adapter

            // Establecer el listener para el evento de clic del Spinner
            spinnerAsignaturas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    updateSelectedValues()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se realiza ninguna acción si no se selecciona nada
                }
            }
        }
        //Spinner de tipo de examen
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoExamenList)
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipos.adapter = adapterTipos

        // Establecer el listener para el evento de clic del Spinner
        spinnerTipos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateSelectedValues()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se realiza ninguna acción si no se selecciona nada
            }
        }
        setInitialValues()




        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationViewestadisticas)
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.selectedItemId = R.id.action_calificaciones

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
                R.id.action_calificaciones -> true
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
                else -> false
            }
        }

    }
    private fun testChartAA(asignatura: String, tipo: String) {
        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)

        // Obtén tus datos de calificaciones
        val listaCalificaciones = dbCalificaciones?.getSubjectGradesList(asignatura, tipo)
        if (listaCalificaciones.isNullOrEmpty() ) {
            //Toast.makeText(this, "No hay datos disponibles para mostrar en el gráfico.", Toast.LENGTH_SHORT).show()
            aaChartView.aa_drawChartWithChartModel(AAChartModel())  // Dibuja un gráfico vacío
            return
        }

        // Ordenar las calificaciones por fecha
        val calificacionesOrdenadas = listaCalificaciones.sortedBy { parseDate(it.first) }

        // Generar datos para la gráfica
        val data = generateAreaChartData(calificacionesOrdenadas)

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Bar)
            .title("Calificaciones de $asignatura")
            .subtitle("Tipo: $tipo")
            .backgroundColor("#d8fcf2")
            .colorsTheme(arrayOf("#f13e71", "#d8fcf2", "#06caf4", "#7dffc0"))
            .dataLabelsEnabled(true)
            .categories(data.map { it.first }.toTypedArray())
            .series(arrayOf(
                AASeriesElement()
                    .name("Calificaciones")
                    .data(data.map { it.second }.toTypedArray())
            )
            )

        // Dibuja el gráfico con el modelo configurado
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    // Función para convertir una cadena de fecha en un objeto Date
    private fun parseDate(dateString: String): Date {
        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        return dateFormat.parse(dateString) ?: Date()
    }


    // Función para generar datos para el gráfico de área
    private fun generateAreaChartData(data: List<Pair<String, Float>>): List<Pair<String, Float>> {
        return data.map { it.first to it.second }
    }


    private fun testChart(asignatura: String, tipo: String) {
        val plot: XYPlot = binding.plot
        val listaCalificaciones = dbCalificaciones?.getSubjectGradesList(asignatura, tipo)
        if (listaCalificaciones.isNullOrEmpty()) {
            //Toast.makeText(this, "No hay datos disponibles para mostrar en el gráfico.", Toast.LENGTH_SHORT).show()
            return
        }

        // Ordenar las calificaciones por fecha
        val calificacionesOrdenadas = listaCalificaciones.sortedBy { it.first }

        // Generar datos para la gráfica
        val data = generateHorizontalBarData(calificacionesOrdenadas)

        if (data.isNotEmpty()) {
            // Crear series XY para calificaciones
            val series: XYSeries = SimpleXYSeries(
                data.mapIndexed { index, _ -> index.toDouble() },
                data.map { it.second },
                "Calificaciones"
            )

            // Configurar formato de línea y punto
            val seriesFormat = LineAndPointFormatter(Color.BLUE, Color.BLACK, null, null)
            seriesFormat.setInterpolationParams(CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal))

            // Limpiar las series existentes y agregar la nueva serie
            plot.clear()
            plot.addSeries(series, seriesFormat)

            // Configurar el gráfico
            plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
                override fun format(obj: Any?, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
                    val i = Math.round((obj as Number).toFloat())
                    return toAppendTo.append(calificacionesOrdenadas[i.toInt()].first)  // Asignatura como etiquetas
                }

                override fun parseObject(source: String?, pos: ParsePosition): Any? {
                    return null
                }
            }

            PanZoom.attach(plot)
            plot.redraw()
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
     * Función para mostrar las calificaciones en la gráfica
     * @param asignatura
     * @param tipo
     */
    private fun obtenerCalificaciones(asignatura: String, tipo: String) {

        val listaCalificaciones= dbCalificaciones?.getSubjectGradesList(asignatura,tipo)
        if (listaCalificaciones.isNullOrEmpty()) {
            Toast.makeText(this, "No hay datos disponibles para mostrar en el gráfico.", Toast.LENGTH_SHORT).show()
            binding.barChartCalificaciones.animate(emptyList())
        }else{
            //printSubjectGradesList(listaCalificaciones)
            binding.barChartCalificaciones.animation.duration = animationDuration
            val data = generateHorizontalBarData(listaCalificaciones)

            if (data.isNotEmpty()) {
                binding.barChartCalificaciones.animate(data)
                binding.barChartCalificaciones.invalidate()
            }
        }

    }
    /**
     * Función para imprimir todas las calificaciones
     * @param subjectGradesList Lista de pares de asignatura y nota
     *
     */
    private fun printSubjectGradesList(subjectGradesList: List<Pair<String, Float>>) {
        val context = applicationContext
        for ((subject, grade) in subjectGradesList) {
            val message = "Asignatura: $subject, Calificación: $grade"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Función para generar datos que se puedan leer por las gráficas
     * @param data
     * @return mappedData
     *
     */
    /*private fun generateHorizontalBarData(data: List<Pair<String, Float>>): List<Pair<String, Float>> {
        val mappedData = mutableListOf<Pair<String, Float>>()

        rectangle = binding.rectangleVisualizarCalificaciones
        for (i in 0 until data.size) {
            mappedData.add(data[i])

        }
        if (data.size == 1) {
            mappedData.add(data[0])

            rectangle!!.visibility = View.VISIBLE

        }else{

            rectangle!!.visibility = View.INVISIBLE

        }

        return mappedData
    }*/
    private fun generateHorizontalBarData(data: List<Pair<String, Float>>): List<Pair<String, Float>> {
        val mappedData = mutableListOf<Pair<String, Float>>()

        rectangle = binding.rectangleVisualizarCalificaciones
        for (i in 0 until data.size) {
            mappedData.add(Pair(i.toString(), data[i].second))
        }

        if (data.size == 1) {
            mappedData.add(Pair(data.size.toString(), data[0].second))
            rectangle!!.visibility = View.VISIBLE
        } else {
            rectangle!!.visibility = View.INVISIBLE
        }

        return mappedData
    }

    /**
     * Función para mostrar los valores en la gráfica en base a los predeterminados al inicio por los
     * spinners
     *
     */
    private fun setInitialValues() {
        val asignaturasList = dbAsig?.getAsignaturasList()
        val tipoExamenList = listOf("Parcial", "Final")
        asignaturaSeleccionada = asignaturasList?.get(0)
        tipoSeleccionado = tipoExamenList[0]
        //obtenerCalificaciones(asignaturaSeleccionada!!, tipoSeleccionado!!)
        testChart(asignaturaSeleccionada!!, tipoSeleccionado!!)
        testChartAA(asignaturaSeleccionada!!, tipoSeleccionado!!)
    }
    /**
     * Función para actualizar las gráficas al cambiar valores de spinner
     *
     */
    private fun updateSelectedValues() {
        asignaturaSeleccionada = spinnerAsignaturas.selectedItem.toString()
        tipoSeleccionado = spinnerTipos.selectedItem.toString()
        //obtenerCalificaciones(asignaturaSeleccionada!!, tipoSeleccionado!!)
        testChart(asignaturaSeleccionada!!, tipoSeleccionado!!)
        testChartAA(asignaturaSeleccionada!!, tipoSeleccionado!!)
    }


    override fun onResume() {
        super.onResume()
        setInitialValues()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        private val horizontalBarSet = listOf("Default" to 0F)

        private const val animationDuration = 1000L
    }
}