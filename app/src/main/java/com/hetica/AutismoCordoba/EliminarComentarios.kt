package com.hetica.AutismoCordoba

import AdminSQLiteOpenHelperComentarios
import CustomListAdapter
import CustomToolbarAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import java.util.ArrayList


class EliminarComentarios : AppCompatActivity() {
    private var asignaturaSeleccionada: String? = null
    private var asignaturaTextView: TextView?=null
    private lateinit var listaComentarios: ListView
    private lateinit var adapter: CustomListAdapter
    private lateinit var checkBoxSelectAllEliminarComentarios: CheckBox
    private lateinit var noHayComentariosEliminar:TextView
    private val indicesSeleccionados: MutableList<Int> = mutableListOf()
    private var botonEliminar: Button? = null
    private var btnVolverAVisualizarCalificaciones: Button?=null
    private lateinit var toolbar: Toolbar
    private lateinit var customToolbarAdapter: CustomToolbarAdapter
    private var dbComentarios: AdminSQLiteOpenHelperComentarios?=null
    // Variables para el gestor de eventos de toques largos
    private lateinit var gestureDetector: GestureDetector
    private lateinit var comentariosCompletos: List<String> // Lista para almacenar los comentarios completos

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_comentarios)
        asignaturaSeleccionada = intent.getStringExtra("asignaturaSeleccionadaEliminarComentarios")
        asignaturaTextView=findViewById(R.id.asignaturaComentarioEliminar)
        asignaturaTextView?.text = asignaturaSeleccionada
        dbComentarios = AdminSQLiteOpenHelperComentarios(this)

        botonEliminar = findViewById(R.id.botonEliminarComentarios)
        btnVolverAVisualizarCalificaciones=findViewById(R.id.volverAMostrarComentariosDesdeEliminar)
        listaComentarios = findViewById(R.id.listaComentariosEliminar)
        checkBoxSelectAllEliminarComentarios = findViewById(R.id.checkBoxSelectAllEliminarComentario)
        noHayComentariosEliminar=findViewById(R.id.noComentariosesEliminar)

        toolbar = findViewById(R.id.toolbarEliminarComentarios)
        setSupportActionBar(toolbar)
        customToolbarAdapter = CustomToolbarAdapter(this, toolbar)
        customToolbarAdapter.setTextSizeBasedOnScreenWidth()
        // Inicializar el adapter sin datos
        adapter = CustomListAdapter(this, android.R.layout.simple_list_item_multiple_choice, ArrayList())
        listaComentarios.adapter = adapter
        listaComentarios.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        botonEliminar?.isEnabled = false
        checkBoxSelectAllEliminarComentarios.isEnabled =true

        if (asignaturaSeleccionada != null) {
            viewSubjectComentarios()
        }
        // Inicializar el gestor de eventos de toques largos
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                // Obtener el comentario correspondiente al elemento presionado
                val position = listaComentarios.pointToPosition(e.x.toInt(), e.y.toInt())
                if (position != AdapterView.INVALID_POSITION) {
                    val comentario = adapter.getItem(position)
                    // Mostrar el cuadro flotante con el comentario
                    if (comentario != null) {
                        mostrarCuadroFlotante(position)
                    }

                }
            }
        })




        // Configurar el Listener para el toque de los items
        listaComentarios.setOnTouchListener { _, event ->
            // Detectar el evento de toque en la lista y pasar al gestor de eventos
            gestureDetector.onTouchEvent(event)
            false
        }
        // Configurar el Listener para la lista de comentarios
        listaComentarios.setOnItemClickListener { _, _, _, _ ->
            // Verificar si al menos un elemento está seleccionado
            if (listaComentarios.checkedItemCount > 0) {
                // Si hay elementos seleccionados, activar el botón de eliminar
                botonEliminar?.isEnabled = true
                checkBoxSelectAllEliminarComentarios.isEnabled =true


            }else{
                botonEliminar?.isEnabled = false
            }
        }

        botonEliminar?.setOnClickListener {
            if (listaComentarios.checkedItemCount > 0) {
                mostrarDialogoConfirmacion()
            }else{
                FuncionesComunes.showSnackbarWithCustomTextSize(this,"Selecciona al menos una anotación")
            }

        }


        // Configuración del evento de clic en "Seleccionar Todo"
        checkBoxSelectAllEliminarComentarios.setOnCheckedChangeListener { _, isChecked ->
            selectAllItems(isChecked)
            // Verificar si al menos un elemento está seleccionado
            if (isChecked || listaComentarios.checkedItemCount > 0) {
                // Si el "Seleccionar Todo" está checkeado o si hay elementos seleccionados, activar el botón de eliminar
                botonEliminar?.isEnabled = true
            } else {
                // Si el "Seleccionar Todo" está descheckeado y no hay elementos seleccionados, desactivar el botón de eliminar
                botonEliminar?.isEnabled = false
            }
        }

    }

    /**
     * Función para mostrar el cuadro con el comentario al completo al pulsarlo en la lista
     *
     */
    private fun mostrarCuadroFlotante(posicion:Int) {
        val partes = comentariosCompletos[posicion].split(" | ", limit = 2)
        val fecha = if (partes.isNotEmpty()) partes[0] else ""
        val soloComentario = partes[1]

        val dialogTextSize = getDialogTextSize()
        val formattedText = getFormattedText(soloComentario)

        val builder = AlertDialog.Builder(this, dialogTextSize)
        builder.setTitle(fecha)
            .setMessage(formattedText)
            .setPositiveButton("Salir") { dialog, _ ->
                dialog.dismiss()
                //para que no se checkee al intentar verlo
                listaComentarios.setItemChecked(posicion, false)
            }

        val dialog = builder.create()
        dialog.show()
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

    private fun mostrarDialogoConfirmacion() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_custom)

        val btnBorrar = dialog.findViewById<Button>(R.id.btn_exit)
        val btnCancelar = dialog.findViewById<Button>(R.id.btn_cancel)
        val tituloDialogo = dialog.findViewById<TextView>(R.id.text_message)

        tituloDialogo.text = "¿Seguro que quieres eliminar los elementos seleccionados?"
        btnBorrar.text = "Eliminar"
        btnBorrar.setOnClickListener {
            // Lógica para eliminar
            deleteSelectedItems()
            FuncionesComunes.showSnackbarWithCustomTextSize(this,"Anotaciones eliminadas con éxito.")
            dialog.dismiss()

        }

        btnCancelar.setOnClickListener {
            // Lógica para cancelar
            dialog.dismiss()

        }

        dialog.show()
    }
    /**
     * Función para eliminar las calificaciones seleccionadas
     */
    private fun deleteSelectedItems() {
        val existentRows = dbComentarios?.getCommentsForSubject(asignaturaSeleccionada ?: "")
        if (existentRows != null) {
            Log.d("deleteSelectedItems", "Rows before deletion: ${existentRows.size}")
        }

        // Eliminar los elementos seleccionados
        for (i in 0 until listaComentarios.adapter.count) {
            if (listaComentarios.isItemChecked(i)) {
                indicesSeleccionados.add(i)
                val deleted = dbComentarios?.deleteDataByIndex(indicesSeleccionados,
                    asignaturaSeleccionada)
                if (deleted == true) {
                    Log.d("deleteSelectedItems", "Se eliminó el elemento con éxito ")
                } else {
                    Log.e("deleteSelectedItems", "Error al eliminar")
                }
            }
        }

        // Actualizar la lista después de eliminar elementos
        viewSubjectComentarios()
        listaComentarios.clearChoices()
        checkBoxSelectAllEliminarComentarios.isChecked = false
        Log.d("deleteSelectedItems", "Saliendo de deleteSelectedItems")
    }

    /**
     * Función para seleccionar o deseleccionar todos los elementos en la lista
     * @param isChecked Estado de la caja de "Seleccionar todos" (checkeado/descheckeado)
     */
    private fun selectAllItems(isChecked: Boolean) {
        for (i in 0 until listaComentarios.adapter.count) {
            listaComentarios.setItemChecked(i, isChecked)
        }
    }
    /**
     * Función para mostrar los comentarios de la asignatura seleccionada en la lista
     *
     */
    private fun viewSubjectComentarios() {
        Log.d("viewSubjectComentarios", "Entrando a viewSubjectComentarios")
        val comentariosBDList = dbComentarios?.getCommentsForSubject(asignaturaSeleccionada!!)
        Log.d("viewSubjectComentarios", "después de llamar a la getcomments")
        if (!comentariosBDList.isNullOrEmpty()) {
            comentariosCompletos = comentariosBDList // Almacenamos los comentarios completos
            adapter.clear()
            // Mostramos solo las abreviaturas en la lista
            val comentariosAbreviados = comentariosBDList.map { abreviarComentario(it) }
            adapter.addAll(comentariosAbreviados)
            adapter.notifyDataSetChanged()
            noHayComentariosEliminar.visibility = View.INVISIBLE
        } else {
            botonEliminar?.isEnabled = false
            checkBoxSelectAllEliminarComentarios.isEnabled = false
            adapter.clear()
            adapter.notifyDataSetChanged()
            noHayComentariosEliminar.visibility = View.VISIBLE
        }
    }
    /**
     * Función para acortar el comentario al mostrarlo en el listview
     * @param comentario El comentario a acortar si excede la longitud máxima
     * @return El comentario acortado si excede la longitud máxima, de lo contrario, el comentario sin cambios
     */
    private fun abreviarComentario(comentario: String): String {
        val maxLength = 30
        val comentarioSinSaltos = comentario.replace("\n", " ") // Reemplazar saltos de línea por espacios
        return if (comentarioSinSaltos.length > maxLength) {
            comentarioSinSaltos.substring(0, maxLength) + "..."
        } else {
            comentarioSinSaltos
        }
    }


    fun volverAMostrarComentarios(view: View?){
        val intent = Intent(view!!.context, MostrarComentarios::class.java)
        startActivity(intent)
    }
}