package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowInsets
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * The type Main activity.
 *
 * @author Miguel Ángel Borreguero Aparicio
 */
class MainActivity : AppCompatActivity() {
    var siguiente: Intent? = null
    private val longClickDuration = 3000
    private var then: Long = 0

    /**
     * The Db.
     */
    var db: AdminSQLiteOpenHelperAsig? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serviceIntent = Intent(this, ConcentrationModeService::class.java)
        startService(serviceIntent)
        setContentView(R.layout.activity_main)
        db = AdminSQLiteOpenHelperAsig(this)
        botonTemp = findViewById<View>(R.id.button45) as Button
        botonOpc = findViewById<View>(R.id.button4) as Button
        botonStat = findViewById<View>(R.id.button5) as Button
        botonCred = findViewById<View>(R.id.button6) as Button
        botonCalendario = findViewById<View>(R.id.button16) as? Button ?: return
        botonTemp!!.visibility = View.INVISIBLE
        boton = findViewById<View>(R.id.button) as Button
        boton1 = findViewById<View>(R.id.button2) as Button
        boton2 = findViewById<View>(R.id.button3) as Button
        text = findViewById<View>(R.id.textView59) as TextView
        text1 = findViewById<View>(R.id.textView60) as TextView
        text2 = findViewById<View>(R.id.textView61) as TextView
        leerTemp()

        // Nuevo código para obtener height and widht
        val windowMetrics = windowManager.currentWindowMetrics
        val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        val height = windowMetrics.bounds.height() - insets.bottom - insets.top
        val weight = windowMetrics.bounds.width() - insets.right - insets.left

        /*
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int weight = size.x;
        */if (height < 721) {
            boton!!.layoutParams.width = 250
            boton!!.layoutParams.height = 250
            boton1!!.layoutParams.width = 250
            boton1!!.layoutParams.height = 250
            boton2!!.layoutParams.width = 250
            boton2!!.layoutParams.height = 250
        }
        if (weight < 1300) {
            text!!.textSize = 11f
            text1!!.textSize = 11f
            text2!!.textSize = 11f
        }

        botonOpc!!.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                then = System.currentTimeMillis()
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (System.currentTimeMillis() - then > longClickDuration) {
                    siguiente = Intent(baseContext, SettingsActivity::class.java)
                    startActivity(siguiente)
                    println("Long Click has happened!")
                    return@OnTouchListener false
                } else {
                    /* Implement short click behavior here or do nothing */
                    println("Short Click has happened...")
                    return@OnTouchListener false
                }
            }
            true
        })
        botonStat!!.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                then = System.currentTimeMillis()
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (System.currentTimeMillis() - then > longClickDuration) {
                    siguiente = Intent(baseContext, estadisticasDias::class.java)
                    startActivity(siguiente)
                    println("Long Click has happened!")
                    return@OnTouchListener false
                } else {
                    /* Implement short click behavior here or do nothing */
                    println("Short Click has happened...")
                    return@OnTouchListener false
                }
            }
            true
        })
        botonCred!!.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                then = System.currentTimeMillis()
            } else if (event.action == MotionEvent.ACTION_UP) {
                if (System.currentTimeMillis() - then > longClickDuration) {
                    siguiente = Intent(baseContext, creditos::class.java)
                    startActivity(siguiente)
                    println("Long Click has happened!")
                    return@OnTouchListener false
                } else {
                    /* Implement short click behavior here or do nothing */
                    println("Short Click has happened...")
                    return@OnTouchListener false
                }
            }
            true
        })
        /*botonCred.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                siguiente = new Intent(getBaseContext(), creditos.class);
                startActivity(siguiente);
                return true;
            }
        });*/
        var fos: FileOutputStream? = null
        var filename = "tiempo_trabajar.txt"
        var file = File(applicationContext.filesDir, filename)
        if (file.exists()) {
        } else {
            try {
                fos = openFileOutput(filename, MODE_PRIVATE)
                fos.write("30".toByteArray())
                fos.write("\n".toByteArray())
                fos.write("0".toByteArray())
                fos.write("\n".toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        filename = "pausa.txt"
        file = File(applicationContext.filesDir, filename)
        if (file.exists()) {
        } else {
            try {
                fos = openFileOutput(filename, MODE_PRIVATE)
                fos.write("1".toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        filename = "fin.txt"
        file = File(applicationContext.filesDir, filename)
        if (file.exists()) {
        } else {
            try {
                fos = openFileOutput(filename, MODE_PRIVATE)
                fos.write("1".toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        //new AlertDialog.Builder(this)
        //        .setTitle("Really Exit?")
        //        .setMessage("Are you sure you want to exit?")
        //        .setNegativeButton(android.R.string.no, null)
        //        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

        //                   public void onClick(DialogInterface arg0, int arg1) {
        //                MainActivity.super.onBackPressed();
        //            }
        //        }).create().show();
    }
    /**
     * Pasar a la pantalla de "Asignatura de hoy"
     *
     * @param view the view
     */
    fun pasarAsignaturaDeHoy(view: View) {
        if (countData() == 0) {
            showSnackbarWithCustomTextSize(view.context, "Introduce primero una asignatura")
        } else {
            val intent = Intent(this, AsignaturaDeHoy::class.java)
            startActivity(intent)
        }
    }

    /**
     * Pasar a la pantalla de "Organizar el Estudio"
     *
     * @param view the view
     */
    fun pasarOrgEst(view: View?) {
        if (countData() == 0) {
            showSnackbarWithCustomTextSize(view!!.context, "Introduce primero una asignatura")
        } else {
            val siguiente = Intent(this, organizar_tareas1::class.java)
            startActivity(siguiente)
        }
    }

    /**
     * Pasar org tar.
     *
     * @param view the view
     */
    fun pasarOrgTar(view: View?) {
        if (countData() == 0) {
            showSnackbarWithCustomTextSize(view!!.context, "Introduce primero una asignatura")
        } else {
            val siguiente = Intent(this, OrganizarEstudio1::class.java)
            startActivity(siguiente)
        }
    }

    /**
     * Pasar a la pantalla de "Organizar la mochila"
     *
     * @param view the view
     */
    fun pasarOrgMoch(view: View?) {
        val siguiente = Intent(view!!.context, OrganizarMochila1::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasar a la pantalla de "Opciones"
     *
     * @param view the view
     */
    fun pasarOpciones(view: View?) {
        val siguiente = Intent(view!!.context, SettingsActivity::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasar a la pantalla de "Estadisticas"
     *
     * @param view the view
     */
    fun pasarEstadisticas(view: View?) {
        val siguiente = Intent(view!!.context, estadisticasDias::class.java)
        startActivity(siguiente)
    }
    /**
     * Pasar a la pantalla de "Creditos"
     *
     */
    fun pasarCreditos(view: View?) {
        val siguiente = Intent(view!!.context, creditos::class.java)
        startActivity(siguiente)
    }

    /**
     * Comprobar que la base de datos no este vacia
     *
     * @param view the view
     */
    fun pasarTemporizador(view: View?) {
        if (countData() == 0) {
            showSnackbarWithCustomTextSize(view!!.context, "Introduce primero una asignatura" )
        } else {
            val siguiente = Intent(this, tiempoEstudio::class.java)
            startActivity(siguiente)
        }
    }

    /**
     * Mostrar o no la función del temporizador
     */
    fun leerTemp() {
        var fis: FileInputStream? = null
        try {
            fis = openFileInput("temporizador.txt")
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            StringBuilder()
            val text: String
            text = br.readLine()
            if (text.equals("1", ignoreCase = true)) {
                botonTemp!!.visibility = View.VISIBLE
            } else {
                botonTemp!!.visibility = View.INVISIBLE
            }
            try {
                fis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fis != null) {
            }
        }
    }

    /**
     * Devuelve el numero de asignaturas en la base de datos
     *
     * @return
     */
    private fun countData(): Int {
        val cursor = db!!.viewData()
        return cursor.count
    }

    companion object {
        /**
         * The constant botonTemp.
         */
        var botonTemp: Button? = null
        var botonOpc: Button? = null
        var botonStat: Button? = null
        var botonCred: Button? = null
        var text: TextView? = null
        var text1: TextView? = null
        var text2: TextView? = null
        var boton: Button? = null
        var boton1: Button? = null
        var boton2: Button? = null
        var botonCalendario:Button? = null
    }
}