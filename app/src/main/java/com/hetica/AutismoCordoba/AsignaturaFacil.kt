package com.hetica.AutismoCordoba

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

/**
 * The type Asignatura facil.
 */
class AsignaturaFacil : AppCompatActivity() {
    /**
     * The Asignaturas.
     */
    var asignaturas: ArrayList<String>? = null

    /**
     * The Bundle.
     */
    var bundle: Bundle? = null

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
     * The Tiempo constante.
     */
    var tiempoConstante: String? = null

    /**
     * The Tiempo.
     */
    var tiempo: String? = null

    /**
     * The Cuantas.
     */
    var cuantas: String? = null

    /**
     * The Actual.
     */
    var actual: String? = null

    /**
     * The Button.
     */
    var button: Button? = null

    /**
     * The Lv.
     */
    var lv: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignatura_facil)
        db = AdminSQLiteOpenHelperAsig(this)
        if (tiempoConstante === "1") {
            seekBar!!.visibility = View.INVISIBLE
        }
        bundle = intent.extras
        cuantas = bundle!!.getString("numAsig")
        actual = bundle!!.getString("actAsig")
        seebbarr()
        textView2 = findViewById<View>(R.id.textView47) as TextView
        lv = findViewById<View>(R.id.listViewSiguiente) as ListView
        arrayList = ArrayList()
        viewData()
        button = findViewById<View>(R.id.button37) as Button
        textView = findViewById<View>(R.id.textView45) as TextView
        readParams()


        //read();
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ -> textView2!!.text = arrayList!![position] }
    }

    var doubleBackToExitPressedOnce = false
    var siguiente: Intent? = null
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            siguiente = Intent(baseContext, MainActivity::class.java)
            startActivity(siguiente)
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Presiona de nuevo para salir", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    /**
     * Función que mide el valor del seekbar
     */
    fun seebbarr() {
        seekBar = findViewById<View>(R.id.seekBar) as SeekBar


        //textView.setText("Covered : " + seekBar.getProgress() + " / " +seekBar.getMax());
        seekBar!!.max = 30
        seekBar!!.setOnSeekBarChangeListener(
                object : OnSeekBarChangeListener {
                    var progress_value = 0
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        progress_value = progress + 30
                        textView!!.text = "$progress_value minutos "
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        textView!!.text = "$progress_value minutos "
                    }
                }
        )
    }
    /*public void read() {
            FileInputStream fis = null;

            try {
                fis = openFileInput("asignaturas_listado.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;

                while ((text = br.readLine()) != null) {
                    arrayList.add(text);
                    adapter.notifyDataSetChanged();

                }
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {

                }
            }
        }*/
    /**
     * Función que se encarga de pasar al temporizador todos los datos necesarios
     *
     * @param view the view
     */
    fun pasar() {
        val siguiente = Intent(this, TimerSimple::class.java)
        bundle!!.putString("actAsig", "1")
        bundle!!.putString("numAsig", cuantas)
        bundle!!.putString("asig", textView2!!.text.toString())
        if (tiempoConstante.equals("0", ignoreCase = true)) {
            bundle!!.putString("time", Integer.toString(seekBar!!.progress + 30))
            //bundle.putString("time", Integer.toString(1));
        } else {
            bundle!!.putString("time", tiempo)
        }
        siguiente.putExtras(bundle!!)
        if (textView2!!.text.toString().equals("Clica una asignatura", ignoreCase = true)) {
            Toast.makeText(this,
                    "Selecciona una asignatura", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(siguiente)
        }
    }

    /**
     * Función que aplica la opcion de visualizar o no el SeekBar
     */
    fun readParams() {
        var fis: FileInputStream? = null
        try {
            fis = openFileInput("tiempo_trabajar.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            tiempo = br.readLine()
            tiempoConstante = br.readLine()
            if (tiempoConstante.equals("1", ignoreCase = true)) {
                seekBar!!.visibility = View.INVISIBLE
                textView!!.text = "$tiempo minutos"
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Función que añade las asignaturas a la pantalla
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
                object : ArrayAdapter<String>(this@AsignaturaFacil, android.R.layout.simple_list_item_1, arrayList!!) {
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
                adapter = ArrayAdapter(this@AsignaturaFacil, android.R.layout.simple_list_item_1, arrayList!!)
            }
            lv!!.adapter = adapter
        }
    }

    companion object {
        private var seekBar: SeekBar? = null
        private var textView: TextView? = null
        private var textView2: TextView? = null
    }
}