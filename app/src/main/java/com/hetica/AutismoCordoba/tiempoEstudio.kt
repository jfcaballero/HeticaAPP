package com.hetica.AutismoCordoba

import CustomListAdapter
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
    var adapter: CustomListAdapter? = null

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
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ -> textView3!!.text = arrayList!![position] }
    }

    /**
     * Función que pasa los datos necesarios al temporizador
     *
     * @param view the view
     */
    fun pasarOrgEst(view: View?) {
        bundle = Bundle()
        val siguiente = Intent(view!!.context, temporizadorUnico::class.java)
        if (editText!!.text.toString() == "" || editText!!.text.toString().toInt() == 0 ) {
            Toast.makeText(this, "Introduce un tiempo mayor de 0", Toast.LENGTH_LONG).show()
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
        if (cursor.count == 0) {
            Toast.makeText(this, "No hay ninguna asignatura", Toast.LENGTH_SHORT).show()
        } else {
            while (cursor.moveToNext()) {
                arrayList!!.add(cursor.getString(1))
            }

            adapter = CustomListAdapter(this@tiempoEstudio, android.R.layout.simple_list_item_1, arrayList!!)

            lv!!.adapter = adapter
        }
    }
}