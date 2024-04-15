import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.hetica.AutismoCordoba.R

class CustomListAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<String>(context, resource, objects) {
    private val screenWidthDp = context.resources.configuration.screenWidthDp
    private val screenHeightDp = context.resources.configuration.screenHeightDp
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        setTextViewSize(textView)
        return view
    }

   

    @RequiresApi(Build.VERSION_CODES.R)
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
}
