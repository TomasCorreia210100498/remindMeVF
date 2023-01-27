package com.example.remindme;

import static com.example.remindme.Camera.sqLiteHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.remindme.adapters.galeriaAdapter;
import com.example.remindme.singleton.galeria;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class galeriaList extends AppCompatActivity {
    GridView gridView;
    ArrayList<galeria> lista;
    galeriaAdapter adapter = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.galeria_all);

        gridView = (GridView) findViewById(R.id.gridView);
        lista = new ArrayList<>();
        adapter = new galeriaAdapter(this, R.layout.galeria_item, lista);
        gridView.setAdapter(adapter);

        Cursor cursor = Camera.sqLiteHelper.getData("SELECT * FROM GALERIA");
        lista.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String desc = cursor.getString(1);
            byte[] image = cursor.getBlob(2);

            lista.add(new galeria(desc, image, id));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                CharSequence[] items = {"Update", "Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(galeriaList.this);
                dialog.setTitle("Escolha uma ação");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            Cursor c = Camera.sqLiteHelper.getData("SELECT id FROM GALERIA");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogUpdate(galeriaList.this, arrID.get(position));
                        } else {
                            Cursor c = Camera.sqLiteHelper.getData("SELECT id FROM GALERIA");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                           showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    ImageView imageViewgaleria;
    private void showDialogUpdate (Activity activity, final int position) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_galeria);
        dialog.setTitle("Update");

        imageViewgaleria = (ImageView) dialog.findViewById(R.id.imageUpdate);
        final EditText edtDesc = (EditText) dialog.findViewById(R.id.updateDesc);
        ImageView camera = (ImageView) dialog.findViewById(R.id.cameraUpate);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);

        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_camera, 100);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sqLiteHelper.updateData(
                            edtDesc.getText().toString().trim(),
                            Camera.imageviewToByte(imageViewgaleria),
                            position

                    );

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Update com sucesso!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Udpate Error", error.getMessage());
                }
                updateGalleryList();
            }
        });

    }

    private void showDialogDelete(final int idGaleria){
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(galeriaList.this);

        dialogDelete.setTitle("Aviso");
        dialogDelete.setMessage("Deseja apagar?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    sqLiteHelper.deleteData(idGaleria);
                    Toast.makeText(galeriaList.this, "Apagado com sucesso!", Toast.LENGTH_SHORT).show();
                } catch(Exception e){
                    Log.e("error", e.getMessage());
                }
                updateGalleryList();
            }
        });

        dialogDelete.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateGalleryList(){
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM GALERIA");
        lista.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String desc = cursor.getString(1);
            byte[] image = cursor.getBlob(2);

            lista.add(new galeria(desc, image, id));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = (Bitmap)data.getExtras().get("data");
        imageViewgaleria.setImageBitmap(photo);
    }
}
