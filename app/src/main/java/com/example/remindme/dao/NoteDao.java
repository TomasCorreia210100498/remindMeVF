package com.example.remindme.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.remindme.singleton.Nota;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Nota>getAllNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Nota note);

    @Delete
    void deleteNote(Nota note);
    

}
