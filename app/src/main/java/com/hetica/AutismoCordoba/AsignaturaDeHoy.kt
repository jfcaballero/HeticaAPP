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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hetica.AutismoCordoba.MainActivity
import java.util.ArrayList
import java.util.Calendar


class AsignaturaDeHoy : AppCompatActivity() {

    private var dbCalendario: AdminSQLiteOpenHelperCalendario? = null
    private var calendarioListView: ListView? = null
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

    /**
     * Función para mostrar la lista de asignaturas programadas para el día de hoy
     */
    private fun viewData() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateString = String.format("%02d%02d%d", month, day, year)
        var nohaytareas: TextView?=findViewById(R.id.textoNoHayTareasHoy)
        val asignaturasData = dbCalendario?.getAsignaturasForDayWithMinutos(dateString)
        adapter.clear()

        if (!asignaturasData.isNullOrEmpty()) {
            val displayList = mutableListOf<String>()
            for (asignaturaData in asignaturasData) {
                val displayText = "${asignaturaData.first} - ${asignaturaData.second} minutos - Estudiado: ${if (asignaturaData.third == 1) "Sí" else "No"}"
                displayList.add(displayText)
                if (asignaturaData.third == 1) { // Si la asignatura está estudiada, desactivar la interacción
                    val position = displayList.size - 1
                    adapter.checkedPositions.put(position, true)
                }
            }
            adapter.addAll(displayList)
            if (nohaytareas != null) {
                nohaytareas.visibility=View.INVISIBLE
            }
        } else {
            if (nohaytareas != null) {
                nohaytareas.visibility=View.VISIBLE
            }
            calendarioListView?.adapter = null
        }
        adapter.notifyDataSetChanged()
        Log.d("Asignaturas", asignaturasData.toString())
    }





    private fun comenzarSesion() {
        ComenzarSesion?.setOnClickListener {
            val position = adapter.lastCheckedPosition
            if (position != -1) {
                val asignaturaData = adapter.getItem(position)
                val nombreAsignatura = asignaturaData?.substringBefore(" - ")
                val minutosIndex = asignaturaData?.indexOf("minutos") ?: -1

                if (minutosIndex != -1 && nombreAsignatura != null) {
                    val minutosString = asignaturaData.substring(minutosIndex - 3, minutosIndex).trim()
                    val minutos = minutosString.toIntOrNull()

                    if (minutos != null && minutos != 0) {
                        if (asignaturaData.endsWith("Sí")) {
                            showToast("Esta asignatura ya ha sido estudiada")
                        } else {
                            val intent = Intent(this, TimerSimple::class.java).apply {
                                putExtra("time", minutos.toString())
                                putExtra("asig", nombreAsignatura)
                                putExtra("actAsig", "1")
                                putExtra("numAsig", "1")
                            }

                            startActivity(intent)
                            dbCalendario?.marcarComoEstudiado(position, getDateAsString())
                        }
                    } else {
                        showToast("Los datos de la asignatura son inválidos")
                    }
                } else {
                    showToast("Los datos de la asignatura son inválidos")
                }
            } else {
                showToast("Selecciona un único elemento")
            }
        }
    }





    private fun getDateAsString(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format("%02d%02d%d", month, day, year)
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