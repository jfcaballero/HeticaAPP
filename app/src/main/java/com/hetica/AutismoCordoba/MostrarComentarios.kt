package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperComentarios
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.AlertDialog
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

var dbAsig: AdminSQLiteOpenHelperAsig? = null
var dbComentarios: AdminSQLiteOpenHelperComentarios? = null
var arrayList: ArrayList<String>? = null
var adapter: ArrayAdapter<String>? = null
var lv: ListView? = null
var yearFinal: String? = null
var yearFinal2: String? = null
var monthFinal: String? = null
var dayFinal: String? = null
private var listViewComentarios: ListView? = null
private var asignaturaSeleccionada: String? = null
private var imageMain2: ImageView? = null
private var fechaInicio: EditText? = null
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
        imageMain2 = findViewById(R.id.botonMain2)
        GoToMain()

        val fechaHoy = obtenerFechaActual()
        val fechaManana = obtenerFechaManana()

        fechaInicio?.setText(fechaHoy)
        fechaFin?.setText(fechaManana)
        if (asignaturaSeleccionada != null) {
            viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())
        }

        val calendarTomorrow = Calendar.getInstance()
        calendarTomorrow.add(Calendar.DAY_OF_MONTH, 1)

        fechaInicio?.setOnClickListener {
            showDatePickerDialog(fechaInicio!!, fechaFin!!)
        }

        fechaFin?.setOnClickListener {
            showDatePickerDialog(fechaFin!!, fechaInicio!!)
        }

        val spinnerOpciones: Spinner = findViewById(R.id.selectorRangoHistorico)

        val opciones = listOf("Rango", "Histórico")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerOpciones.adapter = adapter

        // Agregar un listener para el Spinner de opciones
        spinnerOpciones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Llamar a viewData cada vez que cambies la opción en el Spinner de opciones
                viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
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
                        R.anim.slide_in,
                        R.anim.slide_out
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_tiempo_dedicado -> {
                    val intent = Intent(this, tiempo_dedicado::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,
                        R.anim.slide_out
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_actividad -> {
                    val intent = Intent(this, mayormenoractividad::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,
                        R.anim.slide_out
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_calificaciones -> {
                    val intent = Intent(this, VisualizarCalificaciones::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,
                        R.anim.slide_out
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

        val asignaturasList = dbAsig?.getAsignaturasList()
        if (asignaturasList != null) {
            val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter2

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    asignaturaSeleccionada = parent.getItemAtPosition(position).toString()
                    viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

            if (asignaturaSeleccionada != null) {
                viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())
            }
        }
    }

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
                viewData(asignaturaSeleccionada, fechaInicio?.text.toString(), fechaFin?.text.toString())
                editText.onFocusChangeListener?.onFocusChange(editText, false)
                otherEditText.onFocusChangeListener?.onFocusChange(otherEditText, false)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun GoToMain() {
        imageMain2?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("Range")
    private fun viewData(
        asignaturaSeleccionada: String?,
        fechaInicio: String? = obtenerFechaActual(),
        fechaFin: String? = obtenerFechaManana()
    ) {
        val comentariosList: ArrayList<Pair<String, String>> = ArrayList()
        val cursor = dbComentarios?.viewData()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex("NAME"))
                val date = cursor.getString(cursor.getColumnIndex("DATE"))
                val comments = cursor.getString(cursor.getColumnIndex("COMMENTS"))

                if (name == asignaturaSeleccionada) {
                    if (esOpcionHistoricoSeleccionada() || fechaEstaEntre(date, fechaInicio, fechaFin)) {
                        val abreviado = abreviarComentario(comments)
                        comentariosList.add(Pair("$date - $comments", "$date - $abreviado"))
                    }
                }
            }
        }

        mostrarComentarios(comentariosList)
    }

    private fun mostrarComentarios(comentariosList: List<Pair<String, String>>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, comentariosList.map { it.second })
        listViewComentarios?.adapter = adapter
        listViewComentarios?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val comentarioCompleto = comentariosList[position].first
                mostrarCuadroFlotante(comentarioCompleto)
            }

        if (comentariosList.isEmpty()) {
            Toast.makeText(this, "No hay comentarios para ese intervalo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun esOpcionHistoricoSeleccionada(): Boolean {
        val spinnerOpciones: Spinner = findViewById(R.id.selectorRangoHistorico)
        return spinnerOpciones.selectedItem == "Histórico"
    }

    private fun mostrarCuadroFlotante(comentario: String) {
        val partes = comentario.split(" - ", limit = 2)
        val fecha = if (partes.isNotEmpty()) partes[0] else ""
        val soloComentario = if (partes.size > 1) partes[1] else comentario

        val builder = AlertDialog.Builder(this)
        builder.setTitle(fecha)
            .setMessage(soloComentario)
            .setPositiveButton("Salir") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun abreviarComentario(comentario: String): String {
        val maxLength = 50
        return if (comentario.length > maxLength) {
            comentario.substring(0, maxLength - 3) + "..."
        } else {
            comentario
        }
    }

    fun fechaEstaEntre(fecha: String, fechaInicio: String?, fechaFin: String?): Boolean {
        val formato = SimpleDateFormat("dd/MM/yyyy")

        try {
            val dateFecha = formato.parse(fecha)!!
            val dateInicio = fechaInicio?.let { formato.parse(it) }!!
            val dateFin = fechaFin?.let { formato.parse(it) }!!

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

        return false
    }

    fun obtenerFechaActual(): String {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH) + 1
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        val mesFormateado = if (month < 10) "0$month" else month.toString()
        val diaFormateado = if (day < 10) "0$day" else day.toString()

        return "$diaFormateado/$mesFormateado/$year"
    }

    fun obtenerFechaManana(): String {
        val calendario = Calendar.getInstance()
        calendario.add(Calendar.DAY_OF_MONTH, 1)

        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH) + 1
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        val mesFormateado = if (month < 10) "0$month" else month.toString()
        val diaFormateado = if (day < 10) "0$day" else day.toString()

        return "$diaFormateado/$mesFormateado/$year"
    }
}