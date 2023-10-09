package com.hetica.AutismoCordoba

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class estadisticasSemana : AppCompatActivity() {
    var lunes: String? = null
    var martes: String? = null
    var miercoles: String? = null
    var jueves: String? = null
    var viernes: String? = null
    var sabado: String? = null
    var domingo: String? = null
    var db: AdminSQLiteOpenHelperStats? = null
    var arrayList: ArrayList<String>? = null
    var adapter: ArrayAdapter<String>? = null
    var lv: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas_semana)
        db = AdminSQLiteOpenHelperStats(this, "Stats.db", null, 1)
        lv = findViewById<View>(R.id.listViewSemana) as ListView
        arrayList = ArrayList()
        adapter = ArrayAdapter(this@estadisticasSemana, android.R.layout.simple_list_item_1, arrayList!!)
        lv!!.adapter = adapter
        /*Calendar cal=Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
        if(dayOfWeek == 1){
            Date date = new Date();
            lunes=sdf.format(date);
            date.setTime(date.getTime()+1);
            martes=sdf.format(date);
        }*/
    } /*private void viewData(){



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