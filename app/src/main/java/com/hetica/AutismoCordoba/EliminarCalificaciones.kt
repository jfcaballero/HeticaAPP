package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalificaciones
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import java.util.Calendar

/**
 * ListView de calificaciones.
 */
@SuppressLint("StaticFieldLeak")
private var listViewCalificaciones: ListView? = null


class EliminarCalificaciones : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_calificaciones)
        val FechaEliminar = findViewById<View>(R.id.fechaEliminar) as EditText
        dbCalificaciones =AdminSQLiteOpenHelperCalificaciones(this, "Calificaciones.db", null, 3)
        listViewCalificaciones=findViewById(R.id.listaCalificaciones)

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
        FechaEliminar.setText("$dayAux/$monthAux/$yearAux")

        viewData(yearFinal!!)
        FechaEliminar.setOnClickListener { // TODO Auto-generated method stub
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
            val mDatePicker = DatePickerDialog(this@EliminarCalificaciones, { datepicker, selectedYear, selectedMonth, selectedDay ->
                var selectedMonth = selectedMonth
                Log.e("Date Selected", "Month: $selectedMonth Day: $selectedDay Year: $selectedYear")
                selectedMonth = selectedMonth + 1
                FechaEliminar.setText("$selectedDay/$selectedMonth/$selectedYear")
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
     * Función para mostrar dada la fecha las calificaciones en la lista
     *
     */
    private fun viewData(dateString: String) {
        val calificacionesBDList = dbCalificaciones?.getSubjectGradesForDate(dateString)

        if (!calificacionesBDList.isNullOrEmpty()) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, calificacionesBDList)
            listViewCalificaciones?.adapter = adapter
            adapter.notifyDataSetChanged()

            listViewCalificaciones?.setOnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as String
                // Para dividir el elemento en las partes como las mostramos en el ListView
                val parts = selectedItem.split(" | ")

                val subject = parts[0]
                val type = parts[1]
                val grade = parts[2].toFloat()

                // Llamar a la función para eliminar la entrada de la base de datos
                deleteData(dateString, subject, type, grade)
            }
        } else {
            listViewCalificaciones?.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emptyList<String>())
            Toast.makeText(this, "No hay calificaciones para el día seleccionado $dateString", Toast.LENGTH_LONG).show()
        }
    }
    /**
     * Función para eliminar una calificación dadas fecha, asignatura, tipo y nota
     *
     */
    private fun deleteData(date: String, subject: String, type: String, grade: Float) {
        val deleted = dbCalificaciones?.deleteDataByDetails(date, subject, type, grade)
        if (deleted == true) {
            Toast.makeText(this, "Se ha eliminado la calificación: $subject | $type | $grade", Toast.LENGTH_SHORT).show()
            viewData(date) // Actualizar la lista después de eliminar un elemento
        } else {
            Toast.makeText(this, "Error al eliminar la calificación.", Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Función para imprimir una lista de notas
     *
     */
    private fun printGradesList(gradesList: MutableList<String>) {
        for (grade in gradesList) {
            Toast.makeText(this, "$grade", Toast.LENGTH_LONG).show()
        }
    }




}

