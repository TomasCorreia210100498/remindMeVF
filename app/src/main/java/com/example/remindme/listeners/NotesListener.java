package com.example.remindme.listeners;

import com.example.remindme.singleton.Nota;

public interface NotesListener {
    void onNoteClicked(Nota nota, int position);
}
