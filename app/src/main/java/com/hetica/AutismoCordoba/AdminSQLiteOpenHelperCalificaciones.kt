import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class AdminSQLiteOpenHelperCalificaciones(
    context: Context?,
    name: String?,
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

    fun insertData(subject: String?, grade: Float, type: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SUBJECT, subject)
        contentValues.put(GRADE, grade)
        contentValues.put(TYPE, type)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())
        contentValues.put(DATE, currentDate)
        val result = db.insert(DB_TABLE, null, contentValues)
        return result != -1L
    }

    @SuppressLint("Range")
    fun getSubjectGradesList(asignatura: String, tipo: String): List<Pair<String, Float>> {
        val db = this.readableDatabase
        val query = "SELECT $SUBJECT, $GRADE, $DATE FROM $DB_TABLE WHERE $SUBJECT = ? AND $TYPE = ?"
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
    private fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateObj = inputFormat.parse(date)
        val outputFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        return outputFormat.format(dateObj)
    }

    companion object {
        private const val DB_NAME = "Calificaciones.db"
        private const val DB_TABLE = "Calificaciones_Table"
        private const val ID = "ID"
        private const val SUBJECT = "SUBJECT"
        private const val GRADE = "GRADE"
        private const val TYPE = "TYPE"
        private const val DATE = "DATE" // New field for date
        private const val DB_VERSION = 3
        private const val CREATE_TABLE = "CREATE TABLE $DB_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $SUBJECT TEXT, $GRADE FLOAT, $TYPE TEXT, $DATE TEXT)"
    }
}
