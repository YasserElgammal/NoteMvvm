package com.yasserelgammal.notemv.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yasserelgammal.notemv.persistence.Note
import com.yasserelgammal.notemv.repository.NoteRepository
import javax.inject.Inject

class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    //Database Operations in view model

    fun insert(note: Note) {
        return noteRepository.insert(note)
    }

    fun delete(note: Note) {
        noteRepository.delete(note)
    }

    fun deleteById(id:Int){
        noteRepository.deleteById(id)
    }

    fun update(note: Note) {
        noteRepository.update(note)
    }

    fun archiveNote(archive:Int, id:Int) {
        noteRepository.archiveNote(archive,id)
    }

    fun getAllArchivesNotes(): LiveData<List<Note>> {
        return noteRepository.getAllArchivesNotes()
    }
    fun getAllNotes(): LiveData<List<Note>> {
        return noteRepository.getAllNotes()
    }


}