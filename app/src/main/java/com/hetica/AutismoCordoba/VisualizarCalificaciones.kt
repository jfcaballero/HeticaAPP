package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalificaciones
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.hetica.AutismoCordoba.databinding.ActivityVisualizarCalificacionesBinding


/**
 * La asignatura seleccionada.
 */
private var asignaturaSeleccionada: String? = null
/**
 * El tipo de examen seleccionado.
 */
private var tipoSeleccionado: String? = null



@SuppressLint("StaticFieldLeak")
private var _binding: ActivityVisualizarCalificacionesBinding? = null
private val binding get() = _binding!!
class VisualizarCalificaciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbAsig = AdminSQLiteOpenHelperAsig(this, "Asig.db", null, 1)
        _binding = ActivityVisualizarCalificacionesBinding.inflate(layoutInflater)
        val spinnerAsignaturas: Spinner = binding.spinnerCalificacionAsig
        val spinnerTipos: Spinner = binding.spinnerParcialFinal

        val asignaturasList = dbAsig?.getAsignaturasList()
        val tipoExamenList = listOf("Parcial", "Final")

        setContentView(binding.root)
        binding.apply {

            barChartCalificaciones.animation.duration = animationDuration
            barChartCalificaciones.animate(horizontalBarSet)

        }



        //Spinner de asignaturas
        if (asignaturasList != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAsignaturas.adapter = adapter

            // Establecer el listener para el evento de clic del Spinner
            spinnerAsignaturas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val asignaturaSeleccionada = spinnerAsignaturas.selectedItem.toString() // Asignar valor a la variable global
                    val tipoSeleccionado = spinnerTipos.selectedItem.toString()
                    //Toast.makeText(applicationContext, "Asignatura seleccionada: $asignaturaSeleccionada", Toast.LENGTH_SHORT).show()
                    obtenerCalificaciones(asignaturaSeleccionada, tipoSeleccionado)
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
                val tipoSeleccionado = spinnerTipos.selectedItem.toString() // Asignar valor a la variable global
                val asignaturaSeleccionada = spinnerAsignaturas.selectedItem.toString()
                //Toast.makeText(applicationContext, "Tipo seleccionado: $tipoSeleccionado", Toast.LENGTH_SHORT).show()
                obtenerCalificaciones(asignaturaSeleccionada, tipoSeleccionado)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se realiza ninguna acción si no se selecciona nada
            }
        }




        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationViewestadisticas)
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_estadisticas -> {
                    val intent = Intent(this, estadisticasDias::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_tiempo_dedicado -> {
                    // Abre otra actividad cuando se hace clic en otro elemento
                    val intent = Intent(this, tiempo_dedicado::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_actividad -> {
                    // Abre la actividad que deseas al hacer clic en el primer elemento
                    val intent2 = Intent(this, mayormenoractividad::class.java)
                    startActivity(intent2)
                    true
                }
                R.id.action_calificaciones -> {
                    // No hagas nada si el ítem seleccionado ya está activo
                    true
                }
                R.id.action_comentarios -> {
                    val intent = Intent(this, MostrarComentarios::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


    }

    private fun obtenerCalificaciones(asignatura: String, tipo: String) {
        val dbCalificaciones = AdminSQLiteOpenHelperCalificaciones(this, "Calificaciones.db", null, 2)
        //dbCalificaciones.clearData()
        dbCalificaciones.insertData("lengua", 5.0F,"Parcial")

        val listaCalificaciones= dbCalificaciones.getSubjectGradesList(asignatura,tipo)
        printSubjectGradesList(listaCalificaciones)
        if (listaCalificaciones.isEmpty()) {
            Toast.makeText(this, "No hay datos disponibles para mostrar en el gráfico.", Toast.LENGTH_SHORT).show()
            binding.barChartCalificaciones.animate(emptyList())
            return
        }


        binding.barChartCalificaciones.animation.duration = animationDuration
        val data = generateHorizontalBarData(listaCalificaciones)
        binding.barChartCalificaciones.animate(data)
        binding.barChartCalificaciones.invalidate()

    }
    private fun printSubjectGradesList(subjectGradesList: List<Pair<String, Float>>) {
        val context = applicationContext
        for ((subject, grade) in subjectGradesList) {
            val message = "Asignatura: $subject, Calificación: $grade"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateHorizontalBarData(data: List<Pair<String, Float>>): List<Pair<String, Float>> {
        val mappedData = mutableListOf<Pair<String, Float>>()

        //for (i in 0 until data.size) {
        //    mappedData.add(Pair(data[i].first, data[i].second))
       // }
        for (i in 0 until data.size) {
            mappedData.add(data[i])

        }
        return mappedData
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