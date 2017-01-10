package tech.destinum.listapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ListappDB";
    private static final int DB_VERSION = 1;
    public static final String DB_TABLE = "List";
    public static final String DB_COLUMN = "Item";
    //added this variable to use it while deleting the record
    public static final String DB_COLUMN_ID = "_id";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL)", DB_TABLE, DB_COLUMN_ID, DB_COLUMN);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    public void insertNewItem(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, item);
        db.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }


    public void deleteItem(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN + "=?", new String[]{item});
        db.close();
    }

    //Function to delete item using ID
    public void deleteItemById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN_ID + "=?", new String[]{id + ""});
        db.close();
    }

    public ArrayList<ItemClass> getItemList() {
        ArrayList<ItemClass> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN, DB_COLUMN_ID}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            final String name = cursor.getString(cursor.getColumnIndex(DB_COLUMN));
            final long id = cursor.getLong(cursor.getColumnIndex(DB_COLUMN_ID));
            itemList.add(new ItemClass(id, name));
        }
        cursor.close();
        db.close();
        return itemList;
    }
}