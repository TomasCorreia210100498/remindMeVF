package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Perfil extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private Button logout;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        final TextView bemvindo = (TextView) findViewById(R.id.txtbemvindo);
        final TextView nomeTextView = (TextView) findViewById(R.id.txtnome);
        final TextView emailTextView = (TextView) findViewById(R.id.txtemail);
        final TextView idadeTextView = (TextView) findViewById(R.id.txtidade);


        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User perfilUser = snapshot.getValue(User.class);

                if(perfilUser != null ){
                    String nome = perfilUser.nome;
                    String email = perfilUser.email;
                    String idade = perfilUser.idade;


                    bemvindo.setText("Bem-vindo " + nome + "!" );
                    nomeTextView.setText(nome);
                    emailTextView.setText(email);
                    idadeTextView.setText(idade);




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Perfil.this, "Algo de errado nao esta certol", Toast.LENGTH_SHORT).show();
            }
        });



    }
    public void voltarInicio(View view){
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
    }
}
