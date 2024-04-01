import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.hetica.AutismoCordoba.R

class CustomListAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {
    private val screenWidthDp = context.resources.configuration.screenWidthDp

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        setTextViewSize(textView)
        return view
    }

   

    private fun setTextViewSize(textView: TextView) {
        val textSizeResId = when {
            screenWidthDp >= 720 -> R.dimen.list_item_720
            screenWidthDp >= 480 -> R.dimen.list_item_480
            else -> R.dimen.list_item_320
        }
        val textSizePx = context.resources.getDimensionPixelSize(textSizeResId)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx.toFloat())
    }
}
