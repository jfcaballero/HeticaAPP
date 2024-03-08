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
        val screenWidthDp = context.resources.configuration.screenWidthDp
        // Adaptar el tamaño de texto basado en el tamaño de pantalla
        val textSizeResId = when {
            // Ajusta los valores de screenWidthDp según tus necesidades
            screenWidthDp >= 720 -> R.dimen.text_size_small_720dp
            screenWidthDp >= 480 -> R.dimen.text_size_small_480dp
            else -> R.dimen.text_size_small_less_than_480dp
        }

        // Obtener el tamaño de texto desde dimens.xml
        val textSizePx = context.resources.getDimensionPixelSize(textSizeResId)

        // Establecer el tamaño de texto en el TextView
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx.toFloat())

        return rowView
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
