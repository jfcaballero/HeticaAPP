package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

/**
 * The type Admin sq lite open helper asig.
 */
class AdminSQLiteOpenHelperAsig
/**
 * Instantiates a new Admin sq lite open helper asig.
 *
 * @param context the context
 * @param name    the name
 * @param factory the factory
 * @param version the version
 */
(context: Context?, name: String?, factory: CursorFactory?, version: Int) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE)
        onCreate(db)
    }

    /**
     * Función para visualizar todas las asignaturas en pantalla
     *
     * @return cursor
     */
    fun viewData(): Cursor {
        val db = this.readableDatabase
        val query = "Select * from " + DB_TABLE
        return db.rawQuery(query, null)
    }

    /**
     * Función para agregar una asignatura a la base de datos
     *
     * @param name Nombre de la asignatura a insertar
     * @return boolean
     */
    fun insertData(name: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, name)
        val result = db.insert(DB_TABLE, null, contentValues)
        db.close()
        return result != -1L
    }

    /**
     * Función para eliminar una asignatura a la base de datos
     *
     * @param name Nombre de la asignatura a eliminar
     * @return boolean
     */
    fun Eliminar(name: String): Boolean {
        val db = this.writableDatabase
        val cantidad = db.delete(DB_TABLE, "NAME= '$name';", null).toLong()
        db.close()
        return cantidad != -1L
    }

    /**
     * Función para modificar una asignatura a la base de datos
     *
     * @param name   Nombre modificado de la asignatura
     * @param nameID Nombre de la asignatura a modificar
     * @return boolean
     */
    fun Modificar(name: String?, nameID: String?): Boolean {
        val db = this.writableDatabase
        val registro = ContentValues()
        registro.put(NAME, name)
        val cantidad = db.update(DB_TABLE, registro, "NAME= '$nameID';", null)

        return cantidad != -1
    }

    /**
     * Funcuón para buscar una asignatura en concreto
     *
     * @param name Nombre de la asignatura a buscar
     * @return boolean
     */
    fun buscar(name: String): Boolean {
        val db = this.readableDatabase
        val query = "Select * from " + DB_TABLE + " where NAME='" + name + "'"
        val cursor = db.rawQuery(query, null)
        val aux = cursor.count
        return aux == 0
    }
    /**
     * Funcuón para devolver una lista con todas las asignaturas
     *
     * @return List<String>
     */
    @SuppressLint("Range")
    fun getAsignaturasList(): List<String> {
        val asignaturasList = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $DB_TABLE"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(NAME))
                asignaturasList.add(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return asignaturasList
    }

    companion object {
        private const val DB_NAME = "Asig.db"
        private const val DB_TABLE = "Asig_Table"
        private const val ID = "ID"
        private const val NAME = "NAME"
        private const val CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT " + ")"
    }
}