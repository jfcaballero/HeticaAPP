package com.hetica.AutismoCordoba

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

/**
 * The type Activity organizar estudio 4.
 */
class activity_organizar_estudio4 : AppCompatActivity() {
    /*
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Your business logic to handle the back pressed event
            Intent siguiente =new Intent(this, activity_organizar_estudio3.class);
            startActivity(siguiente);
        }
    }*/
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organizar_estudio4)
        text = findViewById<View>(R.id.textView8) as TextView

        //onBackPressed() is deprecated
        /* adding onbackpressed callback listener.
        this.getOnBackPressedDispatcher().addCallback(this,onBackPressedCallback);
        */


        /* //getDefaultDisplay() is deprecated
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.x;
        */

        // Nuevo código para obtener height
        val windowMetrics = windowManager.currentWindowMetrics
        val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        val height = windowMetrics.bounds.height() - insets.bottom - insets.top
        if (height < 1300) {
            text!!.textSize = 25f
        }
        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)

    }
    /*
    override fun onBackPressed(){
       super.onBackPressed();
    }*/
    /**
     * Pasa al siguiente consejo
     *
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
        val siguiente = Intent(this@activity_organizar_estudio4, activity_organizar_estudio3::class.java)
        startActivity(siguiente)
    }}

    fun pasar5(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_estudio5::class.java)
        startActivity(siguiente)
    }

    /**
     * Pasa al anterior consejo
     *
     * @param view the view
     */
    fun pasaratras(view: View?) {
        val siguiente = Intent(view!!.context, activity_organizar_estudio3::class.java)
        startActivity(siguiente)
    }

    companion object {
        var text: TextView? = null
    }
}