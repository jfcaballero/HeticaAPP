package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * The type Elegir asignaturas.
 */
public class ElegirAsignaturas extends AppCompatActivity {

    /**
     * The Db.
     */
    AdminSQLiteOpenHelperAsig db;

    /**
     * The Et.
     */
    EditText et;
    /**
     * The Bt.
     */
    Button bt;
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
     * The Position 1.
     */
    int position1;
    /**
     * The Bt 2.
     */
    Button bt2;
    /**
     * The Agregar.
     */
    int agregar;
    /**
     * The Modificar aux.
     */
    String modificarAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_asignaturas);

        agregar=1;

        et = (EditText) findViewById(R.id.editTextAsig);
        bt = (Button) findViewById(R.id.buttonAdd);
        lv = (ListView) findViewById(R.id.listViewAsig);

        db = new AdminSQLiteOpenHelperAsig(this, "Asig.db", null, 1);

        arrayList = new ArrayList<String>();
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            adapter = new ArrayAdapter<String>(ElegirAsignaturas.this, android.R.layout.simple_list_item_1, arrayList){
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
            adapter = new ArrayAdapter<String>(ElegirAsignaturas.this, android.R.layout.simple_list_item_1, arrayList);
        }
        viewData();

        //onBtnClick();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et.setText(arrayList.get(position));
                position1=position;
                bt.setText("Modificar");
                agregar=0;
                modificarAux=arrayList.get(position);
            }
        });
    }

    /*public void onBtnClick(){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = et.getText().toString();
                arrayList.add(result);
                adapter.notifyDataSetChanged();
                et.setText("");
            }
        });

    }*/

    /*public void guardar(View view){
        FileOutputStream fos = null;

        try {
            fos = openFileOutput("asignaturas_listado.txt", MODE_PRIVATE);
            for (int i=0; i<arrayList.size(); i++){
                fos.write(arrayList.get(i).getBytes());
                fos.write("\n".getBytes());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }*/

    /**
     * Función que introduce o modifica una asignatura en la BBDD y tiene control de errores
     *
     * @param view the view
     */
    public void Registrar(View view) {

        if (agregar==1) {
            String asignatura = et.getText().toString();
            if (asignatura.length() > 25) {
                Toast.makeText(this, "La asignatura es demasiado larga", Toast.LENGTH_LONG).show();
            } else {

                if (db.buscar(asignatura)) {

                    if (!asignatura.equals("") && db.insertData(asignatura)) {
                        Toast.makeText(this, "Se ha introducido correctamente", Toast.LENGTH_LONG).show();

                        arrayList.add(asignatura);
                        adapter.notifyDataSetChanged();
                        lv.setAdapter(adapter);
                        et.setText("");
                    } else {
                        Toast.makeText(this, "Debe escribir una asignatura", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "La asignatura ya existe", Toast.LENGTH_LONG).show();
                }
            }
        }else{


            String asignatura = et.getText().toString();
            if (asignatura.length() > 25) {
                Toast.makeText(this, "La asignatura es demasiado larga", Toast.LENGTH_LONG).show();
            } else {

                if (db.buscar(asignatura)) {

                    if (!asignatura.equals("") && db.Modificar(asignatura, modificarAux)) {
                        Toast.makeText(this, "Se ha modificado correctamente", Toast.LENGTH_LONG).show();

                        arrayList.set(position1, asignatura);
                        adapter.notifyDataSetChanged();
                        lv.setAdapter(adapter);
                        et.setText("");
                        bt.setText("Agregar");
                        agregar=1;
                    } else {
                        Toast.makeText(this, "Debe escribir una asignatura", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "La asignatura ya existe", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Función que elimina una asignatura de la BBDD y tiene control de errores
     *
     * @param view the view
     */
    public void Eliminar(View view){


        String asignatura = et.getText().toString();
        if(!db.buscar(asignatura) || asignatura.equals("")) {
            if (!asignatura.equals("") && db.Eliminar(asignatura)) {
                Toast.makeText(this, "Se ha eliminado correctamente", Toast.LENGTH_LONG).show();

                arrayList.remove(position1);
                adapter.notifyDataSetChanged();
                et.setText("");
                bt.setText("AGREGAR");
                agregar=1;


            } else {
                Toast.makeText(this, "Debe seleccionar una asignatura", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "La asignatura a eliminar no existe", Toast.LENGTH_LONG).show();
            et.setText("");
        }
    }

    /**
     * Comprobar que no hay ninguna asignatura en la BBDD
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
                adapter = new ArrayAdapter<String>(ElegirAsignaturas.this, android.R.layout.simple_list_item_1, arrayList){
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
                adapter = new ArrayAdapter<String>(ElegirAsignaturas.this, android.R.layout.simple_list_item_1, arrayList);
            }
            lv.setAdapter(adapter);
        }

    }


    /*public void ver(View view) throws IOException {
        String data = "";
        StringBuffer sbuffer = new StringBuffer();

        InputStream is = this.getResources().openRawResource(R.raw.asignaturas_listado);

        BufferedReader reader = new BufferedReader( new InputStreamReader(is));

        data=reader.readLine();

        sbuffer.append(data + "n");

        et.setText(sbuffer);
    }*/
}
