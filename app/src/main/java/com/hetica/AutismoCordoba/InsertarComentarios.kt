package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperComentarios
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.util.Log
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

        handler.postDelayed({
            startActivity(intent)
        }, 500)
    }
    private fun hayComentario(): Boolean {
        val textoComentario = comentario.text.toString().trim()
        return textoComentario.isNotEmpty()
    }
    private fun mostrarDialogoConfirmacion() {
        val mensaje="No ha guardado los cambios, ¿desea salir?"
        val dialogTextSize = getDialogTextSize()
        val formattedText = getFormattedText(mensaje)
        AlertDialog.Builder(this,dialogTextSize)
            .setMessage(formattedText)
            .setPositiveButton("Sí") { dialog, _ ->
                volverATiempoDedicado()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun getDialogTextSize(): Int {
        val screenSize = resources.configuration.screenWidthDp
        // Tamaños de titulos y boton del alertdialog para diferentes tamaños de pantalla
        val textSizeSmall = R.style.DialogTextStyleSmall
        val textSizeMedium = R.style.DialogTextStyleMedium
        val textSizeLarge = R.style.DialogTextStyleLarge

        return when {
            screenSize >= 720 -> textSizeLarge // Pantalla grande (más de 720dp)
            screenSize >= 480 -> textSizeMedium // Pantalla mediana (entre 480dp y 720dp)
            else -> textSizeSmall // Pantalla pequeña (menos de 480dp)
        }
    }
    fun getFormattedText(texto: String): CharSequence {
        val spannableStringBuilder = SpannableStringBuilder(texto)
        val screenSize = resources.configuration.screenWidthDp

        val textSizeSmall = 19
        val textSizeMedium = this.resources.getDimensionPixelSize(R.dimen.text_size_small_less_than_480dp)
        val textSizeLarge = this.resources.getDimensionPixelSize(R.dimen.text_size_medium_less_than_480dp)

        val textSize = when {
            screenSize >= 720 -> textSizeLarge
            screenSize >= 480 -> textSizeMedium
            else -> textSizeSmall
        }

        spannableStringBuilder.setSpan(
            AbsoluteSizeSpan(textSize, true),
            0,
            spannableStringBuilder.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableStringBuilder
    }


}