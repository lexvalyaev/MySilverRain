package com.valyaev.lex.mysilverrain.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import android.provider.BaseColumns
import com.valyaev.lex.mysilverrain.model.ProgUrlObject
import com.valyaev.lex.mysilverrain.model.UrlObject
import java.util.*
import kotlin.collections.ArrayList

class DbHelper(
    context: Context,
    DATABASE_VERSION: Int = 1,
    DATABASE_NAME: String = "Programms.db"
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object RadioProgramsContract {
        const val SQL_CREATE_PROGRAM_TABLE =
            "CREATE TABLE ${ProgramEntry.TABLE_NAME} (" +
                    "${ProgramEntry.COLUMN_PROGRAMS_NAME} TEXT NOT NULL, " +
                    "${ProgramEntry.COLUMN_PROGRAMS_URL} TEXT NOT NULL, " +
                    "${ProgramEntry.COLUMN_ICON_ID} INTEGER NOT NULL )"


        const val SQL_CREATE_BROADCAST_TABLE =
            " CREATE TABLE ${BroadcastEntry.TABLE_NAME} (" +
                    "${BroadcastEntry.COLUMN_BROADCAST_NAME} TEXT NOT NULL, " +
                    "${BroadcastEntry.COLUMN_BROADCAST_URL} TEXT NOT NULL, " +
                    "${BroadcastEntry.COLUMN_MP3_URL} TEXT NOT NULL, " +
                    "${BroadcastEntry.COLUMN_DATE} TEXT NOT NULL, " +
                    "${BroadcastEntry.COLUMN_PROGRAM_URL} TEXT NOT NULL," +
                    "${BroadcastEntry.COLUMN_ICON_ID} INTEGER NOT NULL )"


        object ProgramEntry : BaseColumns {
            const val TABLE_NAME = "programs"
            const val COLUMN_PROGRAMS_NAME = "program_name"
            const val COLUMN_PROGRAMS_URL = "program_url"
            const val COLUMN_ICON_ID = "icon_id"
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
    }


    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL(SQL_CREATE_PROGRAM_TABLE)
            db.execSQL(SQL_CREATE_BROADCAST_TABLE)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insert(urlObject: UrlObject) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ProgramEntry.COLUMN_PROGRAMS_NAME, urlObject.text)
            put(ProgramEntry.COLUMN_PROGRAMS_URL, urlObject.url)
            put(ProgramEntry.COLUMN_ICON_ID, urlObject.iconID)
        }
        val newRowId = db?.insert(ProgramEntry.TABLE_NAME, null, values)
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
                val progUrl = cursor.getString(cursor.getColumnIndex(ProgramEntry.TABLE_NAME))
                val progIconId = cursor.getInt(cursor.getColumnIndex(ProgramEntry.COLUMN_ICON_ID))
                result.add(UrlObject(text = progName, url = progUrl, iconID = progIconId))
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
                val brName = cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_BROADCAST_NAME))
                val brUrl = cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_BROADCAST_URL))
                val brMp3Url = cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_MP3_URL))
                val brProgUrl = cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_PROGRAM_URL))
                val brDateString = cursor.getString(cursor.getColumnIndex(BroadcastEntry.COLUMN_DATE))
                val locale = Locale("ru")
                val sdf = SimpleDateFormat("yyyy-MM-dd", locale)
                val brDate = sdf.parse(brDateString)
                val brIconID =  cursor.getInt(cursor.getColumnIndex(BroadcastEntry.COLUMN_ICON_ID))
                result.add(ProgUrlObject(brName,brProgUrl,brUrl,brIconID,brMp3Url,brDate))
            }
        }

        return result
    }
}