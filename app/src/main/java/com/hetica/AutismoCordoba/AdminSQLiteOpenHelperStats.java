package com.hetica.AutismoCordoba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * The type Admin sq lite open helper stats.
 */
public class AdminSQLiteOpenHelperStats extends SQLiteOpenHelper {

    private static final String DB_NAME = "Stats.db";
    private static final String DB_TABLE = "Stats_Table";

    private static final String ID= "ID";
    private static final String NAME = "NAME";
    private static final String DATE = "DATE";
    private static final String TIME = "TIME";

    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT ," + DATE + " STRING ," + TIME + " INTEGER " + ")";

    /**
     * Instantiates a new Admin sq lite open helper stats.
     *
     * @param context the context
     * @param name    the name
     * @param factory the factory
     * @param version the version
     */
    public AdminSQLiteOpenHelperStats(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    /**
     * Función para insertar una estadística
     *
     * @param name Nombre de la asignatura a agregar en las estadísticas
     * @param date Fecha de la asignatura a agregar en las estadísticas
     * @param time Tiempo de la asignatura a agregar en las estadísticas
     * @return boolean
     */
    public boolean insertData(String name, String date, Integer time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(DATE, date);
        contentValues.put(TIME, time);

        Log.e("Date Selected", date);

        long result = db.insert(DB_TABLE, null, contentValues);
        db.close();
        return result != -1;
    }

    /**
     * Función para eliminar una estadistica
     *
     * @param name Nombre de la estadistica a eliminar
     * @return boolean
     */
    public boolean Eliminar(String name){
        SQLiteDatabase db = this.getWritableDatabase();

        long cantidad = db.delete(DB_TABLE, "NAME= '"+name + "';", null);

        return cantidad != -1;

    }

    /**
     * Modificar boolean.
     *
     * @param name Nombre de la asignatura a modificar en las estadísticas
     * @param date Fecha de la asignatura a modificar en las estadísticas
     * @param time Tiempo de la asignatura a modificar en las estadísticas
     * @return boolean
     */
    public boolean Modificar(String name, Integer date, Integer time){
        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(DATE, date);
        contentValues.put(TIME, time);

        int cantidad = db.update(DB_TABLE, contentValues, "NAME= '"+name + "';", null);

        return cantidad != -1;
    }

    /**
     * View data dias cursor.
     *
     * @param date Fecha de la que queremos visualizar las estadísticas
     * @return cursor
     */
    public Cursor viewDataDias(String date){
        SQLiteDatabase db = this.getReadableDatabase();

        String query ="Select * from "+ DB_TABLE + " where DATE='" + date+ "'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

}
