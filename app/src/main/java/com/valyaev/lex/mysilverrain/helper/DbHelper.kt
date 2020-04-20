package com.valyaev.lex.mysilverrain.helper

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import android.provider.BaseColumns
import com.valyaev.lex.mysilverrain.model.ProgUrlObject
import com.valyaev.lex.mysilverrain.model.UrlObject
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

class DbHelper(
    val context: Context,
    DATABASE_VERSION: Int = 1,
    val DATABASE_NAME: String = "silver.db"
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    private fun installDatabaseFromAssets() {
        val inputStream = context.assets.open("$DB_PATH/$DATABASE_NAME")
        try {
            val outputFile = File(context.getDatabasePath(DATABASE_NAME).path)
            val outputStream = FileOutputStream(outputFile)

            inputStream.copyTo(outputStream)
            inputStream.close()

            outputStream.flush()
            outputStream.close()
        } catch (exception: Throwable) {
            throw RuntimeException("The $DATABASE_NAME database couldn't be installed.", exception)
        }


    }

    override fun getReadableDatabase(): SQLiteDatabase {
        installOrUpdateIfNecessary()
        return super.getReadableDatabase()
    }

    @Synchronized
    private fun installOrUpdateIfNecessary() {
        installDatabaseFromAssets()
    }


    companion object RadioProgramsContract {
        const val DB_PATH = "databases"

        const val SQL_CREATE_PROGRAM_TABLE =
            "CREATE TABLE ${ProgramEntry.TABLE_NAME} (" +

                    "${ProgramEntry.COLUMN_PROGRAMS_NAME} TEXT NOT NULL, " +
                    "${ProgramEntry.COLUMN_PROGRAMS_URL} TEXT UNIQUE NOT NULL, " +
                    "${ProgramEntry.COLUMN_ICON_ID} INTEGER NOT NULL, " +
                    "${ProgramEntry.COLUMN_NEXT_LOAD_PAGE} TEXT )"


        const val SQL_CREATE_BROADCAST_TABLE =
            " CREATE TABLE ${BroadcastEntry.TABLE_NAME} (" +

                    "${BroadcastEntry.COLUMN_BROADCAST_NAME} TEXT NOT NULL, " +
                    "${BroadcastEntry.COLUMN_BROADCAST_URL} TEXT UNIQUE NOT NULL, " +
                    "${BroadcastEntry.COLUMN_MP3_URL} TEXT NOT NULL, " +
                    "${BroadcastEntry.COLUMN_DATE} TEXT NOT NULL, " +
                    "${BroadcastEntry.COLUMN_PROGRAM_URL} TEXT NOT NULL," +
                    "${BroadcastEntry.COLUMN_ICON_ID} INTEGER NOT NULL )"

        const val SQL_CREATE_MP3_STORAGE_TABLE =
            " CREATE TABLE ${Mp3BaseEntry.TABLE_NAME} (" +
                    "${Mp3BaseEntry.COLUMN_BROADCAST_URL}TEXT UNIQUE NOT NULL, " +
                    "${Mp3BaseEntry.COLUMN_MP3}TEXT NOT NULL)"


        object ProgramEntry : BaseColumns {
            const val TABLE_NAME = "programs"
            const val COLUMN_PROGRAMS_NAME = "program_name"
            const val COLUMN_PROGRAMS_URL = "program_url"
            const val COLUMN_ICON_NAME = "icon_name"
            const val COLUMN_ICON_ID = "icon_id"
            const val COLUMN_NEXT_LOAD_PAGE = "next_load_page"
        }

        object BroadcastEntry : BaseColumns {
            const val TABLE_NAME = "broadcasts"
            const val COLUMN_PROGRAM_URL = "program_url"
            const val COLUMN_BROADCAST_NAME = "broadcast_name"
            const val COLUMN_BROADCAST_URL = "broadcast_url"
            const val COLUMN_MP3_URL = "mp3_url"
            const val COLUMN_DATE = "date"
            const val COLUMN_ICON_ID = "icon_id"
        }

        object Mp3BaseEntry : BaseColumns {
            const val TABLE_NAME = "mp3"
            const val COLUMN_BROADCAST_URL = "broadcast_url"
            const val COLUMN_MP3 = "mp3_"
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        /*if (db != null) {
            db.execSQL(SQL_CREATE_PROGRAM_TABLE)
            db.execSQL(SQL_CREATE_BROADCAST_TABLE)
            db.execSQL(SQL_CREATE_MP3_STORAGE_TABLE)
        }*/



    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    fun insert(urlObject: UrlObject) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ProgramEntry.COLUMN_PROGRAMS_NAME, urlObject.text)
            put(ProgramEntry.COLUMN_PROGRAMS_URL, urlObject.url)
            put(ProgramEntry.COLUMN_ICON_NAME, urlObject.iconName)
            put(ProgramEntry.COLUMN_ICON_ID, urlObject.iconID)
        }
        val newRowId = db?.insert(ProgramEntry.TABLE_NAME, null, values)
    }

    fun update(urlObject: UrlObject)
    {
        val db = writableDatabase

    }

    fun insert(progUrlObject: ProgUrlObject) {
        val db = writableDatabase
        val locale = Locale("ru")
        val sdf = SimpleDateFormat("(yyyy-MM-dd", locale)
        val dateToString = sdf.format(progUrlObject.date)
        val values = ContentValues().apply {
            put(BroadcastEntry.COLUMN_BROADCAST_NAME, progUrlObject.text)
            put(BroadcastEntry.COLUMN_BROADCAST_URL, progUrlObject.url)
            put(BroadcastEntry.COLUMN_DATE, dateToString)
            put(BroadcastEntry.COLUMN_MP3_URL, progUrlObject.mp3)
            put(BroadcastEntry.COLUMN_PROGRAM_URL, progUrlObject.programs_url)
            put(BroadcastEntry.COLUMN_ICON_ID, progUrlObject.imgID)

        }
        val newRowId = db?.insert(BroadcastEntry.TABLE_NAME, null, values)
    }

    fun getAllPrograms(): ArrayList<UrlObject> {
        val result = arrayListOf<UrlObject>()
        val db = readableDatabase
        val cursor =
            db.query(ProgramEntry.TABLE_NAME, null, null, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val progName =
                    cursor.getString(cursor.getColumnIndex(ProgramEntry.COLUMN_PROGRAMS_NAME))
                val progUrl =
                    cursor.getString(cursor.getColumnIndex(ProgramEntry.COLUMN_PROGRAMS_URL))
                val progIconString = cursor.getString(cursor.getColumnIndex(ProgramEntry.COLUMN_ICON_NAME))

                result.add(UrlObject(text = progName, url = progUrl, iconName = progIconString))
            }
        }


        return result
    }

    fun getAllBroadcasts(urlProgram: String): ArrayList<ProgUrlObject> {
        val result = arrayListOf<ProgUrlObject>()
        val db = readableDatabase
        val cursor =
            db.query(BroadcastEntry.TABLE_NAME, null, null, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val brName =
                    cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_BROADCAST_NAME))
                val brUrl =
                    cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_BROADCAST_URL))
                val brMp3Url =
                    cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_MP3_URL))
                val brProgUrl =
                    cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_PROGRAM_URL))
                val brDateString =
                    cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_DATE))
                val locale = Locale("ru")
                val sdf = SimpleDateFormat("yyyy-MM-dd", locale)
                val brDate = sdf.parse(brDateString)
                val brIconID = cursor.getInt(cursor.getColumnIndex(BroadcastEntry.COLUMN_ICON_ID))
                result.add(ProgUrlObject(brName, brProgUrl, brUrl, brIconID, brMp3Url, brDate))
            }
        }

        return result
    }

    fun setLastLoadPage(urlProgram: String, nextLoadPage: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ProgramEntry.COLUMN_NEXT_LOAD_PAGE, nextLoadPage)
        }
        val selection = ProgramEntry.COLUMN_PROGRAMS_URL + "=?"
        val selectionArgs = arrayOf(urlProgram)
        db.update(ProgramEntry.TABLE_NAME, values, selection, selectionArgs)

    }

    fun getLastLoadPage(urlProgram: String): String {
        val db = readableDatabase
        val cursor = db.query(ProgramEntry.TABLE_NAME, null, null, null, null, null, null)

        return ""
    }

    fun insertMp3InternalStorage(urlProgram: String, mp3Path: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Mp3BaseEntry.COLUMN_BROADCAST_URL, urlProgram)
            put(Mp3BaseEntry.COLUMN_MP3, mp3Path)
        }
        val newRowId = db?.insert(Mp3BaseEntry.TABLE_NAME, null, values)
    }


}