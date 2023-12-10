package com.hetica.AutismoCordoba

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import AdminSQLiteOpenHelperComentarios
import android.annotation.SuppressLint
import android.widget.ArrayAdapter
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date

var dbAsignaturas: AdminSQLiteOpenHelperAsig? = null
var dbComents: AdminSQLiteOpenHelperComentarios? = null
@SuppressLint("Instantiatable")
 class EnviarComentarios  : DialogFragment()  {
    private lateinit var spinner: Spinner
    private lateinit var editText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.enviar_comentarios, container, false)

        spinner = view.findViewById(R.id.spinnerAsignaturaComentarios)
        editText = view.findViewById(R.id.comentariosTexto)
        dbAsignaturas = AdminSQLiteOpenHelperAsig(requireContext())
        // Obtener la lista de asignaturas desde la base de datos
        val asignaturasList = dbAsignaturas?.getAsignaturasList() // Obtén la lista de asignaturas desde la base de datos
        if (!asignaturasList.isNullOrEmpty()) {
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, asignaturasList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        } else {
            Toast.makeText(requireContext(), "La lista de asignaturas está vacía", Toast.LENGTH_SHORT).show()
        }


        val btnEnviar: Button = view.findViewById(R.id.registrarComentarios)
        btnEnviar.setOnClickListener {
            enviarDatos()
        }

        return view
    }
    /**
     * Función para insertar comentarios en la base de datos
     *
     */
    private fun enviarDatos() {
        val asignaturaSeleccionada = spinner.selectedItem.toString()
        val mensaje = editText.text.toString()
        dbComents= AdminSQLiteOpenHelperComentarios(requireContext())

        val sdf = SimpleDateFormat("Mddyyyy")
        val fechadehoy = sdf.format(Date())
        val comentarioAgregado = dbComents?.insertData(fechadehoy, asignaturaSeleccionada, mensaje)
        dismiss()
    }

}