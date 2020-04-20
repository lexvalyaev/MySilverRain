package com.valyaev.lex.mysilverrain.room

import androidx.room.*

@Dao
interface ProgDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProg(ProgObj: ProgObj)

    @Update
    fun updateProg(ProgObj: ProgObj)

    @Query("SELECT * FROM programs")
    fun getAllPrograms():List<ProgObj>


}
