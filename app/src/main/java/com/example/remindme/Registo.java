package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remindme.singleton.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Registo extends AppCompatActivity implements View.OnClickListener  {

    private Button btnRegistar;

    private EditText edNome, edIdade, edEmail, edPass;
    private ProgressBar progressBar2;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        mAuth = FirebaseAuth.getInstance();


        btnRegistar = (Button) findViewById(R.id.btnRegistar);
        btnRegistar.setOnClickListener(this);



        edNome =  findViewById(R.id.edNome);
        edIdade = findViewById(R.id.edIdade);

        edEmail =  findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.btnRegistar:
                registarUser();
                break;
        }

    }

    private void registarUser() {

        String nome = edNome.getText().toString().trim();
        String idade = edIdade.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String pass = edPass.getText().toString().trim();

        if (nome.isEmpty()) {
            edNome.setError("Nome é necessário");
            edNome.requestFocus();
            return;
        }
        if (idade.isEmpty()) {
            edIdade.setError("Idade é necessária");
            edIdade.requestFocus();
            return;
        }



        if (email.isEmpty()) {
            edEmail.setError("email é necessário");
            edEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edEmail.setError("Coloque um email valido");
            edEmail.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            edPass.setError("password necessária");
            edPass.requestFocus();
            return;
        }
        if(pass.length()<6){
            edPass.setError("Insira uma password com mais de 6 caracteres");
            edPass.requestFocus();
            return;
        }

        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user = new User(nome, idade, email);

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(Registo.this, "Registado com sucesso!   " +
                                                "Bem vindo, " + nome + "!", Toast.LENGTH_SHORT).show();
                                        progressBar2.setVisibility(View.GONE);
                                        startActivity(new Intent(Registo.this, MainActivity.class));
                                    }else{
                                        Toast.makeText(Registo.this, "Falha ao registar", Toast.LENGTH_SHORT).show();
                                        progressBar2.setVisibility(View.GONE);
                                    }
                                }
                            });
                }else{
                    Toast.makeText(Registo.this, "Falha ao registar o utilizador", Toast.LENGTH_SHORT).show();
                    progressBar2.setVisibility(View.GONE);
                }
            }
        });

    }

}