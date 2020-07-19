package com.hetica.AutismoCordoba;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The type Admin sq lite open helper asig.
 */
public class AdminSQLiteOpenHelperAsig extends SQLiteOpenHelper {

    private static final String DB_NAME = "Asig.db";
    private static final String DB_TABLE = "Asig_Table";

    private static final String ID= "ID";
    private static final String NAME = "NAME";

    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT " + ")";

    /**
     * Instantiates a new Admin sq lite open helper asig.
     *
     * @param context the context
     * @param name    the name
     * @param factory the factory
     * @param version the version
     */
    public AdminSQLiteOpenHelperAsig(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
     * Función para visualizar todas las asignaturas en pantalla
     *
     * @return cursor
     */
    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query ="Select * from "+ DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    /**
     * Función para agregar una asignatura a la base de datos
     *
     * @param name Nombre de la asignatura a insertar
     * @return boolean
     */
    public boolean insertData(String name){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);

        long result = db.insert(DB_TABLE, null, contentValues);
        db.close();
        return result != -1;
    }

    /**
     * Función para agregar una asignatura a la base de datos
     *
     * @param name Nombre de la asignatura a eliminar
     * @return boolean
     */
    public boolean Eliminar(String name){

            SQLiteDatabase db = this.getWritableDatabase();

            long cantidad = db.delete(DB_TABLE, "NAME= '"+name + "';", null);
            db.close();
            return cantidad != -1;

    }

    /**
     * Función para modificar una asignatura a la base de datos
     *
     * @param name   Nombre modificado de la asignatura
     * @param nameID Nombre de la asignatura a modificar
     * @return boolean
     */
    public boolean Modificar(String name, String nameID){
        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues registro = new ContentValues();
        registro.put(NAME, name);

        int cantidad = db.update(DB_TABLE, registro, "NAME= '"+nameID + "';", null);

        return cantidad != -1;
    }

    /**
     * Funcuón para buscar una asignatura en concreto
     *
     * @param name Nombre de la asignatura a buscar
     * @return boolean
     */
    public boolean buscar(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        String query ="Select * from "+ DB_TABLE + " where NAME='" + name + "'";
        Cursor cursor = db.rawQuery(query,null);

        int aux =cursor.getCount();

        return aux == 0;
    }
}
