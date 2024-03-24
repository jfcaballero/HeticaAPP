package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperCalendario
import AdminSQLiteOpenHelperCalificaciones
import AdminSQLiteOpenHelperComentarios
import CustomListAdapter
import CustomToolbarAdapter
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize


/**
 * The type Elegir asignaturas.
 */
class ElegirAsignaturas : AppCompatActivity() {
    /**
     * The Db.
     */
    var db: AdminSQLiteOpenHelperAsig? = null

    /**
     * The Et.
     */
    var et: EditText? = null

    /**
     * The Bt.
     */
    var bt: Button? = null

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
     * The Position 1.
     */
    var position1 = 0

    /**
     * The Bt 2.
     */
    var bt2: Button? = null

    /**
     * The Agregar.
     */
    var agregar = 0

    /**
     * The Modificar aux.
     */
    var modificarAux: String? = null

    private lateinit var toolbar: Toolbar
    private lateinit var customToolbarAdapter: CustomToolbarAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elegir_asignaturas)
        agregar = 1
        et = findViewById<View>(R.id.editTextAsig) as EditText
        bt = findViewById<View>(R.id.buttonAdd) as Button
        lv = findViewById<View>(R.id.listViewAsig) as ListView
        db = AdminSQLiteOpenHelperAsig(this)
        arrayList = ArrayList()
        toolbar = findViewById(R.id.toolbar2)
        setSupportActionBar(toolbar)
        customToolbarAdapter = CustomToolbarAdapter(this, toolbar)
        customToolbarAdapter.setTextSizeBasedOnScreenWidth()
        try {
            /* Intenta acceder al directorio de recientes y ejecuta el código correspondiente
            if (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                adapter = object : ArrayAdapter<String>(
                    this@ElegirAsignaturas,
                    android.R.layout.simple_list_item_1,
                    arrayList!!
                ) {
                    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                        val view = super.getView(position, convertView, parent)
                        val tv = view.findViewById<View>(android.R.id.text1) as TextView
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35f)
                        return view
                    }
                }
            }*/
            adapter = CustomListAdapter(this@ElegirAsignaturas, android.R.layout.simple_list_item_1, arrayList!!)

            viewData()
            lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
                et!!.setText(arrayList!![position])
                position1 = position
                bt!!.text = "Modificar"
                agregar = 0
                modificarAux = arrayList!![position]
            }
        } catch (e: Exception) {
            Log.e("Error", "Error al acceder al directorio de recientes: ${e.message}")
            e.printStackTrace()
        }
    }


    /**
     * Función para volver a Settings
     *
     * @param view the view
     */
    fun Settings(view: View?) {
        val siguiente = Intent(view?.context, SettingsActivity::class.java)
        startActivity(siguiente)
    }
    /**
     * Función que introduce o modifica una asignatura en la BBDD y tiene control de errores
     *
     * @param view the view
     */
    fun Registrar(view: View?) {
        val dbCalificaciones = AdminSQLiteOpenHelperCalificaciones(this, null, 3)
        val dbStats = AdminSQLiteOpenHelperStats(this)
        val dbComentarios=AdminSQLiteOpenHelperComentarios(this)
        val dbCalendario=AdminSQLiteOpenHelperCalendario(this)

        val asignatura = et!!.text.toString().trim() // Eliminar espacios al principio y al final

        if (agregar == 1) {
            if (asignatura.length > 25) {
                showSnackbarWithCustomTextSize(this,"La asignatura es demasiado larga")
            } else {
                if (db!!.buscar(asignatura)) {
                    if (asignatura.isNotBlank() && db!!.insertData(asignatura)) {
                        showSnackbarWithCustomTextSize(this,"Se ha introducido correctamente")
                        // Actualizar la lista y notificar al adaptador
                        arrayList!!.add(asignatura)
                        adapter!!.notifyDataSetChanged()

                        lv!!.adapter = adapter
                        et!!.setText("")
                    } else {
                        showSnackbarWithCustomTextSize(this,"Debe escribir una asignatura")
                    }
                } else {
                    showSnackbarWithCustomTextSize(this,"La asignatura ya existe")
                }
            }
        } else {
            //val asignatura = et!!.text.toString()
            if (asignatura.length > 25) {
                showSnackbarWithCustomTextSize(this,"La asignatura es demasiado larga")
            } else {
                if (db!!.buscar(asignatura)) {
                    if (asignatura.isNotBlank() && db!!.Modificar(asignatura, modificarAux)) {
                        // Modifico también la base de datos de estadísticas

                        val actualizadoEnStats = dbStats.ModificarNombreAsignatura(
                            modificarAux?.let { it }, asignatura)
                        dbComentarios.updateSubjectName(modificarAux ?: "", asignatura)
                        dbCalificaciones.updateSubjectName(modificarAux ?: "", asignatura)
                        dbCalendario.updateAsignaturaName(modificarAux ?: "", asignatura)
                        if (actualizadoEnStats) {
                            showSnackbarWithCustomTextSize(this,"Se ha modificado correctamente")
                        }

                        // Actualizar la lista y notificar al adaptador
                        arrayList!![position1] = asignatura
                        adapter!!.notifyDataSetChanged()

                        lv!!.adapter = adapter
                        et!!.setText("")
                        bt!!.text = "Agregar"
                        agregar = 1
                    } else {
                        showSnackbarWithCustomTextSize(this,"Debe escribir una asignatura")
                    }
                } else {
                    showSnackbarWithCustomTextSize(this,"La asignatura ya existe")
                }
            }
        }
    }


    /**
     * Función para reemplazar espacios en blanco con guiones bajos
     *
     * @param input El texto que puede contener espacios en blanco
     * @return El texto con espacios reemplazados por guiones bajos
     */
    private fun replaceSpacesWithUnderscore(input: String): String {
        return input.replace(" ", "_")
    }

    /**
     * Función que elimina una asignatura de la BBDD y tiene control de errores
     *
     * @param view the view
     */
    fun Eliminar(view: View?) {
        val asignatura = et!!.text.toString()
        val dbCalificaciones = AdminSQLiteOpenHelperCalificaciones(this, null, 3)
        val dbStats = AdminSQLiteOpenHelperStats(this)
        val dbComentarios=AdminSQLiteOpenHelperComentarios(this)
        val dbCalendario=AdminSQLiteOpenHelperCalendario(this)

        if (!db!!.buscar(asignatura) || asignatura == "") {
            if (asignatura != "" && db!!.Eliminar(asignatura)) {
                showSnackbarWithCustomTextSize(this,"Se ha eliminado correctamente")
                Log.d("eliminar","se ha eliminado correctamente la asignatura")
                arrayList!!.removeAt(position1)
                adapter!!.notifyDataSetChanged()
                et!!.setText("")
                bt!!.text = "AGREGAR"
                agregar = 1
                dbCalificaciones.deleteAllGradesForSubject(asignatura) //borramos las calificaciones
                dbStats.borrarEstadisticasAsignatura(asignatura)//borramos las estadísticas de tiempo
                dbComentarios.borrarComentariosAsignatura(asignatura)//borramos los comentarios
                dbCalendario.deleteAsignaturaFromAllDates(asignatura) //borramos las sesiones asignadas a esa asignatura
            } else {
                showSnackbarWithCustomTextSize(this,"Debe seleccionar una asignatura")
            }
        } else {
            showSnackbarWithCustomTextSize(this,"La asignatura a eliminar no existe")
            et!!.setText("")
        }
    }

    /**
     * Comprobar que no hay ninguna asignatura en la BBDD
     *
     */
    private fun viewData() {
        val cursor = db!!.viewData()
        if (cursor.count == 0) {
            showSnackbarWithCustomTextSize(this,"No hay ninguna asignatura")
        } else {
            while (cursor.moveToNext()) {
                arrayList!!.add(cursor.getString(1))
            }
            if (resources.configuration.screenLayout and
                    Configuration.SCREENLAYOUT_SIZE_MASK ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                object : ArrayAdapter<String>(this@ElegirAsignaturas, android.R.layout.simple_list_item_1, arrayList!!) {
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
                adapter = CustomListAdapter(this@ElegirAsignaturas, android.R.layout.simple_list_item_1, arrayList!!)
            }
            lv!!.adapter = adapter
        }
    } /*public void ver(View view) throws IOException {
        String data = "";
        StringBuffer sbuffer = new StringBuffer();

        InputStream is = this.getResources().openRawResource(R.raw.asignaturas_listado);

        BufferedReader reader = new BufferedReader( new InputStreamReader(is));

        data=reader.readLine();

        sbuffer.append(data + "n");

        et.setText(sbuffer);
    }*/
}