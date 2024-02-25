package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperComentarios
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date

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
                Toast.makeText(this, "Por favor ingrese un comentario", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolverATiempoDedicado?.setOnClickListener {
            volverATiempoDedicado()
        }




    }

    /**
     * Función para insertar comentarios en la base de datos
     *
     */
    private fun insertarComentario(asignatura: String,mensaje:String,fecha:String) {
        Log.d("Insertando comentario","Fecha $fecha para $asignatura y dice $mensaje")
        comentariosDB?.insertData(fecha, asignatura, mensaje)
        Toast.makeText(this, "Comentario insertado", Toast.LENGTH_SHORT).show()
        volverATiempoDedicado()

    }

    private fun volverATiempoDedicado(){
        val intent = Intent(this, tiempo_dedicado::class.java)
        startActivity(intent)
    }

}