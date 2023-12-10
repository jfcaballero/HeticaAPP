package com.hetica.AutismoCordoba

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * The type Admin sq lite open helper stats.
 */
class AdminSQLiteOpenHelperStats
/**
 * Instantiates a new Admin sq lite open helper stats.
 *
 * @param context the context
 * @param name    the name
 * @param factory the factory
 * @param version the version
 */(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE)
        onCreate(db)
    }

    /**
     * Función para insertar una estadística
     *
     * @param name Nombre de la asignatura a agregar en las estadísticas
     * @param date Fecha de la asignatura a agregar en las estadísticas
     * @param time Tiempo de la asignatura a agregar en las estadísticas
     * @return boolean
     */
    fun insertData(name: String?, date: String?, time: Int?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, name)
        contentValues.put(DATE, date)
        contentValues.put(TIME, time)
        Log.e("Date Selected", date!!)
        val result = db.insert(DB_TABLE, null, contentValues)
        db.close()
        return result != -1L
    }

    /**
     * Función para eliminar una estadistica
     *
     * @param name Nombre de la estadistica a eliminar
     * @return boolean
     */
    fun Eliminar(name: String): Boolean {
        val db = this.writableDatabase
        val cantidad = db.delete(DB_TABLE, "NAME= '$name';", null).toLong()
        return cantidad != -1L
    }

    /**
     * Modificar boolean.
     *
     * @param name Nombre de la asignatura a modificar en las estadísticas
     * @param date Fecha de la asignatura a modificar en las estadísticas
     * @param time Tiempo de la asignatura a modificar en las estadísticas
     * @return boolean
     */
    fun Modificar(name: String, date: Int?, time: Int?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, name)
        contentValues.put(DATE, date)
        contentValues.put(TIME, time)
        val cantidad = db.update(DB_TABLE, contentValues, "NAME= '$name';", null)
        return cantidad != -1
    }
    /**
     * Modificar nombre de asignatura.
     *
     * @param name NombreAntiguo de la asignatura a modificar en las estadísticas
     * @param name NombreNuevo de la asignatura a modificar en las estadísticas
     * @return boolean
     */
    fun ModificarNombreAsignatura(nombreAntiguo: String?, nombreNuevo: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, nombreNuevo)

        // Utiliza la cláusula WHERE para identificar la asignatura que deseas actualizar
        val whereClause = "$NAME = ?"
        val whereArgs = arrayOf(nombreAntiguo)

        val cantidad = db.update(DB_TABLE, contentValues, whereClause, whereArgs)
        db.close()

        return cantidad != -1
    }


    /**
     * View data dias cursor.
     *
     * @param date Fecha de la que queremos visualizar las estadísticas
     * @return cursor
     */
    fun viewDataDias(date: String?): Cursor {
        val db = this.readableDatabase
        val query = "Select * from " + DB_TABLE + " where DATE='" + date + "'"

        return db.rawQuery(query, null)
    }

    @SuppressLint("Range")
    fun obtenerListaDiasOrdenadosPorMinutosEstudiadosEnUnMes(mes: String, anyo: String): List<Pair<String, Int>> {

        val db = this.readableDatabase
        val query =
            "SELECT DATE, SUM(TIME) as TotalMinutes FROM $DB_TABLE WHERE DATE LIKE '$mes%$anyo%' GROUP BY DATE ORDER BY TotalMinutes DESC"
        Log.d("SQL_QUERY", "Query: $query")

        val cursor = db.rawQuery(query, null)
        val listaDias = mutableListOf<Pair<String, Int>>()

        if (cursor.moveToFirst()) {
            do {
                val fecha = cursor.getString(cursor.getColumnIndex(DATE))
                val totalMinutos = cursor.getInt(cursor.getColumnIndex("TotalMinutes"))
                // Extraer solo los primeros dos caracteres que representan el día de la fecha
                val dia = fecha.substring(2, 4)
                listaDias.add(dia to totalMinutos) // Añadir solo el día al par
            } while (cursor.moveToNext())
        }

        Log.d("CURSOR_COUNT", "Count: ${cursor.count}")

        cursor.close()
        db.close()

        return listaDias
    }



    /**
     * Función para calcular el promedio de tiempo para una asignatura específica
     *
     * @param name Nombre de la asignatura para la que deseas calcular el promedio de tiempo
     * @return Double
     */
    @SuppressLint("Range")
    fun calcularPromedioAsignatura(name: String): Double {
        val db = this.readableDatabase
        val query = "SELECT * FROM $DB_TABLE WHERE $NAME = '$name'"
        val cursor = db.rawQuery(query, null)
        var totalTiempo = 0
        var contador = 0

        if (cursor.moveToFirst()) {
            do {
                val tiempo = cursor.getInt(cursor.getColumnIndex(TIME))
                totalTiempo += tiempo
                contador++
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        val promedio = if (contador > 0) {
            totalTiempo.toDouble() / contador
        } else {
            0.0
        }

        return promedio
    }



    companion object {
        private const val DB_NAME = "Stats.db"
        private const val DB_TABLE = "Stats_Table"
        private const val ID = "ID"
        private const val NAME = "NAME"
        private const val DATE = "DATE"
        private const val TIME = "TIME"
        private const val CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT ," + DATE + " STRING ," + TIME + " INTEGER " + ")"
    }
}