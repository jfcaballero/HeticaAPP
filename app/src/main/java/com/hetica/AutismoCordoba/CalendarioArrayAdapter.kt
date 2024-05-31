package com.hetica.AutismoCordoba

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.util.SparseBooleanArray
import android.util.TypedValue

/**
 * Adaptador de la lista de checkboxes que se utiliza en la actividad de calendario.
 * @author Álvaro Berjillos
 */
class CalendarioArrayAdapter(
    context: Context,
    resource: Int,
    objects: MutableList<String>
) : ArrayAdapter<String>(context, resource, objects) {
    private val screenWidthDp = context.resources.configuration.screenWidthDp
    private val screenHeightDp = context.resources.configuration.screenHeightDp
    val checkedPositions = SparseBooleanArray()
    var lastCheckedPosition = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = convertView ?: inflater.inflate(R.layout.list_item_checkbox_calendario, parent, false)

        val checkBox = rowView.findViewById<CheckBox>(R.id.checkBoxItem)
        val textView = rowView.findViewById<TextView>(R.id.textViewItem)

        val itemText = getItem(position) ?: ""
        textView.text = itemText

        // Verificar si el elemento está marcado como estudiado
        val isStudied = isStudied(position)

        // Si está estudiado, deshabilitar el CheckBox
        checkBox.isEnabled = !isStudied

        // Establecer el estado del CheckBox y el evento de clic
        checkBox.isChecked = checkedPositions.get(position, false)
        checkBox.setOnClickListener {
            if (!isStudied) {
                handleItemClick(position)
            }
        }
        setTextViewSize(textView)
        return rowView
    }

    private fun setTextViewSize(textView: TextView) {
        // Obtener la rotación de la pantalla según el nivel de API
        val rotation = context.display?.rotation ?: android.view.Surface.ROTATION_0

        val isPortrait = rotation == android.view.Surface.ROTATION_0 || rotation == android.view.Surface.ROTATION_180

        val textSize = when {
            isPortrait -> {
                when {
                    screenWidthDp >= 720 -> context.resources.getDimension(R.dimen.list_item_portrait_720)
                    screenWidthDp >= 480 -> context.resources.getDimension(R.dimen.list_item_portrait_480)
                    else -> context.resources.getDimension(R.dimen.list_item_portrait_320)
                }
            }else -> {
                when {
                    //En el primero se hace la intersección para que no quede muy pequeño en tablets
                    screenHeightDp >= 600 && screenWidthDp >= 720 -> context.resources.getDimension(R.dimen.list_item_landscape_720)
                    screenHeightDp >= 400 -> context.resources.getDimension(R.dimen.list_item_landscape_480)
                    else -> context.resources.getDimension(R.dimen.list_item_landscape_320)
                }

            }

        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun uncheckAll() {
        checkedPositions.clear()
        notifyDataSetChanged()
    }


    /**
     * Función para verificar si el elemento en la posición dada está marcado como estudiado
     * @param position Posición que ocupa
     * @return boolean
     */

    private fun isStudied(position: Int): Boolean {
        // Obtener el texto del elemento en la posición dada
        val itemText = getItem(position)

        // Verificar si el texto contiene "Estudiado: Sí"
        return itemText?.contains("Estudiado: Sí") ?: false
    }

    /**
     * Función para manejar que solo se seleccione un item al mismo tiempo de los disponibles
     * @param position
     */
    private fun handleItemClick(position: Int) {
        // Desmarcar el elemento anteriormente seleccionado
        if (lastCheckedPosition != -1) {
            checkedPositions.put(lastCheckedPosition, false)
        }

        // Marcar el nuevo elemento seleccionado
        lastCheckedPosition = position
        checkedPositions.put(position, true)

        notifyDataSetChanged()
    }
}
