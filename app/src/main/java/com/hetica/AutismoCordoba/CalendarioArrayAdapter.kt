package com.hetica.AutismoCordoba

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.util.SparseBooleanArray
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

        return rowView
    }

    // Método para verificar si el elemento en la posición dada está marcado como estudiado
    private fun isStudied(position: Int): Boolean {
        // Obtener el texto del elemento en la posición dada
        val itemText = getItem(position)

        // Verificar si el texto contiene "Estudiado: Sí"
        return itemText?.contains("Estudiado: Sí") ?: false
    }


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
