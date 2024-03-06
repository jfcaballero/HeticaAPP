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
import android.view.View.INVISIBLE
import android.widget.TextView
import com.androidplot.xy.*
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartAnimationType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartZoomType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import org.w3c.dom.Text
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

var noHayDatos: TextView? = null

@SuppressLint("StaticFieldLeak")
private var _binding: ActivityVisualizarCalificacionesBinding? = null
private val binding get() = _binding!!
class VisualizarCalificaciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbAsig = AdminSQLiteOpenHelperAsig(this)
        dbCalificaciones = AdminSQLiteOpenHelperCalificaciones(this, null, 3)
        _binding = ActivityVisualizarCalificacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageMain=findViewById(R.id.botonMain3)
        noHayDatos = findViewById(R.id.noHayDatos)

        val noHayDatosTextView = noHayDatos
        if (noHayDatosTextView != null) {
            noHayDatosTextView.visibility = INVISIBLE
        }

        GoToMain()

        val asignaturasList = dbAsig?.getAsignaturasList()
        val tipoExamenList = listOf("Parcial", "Final")

        asignaturaSeleccionada = asignaturasList?.get(0)
        tipoSeleccionado = tipoExamenList[0]

        val buttonActividadCalificacion: Button = binding.buttonActividadCalificacion
        buttonActividadCalificacion.setOnClickListener {
            val asignaturaSeleccionada = spinnerAsignaturas.selectedItem.toString()
            val intent = Intent(this, AddCalificaciones::class.java)
            intent.putExtra("asignaturaSeleccionada", asignaturaSeleccionada)
            startActivity(intent)
        }

        val buttonActividadEliminarCalificaciones: Button = binding.buttonActividadEliminarCalificacion
        buttonActividadEliminarCalificaciones.setOnClickListener {
            val intent = Intent(this, EliminarCalificaciones::class.java)
            startActivity(intent)
        }




        spinnerAsignaturas = binding.spinnerCalificacionAsig
        spinnerTipos = binding.spinnerParcialFinal


        //Spinner de asignaturas
        if (asignaturasList != null) {
            val adapter = CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
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
        val adapterTipos = CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, tipoExamenList)
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipos.adapter = adapterTipos

        // Establecemos el listener para el evento de clic del Spinner
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

    /**
     * Función para dibujar el gráfico
     * @param asignatura
     * @param tipo
     */
    private fun testChartAA(asignatura: String, tipo: String) {
        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)

        // Obtenemos los datos de calificaciones
        val listaCalificaciones = dbCalificaciones?.getSubjectGradesList(asignatura, tipo)

        if (listaCalificaciones.isNullOrEmpty() ) {
            noHayDatos?.visibility = View.VISIBLE
            aaChartView.aa_drawChartWithChartModel(AAChartModel())  // Dibuja un gráfico vacío
            return
        }else {
            noHayDatos?.visibility = INVISIBLE
        }


        // Generamos los datos para la gráfica
        val data = generateAreaChartData(listaCalificaciones)

        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .title("Calificaciones de $asignatura")
            .subtitle("Tipo: $tipo")
            .yAxisLabelsEnabled(true)
            .yAxisTitle("Nota")
            .zoomType(AAChartZoomType.XY)
            .backgroundColor("#d8fcf2")
            .colorsTheme(arrayOf("#f13e71", "#d8fcf2", "#06caf4", "#7dffc0"))
            .dataLabelsEnabled(true)
            .categories(data.map { it.first }.toTypedArray())
            .series(arrayOf(
                AASeriesElement()
                    .name("Nota del día")
                    .data(data.map { it.second }.toTypedArray())
            )
            )

        // Dibujamos el gráfico con el modelo configurado
        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }



    /**
     * Función para generar datos para el gráfico de área
     * @param data Lista de Fecha y Notas
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
        testChartAA(asignaturaSeleccionada!!, tipoSeleccionado!!)
    }
    /**
     * Función para actualizar las gráficas al cambiar valores de spinner
     *
     */
    private fun updateSelectedValues() {
        asignaturaSeleccionada = spinnerAsignaturas.selectedItem.toString()
        tipoSeleccionado = spinnerTipos.selectedItem.toString()
        testChartAA(asignaturaSeleccionada!!, tipoSeleccionado!!)
    }


    override fun onResume() {
        super.onResume()
        //setInitialValues()
        updateSelectedValues()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}