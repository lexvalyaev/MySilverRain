package com.valyaev.lex.mysilverrain.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProgObj::class], version = 1)
abstract class ProgDB:RoomDatabase() {
    abstract fun progDAO():ProgDao
}