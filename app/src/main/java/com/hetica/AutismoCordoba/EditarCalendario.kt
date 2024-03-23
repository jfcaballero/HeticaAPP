package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import CustomListAdapter
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
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
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize
import java.text.ParseException
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
 * The Adapter.
 */
var adapterEditarCalendario: ArrayAdapter<String>? = null

var MinutosAsignatura: EditText?=null

var dbCalendario: AdminSQLiteOpenHelperCalendario? = null

var botonCalendario: Button?=null
class EditarCalendario : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_calendario)
        val FechaCalendario = findViewById<View>(R.id.FechaCalendario) as EditText
        dbAsig = AdminSQLiteOpenHelperAsig(this)
        dbCalendario = AdminSQLiteOpenHelperCalendario(this)
        listViewAsignaturasDeUnDia = findViewById(R.id.listViewCalendarioAsignaturas)
        val spinner: Spinner = findViewById(R.id.asignaturasCalendario)
        addAsignaturaCalendario=findViewById(R.id.addAsignaturaCalendario)
        MinutosAsignatura=findViewById(R.id.calendarioMinutosAsignatura)


        botonCalendario=findViewById(R.id.volverCalendarioBoton)
        botonCalendario?.setOnClickListener {
            val intent = Intent(this, AsignaturaDeHoy::class.java)
            startActivity(intent)
        }

        addAsignaturaCalendario?.setOnClickListener {
            val asignaturaSeleccionada = spinner.selectedItem as String
            val minutosAsignatura = MinutosAsignatura?.text.toString().toIntOrNull()

            if (validateDate(yearFinal) && minutosAsignatura != null && minutosAsignatura!=0) {
                addAsignatura(asignaturaSeleccionada, yearFinal!!, minutosAsignatura)
            } else {
                showSnackbarWithCustomTextSize(this, "Introduce una fecha válida y minutos para la asignatura.")
            }
        }
        listViewAsignaturasDeUnDia?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            deleteAsignatura(position, yearFinal!!)
        }




        // Obtener la lista de asignaturas desde la base de datos
        val asignaturasList = dbAsig?.getAsignaturasList()
        if (asignaturasList != null) {
            val adapter = CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, asignaturasList)
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
            val mDatePicker = DatePickerDialog(this@EditarCalendario, { _, selectedYear, selectedMonth, selectedDay ->
                var adjustedMonth = selectedMonth
                Log.e("Date Selected", "Month: $adjustedMonth Day: $selectedDay Year: $selectedYear")
                adjustedMonth = adjustedMonth + 1
                FechaCalendario.setText("$selectedDay/$adjustedMonth/$selectedYear")
                yearFinal = if (adjustedMonth < 10) {
                    "0" + Integer.toString(adjustedMonth)
                } else {
                    Integer.toString(adjustedMonth)
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
     * @param asignaturaSeleccionada La asignatura seleccionada
     * @param dateString Fecha de la asignatura
     *
     */
    private fun addAsignatura(asignaturaSeleccionada: String, dateString: String, minutos: Int) {
        val success = dbCalendario?.insertData(dateString, listOf(asignaturaSeleccionada to minutos))
        if (success == true) {
            // Actualizar la lista de asignaturas mostrada
            viewData(dateString)
            MinutosAsignatura?.text?.clear()
        } else {
            showSnackbarWithCustomTextSize(this, "Ha ocurrido un error al añadir la asignatura")
        }
    }

    /**
     * Función para mostrar las asignaturas del calendario para una fecha
     * @param dateString Fecha de las asignaturas
     *
     */
    private fun viewData(dateString: String) {
        val asignaturasList = dbCalendario?.getAsignaturasForDayWithMinutos(dateString)

        if (!asignaturasList.isNullOrEmpty()) {
            val adapter = CustomListAdapter(this, android.R.layout.simple_list_item_1, asignaturasList.map { "${it.first} - ${it.second} minutos" })
            listViewAsignaturasDeUnDia?.adapter = adapter
            adapterEditarCalendario = adapter
            adapter.notifyDataSetChanged()
        } else {
            listViewAsignaturasDeUnDia?.adapter = CustomListAdapter(this, android.R.layout.simple_list_item_1, emptyList<String>())
        }
    }



    /**
     * Función para gestionar el borrar una asignatura dada la posición en la lista y la fecha
     * @param position Posición de la asignatura en la lista
     * @param dateString Fecha de la asignatura
     *
     */
    private fun deleteAsignatura(position: Int, dateString: String) {
        val success = dbCalendario?.deleteAsignaturaByPosition(position, dateString)
        if (success == true) {
            // Actualizar la lista de asignaturas mostrada
            viewData(dateString)

        } else {
            showSnackbarWithCustomTextSize(this, "Ha ocurrido un error al eliminar la asignatura")
        }
    }

    /**
     * Función para comprobar si una fecha contiene carácteres no aceptados, está vacía y cumple la longitud de 8 carácteres.
     * Después la pasa a formato MMddyyyy
     * @param dateString Fecha a comprobar en formato String
     */
    private fun validateDate(dateString: String?): Boolean {
        if (dateString == null || dateString.isEmpty()) {
            return false
        }
        if (!dateString.matches(Regex("[0-9/]+"))) {
            return false
        }
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
    override fun onBackPressed() {
        // No realizar ninguna acción al presionar el botón de retroceso del dispositivo móvil
        // super.onBackPressed()
    }


}