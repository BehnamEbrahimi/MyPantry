package com.example.mypantry;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    ListView shoppingListView;
    ArrayList<Model> shoppingList;
    ShoppingListAdapter shoppingAdapter = null;
    Button btnBuy;

    ImageView imageViewItem;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAdd(ShoppingListActivity.this);
            }
        });

        shoppingListView = findViewById(R.id.shoppingListView);
        shoppingList = new ArrayList<>();
        shoppingAdapter = new ShoppingListAdapter(this, R.layout.row_shopping_list, shoppingList);
        shoppingListView.setAdapter(shoppingAdapter);
        btnBuy = (Button) findViewById(R.id.btnBuy);

        //get all data from db
        Cursor cursor = MainActivity.mySQLiteHelper.getData("SELECT * FROM items WHERE (isBought = 0 OR isBought = 2)");
        shoppingList.clear();
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
            shoppingList.add(new Model(id, name, price, quantityInPantry, isBought, quantityToBuy, location, image, false));
        }
        shoppingAdapter.notifyDataSetChanged();
        if (shoppingList.size() == 0) {
            //if there is no record in table of database
            Toast.makeText(this, "No item found!", Toast.LENGTH_SHORT).show();
        }

        shoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                Model model = (Model) shoppingList.get(position);
                model.setChecked(!model.getChecked());
                shoppingAdapter.notifyDataSetChanged();

            }
        });

        shoppingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //alert dialog to display options of update and delete
                final CharSequence[] items = {"Update", "Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(ShoppingListActivity.this);

                dialog.setTitle("Choose an action:");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0){
                            //update
                            Cursor c = MainActivity.mySQLiteHelper.getData("SELECT id FROM items WHERE (isBought = 0 OR isBought = 2)");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            //show update dialog
                            showDialogUpdate(ShoppingListActivity.this, arrID.get(position));
                        }
                        if (i==1){
                            //delete
                            Cursor c = MainActivity.mySQLiteHelper.getData("SELECT id FROM items WHERE (isBought = 0 OR isBought = 2)");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
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

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg="";
                for (int i=0; i< shoppingList.size(); i++){
                    Model dataModel = (Model) shoppingList.get(i);
                    if (dataModel.getChecked()){
                        //get data of the each row ticked
                        Cursor cursor = MainActivity.mySQLiteHelper.getData("SELECT * FROM items WHERE id = " + dataModel.getId());
                        final String[] name = {""};
                        final double[] price = {0};
                        final double[] quantityInPantry = {0};
                        final long[] isBought = {0};
                        final double[] quantityToBuy = {0};
                        final String[] location = {""};
                        final byte[][] image = {"".getBytes()};

                        while (cursor.moveToNext()) {
                            name[0] = cursor.getString(1);
                            price[0] = cursor.getDouble(2);
                            quantityInPantry[0] = cursor.getDouble(3);
                            isBought[0] = cursor.getLong(4);
                            quantityToBuy[0] = cursor.getDouble(5);
                            location[0] = cursor.getString(6);
                            image[0] = cursor.getBlob(7);
                        }

                        MainActivity.mySQLiteHelper.updateData(
                                name[0],
                                price[0],
                                quantityInPantry[0] + quantityToBuy[0],
                                1,
                                0,
                                location[0],
                                image[0],
                                dataModel.getId()
                        );
                    }
                }
                Toast.makeText(ShoppingListActivity.this, "Items have been added to pantry!", Toast.LENGTH_SHORT).show();
                updateShoppingList();

            }
        });
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

        edtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //check to see if an item with the same name exits
                    Cursor cursor = MainActivity.mySQLiteHelper.getData("SELECT * FROM items WHERE name = '" + edtName.getText().toString().toLowerCase().trim() + "'");
                    while (cursor.moveToNext()) {
                        Double price = cursor.getDouble(2);
                        String location = cursor.getString(6);
                        byte[] image = cursor.getBlob(7);

                        edtPrice.setText(price.toString());
                        edtLocation.setText(location);
                        imageViewItem.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
                    }
                }
            }
        });

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
                        ShoppingListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //check to see if an item with the same name exits
                    Cursor cursor = MainActivity.mySQLiteHelper.getData("SELECT * FROM items WHERE name = '" + edtName.getText().toString().toLowerCase().trim() + "'");
                    boolean foundInDB = false;
                    while (cursor.moveToNext()) {
                        foundInDB = true;
                        String toastMsg = "Item updated successfully!";

                        int id = cursor.getInt(0);
                        long isBought = cursor.getLong(4);
                        if (isBought == 1) {
                            isBought = 2;
                            toastMsg = "Item added successfully!";
                        }
                        double quantityInPantry = cursor.getDouble(3);

                        MainActivity.mySQLiteHelper.updateData(
                                edtName.getText().toString().toLowerCase().trim(),
                                Double.parseDouble(edtPrice.getText().toString().trim()),
                                quantityInPantry,
                                isBought,
                                Double.parseDouble(edtQuantity.getText().toString().trim()),
                                edtLocation.getText().toString().trim(),
                                MainActivity.imageViewToByte(imageViewItem),
                                id
                        );
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), toastMsg, Toast.LENGTH_SHORT).show();
                    }
                    if (!foundInDB) {
                        MainActivity.mySQLiteHelper.insertData(
                                edtName.getText().toString().toLowerCase().trim(),
                                Double.parseDouble(edtPrice.getText().toString().trim()),
                                0,
                                0,
                                Double.parseDouble(edtQuantity.getText().toString().trim()),
                                edtLocation.getText().toString().trim(),
                                MainActivity.imageViewToByte(imageViewItem)
                        );
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Item added successfully!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception error) {
                    Log.e("Add error", error.getMessage());
                }
                updateShoppingList();
            }
        });

    }

    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_item);
        dialog.setTitle("Update");

        imageViewItem = dialog.findViewById(R.id.imageViewItem);
        final EditText edtName = dialog.findViewById(R.id.edtName);
        final EditText edtPrice = dialog.findViewById(R.id.edtPrice);
        final EditText edtLocation = dialog.findViewById(R.id.edtLocation);
        final EditText edtQuantity = dialog.findViewById(R.id.edtQuantity);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        //get data of the row clicked from the db
        Cursor cursor = MainActivity.mySQLiteHelper.getData("SELECT * FROM items WHERE ((isBought = 0 OR isBought = 2) AND id = " + position + " )");
        final long[] isBought = {0};
        final double[] quantityInPantry = {0};
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            edtName.setText(name);

            Double price = cursor.getDouble(2);
            edtPrice.setText(price.toString());

            quantityInPantry[0] = cursor.getDouble(3);
            isBought[0] = cursor.getLong(4);

            Double quantityToBuy = cursor.getDouble(5);
            edtQuantity.setText(quantityToBuy.toString());

            String location = cursor.getString(6);
            edtLocation.setText(location);

            byte[] image = cursor.getBlob(7);
            imageViewItem.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        }

        //set width of dialog
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        //set height of dialog
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();

        //in update dialog click image view to update image
        imageViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check external storage permission
                ActivityCompat.requestPermissions(
                        ShoppingListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainActivity.mySQLiteHelper.updateData(
                            edtName.getText().toString().toLowerCase().trim(),
                            Double.parseDouble(edtPrice.getText().toString().trim()),
                            quantityInPantry[0],
                            isBought[0],
                            Double.parseDouble(edtQuantity.getText().toString().trim()),
                            edtLocation.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageViewItem),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception error){
                    Log.e("Update error", error.getMessage());
                }
                updateShoppingList();
            }
        });
    }

    private void showDialogDelete(final int position) {
        //get data of the row clicked from the db
        Cursor cursor = MainActivity.mySQLiteHelper.getData("SELECT * FROM items WHERE ((isBought = 0 OR isBought = 2) AND id = " + position + " )");
        final String[] name = {""};
        final double[] price = {0};
        final double[] quantityInPantry = {0};
        final long[] isBought = {0};
        final double[] quantityToBuy = {0};
        final String[] location = {""};
        final byte[][] image = {"".getBytes()};

        while (cursor.moveToNext()) {
            name[0] = cursor.getString(1);
            price[0] = cursor.getDouble(2);
            quantityInPantry[0] = cursor.getDouble(3);
            isBought[0] = cursor.getLong(4);
            quantityToBuy[0] = cursor.getDouble(5);
            location[0] = cursor.getString(6);
            image[0] = cursor.getBlob(7);
        }

        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ShoppingListActivity.this);
        dialogDelete.setTitle("Delete");
        dialogDelete.setMessage("Are you sure?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    if (isBought[0] == 0) {
                        MainActivity.mySQLiteHelper.deleteData(position);
                    } else {
                        MainActivity.mySQLiteHelper.updateData(
                                name[0],
                                price[0],
                                quantityInPantry[0],
                                1,
                                0,
                                location[0],
                                image[0],
                                position
                        );
                    }
                    Toast.makeText(ShoppingListActivity.this, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateShoppingList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateShoppingList() {
        //get all data from db
        Cursor cursor = MainActivity.mySQLiteHelper.getData("SELECT * FROM items WHERE (isBought = 0 OR isBought = 2)");
        shoppingList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Double price = cursor.getDouble(2);
            Double quantityInPantry = cursor.getDouble(3);
            Long isBought = cursor.getLong(4);
            Double quantityToBuy = cursor.getDouble(5);
            String location = cursor.getString(6);
            byte[] image = cursor.getBlob(7);

            shoppingList.add(new Model(id, name, price, quantityInPantry, isBought, quantityToBuy, location, image, false));
        }
        shoppingAdapter.notifyDataSetChanged();
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
