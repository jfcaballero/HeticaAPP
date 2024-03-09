package com.hetica.AutismoCordoba

import CustomListAdapter
import CustomToolbarAdapter
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import java.util.Calendar



/**
 * The type Estadisticas dias.
 */
class estadisticasDias : AppCompatActivity() {

    var db: AdminSQLiteOpenHelperStats? = null
    var arrayList: ArrayList<String>? = null
    var adapter: CustomListAdapter? = null
    var lv: ListView? = null
    var yearFinal: String? = null
    var monthFinal: String? = null
    var dayFinal: String? = null
    var sumaMinutos: TextView? = null
    var imageMain: ImageView? = null
    var totalMinutos: Double = 0.0
    private lateinit var toolbar: Toolbar

    private lateinit var customToolbarAdapter: CustomToolbarAdapter

    fun setAdminSQLiteOpenHelperStats(db: AdminSQLiteOpenHelperStats) {
        this.db = db
    }

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas_dias)
        val editText = findViewById<View>(R.id.editText2) as EditText
        db = AdminSQLiteOpenHelperStats(this)
        lv = findViewById<View>(R.id.listViewAsignaturas) as ListView
        sumaMinutos = findViewById(R.id.sumaMinutos)
        imageMain=findViewById(R.id.botonMain5)
        GoToMain()

        toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
        customToolbarAdapter = CustomToolbarAdapter(this, toolbar)
        customToolbarAdapter.setTextSizeBasedOnScreenWidth()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationViewestadisticas)
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.selectedItemId = R.id.action_estadisticas

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_estadisticas -> true
                R.id.action_tiempo_dedicado -> {
                    val intent = Intent(this, tiempo_dedicado::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out  // Exit animation
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_actividad -> {
                    val intent = Intent(this, mayormenoractividad::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out  // Exit animation
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_comentarios -> {
                    val intent = Intent(this, MostrarComentarios::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out  // Exit animation
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                R.id.action_calificaciones -> {
                    val intent = Intent(this, VisualizarCalificaciones::class.java)
                    val animationBundle = ActivityOptions.makeCustomAnimation(
                        this,
                        R.anim.slide_in,  // Enter animation
                        R.anim.slide_out  // Exit animation
                    ).toBundle()
                    startActivity(intent, animationBundle)
                    true
                }
                else -> false
            }
        }

        arrayList = ArrayList()
        adapter = CustomListAdapter(this, android.R.layout.simple_list_item_1, arrayList!!)
        lv!!.adapter = adapter

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
        editText.setText("$dayAux/$monthAux/$yearAux")
        viewData()
        editText.setOnClickListener {
            val mcurrentDate2 = Calendar.getInstance()
            val year = mcurrentDate2[Calendar.YEAR]
            val month = mcurrentDate2[Calendar.MONTH]
            val day = mcurrentDate2[Calendar.DAY_OF_MONTH]
            yearFinal = if (month < 10) {
                "0" + Integer.toString(month)
            } else {
                Integer.toString(month)
            }
            if (day < 10) {
                yearFinal = yearFinal + "0"
            }
            yearFinal = yearFinal + Integer.toString(day) + Integer.toString(year)
            val mDatePicker = DatePickerDialog(this@estadisticasDias, { _, selectedYear, selectedMonth, selectedDay ->
                var adjustedMonth = selectedMonth
                Log.e("Date Selected", "Month: $adjustedMonth Day: $selectedDay Year: $selectedYear")
                adjustedMonth = adjustedMonth + 1
                editText.setText("$selectedDay/$adjustedMonth/$selectedYear")
                yearFinal = if (adjustedMonth < 10) {
                    "0" + Integer.toString(adjustedMonth)
                } else {
                    Integer.toString(adjustedMonth)
                }
                if (selectedDay < 10) {
                    yearFinal = yearFinal + "0"
                }
                yearFinal = yearFinal + Integer.toString(selectedDay) + Integer.toString(selectedYear)
                viewData()
            }, year, month, day)
            mDatePicker.setTitle("Select date")
            mDatePicker.show()
        }
    }

    fun GoToMain(){
        imageMain?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("Range")
    private fun viewData() {
        Log.e("Date Selected", yearFinal!!)
        val day = yearFinal!!.substring(0, 2)
        val month = yearFinal!!.substring(2, 4)
        val year = yearFinal!!.substring(4)
        val formattedDate = "$month/$day/$year"
        val cursor = db!!.viewDataDias(formattedDate)
        Log.e("Date Formatted", formattedDate)
        var nodatos: TextView?=findViewById(R.id.textoNoComentarios4)
        if (cursor.count == 0) {
            if (nodatos != null) {
                nodatos.visibility=View.VISIBLE
            }
            adapter!!.clear()
            adapter!!.notifyDataSetChanged()
        } else {
            if (nodatos != null) {
                nodatos.visibility=View.INVISIBLE
            }
            adapter!!.clear()
            adapter!!.notifyDataSetChanged()
            while (cursor.moveToNext()) {
                val asignatura = cursor.getString(1)
                val minutos = cursor.getString(3)
                Log.d("Datos de la base de datos", "Asignatura: $asignatura, Minutos: $minutos")
                arrayList!!.add("$asignatura          $minutos minutos")
                totalMinutos += minutos.toDouble()
            }

            sumaMinutos?.text = "Total: $totalMinutos minutos"
            totalMinutos=0.0
            lv!!.adapter = adapter
        }

    }
}
