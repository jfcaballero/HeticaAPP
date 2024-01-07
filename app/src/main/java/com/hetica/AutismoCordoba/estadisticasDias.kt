package com.hetica.AutismoCordoba

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import java.util.Calendar
import kotlin.math.roundToInt

/**
 * The type Estadisticas dias.
 */
class estadisticasDias : AppCompatActivity() {
    /**
     * The Db.
     */
    var db: AdminSQLiteOpenHelperStats? = null

    /**
     * The Array list.
     */
    var arrayList: ArrayList<String>? = null

    /**
     * The Adapter.
     */
    var adapter: ArrayAdapter<String>? = null

    /**
     * The Lv.
     */
    var lv: ListView? = null

    /**
     * The Year final.
     */
    var yearFinal: String? = null

    /**
     * The Month final.
     */
    var monthFinal: String? = null

    /**
     * The Day final.
     */
    var dayFinal: String? = null
    /**
     * Promedio de minutos
     */
    var  promedioMinutos: TextView?=null
    /**
     * Imagen para irnos al Main
     */
    var imageMain: ImageView?=null


    // Método para configurar la instancia de AdminSQLiteOpenHelperStats
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
        promedioMinutos = findViewById(R.id.promedioMinutos)
        imageMain=findViewById(R.id.botonMain5)
        GoToMain()


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationViewestadisticas)
        bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO
        bottomNavigation.selectedItemId = R.id.action_estadisticas

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_estadisticas -> {
                    true
                }
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
        if (resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            object : ArrayAdapter<String>(this@estadisticasDias, android.R.layout.simple_list_item_1, arrayList!!) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    /// Get the Item from ListView
                    val view = super.getView(position, convertView, parent)
                    val tv = view.findViewById<View>(android.R.id.text1) as TextView

                    // Set the text size 25 dip for ListView each item
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35f)

                    // Return the view
                    return view
                }
            }
        } else {
            adapter = ArrayAdapter(this@estadisticasDias, android.R.layout.simple_list_item_1, arrayList!!)
        }
        lv!!.adapter = adapter

        val mcurrentDate = Calendar.getInstance()
        val yearAux = mcurrentDate[Calendar.YEAR]
        var monthAux = mcurrentDate[Calendar.MONTH]
        val dayAux = mcurrentDate[Calendar.DAY_OF_MONTH]
        monthAux = monthAux + 1

        //para establecer el promedio de actividad de una asignatura
        lv!!.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val item = lv!!.getItemAtPosition(position) as String
            val asignatura = item.split("\\s+".toRegex())[0] // Obtener el nombre de la asignatura desde el item de la lista
            val promedio = db?.calcularPromedioAsignatura(asignatura) // Calcular el promedio para la asignatura utilizando la instancia de AdminSQLiteOpenHelperStats
            val promedioRedondeado = (promedio?.toFloat()?.times(100))?.roundToInt()?.div(100.0)// Redondear a dos decimales

            promedioMinutos!!.text = "$promedioRedondeado minutos" // Actualizar el TextView con el resultado del promedio
        }

        //yearFinal = Integer.toString(monthAux) + Integer.toString(dayAux) + Integer.toString(yearAux);
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
        editText.setOnClickListener { // TODO Auto-generated method stub
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
            val mDatePicker = DatePickerDialog(this@estadisticasDias, { _, selectedYear, selectedMonth, selectedDay -> // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
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
    /**
     * Función para irnos al Main
     *
     */
    fun GoToMain(){
        imageMain?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    /**
     * Función que devuelve las estadísticas de una fecha en concreto
     *
     */
    @SuppressLint("Range")
    private fun viewData() {
        Log.e("Date Selected", yearFinal!!)

        //esto es para switchear el mes y el dia porque estan al reves para hacer la consulta
        val day = yearFinal!!.substring(0, 2)
        val month = yearFinal!!.substring(2, 4)
        val year = yearFinal!!.substring(4)

        val formattedDate = "$month/$day/$year"

        val cursor = db!!.viewDataDias(formattedDate)
        Log.e("Date Formatted", formattedDate)

        if (cursor.count == 0) {
            Toast.makeText(this, "No se trabajó este día", Toast.LENGTH_LONG).show()
            adapter!!.clear()
            adapter!!.notifyDataSetChanged()
        } else {
            adapter!!.clear()
            adapter!!.notifyDataSetChanged()
            while (cursor.moveToNext()) {
                arrayList!!.add(cursor.getString(1) + "          " + cursor.getString(3) + " minutos")
                val fecha = cursor.getString(cursor.getColumnIndex("DATE"))
                Log.d("Fecha","$fecha")
            }
            if (resources.configuration.screenLayout and
                    Configuration.SCREENLAYOUT_SIZE_MASK ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                object : ArrayAdapter<String>(this@estadisticasDias, android.R.layout.simple_list_item_1, arrayList!!) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        /// Get the Item from ListView
                        val view = super.getView(position, convertView, parent)
                        val tv = view.findViewById<View>(android.R.id.text1) as TextView

                        // Set the text size 25 dip for ListView each item
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35f)

                        // Return the view
                        return view
                    }
                }
            } else {
                adapter = ArrayAdapter(this@estadisticasDias, android.R.layout.simple_list_item_1, arrayList!!)
            }
            lv!!.adapter = adapter
        }
    }
}