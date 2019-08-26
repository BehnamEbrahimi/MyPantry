package com.example.mypantry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    //constructor
    SQLiteHelper(Context context,
                 String name,
                 SQLiteDatabase.CursorFactory factory,
                 int version){
        super(context, name, factory, version);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //insertData
    public void insertData(String name, double price, double quantityInPantry, long isBought, double quantityToBuy, String location, byte[] image){
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO items VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)"; //where "items" is table name in database we will create

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindDouble(2, price);
        statement.bindDouble(3, quantityInPantry);
        statement.bindLong(4, isBought);
        statement.bindDouble(5, quantityToBuy);
        statement.bindString(6, location);
        statement.bindBlob(7, image);

        statement.executeInsert();
    }

    //updateData
    public void updateData(String name, double price, double quantityInPantry, long isBought, double quantityToBuy, String location, byte[] image, int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to update record
        String sql = "UPDATE items SET name=?, price=?, quantityInPantry=?, isBought=?, quantityToBuy=?, location=?, image=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindDouble(2, price);
        statement.bindDouble(3, quantityInPantry);
        statement.bindLong(4, isBought);
        statement.bindDouble(5, quantityToBuy);
        statement.bindString(6, location);
        statement.bindBlob(7, image);
        statement.bindDouble(8, (double)id);

        statement.execute();
        database.close();
    }

    //deleteData
    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();
        //query to delete record using id
        String sql = "DELETE FROM items WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}