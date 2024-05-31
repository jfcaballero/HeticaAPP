package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.hetica.AutismoCordoba.FuncionesComunes.Companion.showSnackbarWithCustomTextSize

/**
 * The type Put time.
 */
class PutTime : AppCompatActivity() {
    private var mTextViewTime: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_put_time)
        mTextViewTime = findViewById<View>(R.id.edit_view_time) as EditText
    }

    /**
     * Pasar timer simple.
     *
     * @param view the view
     */
    fun pasarTimerSimple(view: View?) {
        val siguiente = Intent(this, TimerSimple::class.java)
        val input = mTextViewTime!!.text.toString()
        if (input.length == 0) {
            showSnackbarWithCustomTextSize(view!!.context, "No ha introducido ningún tiempo")
        }
        val bundle = Bundle()
        bundle.putString("time", input)
        siguiente.putExtras(bundle)
        startActivity(siguiente)
    }

}