package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import android.content.Intent
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.hetica.AutismoCordoba.CalendarioArrayAdapter
import com.hetica.AutismoCordoba.EditarCalendario
import com.hetica.AutismoCordoba.MainActivity
import com.hetica.AutismoCordoba.R
import java.util.Calendar

class AsignaturaDeHoy : AppCompatActivity() {

    private var dbCalendario: AdminSQLiteOpenHelperCalendario? = null
    private var calendarioListView: ListView? = null
    private var asignaturasList: MutableList<Pair<String, Int>>? = null
    private var adapterCalendario: CalendarioArrayAdapter? = null
    private var editarCalendario: ImageView? = null
    private var isLongPressFired = false
    private var ComenzarSesion: Button? = null
    private var SalirCalendario: Button? = null
    private var estadoMarcado: SparseBooleanArray = SparseBooleanArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignatura_de_hoy)

        dbCalendario = AdminSQLiteOpenHelperCalendario(this)
        calendarioListView = findViewById(R.id.asignaturasdehoy)
        ComenzarSesion = findViewById(R.id.asignaturaDeHoyComenzar)
        SalirCalendario = findViewById(R.id.asignaturaDeHoySalir)

        viewData()
        pasarEditarCalendario()
        salir()
        comenzarSesion()
    }

    private fun comenzarSesion() {
        ComenzarSesion?.setOnClickListener {
            // Obtener el estado de los elementos marcados directamente desde el adaptador
            val checkedItems = adapterCalendario?.getCheckedItems()

            // Realizar acciones correspondientes a los elementos marcados (si es necesario)
            // ...

            // Iniciar la actividad correspondiente al botón "Comenzar"
            val intent = Intent(this@AsignaturaDeHoy, MainActivity::class.java)
            startActivity(intent)
        }
    }




    private fun salir() {
        SalirCalendario?.setOnClickListener {
            // Inicia la actividad correspondiente al botón "Salir"
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
        editarCalendario = findViewById(R.id.editarCalendarioBoton)
        editarCalendario?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                isLongPressFired = false
            }
            true
        }
    }

    private fun viewData() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateString = String.format("%02d%02d%d", month, day, year)

        asignaturasList = dbCalendario?.getAsignaturasForDayWithMinutos(dateString) as MutableList<Pair<String, Int>>?

        val displayList = asignaturasList?.map { "${it.first} - ${it.second} minutos" }?.toMutableList()

        if (!displayList.isNullOrEmpty()) {
            val adapter = CalendarioArrayAdapter(this, R.layout.list_item_checkbox_calendario, displayList)
            calendarioListView?.adapter = adapter
            adapterCalendario = adapter

            // Restaurar el estado marcado
            for (i in 0 until (adapterCalendario?.count ?: 0)) {
                calendarioListView?.setItemChecked(i, estadoMarcado.get(i))
            }

        } else {
            calendarioListView?.adapter = ArrayAdapter<String>(this, R.layout.list_item_checkbox_calendario, emptyList<String>())
        }
    }

    private fun guardarEstadoMarcado() {
        for (i in 0 until (adapterCalendario?.count ?: 0)) {
            estadoMarcado.put(i, calendarioListView?.isItemChecked(i) ?: false)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        guardarEstadoMarcado()
    }

    override fun onResume() {
        super.onResume()
        viewData()
    }
}
