package com.hetica.AutismoCordoba

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomSpinnerAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        // Obtener el tamaño de pantalla
        val screenWidthDp = context.resources.configuration.screenWidthDp

        // Establecer el tamaño de texto basado en el tamaño de pantalla
        val textSizeResId = when {
            screenWidthDp >= 720 -> R.dimen.text_size_large_720dp
            screenWidthDp >= 480 -> R.dimen.text_size_medium_480dp
            else -> R.dimen.text_size_medium_less_than_480dp
        }

        // Obtener el tamaño de texto desde dimens.xml
        val textSizePx = context.resources.getDimensionPixelSize(textSizeResId)

        // Establecer el tamaño de texto en el TextView
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx.toFloat())

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        // Obtener el tamaño de pantalla
        val screenWidthDp = context.resources.configuration.screenWidthDp

        // Establecer el tamaño de texto basado en el tamaño de pantalla
        val textSizeResId = when {
            screenWidthDp >= 720 -> R.dimen.text_size_large_720dp
            screenWidthDp >= 480 -> R.dimen.text_size_medium_480dp
            else -> R.dimen.text_size_small_less_than_480dp
        }

        // Obtener el tamaño de texto desde dimens.xml
        val textSizePx = context.resources.getDimensionPixelSize(textSizeResId)

        // Establecer el tamaño de texto en el TextView
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx.toFloat())

        return view
    }
}