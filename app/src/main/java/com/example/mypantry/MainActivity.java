package com.example.mypantry;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by Behnam Ebrahimi (SID: 19584267) on 11/09/2019.
 * Assignment #1
 * School of Computing, Engineering and Mathematics, Western Sydney University.
 * MyPantry: is a simple shopping aid Android App. In this app, there is a
 * shopping list and an inventory of the existing pantry. It has the following features:
 * 1. Add/View/Edit/Delete a pantry item.
 * 2. Add/View/Edit/Delete a shopping list item.
 * 3. Tick off shopping list items and update the pantry.
 * 4. View and change images that are associated with items.
 * 5. Store pantry data using an SQLite database.
 * 6. Update the pantry when shopping.
 * 7. Good display of shopping lists plus scrolling.
 * 8. Adding shopping outings to Google Calendar.
 * 8. Choosing photos from Gallery & associating them with shopping items.
 * 9. Good documentation, comments, naming, etc.
 * 10.Showing the nearby supermarkets in Google Maps.
 **/

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

    public void openMap(View view){
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void openCalebdar(View view){
        Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
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
