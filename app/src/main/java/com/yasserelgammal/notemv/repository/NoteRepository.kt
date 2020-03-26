package com.yasserelgammal.notemv.repository

import androidx.lifecycle.LiveData
import com.yasserelgammal.notemv.persistence.Note
import com.yasserelgammal.notemv.persistence.NoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteRepository @Inject constructor(val noteDao: NoteDao) {

    //function to insert note in database
    fun insert(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.insert(note)
        }
    }

    //function to delete note in database
    fun delete(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.delete(note)
        }
    }

    //function to delete note by Id in database
    fun deleteById(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.deleteById(id)
        }
    }

    //function to update note in database
    fun update(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.update(note)
        }
    }

    //function to update note in database
    fun archiveNote(archive:Int, id:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.archiveNote(archive, id)
        }
    }

    //function to get all notes in database
    fun getAllNotes():LiveData<List<Note>>{
        return noteDao.getAllNotes()
    }

    //function to get all notes in database
    fun getAllArchivesNotes():LiveData<List<Note>>{
        return noteDao.getArchivesNotes()
    }

//    //function to get all notes in database
//    fun getAllNotes(): LiveData<Resource<List<Note>>> = liveData {
//
//        //Loading
//        emit(Resource.loading())
//
//        //Find Data
//        if (noteDao.getAllNotes().size < 0) {
//            emit(Resource.error("No Notes Found"))
//        } else
//            emit(Resource.success(noteDao.getAllNotes()))
//    }
}