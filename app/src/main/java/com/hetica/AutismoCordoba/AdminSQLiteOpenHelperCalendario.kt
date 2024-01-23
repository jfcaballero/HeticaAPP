import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

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
     * Función para insertar en la base de datos
     * @param dateString Fecha de las asignaturas
     * @param asignaturas Lista de asignaturas asociadas a esa fecha
     * @return boolean
     */
    fun insertData(dateString: String, asignaturas: List<Pair<String, Int>>): Boolean {
        val db = this.writableDatabase
        for ((asignatura, tiempo) in asignaturas) {
            val contentValues = ContentValues()
            contentValues.put(DATE, dateString)
            contentValues.put(ASIGNATURAS, asignatura)
            contentValues.put(TIEMPO, tiempo)
            db.insert(DB_TABLE, null, contentValues)
        }
        return true
    }

    /**
     * Función para visualizar todas las asignaturas en pantalla
     *
     * @return cursor
     */
    fun viewData(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $DB_TABLE", null)
    }

    /**
     * Función para obtener una lista de las asignaturas de una fecha en concreto
     * @param date Fecha de las asignaturas que se quieren extraer
     * @return asignaturasList
     */
    @SuppressLint("Range")
    fun getAsignaturasForDayWithMinutos(date: String): List<Pair<String, Int>> {
        val db = this.readableDatabase
        val asignaturasList = mutableListOf<Pair<String, Int>>()
        val query = "SELECT $ASIGNATURAS, $TIEMPO FROM $DB_TABLE WHERE $DATE = ?"
        val cursor = db.rawQuery(query, arrayOf(date))
        if (cursor.moveToFirst()) {
            do {
                val asignatura = cursor.getString(cursor.getColumnIndex(ASIGNATURAS))
                val minutos = cursor.getInt(cursor.getColumnIndex(TIEMPO))
                asignaturasList.add(Pair(asignatura, minutos))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return asignaturasList
    }


    /**
     * Función para borrar los datos de la tabla
     *
     *
     */
    fun borrarTabla() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
        onCreate(db)
    }


    /**
     * Función para borrar una asignatura por su posición
     * @param position Posición que ocupa la asignatura en la base de datos
     * @param dateString Fecha de la asignatura
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


    companion object {

        private const val DB_NAME = "Calendario.db"
        private const val DB_TABLE = "Calendario_Table"
        private const val DB_VERSION = 2
        private const val ID = "ID"
        private const val DATE = "Date"
        private const val ASIGNATURAS = "Asignaturas"
        private const val TIEMPO = "Tiempo"
        private const val CREATE_TABLE =
            "CREATE TABLE $DB_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $DATE DATE, $ASIGNATURAS TEXT, $TIEMPO INTEGER)"

    }
}
