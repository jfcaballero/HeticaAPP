package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperComentarios
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize


private var AsignaturaTextView: TextView? =null
private var FechaTextView: TextView? =null

private var btnRegistrarComentarios: Button?=null
private var btnVolverATiempoDedicado: Button?=null


private lateinit var comentario: EditText
var comentariosDB: AdminSQLiteOpenHelperComentarios? = null

class InsertarComentarios : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertar_comentarios)
        comentariosDB = AdminSQLiteOpenHelperComentarios(this)
        // Obtener los datos pasados desde la actividad anterior
        val asignatura = intent.getStringExtra("asignatura")
        val fecha = intent.getStringExtra("fecha")
        AsignaturaTextView= findViewById(R.id.asignaturaDelComentario)
        AsignaturaTextView?.setText(asignatura)
        FechaTextView= findViewById(R.id.fechaDelComentario)
        FechaTextView?.setText(fecha)

        btnRegistrarComentarios=findViewById(R.id.insertarComentario)
        btnVolverATiempoDedicado=findViewById(R.id.volverATiempoDedicado)
        comentario=findViewById(R.id.textoDeComentario)

        btnRegistrarComentarios?.setOnClickListener {

            val comentarioString = comentario.text.toString()

            // Verificar que nada esté vacío antes de insertar
            if (!comentarioString.isNullOrEmpty() && asignatura != null && fecha != null) {
                insertarComentario(asignatura, comentarioString, fecha)
            } else {
                showSnackbarWithCustomTextSize(this, "Por favor ingrese un comentario",)
            }
        }

        btnVolverATiempoDedicado?.setOnClickListener {
            if (hayComentario()) {
                mostrarDialogoConfirmacion()
            } else {
                volverATiempoDedicado()
            }

        }




    }

    /**
     * Función para insertar comentarios en la base de datos
     *
     */
    private fun insertarComentario(asignatura: String,mensaje:String,fecha:String) {
        Log.d("Insertando comentario","Fecha $fecha para $asignatura y dice $mensaje")
        comentariosDB?.insertData(fecha, asignatura, mensaje)
        showSnackbarWithCustomTextSize(this, "Comentario insertado" )
        btnRegistrarComentarios?.isEnabled=false
        volverATiempoDedicado()

    }

    private fun volverATiempoDedicado(){
        val intent = Intent(this, tiempo_dedicado::class.java)
        val handler = Handler(Looper.getMainLooper())
        val asignatura = AsignaturaTextView!!.text.toString()
        intent.putExtra("asignatura_que_acabo_de_registrar", asignatura)
        intent.putExtra("asignatura_que_acabo_de_registrar_flag", true)
        handler.postDelayed({
            Log.d("Enviando asignatura","$asignatura")
            startActivity(intent)
        }, 500)
    }
    private fun hayComentario(): Boolean {
        val textoComentario = comentario.text.toString().trim()
        return textoComentario.isNotEmpty()
    }

    private fun mostrarDialogoConfirmacion() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_custom)

        val btnSalir = dialog.findViewById<Button>(R.id.btn_exit)
        val btnCancelar = dialog.findViewById<Button>(R.id.btn_cancel)

        btnSalir.setOnClickListener {
            // Lógica para salir
            volverATiempoDedicado()
            dialog.dismiss()
        }

        btnCancelar.setOnClickListener {
            // Lógica para cancelar
            dialog.dismiss()
        }

        dialog.show()
    }


}