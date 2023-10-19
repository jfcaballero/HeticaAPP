import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelperComentarios(context: Context?, name: String?, factory: CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DB_NAME, null, 1) {

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

    fun insertData(fecha: String?, asignatura: String?, comentario: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DATE, fecha)
        contentValues.put(NAME, asignatura)
        contentValues.put(COMMENTS, comentario)
        val result = db.insert(DB_TABLE, null, contentValues)
        return result != -1L
    }

    fun Eliminar(asignatura: String): Boolean {
        val db = this.writableDatabase
        val cantidad = db.delete(DB_TABLE, "NAME= ?", arrayOf(asignatura)).toLong()
        db.close()
        return cantidad != -1L
    }

    fun Modificar(asignatura: String?, asignaturaID: String?, comentario: String?): Boolean {
        val db = this.writableDatabase
        val registro = ContentValues()
        registro.put(NAME, asignatura)
        registro.put(COMMENTS, comentario)
        val cantidad = db.update(DB_TABLE, registro, "NAME= ?", arrayOf(asignaturaID))
        return cantidad != -1
    }

    fun buscar(asignatura: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $DB_TABLE WHERE NAME=?"
        val cursor = db.rawQuery(query, arrayOf(asignatura))
        val aux = cursor.count
        return aux == 0
    }
    fun borrarTabla() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $DB_TABLE")
        onCreate(db)
    }
    companion object {
        private const val DB_NAME = "Comentarios.db"
        private const val DB_TABLE = "Comentarios_Table"
        private const val ID = "ID"
        private const val NAME = "NAME"
        private const val COMMENTS = "COMMENTS"
        private const val DATE = "DATE"
        private const val CREATE_TABLE = "CREATE TABLE $DB_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $DATE TEXT, $NAME TEXT, $COMMENTS TEXT)"
    }
}
