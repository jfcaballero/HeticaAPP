package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * ListView de asignaturas asignadas.
 */
@SuppressLint("StaticFieldLeak")
private var listViewAsignaturasDeUnDia: ListView? = null

/**
 * Botón para añadir asignatura
 */
private var addAsignaturaCalendario: ImageView?=null

/**
 * La lista de asignaturas donde volcaremos la base de datos
 */
var asignaturasCalendarioBD: MutableList<String>? = null
/**
 * The Adapter.
 */
var adapterEditarCalendario: ArrayAdapter<String>? = null


class EditarCalendario : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_calendario)
        val FechaCalendario = findViewById<View>(R.id.FechaCalendario) as EditText
        dbAsig = AdminSQLiteOpenHelperAsig(this, "Asig.db", null, 1)
        dbCalendario = AdminSQLiteOpenHelperCalendario(this, "Calendario.db", null, 1)
        listViewAsignaturasDeUnDia = findViewById(R.id.listViewCalendarioAsignaturas)
        val spinner: Spinner = findViewById(R.id.asignaturasCalendario)
        addAsignaturaCalendario=findViewById(R.id.addAsignaturaCalendario)


        addAsignaturaCalendario?.setOnClickListener {
            val asignaturaSeleccionada = spinner.selectedItem as String
            addAsignatura(asignaturaSeleccionada, yearFinal!!)
        }
        listViewAsignaturasDeUnDia?.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            deleteAsignatura(position, yearFinal!!)
        }




        // Obtener la lista de asignaturas desde la base de datos
        val asignaturasList = dbAsig?.getAsignaturasList()
        if (asignaturasList != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            }

        //gestionar el EditText de la fecha:
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
        FechaCalendario.setText("$dayAux/$monthAux/$yearAux")
        //viewData()
        //viewData(asignaturaSeleccionada, yearFinal)
        viewData(yearFinal!!)
        FechaCalendario.setOnClickListener { // TODO Auto-generated method stub
            //To show current date in the datepicker
            val mcurrentDate = Calendar.getInstance()
            val year = mcurrentDate[Calendar.YEAR]
            val month = mcurrentDate[Calendar.MONTH]
            val day = mcurrentDate[Calendar.DAY_OF_MONTH]
            //month=month +1;
            //yearFinal = Integer.toString(month) + Integer.toString(day) + Integer.toString(year);
            yearFinal = if (month < 10) {
                "0" + Integer.toString(month)
            } else {
                Integer.toString(month)
            }
            if (day < 10) {
                yearFinal = yearFinal + "0"
            }
            yearFinal = yearFinal + Integer.toString(day) + Integer.toString(year)
            val mDatePicker = DatePickerDialog(this@EditarCalendario, { datepicker, selectedYear, selectedMonth, selectedDay ->
                var selectedMonth = selectedMonth
                Log.e("Date Selected", "Month: $selectedMonth Day: $selectedDay Year: $selectedYear")
                selectedMonth = selectedMonth + 1
                FechaCalendario.setText("$selectedDay/$selectedMonth/$selectedYear")
                yearFinal = if (selectedMonth < 10) {
                    "0" + Integer.toString(selectedMonth)
                } else {
                    Integer.toString(selectedMonth)
                }
                if (selectedDay < 10) {
                    yearFinal = yearFinal + "0"
                }
                yearFinal = yearFinal + Integer.toString(selectedDay) + Integer.toString(selectedYear)

                // Llamar a viewData después de seleccionar la fecha

                viewData(yearFinal!!)
            }, year, month, day)
            mDatePicker.setTitle("Select date")
            mDatePicker.show()
        }
        //val dateString = FechaCalendario?.text.toString()
        viewData(yearFinal!!)



    }
    /**
     * Función para añadir una asignatura dado el nombre y la fecha
     *
     */
    private fun addAsignatura(asignaturaSeleccionada: String, dateString: String) {
        val success = dbCalendario?.insertData(dateString, listOf(asignaturaSeleccionada))
        if (success == true) {
            //Toast.makeText(this, "Se ha añadido la asignatura correctamente", Toast.LENGTH_SHORT).show()
            // Actualizar la lista de asignaturas mostrada
            viewData(dateString)
        } else {
            Toast.makeText(this, "Ha ocurrido un error al añadir la asignatura", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Función para mostrar dada una fecha las asignaturas para ese día
     *
     */
    private fun viewData(dateString: String) {
        asignaturasCalendarioBD = dbCalendario?.getAsignaturasForDay(dateString) as MutableList<String>?

        if (!asignaturasCalendarioBD.isNullOrEmpty()) {
            val adapter = ArrayAdapter(this, R.layout.list_item_layout, asignaturasCalendarioBD!!)
            listViewAsignaturasDeUnDia?.adapter = adapter
            adapterEditarCalendario = adapter
            adapter.notifyDataSetChanged()
        } else {
            listViewAsignaturasDeUnDia?.adapter = ArrayAdapter(this, R.layout.list_item_layout, emptyList<String>())
            //Toast.makeText(this, "No hay asignaturas para el día seleccionado", Toast.LENGTH_LONG).show()
        }
    }
    /**
     * Función para gestionar el borrar una asignatura dada la posición en la lista y la fecha
     *
     */
    private fun deleteAsignatura(position: Int, dateString: String) {
        val success = dbCalendario?.deleteAsignaturaByPosition(position, dateString)
        if (success == true) {
            //Toast.makeText(this, "Se ha eliminado la asignatura correctamente", Toast.LENGTH_SHORT).show()
            // Actualizar la lista de asignaturas mostrada
            viewData(dateString)

        } else {
            Toast.makeText(this, "Ha ocurrido un error al eliminar la asignatura", Toast.LENGTH_SHORT).show()
        }
    }





}