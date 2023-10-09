package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

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
            displayToast(view)
        }
        val bundle = Bundle()
        bundle.putString("time", input)
        siguiente.putExtras(bundle)
        startActivity(siguiente)
    }

    /**
     * Display toast.
     *
     * @param view the view
     */
    fun displayToast(view: View?) {
        Toast.makeText(this@PutTime, "No ha introducido ningún tiempo", Toast.LENGTH_LONG).show()
    }
}