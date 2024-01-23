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

        dbCalendario = AdminSQLiteOpenHelperCalendario(this)
        calendarioListView = findViewById(R.id.asignaturasdehoy)


        viewData()
        pasarEditarCalendario()


    }
    /**
     * Función para pasar a la actividad EditarCalendario
     * @return boolean
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

        val asignaturasListWithMinutos = dbCalendario?.getAsignaturasForDayWithMinutos(dateString) as MutableList<Pair<String, Int>>?

        val displayList = asignaturasListWithMinutos?.map { "${it.first} - ${it.second} minutos" }?.toMutableList()

        if (!displayList.isNullOrEmpty()) {
            val adapter = CalendarioArrayAdapter(this, R.layout.list_item_checkbox_calendario, displayList)
            calendarioListView?.adapter = adapter
            adapterCalendario = adapter
        } else {
            calendarioListView?.adapter = ArrayAdapter<String>(this, R.layout.list_item_checkbox_calendario, emptyList<String>())
        }
    }




    override fun onResume() {
        super.onResume()
        viewData()
    }
}



