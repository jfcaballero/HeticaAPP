package com.hetica.AutismoCordoba

import CustomListAdapter
import CustomToolbarAdapter
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import java.util.ArrayList

/**
 * La asignatura seleccionada.
 */
private var asignaturaSeleccionada: String? = null

@SuppressLint("StaticFieldLeak")
private var botonEliminar: Button? = null


private var btnVolverAVisualizarCalificaciones: Button?=null
private lateinit var toolbar: Toolbar
private lateinit var customToolbarAdapter: CustomToolbarAdapter
class EliminarCalificaciones : AppCompatActivity() {

    private lateinit var listaCalificaciones: ListView
    private lateinit var adapter: CustomListAdapter
    private lateinit var checkBoxSelectAll: CheckBox
    private lateinit var noHayCalificacionesEliminar:TextView
    private val indicesSeleccionados: MutableList<Int> = mutableListOf()
    private var asignaturaSeleccionadaTextView: TextView?=null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_calificaciones)

        asignaturaSeleccionada = intent.getStringExtra("asignaturaSeleccionada")
        asignaturaSeleccionadaTextView=findViewById(R.id.textView87)
        asignaturaSeleccionadaTextView?.text = asignaturaSeleccionada

        botonEliminar = findViewById(R.id.botonEliminarCalificacion)
        btnVolverAVisualizarCalificaciones=findViewById(R.id.volverAVisualizarCalificacionesDesdeEliminar)
        listaCalificaciones = findViewById(R.id.listaCalificaciones)
        checkBoxSelectAll = findViewById(R.id.checkBoxSelectAll)
        noHayCalificacionesEliminar=findViewById(R.id.noCalificacionesEliminar)

        toolbar = findViewById(R.id.toolbar6)
        setSupportActionBar(toolbar)


        customToolbarAdapter = CustomToolbarAdapter(this, toolbar)
        customToolbarAdapter.setTextSizeBasedOnScreenWidth()

        // Inicializar el adapter sin datos
        adapter = CustomListAdapter(this, android.R.layout.simple_list_item_multiple_choice, ArrayList())
        listaCalificaciones.adapter = adapter
        listaCalificaciones.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Llamar a viewSubjectGrades con el valor inicial del Spinner
        if (asignaturaSeleccionada != null) {
            viewSubjectGrades()
        }
        botonEliminar?.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        // Configuración del evento de clic en los elementos de la lista
        listaCalificaciones.setOnItemClickListener { _, _, _, _ ->
            // No es necesario gestionar manualmente los estados de las casillas de verificación
        }

        // Configuración del evento de clic en "Seleccionar Todo"
        checkBoxSelectAll.setOnCheckedChangeListener { _, isChecked ->
            selectAllItems(isChecked)
        }

        btnVolverAVisualizarCalificaciones?.setOnClickListener {
            volverAVisualizarCalificaciones()
        }
    }

    private fun mostrarDialogoConfirmacion() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_custom)

        val btnBorrar = dialog.findViewById<Button>(R.id.btn_exit)
        val btnCancelar = dialog.findViewById<Button>(R.id.btn_cancel)
        val tituloDialogo = dialog.findViewById<TextView>(R.id.text_message)

        tituloDialogo.text = "¿Seguro que quieres eliminar los elementos seleccionados?"
        btnBorrar.text = "Eliminar"
        btnBorrar.setOnClickListener {
            // Lógica para eliminar
            deleteSelectedItems()
            FuncionesComunes.showSnackbarWithCustomTextSize(this,"Calificaciones eliminadas con éxito.")
            dialog.dismiss()
            //volverAVisualizarCalificaciones()
        }

        btnCancelar.setOnClickListener {
            // Lógica para cancelar
            dialog.dismiss()

        }

        dialog.show()
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
                botonEliminar?.isEnabled=false
                checkBoxSelectAll?.isEnabled=false
                adapter.clear()
                adapter.notifyDataSetChanged()
                noHayCalificacionesEliminar.visibility=View.VISIBLE
            }
        }
        Log.d("viewSubjectGrades", "Saliendo de viewSubjectGrades")
    }

    /**
     * Función para eliminar las calificaciones seleccionadas
     */
    private fun deleteSelectedItems() {
        val existentRows = dbCalificaciones?.getSubjectGradesForSubject(asignaturaSeleccionada ?: "")
        if (existentRows != null) {
            Log.d("deleteSelectedItems", "Rows before deletion: ${existentRows.size}")
        }

        // Eliminar los elementos seleccionados
        for (i in 0 until listaCalificaciones.adapter.count) {
            if (listaCalificaciones.isItemChecked(i)) {
                indicesSeleccionados.add(i)
                val deleted = dbCalificaciones?.deleteDataByIndex(indicesSeleccionados,
                    asignaturaSeleccionada)
                if (deleted == true) {
                    Log.d("deleteSelectedItems", "Se eliminó el elemento con éxito ")
                } else {
                    Log.e("deleteSelectedItems", "Error al eliminar")
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
