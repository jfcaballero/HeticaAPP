package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalificaciones
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import java.util.Calendar

/**
 * ListView de calificaciones.
 */
@SuppressLint("StaticFieldLeak")
private var listViewCalificaciones: ListView? = null
/**
 * La asignatura seleccionada.
 */
private var asignaturaSeleccionada: String? = null

@SuppressLint("StaticFieldLeak")
private var botonEliminar: Button?=null
class EliminarCalificaciones : AppCompatActivity() {

    private lateinit var listaCalificaciones: ListView

    private lateinit var adapter: ArrayAdapter<String>


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_calificaciones)

        botonEliminar=findViewById(R.id.botonEliminarCalificacion)
        listaCalificaciones = findViewById(R.id.listaCalificaciones)
        val checkBoxSelectAll: CheckBox = findViewById(R.id.checkBoxSelectAll)

        // Inicializar el adapter sin datos
        adapter = ArrayAdapter(this, R.layout.list_item_checkbox, R.id.textViewItem, ArrayList())
        listaCalificaciones.adapter = adapter

        // Llamar a viewSubjectGrades con el valor inicial del Spinner
        if (asignaturaSeleccionada != null) {
            viewSubjectGrades()
        }
        botonEliminar?.setOnClickListener {
            deleteSelectedItems()
        }



        // Configuración del evento de clic en los elementos de la lista
        listaCalificaciones.setOnItemClickListener { _, view, position, _ ->
            // Manejar la lógica al hacer clic en un elemento (en este caso, mostrar posición)
            val selectedItem = listaCalificaciones.adapter.getItem(position).toString()
            val checkBox = view.findViewById<CheckBox>(R.id.checkBoxItem)
            // Realizar acciones con el estado de la casilla de verificación (marcado/desmarcado)
            checkBox.isChecked = !checkBox.isChecked
        }
        checkBoxSelectAll.setOnCheckedChangeListener { _, isChecked ->
            // Iterar sobre los elementos de la lista y actualizar el estado de las CheckBox
            for (i in 0 until listaCalificaciones.adapter.count) {
                val view = listaCalificaciones.getChildAt(i)
                val checkBox = view.findViewById<CheckBox>(R.id.checkBoxItem)
                checkBox.isChecked = isChecked
            }
        }

        val spinner: Spinner = findViewById(R.id.spinnerBorrarCalificacion)

        // Obtener la lista de asignaturas desde la base de datos
        val asignaturasList = dbAsig?.getAsignaturasList()
        if (asignaturasList != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Establecer el listener para el evento de clic del Spinner
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    asignaturaSeleccionada = parent.getItemAtPosition(position).toString() // Asignar valor a la variable global
                    if (asignaturaSeleccionada != null) {
                        viewSubjectGrades()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se realiza ninguna acción si no se selecciona nada
                }
            }

            // Llamar a viewData con los valores iniciales del Spinner y la fecha
            if (asignaturaSeleccionada != null) {
            }

        }
    }

    /**
     * Función para mostrar las calificaciones de la asignatura seleccionada
     */
    private fun viewSubjectGrades() {
        Log.d("viewSubjectGrades", "Entrando a viewSubjectGrades")

        asignaturaSeleccionada?.let { asignatura ->
            val calificacionesBDList = dbCalificaciones?.getSubjectGradesForSubject(asignatura)

            if (!calificacionesBDList.isNullOrEmpty()) {
                adapter.clear()
                adapter.addAll(calificacionesBDList)
                adapter.notifyDataSetChanged()

                listViewCalificaciones?.setOnItemClickListener { parent, _, position, _ ->
                    val selectedItem = parent.getItemAtPosition(position) as String
                    // Para dividir el elemento en las partes como las mostramos en el ListView
                    val parts = selectedItem.split(" | ")

                    val date = parts[0]
                    val type = parts[1]
                    val grade = parts[2].toFloat()

                    // Llamar a la función para eliminar la entrada de la base de datos
                    //deleteData(date, asignatura, type, grade)
                }
            } else {
                listViewCalificaciones?.adapter =
                    ArrayAdapter(this, android.R.layout.simple_list_item_1, emptyList<String>())
                Toast.makeText(
                    this,
                    "No hay calificaciones para la asignatura seleccionada $asignatura",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        Log.d("viewSubjectGrades", "Saliendo de viewSubjectGrades")
    }


    /**
     * Función para eliminar las calificaciones seleccionadas
     */
    /**
     * Función para eliminar las calificaciones seleccionadas
     */
    private fun deleteSelectedItems() {
        Log.d("deleteSelectedItems", "Entrando a deleteSelectedItems")

        // Verificar si hay una asignatura seleccionada
        if (asignaturaSeleccionada.isNullOrBlank()) {
            Log.e("deleteSelectedItems", "No hay una asignatura seleccionada.")
            return
        }

        // Eliminar las calificaciones seleccionadas
        val selectedItems = mutableSetOf<String>()  // Usamos un conjunto para evitar duplicados
        for (i in 0 until listaCalificaciones.adapter.count) {
            val view = listaCalificaciones.getChildAt(i)
            val checkBox = view.findViewById<CheckBox>(R.id.checkBoxItem)

            if (checkBox.isChecked) {
                val selectedItem = listaCalificaciones.adapter.getItem(i).toString()
                selectedItems.add(selectedItem)
            }
        }

        for (selectedItem in selectedItems) {
            val parts = selectedItem.split(" | ")

            if (parts.size == 3) {
                val date = parts[0]
                val type = parts[1]
                val grade = parts[2].toFloat()

                // Llamar a la función para eliminar la entrada de la base de datos
                val deleted = dbCalificaciones?.deleteDataByDetails(date, asignaturaSeleccionada ?: "", type, grade)
                if (deleted == true) {
                    Log.d("deleteSelectedItems", "Se eliminó con éxito: $selectedItem")
                } else {
                    Log.e("deleteSelectedItems", "Error al eliminar: $selectedItem")
                }
            } else {
                Log.e("deleteSelectedItems", "El elemento seleccionado no tiene el formato esperado: $selectedItem")
            }
        }

        // Actualizar la lista después de eliminar elementos
        viewSubjectGrades()
        Log.d("deleteSelectedItems", "Saliendo de deleteSelectedItems")
    }








    /**
     * Función para eliminar una calificación dadas fecha, asignatura, tipo y nota
     *
     */
    private fun deleteData(date: String, subject: String, type: String, grade: Float) {
        val deleted = dbCalificaciones?.deleteDataByDetails(date, subject, type, grade)
        if (deleted == true) {
            Toast.makeText(
                this,
                "Se ha eliminado la calificación: $subject | $date | $type | $grade",
                Toast.LENGTH_SHORT
            ).show()
            viewSubjectGrades() // Actualizar la lista después de eliminar un elemento
        } else {
            Toast.makeText(this, "Error al eliminar la calificación.", Toast.LENGTH_SHORT).show()
        }
    }
    /*
    /**
     * Función para imprimir una lista de notas
     *
     */
    private fun printGradesList(gradesList: MutableList<String>) {
        for (grade in gradesList) {
            Toast.makeText(this, "$grade", Toast.LENGTH_LONG).show()
        }
    }*/


}



