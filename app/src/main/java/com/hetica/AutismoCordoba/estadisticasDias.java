package com.hetica.AutismoCordoba;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * The type Estadisticas dias.
 */
public class estadisticasDias extends AppCompatActivity {

    /**
     * The Db.
     */
    AdminSQLiteOpenHelperStats db;

    /**
     * The Array list.
     */
    ArrayList<String> arrayList;
    /**
     * The Adapter.
     */
    ArrayAdapter<String> adapter;

    /**
     * The Lv.
     */
    ListView lv;

    /**
     * The Year final.
     */
    String yearFinal;
    /**
     * The Month final.
     */
    String monthFinal;
    /**
     * The Day final.
     */
    String dayFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas_dias);

        final EditText editText = (EditText) findViewById(R.id.editText2);

        db = new AdminSQLiteOpenHelperStats(this, "Stats.db", null, 1);
        lv = (ListView) findViewById(R.id.listViewDias);

        arrayList = new ArrayList<String>();

        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            adapter = new ArrayAdapter<String>(estadisticasDias.this, android.R.layout.simple_list_item_1, arrayList){
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
            adapter = new ArrayAdapter<String>(estadisticasDias.this, android.R.layout.simple_list_item_1, arrayList);
        }
        lv.setAdapter(adapter);

        Calendar mcurrentDate=Calendar.getInstance();
        int yearAux = mcurrentDate.get(Calendar.YEAR);
        int monthAux = mcurrentDate.get(Calendar.MONTH);
        int dayAux = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        monthAux=monthAux +1;
        //yearFinal = Integer.toString(monthAux) + Integer.toString(dayAux) + Integer.toString(yearAux);
        if(monthAux<10){
            yearFinal="0"+ Integer.toString(monthAux);
        }else
            {
                yearFinal =Integer.toString(monthAux);
            }

        if(dayAux<10){
            yearFinal=yearFinal + "0";
        }
        yearFinal=yearFinal + Integer.toString(dayAux) + Integer.toString(yearAux);

        editText.setText(dayAux + "/" + monthAux + "/" + yearAux);
        viewData();

        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                Calendar mcurrentDate=Calendar.getInstance();
                int year = mcurrentDate.get(Calendar.YEAR);
                int month = mcurrentDate.get(Calendar.MONTH);
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                //month=month +1;
                //yearFinal = Integer.toString(month) + Integer.toString(day) + Integer.toString(year);

                if(month<10){
                    yearFinal="0"+ Integer.toString(month);
                }else
                {
                    yearFinal =Integer.toString(month);
                }
                if(day<10){
                    yearFinal=yearFinal + "0";
                }
                yearFinal=yearFinal + Integer.toString(day) + Integer.toString(year);

                DatePickerDialog mDatePicker=new DatePickerDialog(estadisticasDias.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        Log.e("Date Selected", "Month: " + selectedMonth + " Day: " + selectedDay + " Year: " + selectedYear);
                        selectedMonth=selectedMonth+1;
                        editText.setText(selectedDay + "/" + selectedMonth + "/" + selectedYear);

                        if(selectedMonth<10){
                            yearFinal="0"+ Integer.toString(selectedMonth);
                        }else
                        {
                            yearFinal =Integer.toString(selectedMonth);
                        }
                        if(selectedDay<10){
                            yearFinal=yearFinal + "0";
                        }
                               yearFinal=yearFinal + Integer.toString(selectedDay) + Integer.toString(selectedYear);
                        viewData();
                    }
                },year, month, day);


                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }

    /**
     * Función que devuelve las estadísticas de una fecha en concreto
     *
     */
    private void viewData(){

        Log.e("Date Selected", yearFinal);

        Cursor cursor = db.viewDataDias(yearFinal);

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
            if ((getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) ==
                    Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                adapter = new ArrayAdapter<String>(estadisticasDias.this, android.R.layout.simple_list_item_1, arrayList){
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
                adapter = new ArrayAdapter<String>(estadisticasDias.this, android.R.layout.simple_list_item_1, arrayList);
            }
            lv.setAdapter(adapter);
        }

    }
}
