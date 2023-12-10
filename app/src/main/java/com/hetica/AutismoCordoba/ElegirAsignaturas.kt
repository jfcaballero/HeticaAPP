package com.hetica.AutismoCordoba

import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elegir_asignaturas)
        agregar = 1
        et = findViewById<View>(R.id.editTextAsig) as EditText
        bt = findViewById<View>(R.id.buttonAdd) as Button
        lv = findViewById<View>(R.id.listViewAsig) as ListView
        db = AdminSQLiteOpenHelperAsig(this)
        arrayList = ArrayList()
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
            adapter = ArrayAdapter(this@ElegirAsignaturas, android.R.layout.simple_list_item_1, arrayList!!)
        }
        viewData()

        //onBtnClick();
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            et!!.setText(arrayList!![position])
            position1 = position
            bt!!.text = "Modificar"
            agregar = 0
            modificarAux = arrayList!![position]
        }
    }
    /*public void onBtnClick(){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = et.getText().toString();
                arrayList.add(result);
                adapter.notifyDataSetChanged();
                et.setText("");
            }
        });

    }*/
    /*public void guardar(View view){
        FileOutputStream fos = null;

        try {
            fos = openFileOutput("asignaturas_listado.txt", MODE_PRIVATE);
            for (int i=0; i<arrayList.size(); i++){
                fos.write(arrayList.get(i).getBytes());
                fos.write("\n".getBytes());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }*/
    /**
     * Función que introduce o modifica una asignatura en la BBDD y tiene control de errores
     *
     * @param view the view
     */
    fun Registrar() {
        if (agregar == 1) {
            val asignatura = et!!.text.toString()
            if (asignatura.length > 25) {
                Toast.makeText(this, "La asignatura es demasiado larga", Toast.LENGTH_LONG).show()
            } else {
                if (db!!.buscar(asignatura)) {
                    if (asignatura != "" && db!!.insertData(asignatura)) {
                        Toast.makeText(this, "Se ha introducido correctamente", Toast.LENGTH_LONG).show()
                        arrayList!!.add(asignatura)
                        adapter!!.notifyDataSetChanged()
                        lv!!.adapter = adapter
                        et!!.setText("")
                    } else {
                        Toast.makeText(this, "Debe escribir una asignatura", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "La asignatura ya existe", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            val asignatura = et!!.text.toString()
            if (asignatura.length > 25) {
                Toast.makeText(this, "La asignatura es demasiado larga", Toast.LENGTH_LONG).show()
            } else {
                if (db!!.buscar(asignatura)) {
                    if (asignatura != "" && db!!.Modificar(asignatura, modificarAux)) {
                        //modifico tambien la base de datos de estadisticas
                        val dbStats = AdminSQLiteOpenHelperStats(this)
                        val actualizadoEnStats = dbStats.ModificarNombreAsignatura(modificarAux, asignatura)
                        if(actualizadoEnStats){
                            Toast.makeText(this, "Se ha modificado correctamente", Toast.LENGTH_LONG).show()
                        }
                        //
                        arrayList!![position1] = asignatura
                        adapter!!.notifyDataSetChanged()
                        lv!!.adapter = adapter
                        et!!.setText("")
                        bt!!.text = "Agregar"
                        agregar = 1
                    } else {
                        Toast.makeText(this, "Debe escribir una asignatura", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "La asignatura ya existe", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Función que elimina una asignatura de la BBDD y tiene control de errores
     *
     * @param view the view
     */
    fun Eliminar() {
        val asignatura = et!!.text.toString()
        if (!db!!.buscar(asignatura) || asignatura == "") {
            if (asignatura != "" && db!!.Eliminar(asignatura)) {
                Toast.makeText(this, "Se ha eliminado correctamente", Toast.LENGTH_LONG).show()
                arrayList!!.removeAt(position1)
                adapter!!.notifyDataSetChanged()
                et!!.setText("")
                bt!!.text = "AGREGAR"
                agregar = 1
            } else {
                Toast.makeText(this, "Debe seleccionar una asignatura", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "La asignatura a eliminar no existe", Toast.LENGTH_LONG).show()
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
            Toast.makeText(this, "No hay ninguna asignatura", Toast.LENGTH_SHORT).show()
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
                adapter = ArrayAdapter(this@ElegirAsignaturas, android.R.layout.simple_list_item_1, arrayList!!)
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