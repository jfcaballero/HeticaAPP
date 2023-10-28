package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.hetica.AutismoCordoba.databinding.TiempoDedicadoBinding
import java.util.Calendar



class tiempo_dedicado: AppCompatActivity()  {
    /**
     * The Db.
     */
    var db: AdminSQLiteOpenHelperStats? = null

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




    // Método para configurar la instancia de AdminSQLiteOpenHelperStats
    fun setAdminSQLiteOpenHelperStats(db: AdminSQLiteOpenHelperStats) {
        this.db = db
    }

    private var _binding: TiempoDedicadoBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tiempo_dedicado)

        _binding = TiempoDedicadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            barChartHorizontal.animation.duration = animationDuration
            barChartHorizontal.animate(horizontalBarSet)

        }

        val editText = findViewById<View>(R.id.fechatiempodedicado) as EditText

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationViewestadisticas)
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_estadisticas -> {
                    // Abre la actividad que deseas al hacer clic en el primer elemento
                    val intent = Intent(this, estadisticasDias::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_actividad -> {
                    // Abre la actividad que deseas al hacer clic en el primer elemento
                    val intent = Intent(this, mayormenoractividad::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_comentarios -> {
                    val intent = Intent(this, MostrarComentarios::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_calificaciones -> {
                    val intent = Intent(this, VisualizarCalificaciones::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_tiempo_dedicado -> {
                    // No hagas nada si el ítem seleccionado ya está activo
                    true
                }
                // Agrega otros casos para los elementos adicionales del menú si es necesario
                else -> false
            }
        }



        db = AdminSQLiteOpenHelperStats(this, "Stats.db", null, 1)

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
        viewData()
        editText.setOnClickListener {
            //To show current date in the datepicker
            val mcurrentDate = Calendar.getInstance()
            val year = mcurrentDate[Calendar.YEAR]
            val month = mcurrentDate[Calendar.MONTH]
            val day = mcurrentDate[Calendar.DAY_OF_MONTH]

            yearFinal = if (month < 10) {
                "0" + Integer.toString(month)
            } else {
                Integer.toString(month)
            }
            if (day < 10) {
                yearFinal = yearFinal + "0"
            }
            yearFinal = yearFinal + Integer.toString(day) + Integer.toString(year)
            val mDatePicker = DatePickerDialog(this@tiempo_dedicado, { datepicker, selectedYear, selectedMonth, selectedDay -> // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
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
                viewData()
            }, year, month, day)
            mDatePicker.setTitle("Select date")
            mDatePicker.show()
        }

    }

    /**
     * Función que devuelve las estadísticas de una fecha en concreto
     *
     */
    @SuppressLint("Range")
    private fun viewData() {
        Log.e("Date Selected", yearFinal!!)
        val cursor = db!!.viewDataDias(yearFinal)
        if (cursor!!.count == 0) {
            Toast.makeText(this, "No se trabajó este día", Toast.LENGTH_LONG).show()
            binding.barChartHorizontal.visibility = View.INVISIBLE  // Ocultar la gráfica cuando no hay datos

        } else {
            obtenerTiempoTotalAsignaturas(yearFinal)
        }
    }

    private fun convertMapToList(mapAsignaturas: HashMap<String, Int>): List<Pair<String, Float>> {
        val list = mutableListOf<Pair<String, Float>>()
        for ((asignatura, tiempoTotal) in mapAsignaturas) {
            list.add(asignatura to tiempoTotal.toFloat())
        }
        return list
    }

    private fun generateHorizontalBarData(data: List<Pair<String, Float>>): List<Pair<String, Float>> {
        val mappedData = mutableListOf<Pair<String, Float>>()
        val toastMessage = "Asignatura: ${data[0].first} - Tiempo Total: ${data[0].second} minutos"
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        private val horizontalBarSet = listOf("Default" to 0F)

        private const val animationDuration = 1000L
    }
}