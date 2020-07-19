package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The type Put time.
 */
public class PutTime extends AppCompatActivity {

    private EditText mTextViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_time);

        mTextViewTime = (EditText)findViewById(R.id.edit_view_time);
    }

    /**
     * Pasar timer simple.
     *
     * @param view the view
     */
    public void pasarTimerSimple(View view)
    {
        Intent siguiente = new Intent(this, TimerSimple.class);
        String input =mTextViewTime.getText().toString();

        if(input.length() == 0)
        {
            displayToast(view);
        }
        Bundle bundle = new Bundle();
        bundle.putString("time", input);
        siguiente.putExtras(bundle);

        startActivity(siguiente);


    }

    /**
     * Display toast.
     *
     * @param view the view
     */
    public void displayToast (View view)
    {
        Toast.makeText(PutTime.this, "No ha introducido ningún tiempo", Toast.LENGTH_LONG).show();
    }
}
