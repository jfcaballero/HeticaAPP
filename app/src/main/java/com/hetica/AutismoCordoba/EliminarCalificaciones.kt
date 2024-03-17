package com.hetica.AutismoCordoba

import CustomListAdapter
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import java.util.ArrayList

/**
 * La asignatura seleccionada.
 */
private var asignaturaSeleccionada: String? = null

@SuppressLint("StaticFieldLeak")
private var botonEliminar: Button? = null


private var btnVolverAVisualizarCalificaciones: Button?=null

class EliminarCalificaciones : AppCompatActivity() {

    private lateinit var listaCalificaciones: ListView
    private lateinit var adapter: CustomListAdapter
    private lateinit var checkBoxSelectAll: CheckBox
    private lateinit var noHayCalificacionesEliminar:TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_calificaciones)

        botonEliminar = findViewById(R.id.botonEliminarCalificacion)
        btnVolverAVisualizarCalificaciones=findViewById(R.id.volverAVisualizarCalificacionesDesdeEliminar)
        listaCalificaciones = findViewById(R.id.listaCalificaciones)
        checkBoxSelectAll = findViewById(R.id.checkBoxSelectAll)
        noHayCalificacionesEliminar=findViewById(R.id.noCalificacionesEliminar)

        // Inicializar el adapter sin datos
        adapter = CustomListAdapter(this, android.R.layout.simple_list_item_multiple_choice, ArrayList())
        listaCalificaciones.adapter = adapter
        listaCalificaciones.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Llamar a viewSubjectGrades con el valor inicial del Spinner
        if (asignaturaSeleccionada != null) {
            viewSubjectGrades()
        }
        botonEliminar?.setOnClickListener {
            deleteSelectedItems()
        }

        // Configuración del evento de clic en los elementos de la lista
        listaCalificaciones.setOnItemClickListener { _, _, _, _ ->
            // No es necesario gestionar manualmente los estados de las casillas de verificación
        }

        // Configuración del evento de clic en "Seleccionar Todo"
        checkBoxSelectAll.setOnCheckedChangeListener { _, isChecked ->
            selectAllItems(isChecked)
        }

        val spinner: Spinner = findViewById(R.id.spinnerBorrarCalificacion)

        // Obtener la lista de asignaturas desde la base de datos
        val asignaturasList = dbAsig?.getAsignaturasList()
        if (asignaturasList != null) {
            val adapter = CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Establecer el listener para el evento de clic del Spinner
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    asignaturaSeleccionada = parent.getItemAtPosition(position).toString() // Asignar valor a la variable global
                    viewSubjectGrades()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se realiza ninguna acción si no se selecciona nada
                }
            }

            // Llamar a viewData con los valores iniciales del Spinner y la fecha
            if (asignaturaSeleccionada != null) {
                // Realizar acciones adicionales si es necesario
            }
        }
        btnVolverAVisualizarCalificaciones?.setOnClickListener {
            volverAVisualizarCalificaciones()
        }
    }

    /**
     * Función para mostrar las calificaciones de la asignatura seleccionada en la lista
     *
     */
    private fun viewSubjectGrades() {
        Log.d("viewSubjectGrades", "Entrando a viewSubjectGrades")

        asignaturaSeleccionada?.let { asignatura ->
            val calificacionesBDList = dbCalificaciones?.getSubjectGradesForSubject(asignatura)

            if (!calificacionesBDList.isNullOrEmpty()) {
                adapter.clear()
                adapter.addAll(calificacionesBDList)
                adapter.notifyDataSetChanged()
                noHayCalificacionesEliminar.visibility=View.INVISIBLE
            } else {
                adapter.clear()
                adapter.notifyDataSetChanged()
                //Toast.makeText(this, "No hay calificaciones para la asignatura seleccionada $asignatura", Toast.LENGTH_LONG).show()
                noHayCalificacionesEliminar.visibility=View.VISIBLE
            }
        }
        Log.d("viewSubjectGrades", "Saliendo de viewSubjectGrades")
    }

    /**
     * Función para eliminar las calificaciones seleccionadas
     */
    private fun deleteSelectedItems() {
        Log.d("deleteSelectedItems", "Entrando a deleteSelectedItems")

        val existentRows = dbCalificaciones?.getSubjectGradesForSubject(asignaturaSeleccionada ?: "")
        if (existentRows != null) {
            Log.d("deleteSelectedItems", "Rows before deletion: ${existentRows.size}")
        }

        // Eliminar los elementos seleccionados
        for (i in 0 until listaCalificaciones.adapter.count) {
            if (listaCalificaciones.isItemChecked(i)) {
                val selectedItem = listaCalificaciones.adapter.getItem(i).toString()
                val parts = selectedItem.split(" | ")

                if (parts.size == 4) {
                    //val date = parts[0]
                    //val type = parts[1]
                    //val grade = parts[2].toFloat()
                    val id = parts[0]

                    // Llamar a la función para eliminar el elemento seleccionado
                    val deleted = dbCalificaciones?.deleteDataByDetails(id)
                    if (deleted == true) {
                        Log.d("deleteSelectedItems", "Se eliminó el elemento con éxito: $selectedItem")
                    } else {
                        Log.e("deleteSelectedItems", "Error al eliminar: $selectedItem")
                    }
                } else {
                    Log.e("deleteSelectedItems", "El elemento seleccionado no tiene el formato esperado: $selectedItem")
                }
            }
        }

        // Actualizar la lista después de eliminar elementos
        viewSubjectGrades()
        listaCalificaciones.clearChoices()
        checkBoxSelectAll.isChecked = false
        Log.d("deleteSelectedItems", "Saliendo de deleteSelectedItems")
    }

    /**
     * Función para seleccionar o deseleccionar todos los elementos en la lista
     * @param isChecked Estado de la caja de "Seleccionar todos" (checkeado/descheckeado)
     */
    private fun selectAllItems(isChecked: Boolean) {
        for (i in 0 until listaCalificaciones.adapter.count) {
            listaCalificaciones.setItemChecked(i, isChecked)
        }
    }

    private fun volverAVisualizarCalificaciones(){
        val intent = Intent(this, VisualizarCalificaciones::class.java)
        startActivity(intent)
    }
}
