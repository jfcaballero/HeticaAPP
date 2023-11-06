package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.ImageView
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
/**
 * The Adapter.
 */
@SuppressLint("StaticFieldLeak")
var editarCalendario: ImageView? = null

private var isLongPressFired = false


class AsignaturaDeHoy : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignatura_de_hoy)

        dbCalendario = AdminSQLiteOpenHelperCalendario(this, "Calendario.db", null, 1)
        calendarioListView = findViewById(R.id.asignaturasdehoy)


        viewData()
        pasarEditarCalendario()


    }
    /**
     * Función para pasar a la actividad EditarCalendario
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun pasarEditarCalendario() {
        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                if (isLongPressFired) {
                    return
                }
                isLongPressFired = true

                val intent = Intent(this@AsignaturaDeHoy, EditarCalendario::class.java)
                startActivity(intent)
            }
        })
        editarCalendario=findViewById(R.id.editarCalendarioBoton)
        editarCalendario?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                isLongPressFired = false
            }
            true
        }
    }



    /**
     * Función que devuelve las asignaturas de hoy
     *
     */

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
            val adapter = ArrayAdapter(this, R.layout.list_item_layout, asignaturasList!!)
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
    override fun onResume() {
        super.onResume()
        viewData()
    }
}



