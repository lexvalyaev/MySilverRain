package com.valyaev.lex.mysilverrain.room


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "programs", indices = arrayOf(Index(value = ["program_url"], unique = true)))
class ProgObj ( val program_url: String,
                var icon_id : Int,
                @PrimaryKey(autoGenerate = true) val _id: Int,
                val program_name: String,
                val icon_name: String
)