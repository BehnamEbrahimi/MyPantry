package com.example.mypantry;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PantryListActivity extends AppCompatActivity {

    ListView pantryListView;
    ArrayList<Model> pantryList;
    PantryListAdapter pantryAdapter = null;

    ImageView imageViewItem;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd(PantryListActivity.this);
            }
        });

        pantryListView = findViewById(R.id.pantryListView);
        pantryList = new ArrayList<>();
        pantryAdapter = new PantryListAdapter(this, R.layout.row, pantryList);
        pantryListView.setAdapter(pantryAdapter);

        //get all data from db
        Cursor cursor = MainActivity.mySQLiteHelper.getData("SELECT * FROM items");
        pantryList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Double price = cursor.getDouble(2);
            Double quantityInPantry = cursor.getDouble(3);
            Long isBought = cursor.getLong(4);
            Double quantityToBuy = cursor.getDouble(5);
            String location = cursor.getString(6);
            byte[] image = cursor.getBlob(7);
            //add to list
            boolean _isBought = false;
            if (isBought == 1) {
                _isBought = true;
            }
            pantryList.add(new Model(id, name, price, quantityInPantry, _isBought, quantityToBuy, location, image));
        }
        pantryAdapter.notifyDataSetChanged();
        if (pantryList.size() == 0) {
            //if there is no record in table of database
            Toast.makeText(this, "No item found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialogAdd(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.add_item);
        dialog.setTitle("Add Item");

        imageViewItem = dialog.findViewById(R.id.imageViewItem);
        final EditText edtName = dialog.findViewById(R.id.edtName);
        final EditText edtPrice = dialog.findViewById(R.id.edtPrice);
        final EditText edtLocation = dialog.findViewById(R.id.edtLocation);
        final EditText edtQuantity = dialog.findViewById(R.id.edtQuantity);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);

        //set width of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height of dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        //in update dialog click image view to update image
        imageViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check external storage permission
                ActivityCompat.requestPermissions(
                        PantryListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.mySQLiteHelper.insertData(
                            edtName.getText().toString().trim(),
                            Double.parseDouble(edtPrice.getText().toString().trim()),
                            Double.parseDouble(edtQuantity.getText().toString().trim()),
                            1,
                            0,
                            edtLocation.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageViewItem)
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Item added successfully!", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Add error", error.getMessage());
                }
                updatePantryList();
            }
        });

    }

    private void updatePantryList() {
        //get all data from sqlite
        Cursor cursor = MainActivity.mySQLiteHelper.getData("SELECT * FROM items");
        pantryList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Double price = cursor.getDouble(2);
            Double quantityInPantry = cursor.getDouble(3);
            Long isBought = cursor.getLong(4);
            Double quantityToBuy = cursor.getDouble(5);
            String location = cursor.getString(6);
            byte[] image = cursor.getBlob(7);

            boolean _isBought = false;
            if (isBought == 1) {
                _isBought = true;
            }
            pantryList.add(new Model(id, name, price, quantityInPantry, _isBought, quantityToBuy, location, image));
        }
        pantryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(this, "Gallery access is not allowed!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            imageViewItem.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
