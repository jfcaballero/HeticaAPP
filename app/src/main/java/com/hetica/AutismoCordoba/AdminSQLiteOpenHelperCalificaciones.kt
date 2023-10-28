import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.hetica.AutismoCordoba.AdminSQLiteOpenHelperStats

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
        val result = db.insert(DB_TABLE, null, contentValues)
        return result != -1L
    }


    @SuppressLint("Range")
    fun getSubjectGradesList(asignatura: String, tipo: String): List<Pair<String, Float>> {
        val db = this.readableDatabase
       // val query = "SELECT $SUBJECT, $GRADE FROM $DB_TABLE WHERE $SUBJECT = '$asignatura' AND $TYPE = '$tipo'"
        val query = "SELECT $SUBJECT, $GRADE FROM $DB_TABLE"
        val cursor = db.rawQuery(query, null)
        val subjectGradesList = mutableListOf<Pair<String, Float>>()

        if (cursor.moveToFirst()) {
            do {
                val subject = cursor.getString(cursor.getColumnIndex(SUBJECT))
                val grade = cursor.getFloat(cursor.getColumnIndex(GRADE))
                subjectGradesList.add(Pair(subject, grade))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return subjectGradesList
    }





    companion object {
        private const val DB_NAME = "Calificaciones.db"
        private const val DB_TABLE = "Calificaciones_Table"
        private const val ID = "ID"
        private const val SUBJECT = "SUBJECT"
        private const val GRADE = "GRADE"
        private const val TYPE = "TYPE"
        private const val DB_VERSION = 2
        private const val CREATE_TABLE = "CREATE TABLE $DB_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $SUBJECT TEXT, $GRADE FLOAT, $TYPE TEXT)"
    }
}
