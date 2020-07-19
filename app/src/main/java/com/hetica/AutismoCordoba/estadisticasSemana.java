package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class estadisticasSemana extends AppCompatActivity {

    String lunes;
    String martes;
    String miercoles;
    String jueves;
    String viernes;
    String sabado;
    String domingo;

    AdminSQLiteOpenHelperStats db;

    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas_semana);

        db = new AdminSQLiteOpenHelperStats(this, "Stats.db", null, 1);
        lv = (ListView) findViewById(R.id.listViewSemana);

        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(estadisticasSemana.this, android.R.layout.simple_list_item_1, arrayList);
        lv.setAdapter(adapter);
        /*Calendar cal=Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        if(dayOfWeek == 1){
            Date date = new Date();
            lunes=sdf.format(date);
            date.setTime(date.getTime()+1);
            martes=sdf.format(date);
        }*/

    }

    /*private void viewData(){



        Cursor cursor = db.viewDataSemana(yearFinal);

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No se trabajó este día", Toast.LENGTH_LONG).show();
            adapter.clear();
            adapter.notifyDataSetChanged();
        }else{
            adapter.clear();
            adapter.notifyDataSetChanged();
            while(cursor.moveToNext()){
                arrayList.add(cursor.getString(1) + "          " + cursor.getString(3) + " minutos");
            }
            adapter = new ArrayAdapter<String>(estadisticasSemana.this, android.R.layout.simple_list_item_1, arrayList);
            lv.setAdapter(adapter);
        }

    }
    private void getDias(){

    }*/
}
