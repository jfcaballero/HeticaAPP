package com.hetica.AutismoCordoba

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Tiempo estudio.
 */
class tiempoEstudio : AppCompatActivity() {
    /**
     * The Lv.
     */
    var lv: ListView? = null

    /**
     * The Array list.
     */
    var arrayList: ArrayList<String>? = null

    /**
     * The Adapter.
     */
    var adapter: ArrayAdapter<String>? = null

    /**
     * The Db.
     */
    var db: AdminSQLiteOpenHelperAsig? = null

    /**
     * The Text view 3.
     */
    var textView3: TextView? = null

    /**
     * The Bundle.
     */
    var bundle: Bundle? = null

    /**
     * The Edit text.
     */
    var editText: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiempo_estudio)
        lv = findViewById<View>(R.id.listView1) as ListView
        arrayList = ArrayList()
        db = AdminSQLiteOpenHelperAsig(this)
        textView3 = findViewById<View>(R.id.textView55) as TextView
        editText = findViewById<View>(R.id.editText4) as EditText
        viewData()
        lv!!.onItemClickListener = OnItemClickListener { parent, view, position, id -> textView3!!.text = arrayList!![position] }
    }

    /**
     * Función que pasa los datos necesarios al temporizador
     *
     * @param view the view
     */
    fun pasarOrgEst(view: View?) {
        bundle = Bundle()
        val siguiente = Intent(this, temporizadorUnico::class.java)
        if (editText!!.text.toString() == "") {
            Toast.makeText(this, "Introduce un tiempo", Toast.LENGTH_LONG).show()
        } else {
            if (textView3!!.text.toString() == "" || textView3!!.text.toString() == "Clica una asignatura") {
                Toast.makeText(this, "Selecciona una asignatura", Toast.LENGTH_LONG).show()
            } else {
                bundle!!.putString("asig", textView3!!.text.toString())
                bundle!!.putString("time", editText!!.text.toString())
                siguiente.putExtras(bundle!!)
                startActivity(siguiente)
            }
        }
    }

    /**
     * Función que devuelve las asignaturas de la BBDD y las muestra en pantalla
     *
     */
    private fun viewData() {
        val cursor = db!!.viewData()
        if (cursor!!.count == 0) {
            Toast.makeText(this, "No hay ninguna asignatura", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                arrayList!!.add(cursor.getString(1))
            }
            if (resources.configuration.screenLayout and
                    Configuration.SCREENLAYOUT_SIZE_MASK ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                object : ArrayAdapter<String>(this@tiempoEstudio, android.R.layout.simple_list_item_1, arrayList!!) {
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
                adapter = ArrayAdapter(this@tiempoEstudio, android.R.layout.simple_list_item_1, arrayList!!)
            }
            lv!!.adapter = adapter
        }
    }
}