package com.yasserelgammal.notemv.di

import android.app.Application
import androidx.room.Room
import com.yasserelgammal.notemv.persistence.NoteDao
import com.yasserelgammal.notemv.persistence.NoteDatabase
import com.yasserelgammal.notemv.repository.NoteRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesAppDatabase(app:Application):NoteDatabase{
        return Room.databaseBuilder(app,NoteDatabase::class.java,"note_database").build()
    }

    @Singleton
    @Provides
    fun providesNoteDao(db: NoteDatabase): NoteDao {
        return db.noteDao()
    }

    @Provides
    fun providesRepository(noteDao: NoteDao):NoteRepository{
        return NoteRepository(noteDao)
    }
}