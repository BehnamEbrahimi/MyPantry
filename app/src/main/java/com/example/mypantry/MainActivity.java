package com.example.mypantry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    public static SQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating database connection
        mySQLiteHelper = new SQLiteHelper(this, "DB.sqlite", null, 1);

        //creating table in database
        mySQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS items(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR UNIQUE, price DOUBLE, quantityInPantry DOUBLE, isBought INT, quantityToBuy DOUBLE, location VARCHAR, image BLOB)");
    }

    public void openPantryList(View view){
        Intent intent = new Intent(MainActivity.this, PantryListActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void openShoppingList(View view){
        Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
