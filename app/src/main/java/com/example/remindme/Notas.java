package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.remindme.adapters.NotesAdapter;
import com.example.remindme.database.NoteDatabase;
import com.example.remindme.listeners.NotesListener;
import com.example.remindme.singleton.Nota;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Notas extends AppCompatActivity implements NotesListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1; //serve para adicionar uma nova note
    public static final int REQUEST_CODE_UPDATE_NOTE = 2; //serve para dar update as ntoas
    public static final int REQUEST_CODE_SHOW_NOTE = 3;
    private RecyclerView notesRecyclerView;
    private List<Nota>notaList;
    private NotesAdapter notesAdapter;

    private int noteClickedPostion = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas);

        ImageView imageAddNoteMain = findViewById(R.id.imgaddnotemain);
        imageAddNoteMain.setOnClickListener((v) -> {
            startActivityForResult(
                    new Intent(getApplicationContext(), CreateNote.class),
                    REQUEST_CODE_ADD_NOTE
            );
        });

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        notaList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notaList, this);
        notesRecyclerView.setAdapter(notesAdapter);
        getNotes(REQUEST_CODE_SHOW_NOTE, false);


        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(notaList.size() != 0){
                    notesAdapter.searchNotes(editable.toString());
                }

            }
        });

    }

    @Override
    public void onNoteClicked(Nota nota, int position) {
        noteClickedPostion = position;
        Intent intent = new Intent(getApplicationContext(), CreateNote.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", nota);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);

    }

    public  void getNotes(final int requestCode, final boolean isNoteDeleted){
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Nota>>{
            @Override
            protected List<Nota> doInBackground(Void... voids) {
                return NoteDatabase
                        .getDatabase(getApplicationContext())
                        .noteDao()
                        .getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Nota> notas) {
                super.onPostExecute(notas);
                if(requestCode == REQUEST_CODE_SHOW_NOTE){
                    notaList.addAll(notas);
                    notesAdapter.notifyDataSetChanged();
                }else if(requestCode == REQUEST_CODE_ADD_NOTE){
                    notaList.add(0, notas.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                }else if(requestCode == REQUEST_CODE_UPDATE_NOTE){
                    notaList.remove(noteClickedPostion);
                    if(isNoteDeleted){
                        notesAdapter.notifyItemRemoved(noteClickedPostion);
                    }else{
                        notaList.add(noteClickedPostion, notas.get(noteClickedPostion));
                        notesAdapter.notifyItemChanged(noteClickedPostion);

                    }
                }
            }
        }
        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK){
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        }else if(requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK){
            if(data != null ){
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        }
    }

    public void voltarMain(View view){
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
    }
}
