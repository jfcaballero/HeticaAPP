import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.hetica.AutismoCordoba.AdminSQLiteOpenHelperStats
import java.text.SimpleDateFormat
import java.util.Date

class AdminSQLiteOpenHelperComentarios(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, 1) {

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
     * Función para visualizar todas los comentarios en pantalla
     *
     * @return cursor
     */
    fun viewData(): Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $DB_TABLE"
        return db.rawQuery(query, null)
    }
    /**
     * Función para insertar en la base de datos
     * @param fecha Fecha del comentario
     * @param asignaturas Asignatura comentada
     * @param comentario Texto del comentario
     * @return boolean
     */
    fun insertData(fecha: String?, asignatura: String?, comentario: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DATE, fecha)
        contentValues.put(NAME, asignatura)
        contentValues.put(COMMENTS, comentario)
        val result = db.insert(DB_TABLE, null, contentValues)
        Log.d("Comentario base de datos","Fecha $fecha para $asignatura y dice $comentario")
        db.close()
        return result != -1L
    }
    /**
     * Función para eliminar en la base de datos
     * @param asignatura Asignatura a la que se quieren borrar sus comentarios
     * @return boolean
     */
    fun Eliminar(asignatura: String): Boolean {
        val db = this.writableDatabase
        val cantidad = db.delete(DB_TABLE, "NAME= ?", arrayOf(asignatura)).toLong()
        db.close()
        return cantidad != -1L
    }
    /**
     * Función para obtener una lista con la fecha y comentarios de una asignatura
     *
     * @param asignatura Nombre de la asignatura
     * @return Lista de pares Fecha-comentario
     */
    @SuppressLint("Range")
    fun getCommentsForSubject(asignatura: String): List<Pair<String, String>> {
        val db = this.readableDatabase
        val query = "SELECT $DATE, $COMMENTS FROM $DB_TABLE WHERE $NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(asignatura))
        val commentsList = mutableListOf<Pair<String, String>>()

        if (cursor.moveToFirst()) {
            do {
                val fecha = cursor.getString(cursor.getColumnIndex(DATE))
                val comentario = cursor.getString(cursor.getColumnIndex(COMMENTS))
                commentsList.add(Pair(fecha, comentario))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return commentsList
    }
    /**
     * Función para actualizar el nombre de una asignatura en la base de datos
     * @param oldName
     * @param newName
     */
    fun updateSubjectName(oldName: String, newName: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, newName)

        val whereClause = "$NAME = ?"
        val whereArgs = arrayOf(oldName)

        db.update(DB_TABLE, contentValues, whereClause, whereArgs)
    }


    /**
     * Función para borrar todos los comentarios de una asignatura
     * @param nombreAsignatura Asignatura
     * @return boolean
     */
    fun borrarComentariosAsignatura(nombreAsignatura: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "$NAME = ?"
        val whereArgs = arrayOf(nombreAsignatura)
        val cantidad = db.delete(DB_TABLE, whereClause, whereArgs).toLong()
        db.close()
        return cantidad != -1L
    }


    /**
     * Función para borrar los datos de la tabla
     *
     *
     */
    fun clearData(): Boolean {
        val db = this.writableDatabase
        val result = db.delete(DB_TABLE, null, null)
        return result > 0
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
