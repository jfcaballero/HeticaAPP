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

    var lastCheckedPosition = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = convertView ?: inflater.inflate(R.layout.list_item_checkbox_calendario, parent, false)

        val checkBox = rowView.findViewById<CheckBox>(R.id.checkBoxItem)
        val textView = rowView.findViewById<TextView>(R.id.textViewItem)

        val itemText = getItem(position) ?: ""
        textView.text = itemText

        // Establecer el estado del CheckBox y el evento de clic
        checkBox.isChecked = position == lastCheckedPosition
        checkBox.setOnClickListener {
            handleItemClick(position)
        }

        return rowView
    }

    private fun handleItemClick(position: Int) {
        // Desmarcar el elemento anteriormente seleccionado
        if (lastCheckedPosition != -1) {
            getItem(lastCheckedPosition)?.let {
                notifyDataSetInvalidated()
            }
        }

        // Marcar el nuevo elemento seleccionado
        lastCheckedPosition = position
        notifyDataSetChanged()
    }

}