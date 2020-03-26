package com.yasserelgammal.notemv.persistence

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note):Long

    @Update
    suspend fun update(note: Note)

    @Query("delete from tbl_note where id = :id")
    suspend fun deleteById(id:Int)

    @Delete
    suspend fun delete(note: Note)

    @Query("select * from tbl_note WHERE archive= 0 ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM tbl_note WHERE archive= 1 ORDER BY id DESC")
    fun getArchivesNotes():LiveData<List<Note>>

    @Query("UPDATE tbl_note SET archive = :archive WHERE id = :id")
    suspend fun archiveNote(archive: Int, id: Int)

    //UPDATE tbl_note SET archive = :archive WHERE id = :id

}