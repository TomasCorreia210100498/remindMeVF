package com.example.remindme.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.remindme.R;
import com.example.remindme.singleton.galeria;

import java.util.ArrayList;

public class galeriaAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<galeria> galeriaAll;

    public galeriaAdapter(Context context, int layout, ArrayList<galeria> galeriaAll) {
        this.context = context;
        this.layout = layout;
        this.galeriaAll = galeriaAll;
    }

    @Override
    public int getCount() {
        return galeriaAll.size();
    }

    @Override
    public Object getItem(int position) {
        return galeriaAll.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtdesc;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder= new ViewHolder();
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtdesc = (TextView)row.findViewById(R.id.txtdesc);
            holder.imageView = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);

        }
        else
        {
            holder= (ViewHolder) row.getTag();
        }

        galeria Galeria = galeriaAll.get(position);

        holder.txtdesc.setText(Galeria.getDesc());

        byte[] galeriaItem = Galeria.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(galeriaItem,0,galeriaItem.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }

}
