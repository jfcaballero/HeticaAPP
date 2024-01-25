package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hetica.AutismoCordoba.MainActivity
import java.util.ArrayList
import java.util.Calendar


class AsignaturaDeHoy : AppCompatActivity() {

    private var dbCalendario: AdminSQLiteOpenHelperCalendario? = null
    private var calendarioListView: ListView? = null
    private var asignaturasList: MutableList<Pair<String, Int>>? = null
    private var isLongPressFired = false
    private var ComenzarSesion: Button? = null
    private var SalirCalendario: Button? = null
    private lateinit var adapter: CalendarioArrayAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignatura_de_hoy)

        dbCalendario = AdminSQLiteOpenHelperCalendario(this)
        calendarioListView = findViewById(R.id.asignaturasdehoy)
        ComenzarSesion = findViewById(R.id.asignaturaDeHoyComenzar)
        SalirCalendario = findViewById(R.id.asignaturaDeHoySalir)




        // Configurar el adaptador personalizado
        adapter = CalendarioArrayAdapter(this, R.layout.list_item_checkbox, ArrayList())
        calendarioListView?.adapter = adapter
        calendarioListView?.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Manejar eventos de clic en elementos de la lista
        calendarioListView?.setOnItemClickListener { _, _, position, _ ->
            val isChecked = !calendarioListView?.isItemChecked(position)!!
            Log.d("ItemClicked", "Position: $position, Checked: $isChecked")
            calendarioListView?.setItemChecked(position, isChecked)
            val checkedItemPositions = calendarioListView?.checkedItemPositions
            Log.d("Num items:", checkedItemPositions?.size().toString())
        }
        salir()
        viewData()
        pasarEditarCalendario()
        comenzarSesion()
    }
    private fun viewData() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateString = String.format("%02d%02d%d", month, day, year)

        asignaturasList = dbCalendario?.getAsignaturasForDayWithMinutos(dateString) as MutableList<Pair<String, Int>>?
        adapter.clear()
        if (!asignaturasList.isNullOrEmpty()) {
            val displayList = asignaturasList?.map { "${it.first} - ${it.second} minutos" }?.toMutableList() ?: mutableListOf()
            adapter = CalendarioArrayAdapter(this, R.layout.list_item_checkbox, displayList)
            calendarioListView?.adapter = adapter
        } else {
            calendarioListView?.adapter = null
        }
        adapter.notifyDataSetChanged() // Notificar al adaptador de los cambios
        Log.d("Asignaturas", asignaturasList.toString())
    }



    private fun comenzarSesion() {
        ComenzarSesion?.setOnClickListener {
            Log.d("lastcheckpos", adapter.lastCheckedPosition.toString())
            val position = adapter.lastCheckedPosition
            if (position != -1) {

                val intent = Intent(this, TimerSimple::class.java).apply {
                    Log.d("Minutos:", asignaturasList?.get(position)?.second.toString())
                    putExtra("time", asignaturasList?.get(position)?.second.toString())  // Tiempo en minutos como una cadena
                    putExtra("asig", asignaturasList?.get(position)?.first)  // Nombre de la asignatura
                    putExtra("actAsig", "1")  // Actividad de la asignatura actual
                    putExtra("numAsig", "1")  // Número total de asignaturas (1 siempre)
                }
                startActivity(intent)

                startActivity(intent)
            } else {
                showToast("Selecciona un único elemento")
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun salir() {
        SalirCalendario?.setOnClickListener {
            val intent = Intent(this@AsignaturaDeHoy, MainActivity::class.java)
            startActivity(intent)
        }
    }

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
        val editarCalendario = findViewById<ImageView>(R.id.editarCalendarioBoton)
        editarCalendario?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                isLongPressFired = false
            }
            true
        }
    }




    override fun onResume() {
        super.onResume()
        viewData()

    }


}