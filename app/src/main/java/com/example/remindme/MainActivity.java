package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remindme.singleton.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        final TextView bemvindo = (TextView) findViewById(R.id.textBemvindo);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User perfilUser = snapshot.getValue(User.class);

                if (perfilUser != null) {
                    String nome = perfilUser.nome;

                    bemvindo.setText(getString(R.string.BEMVINDO) + " " + nome + "!");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Algo de errado nao esta certol", Toast.LENGTH_SHORT).show();
            }
           }
        );}

            public void verNota(View view) {
                Intent in = new Intent(getApplicationContext(), Notas.class);
                startActivity(in);

            }

            public void escreverNota(View view) {
                Intent in = new Intent(getApplicationContext(), CreateNote.class);
                startActivity(in);

            }

            public void sair(View view) {
                Intent in = new Intent(getApplicationContext(), Login.class);
                startActivity(in);
            }
            public void verPerfil(View view){
        Intent in = new Intent(getApplicationContext(), Perfil.class);
        startActivity(in);
            }

            public void settings(View view){
        Intent in = new Intent(getApplicationContext(), Settings.class);
        startActivity(in);
            }

    public void abrirCamera(View view){
        Intent in = new Intent(getApplicationContext(),Camera.class);
        startActivity(in);
    }
    public void Galeria(View view){
        Intent in = new Intent(getApplicationContext(), Camera.class);
        startActivity(in);
    }

        }
