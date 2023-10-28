import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class AdminSQLiteOpenHelperCalendario(context: Context, s: String, nothing: Nothing?, i: Int) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
        onCreate(db)
    }

    fun insertData(dateString: String, asignaturas: List<String>): Boolean {
        val db = this.writableDatabase
        for (asignatura in asignaturas) {
            val contentValues = ContentValues()
            contentValues.put(DATE, dateString)
            contentValues.put(ASIGNATURAS, asignatura)
            db.insert(DB_TABLE, null, contentValues)
        }
        return true
    }

    fun viewData(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $DB_TABLE", null)
    }
    @SuppressLint("Range")
    fun getAsignaturasForDay(date: String): List<String> {
        val db = this.readableDatabase
        val asignaturasList = mutableListOf<String>()
        val query = "SELECT $ASIGNATURAS FROM $DB_TABLE WHERE $DATE = ?"
        val cursor = db.rawQuery(query, arrayOf(date))
        if (cursor.moveToFirst()) {
            do {
                val asignatura = cursor.getString(cursor.getColumnIndex(ASIGNATURAS))
                asignaturasList.add(asignatura)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return asignaturasList
    }
    fun borrarTabla() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
        onCreate(db)
    }


    companion object {
        private const val DB_NAME = "Calendario.db"
        private const val DB_TABLE = "Calendario_Table"
        private const val DB_VERSION = 1
        private const val ID = "ID"
        private const val DATE = "Date"
        private const val ASIGNATURAS = "Asignaturas"
        private const val CREATE_TABLE =
            "CREATE TABLE $DB_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $DATE DATE, $ASIGNATURAS TEXT)"
    }
}
