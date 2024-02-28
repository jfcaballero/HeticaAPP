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
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
//import com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_AUTO
import com.google.android.material.navigation.NavigationBarView
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar

var dbAsig: AdminSQLiteOpenHelperAsig? = null
var dbComentarios: AdminSQLiteOpenHelperComentarios? = null
var arrayList: ArrayList<String>? = null
var adapter: ArrayAdapter<String>? = null
var lv: ListView? = null
var yearFinal: String? = null
private var listViewComentarios: ListView? = null
private var asignaturaSeleccionada: String? = null
private var imageMain2: ImageView? = null
private var fechaInicio: EditText? = null
private var fechaFin: EditText? = null

class MostrarComentarios : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
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

    /**
     * Función para mostrar el dataPickerDialog para las fechas para dos editTexts
     * @param editText
     * @param otherEditText
     */
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

    /**
     * Función para dirigirnos a main al pulsar el botón de la casa
     */
    fun GoToMain() {
        imageMain2?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Función para añadir los datos a la lista
     */
    @SuppressLint("Range")
    private fun viewData(
        asignaturaSeleccionada: String?,
        fechaInicio: String? = obtenerFechaActual(),
        fechaFin: String? = obtenerFechaManana()


    ) {
        val fechaInicioTexto: EditText? = findViewById(R.id.comentariosDesde)
        val fechaFinTexto: EditText? = findViewById(R.id.comentariosHasta)
        val textoDe: TextView? = findViewById(R.id.textoComentariosDe)
        val textoA: TextView? = findViewById(R.id.textoComentariosA)
        val comentariosList: ArrayList<Pair<String, String>> = ArrayList()
        val cursor = dbComentarios?.viewData()

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex("NAME"))
                val date = cursor.getString(cursor.getColumnIndex("DATE"))
                val comments = cursor.getString(cursor.getColumnIndex("COMMENTS"))

                if (name == asignaturaSeleccionada) {
                    if (fechaInicioTexto != null && fechaFinTexto!=null && textoA!=null &&textoDe!=null) {
                        ajustarVisibilidadElementos()

                    }
                    if (esOpcionHistoricoSeleccionada() || fechaEstaEntre(date, fechaInicio, fechaFin)) {
                        val abreviado = abreviarComentario(comments)
                        comentariosList.add(Pair("$date - $comments", "$date - $abreviado"))
                    }
                }
            }
        }

        mostrarComentarios(comentariosList)

    }

    /**
     * Función para mostrar los comentarios de la lista en el ListView
     * @param comentariosList
     */
    private fun mostrarComentarios(comentariosList: List<Pair<String, String>>) {
        var nocomentarios: TextView?=findViewById(R.id.textoNoComentarios)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, comentariosList.map { it.second })
        listViewComentarios?.adapter = adapter
        listViewComentarios?.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val comentarioCompleto = comentariosList[position].first
                mostrarCuadroFlotante(comentarioCompleto)
            }


        ajustarVisibilidadElementos()
        if (comentariosList.isEmpty()) {
            if (nocomentarios != null) {
                nocomentarios.visibility=View.VISIBLE
            }

            //Toast.makeText(this, "No hay comentarios para ese intervalo", Toast.LENGTH_SHORT).show()
        }else{
            if (nocomentarios != null) {
                nocomentarios.visibility=View.INVISIBLE
            }
        }
    }

    /**
     * Función para verificar si el spinner está marcando la opción de histórico
     * (para esconder las fechas de rango cuando la opción sea esa)
     */
    private fun esOpcionHistoricoSeleccionada(): Boolean {
        val spinnerOpciones: Spinner = findViewById(R.id.selectorRangoHistorico)
        return spinnerOpciones.selectedItem == "Histórico"
    }

    /**
     * Función para mostrar el cuadro con el comentario al completo al pulsarlo en la lista
     *
     */
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

    /**
     * Función para acortar el comentario al mostrarlo en el listview
     * @param comentario El comentario a acortar si excede la longitud máxima
     * @return El comentario acortado si excede la longitud máxima, de lo contrario, el comentario sin cambios
     */
    private fun abreviarComentario(comentario: String): String {
        val maxLength = 20
        val comentarioSinSaltos = comentario.replace("\n", " ") // Reemplazar saltos de línea por espacios
        return if (comentarioSinSaltos.length > maxLength) {
            comentarioSinSaltos.substring(0, maxLength - 3) + "..."
        } else {
            comentarioSinSaltos
        }
    }

    /**
     * Función para verificar que una fecha está entre dos límites
     * @param fecha Fecha a comprobar
     * @param fechaFin
     * @param fechaInicio
     */
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

    /**
     * Función para obtener la fecha actual para la fecha de inicio por defecto
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
     * Función para obtener la fecha de mañana para la fecha final por defecto
     */
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
    private fun ajustarVisibilidadElementos() {
        val fechaInicioTexto: EditText? = findViewById(R.id.comentariosDesde)
        val fechaFinTexto: EditText? = findViewById(R.id.comentariosHasta)
        val textoDe: TextView? = findViewById(R.id.textoComentariosDe)
        val textoA: TextView? = findViewById(R.id.textoComentariosA)
        val layoutParams = listViewComentarios?.layoutParams
        if (esOpcionHistoricoSeleccionada()) { // Si la opción es histórico, ocultar los elementos
            fechaInicioTexto?.visibility = View.INVISIBLE
            fechaFinTexto?.visibility = View.INVISIBLE
            textoDe?.visibility = View.INVISIBLE
            textoA?.visibility = View.INVISIBLE

            if (layoutParams != null) {
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.expanded_coments_height)
            }

        } else { // Si la opción no es histórico, mostrar los elementos
            fechaInicioTexto?.visibility = View.VISIBLE
            fechaFinTexto?.visibility = View.VISIBLE
            textoDe?.visibility = View.VISIBLE
            textoA?.visibility = View.VISIBLE
            if (layoutParams != null) {
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.default_coments_height)
            }
        }
    }

}