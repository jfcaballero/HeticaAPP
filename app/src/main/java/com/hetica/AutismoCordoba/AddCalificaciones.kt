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
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.hetica.AutismoCordoba.databinding.ActivityAddCalificacionesBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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

        dbAsig = AdminSQLiteOpenHelperAsig(this)
        dbCalificaciones = AdminSQLiteOpenHelperCalificaciones(this, null, 3)

        val asignaturasList = dbAsig?.getAsignaturasList()
        val tipoExamenList = listOf("Parcial", "Final")
        val spinnerAsignaturas: Spinner = binding.spinnerAddAsignatura
        val spinnerTipos: Spinner = binding.spinnerAddTipo
        val Nota: EditText = binding.AddNota
        val Guardar: Button = binding.buttonAddCalif
        val FechaEditText: EditText = binding.addCalificacionFecha

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
        FechaEditText.setText("$dayAux/$monthAux/$yearAux")

        FechaEditText.setOnClickListener { // TODO Auto-generated method stub
            //To show current date in the datepicker
            val mcurrentDate2 = Calendar.getInstance()
            val year = mcurrentDate2[Calendar.YEAR]
            val month = mcurrentDate2[Calendar.MONTH]
            val day = mcurrentDate2[Calendar.DAY_OF_MONTH]
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
            val mDatePicker = DatePickerDialog(this@AddCalificaciones, { _, selectedYear, selectedMonth, selectedDay ->
                var adjustedMonth  = selectedMonth
                Log.e("Date Selected", "Month: $adjustedMonth  Day: $selectedDay Year: $selectedYear")
                adjustedMonth  = adjustedMonth  + 1
                FechaEditText.setText("$selectedDay/$adjustedMonth /$selectedYear")
                yearFinal = if (adjustedMonth  < 10) {
                    "0" + Integer.toString(adjustedMonth )
                } else {
                    Integer.toString(adjustedMonth )
                }
                if (selectedDay < 10) {
                    yearFinal = yearFinal + "0"
                }
                yearFinal = yearFinal + Integer.toString(selectedDay) + Integer.toString(selectedYear)

            }, year, month, day)
            mDatePicker.setTitle("Select date")
            mDatePicker.show()
        }

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
            val fechaString = yearFinal

            if (notaString.isNotEmpty()) {
                if(validateDate(yearFinal)){
                    NotaFloat = notaString.toFloat()
                    if (asignaturaSeleccionada != null && tipoSeleccionado != null) {
                        val isInserted = dbCalificaciones?.insertData(
                            asignaturaSeleccionada,
                            NotaFloat!!.toString(),
                            tipoSeleccionado,
                            yearFinal!!
                        )
                        Log.d("Fecha", "$yearFinal")
                        if (isInserted == true) {
                            val toastMessage =
                                "Calificación insertada: $asignaturaSeleccionada, Nota: $NotaFloat, Tipo: $tipoSeleccionado"
                            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "No se pudo insertar la calificación.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Introduce una fecha en formato dd/MM/yyyy.", Toast.LENGTH_SHORT).show()

                }

            } else {
                Toast.makeText(this, "Introduce una nota válida antes de guardar.", Toast.LENGTH_SHORT).show()
            }
        }



    }
    private fun validateDate(dateString: String?): Boolean {
        if (dateString == null || dateString.isEmpty()) {
            return false
        }

        // Verificar que la longitud de la cadena sea la esperada "dd/MM/yyyy"
        if (dateString.length != 8) {
            return false
        }

        try {
            // Intentar parsear la fecha
            val format = SimpleDateFormat("MMddyyyy", Locale.getDefault())
            format.isLenient = false
            format.parse(dateString)
            return true
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }
    }
}