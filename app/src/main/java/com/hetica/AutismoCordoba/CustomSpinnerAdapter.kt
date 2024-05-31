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
    private val screenWidthDp = context.resources.configuration.screenWidthDp
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        setTextViewSize(textView)
        return view
    }


     override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        setTextViewSize(textView)

         val dropdownTextSizeResId = when {
             context.resources.configuration.screenWidthDp >= 720 -> R.dimen.dropdown_item_height_720
             context.resources.configuration.screenWidthDp >= 480 -> R.dimen.dropdown_item_height_480
             else -> R.dimen.dropdown_item_height_320
         }

        // Ajustar la altura del TextView en el desplegable
        val dropdownItemHeight = context.resources.getDimensionPixelSize(dropdownTextSizeResId)
        textView.layoutParams.height = dropdownItemHeight

        return view
    }

    private fun setTextViewSize(textView: TextView) {

        val textSizeResId = when {
            screenWidthDp >= 720 -> R.dimen.text_size_large_720dp
            screenWidthDp >= 480 -> R.dimen.text_size_medium_480dp
            else -> R.dimen.text_size_medium_less_than_480dp
        }
        val textSizePx = context.resources.getDimensionPixelSize(textSizeResId)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx.toFloat())
    }
}
