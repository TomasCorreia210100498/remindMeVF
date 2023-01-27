package com.example.remindme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Linguagens extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linguagens);

        ImageButton en = findViewById(R.id.ENG);
        ImageButton ger = findViewById(R.id.GER);
        ImageButton fr = findViewById(R.id.FR);
        ImageButton pt = findViewById(R.id.PT);
        ImageButton zh = findViewById(R.id.ZH);

        LanguageManager lang = new LanguageManager(this);

        en.setOnClickListener(view ->
        {
            lang.updateResource("en");
            recreate();
        });

        ger.setOnClickListener(view ->
        {
            lang.updateResource("de");
            recreate();
        });

        fr.setOnClickListener(view ->
        {
            lang.updateResource("fr");
            recreate();
        });

        pt.setOnClickListener(view ->
        {
            lang.updateResource("pt");
            recreate();
        });

        zh.setOnClickListener(view ->
        {
            lang.updateResource("zh");
            recreate();
        });
    }

    public void settings(View view) {
        Intent in = new Intent(getApplicationContext(), Settings.class);
        startActivity(in);

    }
}