import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class AdminSQLiteOpenHelperCalificaciones(
    context: Context?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, DB_NAME, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
        onCreate(db)
    }

    fun viewData(): Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $DB_TABLE"
        return db.rawQuery(query, null)
    }

    fun clearData(): Boolean {
        val db = this.writableDatabase
        val result = db.delete(DB_TABLE, null, null)
        return result > 0
    }
    /**
     * Función para insertar una calificación nueva
     **/
    fun insertData(subject: String?, grade: String, type: String?, date: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SUBJECT, subject)
        contentValues.put(GRADE, grade)
        contentValues.put(TYPE, type)
        contentValues.put(DATE, date) // Usar la fecha proporcionada
        val result = db.insert(DB_TABLE, null, contentValues)
        return result != -1L
    }


    /**
     * Función para obtener la nota de una asignatura dados su nombre y tipo
     **/
    @SuppressLint("Range")
    fun getSubjectGradesList(asignatura: String, tipo: String): List<Pair<String, Float>> {
        val db = this.readableDatabase
        val query = "SELECT $SUBJECT, $GRADE, $DATE FROM $DB_TABLE WHERE $SUBJECT = ? AND $TYPE = ? ORDER BY $DATE DESC, $ID DESC"
        val cursor = db.rawQuery(query, arrayOf(asignatura, tipo))
        val subjectGradesList = mutableListOf<Pair<String, Float>>()

        if (cursor.moveToFirst()) {
            do {
                val grade = cursor.getFloat(cursor.getColumnIndex(GRADE))
                val date = cursor.getString(cursor.getColumnIndex(DATE))
                val formattedDate = formatDate(date)
                subjectGradesList.add(Pair(formattedDate, grade))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return subjectGradesList.reversed()
    }

    /**
     * Función para obtener todos los datos de una calificación dada la asignatura
     **/
    @SuppressLint("Range")
    fun getSubjectGradesForSubject(asignatura: String): MutableList<String> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $DB_TABLE WHERE $SUBJECT = ?"
        val cursor = db.rawQuery(query, arrayOf(asignatura))
        val subjectGradesList = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            do {
                //val subject = cursor.getString(cursor.getColumnIndex(SUBJECT))
                val date = cursor.getString(cursor.getColumnIndex(DATE))
                val type = cursor.getString(cursor.getColumnIndex(TYPE))
                val grade = cursor.getFloat(cursor.getColumnIndex(GRADE))
                val id = cursor.getInt(cursor.getColumnIndex(ID))
                val entry = "$date | $type | $grade | $id "
                //val entry = "$date | $type | $grade | $id "
                subjectGradesList.add(entry)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return subjectGradesList
    }
    /**
     * Función para eliminar una asignatura dados sus atributos
     **/
    fun deleteDataByDetails(
        date: String, subject: String, type: String, grade: String, id: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$DATE = ? AND $SUBJECT = ? AND $TYPE = ? AND $GRADE = ? AND $ID=?"
        val whereArgs = arrayOf(date, subject, type, grade,id)
        //Log.d("enBaseDeDatos","Los args son: $date,$subject,$type,$grade y $count")
        // Log para verificar el estado antes de la eliminación
        val rowsBefore = DatabaseUtils.queryNumEntries(db, DB_TABLE, "$DATE = ? AND $SUBJECT = ? AND $TYPE = ? AND $GRADE = ? AND $ID=?", whereArgs)
        //Log.d("deleteDataByDetails", "Rows before deletion: $rowsBefore")

        // Eliminar todas las filas que coincidan con los criterios
        val result = db.delete(DB_TABLE, whereClause, whereArgs)

        // Log después de la eliminación
        //val rowsAfter = DatabaseUtils.queryNumEntries(db, DB_TABLE, "$DATE = ? AND $SUBJECT = ? AND $TYPE = ? AND $GRADE = ? AND $ID=?", whereArgs)
        //Log.d("deleteDataByDetails", "Deleted $result rows. Expected: $rowsBefore, Rows after deletion: $rowsAfter")

        if (result > 0) {
            Log.e("deleteDataByDetails", "$result >0, fila eliminada con éxito")
            return true  // Al menos una fila se eliminó con éxito
        } else {
            Log.e("deleteDataByDetails", "Error al eliminar: $rowsBefore")
            return false  // No se eliminaron filas
        }
    }



    /**
    * Esta función es para mostrar la fecha en formato dd/MM para la gráfica
    **/
    private fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("MMddyyyy", Locale.getDefault()) // Cambiar el formato de entrada a MMddyyyy
        val dateObj = inputFormat.parse(date)
        val outputFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return outputFormat.format(dateObj as Date)
    }


    companion object {
        private const val DB_NAME = "Calificaciones.db"
        private const val DB_TABLE = "Calificaciones_Table"
        private const val ID = "ID"
        private const val SUBJECT = "SUBJECT"
        private const val GRADE = "GRADE"
        private const val TYPE = "TYPE"
        private const val DATE = "DATE" 
        //private const val DB_VERSION = 3
        private const val CREATE_TABLE = "CREATE TABLE $DB_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $SUBJECT TEXT, $GRADE FLOAT, $TYPE TEXT, $DATE TEXT)"
    }
}
