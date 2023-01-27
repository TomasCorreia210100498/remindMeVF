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

public class Settings extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userId = user.getUid();

        final TextView bemvindo = (TextView) this.<View>findViewById(R.id.Bemvindo);
        final TextView emailTextView = (TextView)this.<View> findViewById(R.id.email);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User perfilUser = snapshot.getValue(User.class);
                if (perfilUser != null) {

                    String nome = perfilUser.nome;
                    String email = perfilUser.email;

                    bemvindo.setText("" + nome + "");
                    emailTextView.setText("" + email + "");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Settings.this, "Algo de errado nao esta certol", Toast.LENGTH_SHORT).show();
            }
        }
        );}
    public void voltar(View view){
        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(in);
    }

    public void linguagem(View view){
        Intent in = new Intent(getApplicationContext(), Linguagens.class);
        startActivity(in);
    }
}