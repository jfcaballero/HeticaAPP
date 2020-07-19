package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The type Tiempo estudio.
 */
public class tiempoEstudio extends AppCompatActivity {

    /**
     * The Lv.
     */
    ListView lv;
    /**
     * The Array list.
     */
    ArrayList<String> arrayList;
    /**
     * The Adapter.
     */
    ArrayAdapter<String> adapter;
    /**
     * The Db.
     */
    AdminSQLiteOpenHelperAsig db;
    /**
     * The Text view 3.
     */
    TextView textView3;
    /**
     * The Bundle.
     */
    Bundle bundle;
    /**
     * The Edit text.
     */
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiempo_estudio);

        lv = (ListView) findViewById(R.id.listView1);

        arrayList = new ArrayList<String>();
        db = new AdminSQLiteOpenHelperAsig(this, "Asig.db", null, 1);

        textView3=(TextView)findViewById(R.id.textView55);
        editText=(EditText)findViewById(R.id.editText4);

        viewData();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView3.setText(arrayList.get(position));
            }
        });

    }

    /**
     * Función que pasa los datos necesarios al temporizador
     *
     * @param view the view
     */
    public void pasarOrgEst(View view)
    {
        bundle = new Bundle();
        Intent siguiente =new Intent(this, temporizadorUnico.class);


        if(editText.getText().toString().equals("")) {
            Toast.makeText(this, "Introduce un tiempo", Toast.LENGTH_LONG).show();
        }
        else {
            if (textView3.getText().toString().equals("") || textView3.getText().toString().equals("Clica una asignatura")) {
                Toast.makeText(this, "Selecciona una asignatura", Toast.LENGTH_LONG).show();

            } else {

                bundle.putString("asig", textView3.getText().toString());
                bundle.putString("time", editText.getText().toString());

                siguiente.putExtras(bundle);

                startActivity(siguiente);
            }
        }
    }

    /**
     * Función que devuelve las asignaturas de la BBDD y las muestra en pantalla
     *
     */
    private void viewData(){
        Cursor cursor = db.viewData();

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No hay ninguna asignatura", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                arrayList.add(cursor.getString(1));
            }
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                adapter = new ArrayAdapter<String>(tiempoEstudio.this, android.R.layout.simple_list_item_1, arrayList){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent){
                        /// Get the Item from ListView
                        View view = super.getView(position, convertView, parent);

                        TextView tv = (TextView) view.findViewById(android.R.id.text1);

                        // Set the text size 25 dip for ListView each item
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);

                        // Return the view
                        return view;
                    }

                };

            }else
            {
                adapter = new ArrayAdapter<String>(tiempoEstudio.this, android.R.layout.simple_list_item_1, arrayList);
            }
            lv.setAdapter(adapter);
        }

    }
}
