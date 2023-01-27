package com.example.remindme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.remindme.SQLiteDB.SQLiteHelper;

import java.io.ByteArrayOutputStream;

public class Camera extends AppCompatActivity {

    private ImageView imageview;
    EditText desc;
    Button addDB,verGaleria;
    public static SQLiteHelper sqLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageview = findViewById(R.id.imagemEscolhida);
        ImageView open = findViewById(R.id.btnCamera);
        addDB = (Button)findViewById(R.id.btnAdd);
        verGaleria = (Button)findViewById(R.id.btnGaleria);
        desc = findViewById(R.id.edDesc);

        sqLiteHelper = new SQLiteHelper(this, "GaleriaDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS GALERIA(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, image BLOG)");

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_camera,100);
            }
        });

        addDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    sqLiteHelper.insertData(
                            desc.getText().toString().trim(),
                            imageviewToByte(imageview)

                    );
                    Toast.makeText(Camera.this, "Adicionado à galeria!", Toast.LENGTH_SHORT).show();
                    desc.setText("");

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });

        verGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),galeriaList.class);
                startActivity(intent);
            }
        });
    }

    public static byte[] imageviewToByte(ImageView imageview) {
        Bitmap bitmap = ((BitmapDrawable) imageview.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, stream);
        byte [] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = (Bitmap)data.getExtras().get("data");
        imageview.setImageBitmap(photo);
    }
    public void Galeria(View view){
        Intent in = new Intent(getApplicationContext(), Camera.class);
        startActivity(in);
    }


}