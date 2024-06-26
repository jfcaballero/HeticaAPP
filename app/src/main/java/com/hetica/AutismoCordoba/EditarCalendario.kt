package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import CustomListAdapter
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
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
    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingInflatedId")
    private var limpiarCalendario:Button?=null

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
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

        addAsignaturaCalendario?.setOnClickListener {
            val asignaturaSeleccionada = spinner.selectedItem as String
            val minutosAsignatura = MinutosAsignatura?.text.toString().toIntOrNull()
            if(!(validateDate(yearFinal) && minutosAsignatura!=0 &&minutosAsignatura!=null)){
                showSnackbarWithCustomTextSize(this, "Introduce una fecha y minutos.")
            }
            else if(minutosAsignatura.toInt()>=240){
                showSnackbarWithCustomTextSize(this, "El tiempo es demasiado largo.")
            }
            else {
                addAsignatura(asignaturaSeleccionada, yearFinal!!, minutosAsignatura)
            }
        }
        listViewAsignaturasDeUnDia?.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Obtiene el objeto Asignatura en la posición seleccionada
            val asignaturaSeleccionada = listViewAsignaturasDeUnDia?.getItemAtPosition(position) as String

            // Encuentra la última ocurrencia del guion "-"
            val lastDashIndex = asignaturaSeleccionada.lastIndexOf("-")

            // Si se encuentra el guion, dividimos la cadena desde esa posición, de lo contrario, usamos el nombre completo
            val nombreAsignatura = asignaturaSeleccionada.substring(0, lastDashIndex).trim()

            mostrarDialogoBorrar(position, nombreAsignatura)
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
            mDatePicker.datePicker.firstDayOfWeek = Calendar.MONDAY
            mDatePicker.show()
        }
        limpiarCalendario=findViewById(R.id.limpiarCalendario)
        limpiar()
        //val dateString = FechaCalendario?.text.toString()
        viewData(yearFinal!!)



    }
    private fun limpiar(){
        limpiarCalendario?.setOnClickListener {
            mostrarDialogoBorrar(-1,"-1")

        }
    }

    private fun mostrarDialogoBorrar(position:Int,asignatura:String) {
        val dialog = Dialog(this@EditarCalendario)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_custom)

        val btnBorrar = dialog.findViewById<Button>(R.id.btn_exit)
        val btnCancelar = dialog.findViewById<Button>(R.id.btn_cancel)
        val tituloDialogo = dialog.findViewById<TextView>(R.id.text_message)

        btnBorrar.text = "Eliminar"
        val fechaOriginal = yearFinal!!
        val formatoOriginal = SimpleDateFormat("MMddyyyy")
        val fecha = formatoOriginal.parse(fechaOriginal)

        val formatoNuevo = SimpleDateFormat("dd/MM/yyyy")
        val dia = fecha?.let { formatoNuevo.format(it) }
        if(position==-1){

            tituloDialogo.text = "¿Quieres eliminar todas las asignaturas del día $dia?"
            btnBorrar.setOnClickListener {
                dbCalendario?.limpiar(yearFinal!!)
                viewData(yearFinal!!)
                dialog.dismiss()
            }
        }else{
            tituloDialogo.text = "¿Quieres eliminar la asignatura $asignatura de la planificación del día $dia?"
            btnBorrar.setOnClickListener {
                deleteAsignatura(position, yearFinal!!)
                dialog.dismiss()
            }
        }

        btnCancelar.setOnClickListener {
            // Lógica para cancelar
            dialog.dismiss()

        }

        dialog.show()
    }
    /**
     * Función para añadir una asignatura dado el nombre y la fecha
     * @param asignaturaSeleccionada La asignatura seleccionada
     * @param dateString Fecha de la asignatura
     *
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun addAsignatura(asignaturaSeleccionada: String, dateString: String, minutos: Int) {
        val success = dbCalendario?.insertData(dateString, listOf(asignaturaSeleccionada to minutos))
        if (success == true) {
            // Actualizar la lista de asignaturas mostrada
            viewData(dateString)
            MinutosAsignatura?.text?.clear()
        } else {
            showSnackbarWithCustomTextSize(this, "Error al añadir la asignatura.")
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
            limpiarCalendario?.isEnabled=true
        } else {
            listViewAsignaturasDeUnDia?.adapter = CustomListAdapter(this, android.R.layout.simple_list_item_1, emptyList<String>())
            limpiarCalendario?.isEnabled=false

        }
    }



    /**
     * Función para gestionar el borrar una asignatura dada la posición en la lista y la fecha
     * @param position Posición de la asignatura en la lista
     * @param dateString Fecha de la asignatura
     *
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun deleteAsignatura(position: Int, dateString: String) {
        val success = dbCalendario?.deleteAsignaturaByPosition(position, dateString)
        if (success == true) {
            // Actualizar la lista de asignaturas mostrada
            viewData(dateString)

        } else {
            showSnackbarWithCustomTextSize(this, "Error al eliminar la asignatura.")
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
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@EditarCalendario, AsignaturaDeHoy::class.java)
            startActivity(intent)
        }
    }



}