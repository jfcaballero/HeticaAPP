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
        Log.d("Insertando stat db","Fecha $date, Asig: $name y min $time")
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
    /**
     * Función para ver los atributos dada una asignatura y la fecha.
     *
     * @param date Fecha de la que queremos visualizar las estadísticas
     * @param asignatura Asignatura de la que queremos visualizar las estadísticas
     * @return cursor
     */
    fun viewDataDiaAsignatura(date: String?,asignatura:String?): Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $DB_TABLE WHERE DATE='$date' AND NAME='$asignatura'"
        Log.d("Base de datos","Fecha $date y asign $asignatura")
        return db.rawQuery(query, null)

    }
    /**
     * Función para obtener las estadísticas de una asignatura que se encuentre entre 2 fechas.
     *
     * @param fechaInicio Fecha límite inicial
     * @param fechaFin Fecha límite final
     * @param asignatura Asignatura de la que queremos ver sus datos
     * @return cursor
     */
    @SuppressLint("Range")
    fun viewDataRangoAsignatura(fechaInicio: String, fechaFin: String, asignatura: String): Cursor {
        val db = this.readableDatabase
        val query =
            "SELECT * FROM $DB_TABLE WHERE DATE BETWEEN '$fechaInicio' AND '$fechaFin' AND NAME='$asignatura'"
        Log.d("Base de datos", "FechaInicio: $fechaInicio, FechaFin: $fechaFin, Asignatura: $asignatura")
        return db.rawQuery(query, null)
    }

    /**
     * Función para obtener todas las estadísticas de una asignatura desde su inicio.
     *
     * @param asignatura Asignatura de la que queremos ver sus datos
     * @return cursor
     */
    @SuppressLint("Range")
    fun viewDataHistorico(asignatura: String?): Cursor {
        val db = this.readableDatabase
        val query = "Select * from " + DB_TABLE + " where NAME='" + asignatura + "'"

        return db.rawQuery(query, null)
    }


    /**
     * Función para obtener una lista con días y el total de minutos que se dedicaron en cada día.
     *
     * @return listaDias
     */
    @SuppressLint("Range")
    fun obtenerListaDias(asignatura:String?,opcion:Int?, fechaInicial: String? = null, fechaFinal: String? = null): List<Pair<String, Int>> {
        val db = this.readableDatabase
        if (fechaInicial != null) {
            Log.d("Fecha Inicial: ",fechaInicial)
        }
        if (fechaFinal != null) {
            Log.d("Fecha Final: ",fechaFinal)
        }

        val query: String = when (opcion) {
            1 -> { //rango
                "SELECT DATE, SUM(TIME) as TotalMinutes FROM $DB_TABLE WHERE NAME = '$asignatura' AND DATE BETWEEN '$fechaInicial' AND '$fechaFinal' GROUP BY DATE"

            }
            2 -> { //historico
                "SELECT DATE, SUM(TIME) as TotalMinutes FROM $DB_TABLE WHERE NAME = '$asignatura' GROUP BY DATE"
            }
            else -> {
                // Otra opción: mes
                "SELECT DATE, SUM(TIME) as TotalMinutes FROM $DB_TABLE WHERE NAME = '$asignatura' GROUP BY DATE ORDER BY TotalMinutes DESC"
            }
        }

        Log.d("SQL_QUERY", "Query: $query")


        val cursor = db.rawQuery(query, null)
        val listaDias = mutableListOf<Pair<String, Int>>()

        if (cursor.moveToFirst()) {
            do {
                val fecha = cursor.getString(cursor.getColumnIndex(DATE))
                Log.d("Asi son las fechas: ",fecha)
                val totalMinutos = cursor.getInt(cursor.getColumnIndex("TotalMinutes"))

                Log.d("Fecha y Min","$fecha y $totalMinutos")

                // Asegurar que el día tiene dos dígitos
                val parts = fecha.split("/")
                val dia = parts[0].padStart(2, '0')


                //val mes = fecha.substring()

                listaDias.add(fecha to totalMinutos) // Añadir solo el día al par

                // Agregar log para mostrar la fecha de cada item en la lista
                Log.d("LIST_ITEM", "Fecha: $fecha, Día: $dia, TotalMinutos: $totalMinutos")
            } while (cursor.moveToNext())
        }

        Log.d("CURSOR_COUNT Historico", "Count: ${cursor.count}")

        cursor.close()
        db.close()

        return listaDias
    }
    fun borrarEstadisticasAsignatura(nombreAsignatura: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$NAME = ?"
        val whereArgs = arrayOf(nombreAsignatura)
        val cantidad = db.delete(DB_TABLE, whereClause, whereArgs).toLong()
        db.close()
        return cantidad != -1L
    }


    /**
     * Función para borrar la base de datos
     *
     * @return boolean
     */
    fun clearData(): Boolean {
        val db = this.writableDatabase
        val result = db.delete(DB_TABLE, null, null)
        return result > 0
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