import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AdminSQLiteOpenHelperCalendario(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }
    /**
     * Función para mejorar la base de datos
     * @param db
     * @param oldVersion Número de la versión anterior
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
        onCreate(db)
    }
    /**
     * Función para agregar una lista de asignaturas asociadas a una fecha y con un tiempo cada una.
     *
     * @param dateString Fecha del calendario
     * @param asignaturas Asignaturas asociadas a la fecha con sus minutos
     * @return boolean
     */
    fun insertData(dateString: String, asignaturas: List<Pair<String, Int>>): Boolean {
        val db = this.writableDatabase
        for ((asignatura, tiempo) in asignaturas) {
            val contentValues = ContentValues()
            contentValues.put(DATE, dateString)
            contentValues.put(ASIGNATURAS, asignatura)
            contentValues.put(TIEMPO, tiempo)
            contentValues.put(ESTUDIADO, 0) // Por defecto, no estudiado
            db.insert(DB_TABLE, null, contentValues)
            Log.d("Insertando en calendario","$asignatura,$dateString,$tiempo")
        }
        return true
    }
    /**
     * Función para visualizar todas las asignaturas en pantalla.
     *
     * @return cursor
     */
    fun viewData(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $DB_TABLE", null)
    }
    /**
     * Función para obtener una lista con las asignaturas, el tiempo de cada una y el inidicador de si se ha estudiado.
     * @param date Fecha de la lista
     * @return asignaturasList
     */
    @SuppressLint("Range")
    fun getAsignaturasForDayWithMinutos(date: String): List<Triple<String, Int, Int>> {
        val db = this.readableDatabase
        val asignaturasList = mutableListOf<Triple<String, Int, Int>>()
        val query = "SELECT $ASIGNATURAS, $TIEMPO, $ESTUDIADO FROM $DB_TABLE WHERE $DATE = ?"
        val cursor = db.rawQuery(query, arrayOf(date))
        if (cursor.moveToFirst()) {
            do {
                val asignatura = cursor.getString(cursor.getColumnIndex(ASIGNATURAS))
                val minutos = cursor.getInt(cursor.getColumnIndex(TIEMPO))
                val estudiado = cursor.getInt(cursor.getColumnIndex(ESTUDIADO))
                asignaturasList.add(Triple(asignatura, minutos, estudiado))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return asignaturasList
    }

    /**
     * Función para marcar una asignatura como estudiada dada la fecha y la posición que ocupa en la lista de ésta
     * @param position Posición que ocupa la asignatura en la lista
     * @param dateString Fecha
     */
    fun marcarComoEstudiado(position: Int, dateString: String) {
        Log.d("Marcando", "posicion $position en fecha $dateString")
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ESTUDIADO, 1) // Marcar como estudiado (1)
        val whereClause = "$DATE = ? AND $ID IN (SELECT $ID FROM $DB_TABLE WHERE $DATE = ? LIMIT 1 OFFSET ?)"
        val whereArgs = arrayOf(dateString, dateString, position.toString())
        db.update(DB_TABLE, contentValues, whereClause, whereArgs)
    }

    /**
     * Función para eliminar la tabla
     */
    fun borrarTabla() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
        onCreate(db)
    }

    /**
     * Función para actualizar el nombre de una asignatura en la base de datos
     * @param oldName
     * @param newName
     */
    fun updateAsignaturaName(oldName: String, newName: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ASIGNATURAS, newName)

        val whereClause = "$ASIGNATURAS = ?"
        val whereArgs = arrayOf(oldName)

        db.update(DB_TABLE, contentValues, whereClause, whereArgs)
    }

    /**
     * Función para eliminar una asignatura dada la posición que ocupa en la lista y la fecha
     *
     * @param position Posición que ocupa la asignatura en la lista
     * @param dateString Fecha
     * @return boolean
     */
    @SuppressLint("Range")
    fun deleteAsignaturaByPosition(position: Int, dateString: String): Boolean {
        val db = this.writableDatabase
        val query = "SELECT $ID FROM $DB_TABLE WHERE $DATE = ? LIMIT 1 OFFSET ?"
        val cursor = db.rawQuery(query, arrayOf(dateString, position.toString()))

        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndex(ID))
            val whereClause = "$ID = ?"
            val whereArgs = arrayOf(id.toString())
            val result = db.delete(DB_TABLE, whereClause, whereArgs)
            cursor.close()
            return result != -1
        }
        cursor.close()
        return false
    }

    /**
     * Función para borrar todas las sesiones programadas para una asignatura en la base de datos
     * @param asignatura Asignatura cuyas sesiones se desean borrar
     */
    fun deleteAsignaturaFromAllDates(asignatura: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$ASIGNATURAS = ?"
        val whereArgs = arrayOf(asignatura)
        val result = db.delete(DB_TABLE, whereClause, whereArgs)
        return result != -1
    }


    companion object {
        private const val DB_NAME = "Calendario.db"
        private const val DB_TABLE = "Calendario_Table"
        private const val DB_VERSION = 3
        private const val ID = "ID"
        private const val DATE = "Date"
        private const val ASIGNATURAS = "Asignaturas"
        private const val TIEMPO = "Tiempo"
        private const val ESTUDIADO = "Estudiado"
        private const val CREATE_TABLE =
            "CREATE TABLE $DB_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $DATE DATE, $ASIGNATURAS TEXT, $TIEMPO INTEGER, $ESTUDIADO INTEGER DEFAULT 0)"
    }
}
