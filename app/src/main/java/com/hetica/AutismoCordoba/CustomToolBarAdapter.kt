import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import com.hetica.AutismoCordoba.R

class CustomToolbarAdapter(private val context: Context, private val toolbar: Toolbar) {

    fun setTextSizeBasedOnScreenWidth() {
        val screenWidthDp = context.resources.configuration.screenWidthDp
        val textSizeResId = when {
            screenWidthDp >= 720 -> R.dimen.text_size_large_720dp
            screenWidthDp >= 480 -> R.dimen.text_size_medium_480dp
            else -> R.dimen.text_size_medium_less_than_480dp
        }
        val textSizePx = context.resources.getDimensionPixelSize(textSizeResId)

        for (i in 0 until toolbar.childCount) {
            val view = toolbar.getChildAt(i)
            if (view is TextView) {
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx.toFloat())
            } else if (view is ViewGroup) {
                for (j in 0 until view.childCount) {
                    val innerView = view.getChildAt(j)
                    if (innerView is TextView) {
                        innerView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx.toFloat())
                    }
                }
            }
        }
    }
}
