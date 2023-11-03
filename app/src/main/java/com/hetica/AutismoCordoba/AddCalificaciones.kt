package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalificaciones
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.hetica.AutismoCordoba.databinding.ActivityAddCalificacionesBinding
import com.hetica.AutismoCordoba.databinding.ActivityVisualizarCalificacionesBinding

/**
 * La asignatura seleccionada.
 */
private var asignaturaSeleccionada: String? = null
/**
 * El tipo de examen seleccionado.
 */
private var tipoSeleccionado: String? = null

/**
 * La nota de examen que el usuario haya escrito.
 */
private var NotaFloat: Float? = null

@SuppressLint("StaticFieldLeak")
private var _binding: ActivityAddCalificacionesBinding? = null
private val binding get() = _binding!!
class AddCalificaciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddCalificacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbAsig = AdminSQLiteOpenHelperAsig(this, "Asig.db", null, 1)
        dbCalificaciones = AdminSQLiteOpenHelperCalificaciones(this, "Calificaciones.db", null, 2)

        val asignaturasList = dbAsig?.getAsignaturasList()
        val tipoExamenList = listOf("Parcial", "Final")
        val spinnerAsignaturas: Spinner = binding.spinnerAddAsignatura
        val spinnerTipos: Spinner = binding.spinnerAddTipo
        val Nota: EditText = binding.AddNota
        val Guardar: Button = binding.buttonAddCalif

        //Spinner de asignaturas
        if (asignaturasList != null) {
            val adapterAddAsig = ArrayAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
            adapterAddAsig.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAsignaturas.adapter = adapterAddAsig

            spinnerAsignaturas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    asignaturaSeleccionada = spinnerAsignaturas.selectedItem.toString() // Asignar valor a la variable global
                    tipoSeleccionado = spinnerTipos.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se realiza ninguna acción si no se selecciona nada
                }
            }

        }
        //Spinner de tipo de examen
        val adapterAddTipos = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoExamenList)
        adapterAddTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipos.adapter = adapterAddTipos
        spinnerTipos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                tipoSeleccionado = spinnerTipos.selectedItem.toString() // Asignar valor a la variable global
                asignaturaSeleccionada = spinnerAsignaturas.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se realiza ninguna acción si no se selecciona nada
            }
        }

        Guardar.setOnClickListener {
            val notaString = Nota.text.toString()
            if (notaString.isNotEmpty()) {
                NotaFloat = notaString.toFloat()
                if (asignaturaSeleccionada != null && tipoSeleccionado != null && NotaFloat != null) {
                    val isInserted = dbCalificaciones?.insertData(asignaturaSeleccionada, NotaFloat!!, tipoSeleccionado)
                    if (isInserted == true) {
                        val toastMessage = "Calificación insertada: $asignaturaSeleccionada, Nota: $NotaFloat, Tipo: $tipoSeleccionado"
                        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No se pudo insertar la calificación.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Introduce una nota antes de guardar.", Toast.LENGTH_SHORT).show()
            }
        }



    }
}