package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

/**
 * The type Recompensa.
 *
 * @author Álvaro Berjillos Roldán
 */
class Recompensa : AppCompatActivity() {
    /**
     * Boton de salir.
     */
    @SuppressLint("StaticFieldLeak")
    private var salir: Button? = null

    /**
     * Texto aleatorio.
     */
    @SuppressLint("StaticFieldLeak")
    private var consejo: TextView? = null
    /**
     * Imagen recompensa
     */
    @SuppressLint("StaticFieldLeak")
    private var recompensa: ImageView? = null

    private var Comentarios: Button? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recompensa)
        salir=findViewById(R.id.botonSalirMain)
        consejo=findViewById(R.id.consejos)
        recompensa=findViewById(R.id.imagenRecompensa)

        // Establecer el texto aleatorio en el TextView
        val arrayConsejos = resources.getStringArray(R.array.array_consejos)
        val randomIndex = Random.nextInt(arrayConsejos.size)
        consejo?.text = arrayConsejos[randomIndex]

        // Lista de nombres de imágenes en el directorio drawable
        val arrayImagenes = arrayOf("estrella_freepik", "loro_mihimi", "tortuga_freepik","gato_flaticon","abeja_flaticon")

        // Seleccionar un nombre de imagen aleatorio de la lista
        val randomImageName = arrayImagenes.random()

        // Obtener el identificador de recurso de la imagen
        val imageId = resources.getIdentifier(randomImageName, "drawable", packageName)

        // Establecer la imagen en el ImageView
        if (imageId != 0) {
            recompensa?.setImageResource(imageId)
        }


        salir?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        Comentarios = findViewById<View>(R.id.botonComentarEstudio) as Button
        Comentarios!!.setOnClickListener {
            val dialog = EnviarComentarios()
            dialog.show(supportFragmentManager, "enviarComentarios")

            //true
        }





    }
}