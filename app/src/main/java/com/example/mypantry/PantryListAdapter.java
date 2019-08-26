package com.example.mypantry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PantryListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model> pantryList;

    public PantryListAdapter(Context context, int layout, ArrayList<Model> pantryList) {
        this.context = context;
        this.layout = layout;
        this.pantryList = pantryList;
    }

    @Override
    public int getCount() {
        return pantryList.size();
    }

    @Override
    public Object getItem(int i) {
        return pantryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtPrice, txtLocation, txtQuantity;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtName = row.findViewById(R.id.txtName);
            holder.txtPrice = row.findViewById(R.id.txtPrice);
            holder.txtLocation = row.findViewById(R.id.txtLocation);
            holder.txtQuantity = row.findViewById(R.id.txtQuantity);
            holder.imageView = row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        Model model = pantryList.get(i);

        holder.txtName.setText(model.getName());
        holder.txtPrice.setText(String.valueOf(model.getPrice()));
        holder.txtLocation.setText(model.getLocation());
        holder.txtQuantity.setText(String.valueOf(model.getQuantityInPantry()));

        byte[] recordImage = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
