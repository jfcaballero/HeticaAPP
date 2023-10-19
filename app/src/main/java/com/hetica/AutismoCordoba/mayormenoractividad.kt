package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
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
import android.widget.Spinner
import android.widget.Toast
import com.db.williamchart.view.HorizontalBarChartView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.hetica.AutismoCordoba.databinding.ActivityMayormenoractividadBinding
import com.hetica.AutismoCordoba.databinding.TiempoDedicadoBinding
import java.util.Calendar


class mayormenoractividad : AppCompatActivity() {
    private var _binding: ActivityMayormenoractividadBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMayormenoractividadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            diasmayoractividad.animation.duration = animationDuration
            diasmayoractividad.animate(horizontalBarSet)

            diasmenoractividad.animation.duration = animationDuration
            diasmenoractividad.animate(horizontalBarSet)
        }



        val editTextYear = findViewById<EditText>(R.id.editTextYear)

        // Obtener el año actual
        val year = obtenerAnioActual()

        // Establecer el valor por defecto en el EditText
        editTextYear.setText(year.toString())


        val meses = listOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
        val mesActualEnFormatoString = obtenerMesActualEnFormatoString()
        val spinner: Spinner = binding.selectormes
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, meses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Obtener el índice del mes actual en la lista de meses
        val index = meses.indexOf(mesActualEnFormatoString)
        if (index != -1) {
            spinner.setSelection(index) // Establecer la posición predeterminada del Spinner al índice del mes actual
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val selectedItem: String = parent.getItemAtPosition(pos).toString()
                val db = AdminSQLiteOpenHelperStats(this@mayormenoractividad, "Stats.db", null, 1)

                val editTextYear = findViewById<EditText>(R.id.editTextYear) // Agregar esta línea para obtener el EditText

                val anyo = editTextYear.text.toString()
                if (anyo.isNotEmpty()) { // Asegurarse de que el EditText no esté vacío antes de obtener el valor del año
                    obtenerDiasConMinutosEnUnMes(db, selectedItem, anyo)
                } else {
                    // Realizar alguna acción apropiada si el EditText está vacío
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) { //No se hace nada
            }
        }

// Agregar el TextWatcher al EditText
        editTextYear.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se realiza ninguna acción antes de que cambie el texto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se realiza ninguna acción durante el cambio de texto
            }

            override fun afterTextChanged(s: Editable?) {
                val anyo = s.toString()
                if (anyo.isNotEmpty()) {
                    val yearValue = anyo.toInt()
                    if (yearValue < 2023) {
                        Toast.makeText(this@mayormenoractividad, "El año debe ser igual o superior a 2023", Toast.LENGTH_SHORT).show()
                        return
                    }else{
                        val selectedItem = spinner.selectedItem.toString()
                        val db = AdminSQLiteOpenHelperStats(this@mayormenoractividad, "Stats.db", null, 1)
                        obtenerDiasConMinutosEnUnMes(db, selectedItem, anyo)
                    }

                } else {
                    Toast.makeText(this@mayormenoractividad, "Debe especificar el año de consulta", Toast.LENGTH_SHORT).show()

                    // Realizar alguna acción apropiada si el EditText está vacío
                }
            }
        })


        val bottomNavigation = binding.bottomNavigationViewestadisticas
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_estadisticas -> {
                    val intent = Intent(this, estadisticasDias::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_tiempo_dedicado -> {
                    val intent = Intent(this, tiempo_dedicado::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_comentarios -> {
                    val intent = Intent(this, MostrarComentarios::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_actividad -> {
                    // Nada, estamos ya
                    true
                }
                else -> false
            }
        }
    }

    private fun obtenerDiasConMinutosEnUnMes(db: AdminSQLiteOpenHelperStats, mes: String, anyo: String) {
        val listaDias = db.obtenerListaDiasOrdenadosPorMinutosEstudiadosEnUnMes(mes,anyo)

        if (listaDias.isEmpty()) {
            val mensaje = "No se encontraron resultados para el mes $mes, año $anyo"
            Toast.makeText(this@mayormenoractividad, mensaje, Toast.LENGTH_SHORT).show()
            binding.diasmayoractividad.animate(emptyList())
            binding.diasmenoractividad.animate(emptyList())

            return
        }

        val listaDiasOrdenados = listaDias.sortedByDescending { it.second } // Ordenar la lista por cantidad de minutos en orden descendente
        val listaDiasTop4 = listaDiasOrdenados.take(4) // Obtener solo los 4 primeros elementos de la lista ordenada
        val listaDiasTop4Asc = listaDiasOrdenados.takeLast(4) // Obtener los 4 últimos elementos de la lista ordenada



        val listaDiasFloat = listaDiasTop4.map { it.first to it.second.toFloat() }
        val listaDiasFloatAsc = listaDiasTop4Asc.map { it.first to it.second.toFloat() }

        binding.diasmayoractividad.animation.duration = mayormenoractividad.animationDuration
        val data = generateHorizontalBarData(listaDiasFloat)
        binding.diasmayoractividad.animate(data)
        binding.diasmayoractividad.invalidate()

        binding.diasmenoractividad.animation.duration = mayormenoractividad.animationDuration
        val dataAsc = generateHorizontalBarData(listaDiasFloatAsc)
        binding.diasmenoractividad.animate(dataAsc)
        binding.diasmenoractividad.invalidate()
    }




    private fun generateHorizontalBarData(data: List<Pair<String, Float>>): List<Pair<String, Float>> {
        val mappedData = mutableListOf<Pair<String, Float>>()
        val rectangle2 = findViewById<View>(R.id.rectangle2)
        val rectangle3 = findViewById<View>(R.id.rectangle3)
        for (i in 0 until data.size) {
            mappedData.add(data[i])

        }
        if (data.size == 1) {
            mappedData.add(data[0])
            rectangle2.visibility = View.VISIBLE
            rectangle3.visibility = View.VISIBLE

        }else{
            rectangle2.visibility = View.INVISIBLE
            rectangle3.visibility = View.INVISIBLE
        }

        return mappedData
    }
    fun obtenerMesActualEnFormatoString(): String {
        val calendar = Calendar.getInstance()
        val mesActual = calendar.get(Calendar.MONTH) + 1 // Se suma 1 porque en Calendar, los meses comienzan en 0
        return String.format("%02d", mesActual)
    }
    fun obtenerAnioActual(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
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





