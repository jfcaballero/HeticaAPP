package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * The Db.
 */
var dbCalendario: AdminSQLiteOpenHelperCalendario? = null
/**
 * The Lv.
 */
@SuppressLint("StaticFieldLeak")
var calendarioListView: ListView? = null

/**
 * La lista de asignaturas
 */
var asignaturasList: MutableList<String>? = null
/**
 * The Adapter.
 */
var adapterCalendario: ArrayAdapter<String>? = null


class AsignaturaDeHoy : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignatura_de_hoy)

        dbCalendario = AdminSQLiteOpenHelperCalendario(this, "Calendario.db", null, 1)
        calendarioListView = findViewById(R.id.asignaturasdehoy)

        viewData()


    }

    /**
     * Función que devuelve las asignaturas de hoy
     *
     */


// ...

    private fun viewData() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Suma 1 ya que enero es 0
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateString = String.format("%02d%02d%d", month, day, year)
        val dateFormat = SimpleDateFormat("MMddyyyy", Locale.getDefault())
        //dbCalendario?.borrarTabla()

        asignaturasList = dbCalendario?.getAsignaturasForDay(dateString) as MutableList<String>?
        if (!asignaturasList.isNullOrEmpty()) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, asignaturasList!!)
            calendarioListView?.adapter = adapter
            adapterCalendario = adapter
            adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "No hay asignaturas para hoy", Toast.LENGTH_LONG).show()
        }

        /*
        val asignaturasInsertar = listOf("lengua", "mates")
        val success = dbCalendario?.insertData(dateString, asignaturasInsertar)

        if (success == true) {
            Toast.makeText(this, "Se ha insertado correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Ha ocurrido un error al insertar", Toast.LENGTH_SHORT).show()
        }*/

    }
}



