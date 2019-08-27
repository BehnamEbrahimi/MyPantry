package com.example.mypantry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShoppingListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model> shoppingList;

    public ShoppingListAdapter(Context context, int layout, ArrayList<Model> shoppingList) {
        this.context = context;
        this.layout = layout;
        this.shoppingList = shoppingList;
    }

    @Override
    public int getCount() {
        return shoppingList.size();
    }

    @Override
    public Object getItem(int i) {
        return shoppingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtPrice, txtLocation, txtQuantity;
        CheckBox checkBox;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ShoppingListAdapter.ViewHolder holder = new ShoppingListAdapter.ViewHolder();

        if (row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtName = row.findViewById(R.id.txtName);
            holder.txtPrice = row.findViewById(R.id.txtPrice);
            holder.txtLocation = row.findViewById(R.id.txtLocation);
            holder.txtQuantity = row.findViewById(R.id.txtQuantity);
            holder.imageView = row.findViewById(R.id.imgIcon);
            holder.checkBox = row.findViewById(R.id.checkBox);
            row.setTag(holder);
        }
        else {
            holder = (ShoppingListAdapter.ViewHolder)row.getTag();
        }

        Model model = shoppingList.get(i);

        holder.txtName.setText(model.getName());
        holder.txtPrice.setText(String.valueOf(model.getPrice()));
        holder.txtLocation.setText(model.getLocation());
        holder.txtQuantity.setText(String.valueOf(model.getQuantityToBuy()));

        byte[] recordImage = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.imageView.setImageBitmap(bitmap);

        holder.checkBox.setChecked(model.getChecked());

        return row;
    }

}
