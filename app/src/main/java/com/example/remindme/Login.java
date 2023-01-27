package com.example.remindme;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private TextView registar;
    private EditText edEmail, edPassword;
    private Button btnlogin;

    private FirebaseAuth mAuth;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registar = (TextView) findViewById(R.id.txtRegistar);
        registar.setOnClickListener(this);

        btnlogin = (Button) findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(this);


        edEmail = (EditText) findViewById(R.id.edEmail);
        edPassword = (EditText) findViewById(R.id.edPassword);


        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onClick(View view) {


        switch(view.getId()){
            case R.id.txtRegistar:
                startActivity(new Intent(this, Registo.class));
                break;


            case R.id.btnLogin:
                userLogin();
                break;
        }

    }

    private void userLogin() {
        String email = edEmail.getText().toString().trim();
        String pass = edPassword.getText().toString().trim();


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
            edPassword.setError("password necessária");
            edPassword.requestFocus();
            return;
        }
        if(pass.length()<6){
            edPassword.setError("Insira uma password com mais de 6 caracteres");
            edPassword.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Bem vindo", Toast.LENGTH_SHORT).show();
                    //redireciona para outra pag apos a validacao estar completa e correta
                    startActivity(new Intent(Login.this, MainActivity.class));
                }else{
                    Toast.makeText(Login.this, "Falha ao Logar! Verifique as suas credenciais", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}