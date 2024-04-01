package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalificaciones
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize
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

private var btnVolverAVisualizarCalificaciones: Button?=null

private var asignaturaSeleccionadaTextView: TextView?=null

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
        btnVolverAVisualizarCalificaciones=findViewById(R.id.volverAVisualizarCalificaciones)
        //val asignaturasList = dbAsig?.getAsignaturasList()
        val tipoExamenList = listOf("Parcial", "Final")
        val spinnerTipos: Spinner = binding.spinnerAddTipo
        val Nota: EditText = binding.AddNota
        val Guardar: Button = binding.buttonAddCalif

        val FechaEditText: EditText = binding.addCalificacionFecha
        asignaturaSeleccionada = intent.getStringExtra("asignaturaSeleccionada")
        asignaturaSeleccionadaTextView=findViewById(R.id.asignaturaCalificacionTextView)
        asignaturaSeleccionadaTextView?.setText(asignaturaSeleccionada)

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

        //Spinner de tipo de examen
        val adapterAddTipos = CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, tipoExamenList)
        adapterAddTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipos.adapter = adapterAddTipos
        spinnerTipos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                tipoSeleccionado = spinnerTipos.selectedItem.toString() // Asignar valor a la variable global
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se realiza ninguna acción si no se selecciona nada
            }
        }


        Guardar.setOnClickListener {
            val notaString = Nota.text.toString()
            var toastMessage=""
            if (validateMark(Nota)) {
                if(validateDate(yearFinal)){
                    if (asignaturaSeleccionada != null && tipoSeleccionado != null) {
                        val isInserted = dbCalificaciones?.insertData(
                            asignaturaSeleccionada,
                            notaString,
                            tipoSeleccionado,
                            yearFinal!!
                        )
                        if (isInserted == true) {
                            Guardar.isEnabled=false
                            toastMessage = "Calificación insertada."
                            VolverAVisualizarCalificaciones()
                        } else {
                            toastMessage = "No se pudo insertar la calificación."
                        }
                    }
                }else{
                    toastMessage ="Introduce una fecha en formato dd/MM/yyyy."

                }

            } else {
                toastMessage ="Introduce una nota válida."
            }
            showSnackbarWithCustomTextSize(this, toastMessage)
        }
        btnVolverAVisualizarCalificaciones?.setOnClickListener {
            VolverAVisualizarCalificaciones()
        }



    }

    private fun validateMark(Nota: EditText?):Boolean{
        val notaString = Nota?.text.toString()
        val notaFloat= Nota?.text.toString().toFloat()


        if( !(notaString.isNullOrEmpty()) && notaFloat<=100 && notaFloat>=0
            && notaString.length <= 5) {

            return true
        }else{

        }
        return false

    }

    /**
     * Función para comprobar si una fecha contiene carácteres no aceptados, está vacía y cumple la longitud de 8 caracteres.
     * Después la pasa a formato MMddyyyy
     * @param dateString Fecha a comprobar en formato String
     */
    private fun validateDate(dateString: String?): Boolean {
        Log.d("Como esta la fecha","$dateString")
        if (dateString.isNullOrEmpty()) {
            return false
        }
        if (!dateString.matches(Regex("[0-9/]+"))) {
            return false
        }
        if (dateString.length != 8) {
            return false
        }

        try {
            // comprobar si la fecha está en MMddyyyy
            val format = SimpleDateFormat("MMddyyyy", Locale.getDefault())
            format.isLenient = false
            format.parse(dateString)
            return true
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }
    }

    private fun VolverAVisualizarCalificaciones(){
        val intent = Intent(this, VisualizarCalificaciones::class.java)
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            startActivity(intent)
        }, 500)

    }
}