package com.matchymatchproject.mirassociationdanny.matchymatch.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Created by HP on 10/3/2018.
 */

public class DBHelper extends SQLiteAssetHelper {

    private static final String DB_Name  = "saveBitmap.db";
    private static final int DB_VER      = 1;
    private static final String TBL_Name = "CustomImages";

    private static final String COL_Name = "Name";
    private static final String COL_Data = "Data";

    public DBHelper(Context context) {
        super(context, DB_Name , null, DB_VER);
    }

    // Add Bitmap Byte Array To DB
    public void addBitmap(String name,byte[] image) throws SQLiteException
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv       = new ContentValues();
        cv.put(COL_Name,name);
        cv.put(COL_Data,image);
        database.insert(TBL_Name,null,cv);

    }

    // Get Byte Array Bitmap
    public byte[] GetBitmapByName (String name)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteQueryBuilder qb   = new SQLiteQueryBuilder();

        String[] select = {COL_Data};
        qb.setTables(TBL_Name);
        Cursor c = qb.query(database,select,"Name = ?",new String[]{name},null,null,null);
        byte[] resilt = null;
        if (c.moveToFirst())
        {
            do {
                resilt = c.getBlob(c.getColumnIndex(COL_Data));

            }while (c.moveToNext());

        }
        return resilt;
    }

    public ArrayList<byte[]> GetBitmaps ()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteQueryBuilder qb   = new SQLiteQueryBuilder();

        String[] select = {COL_Data};
        qb.setTables(TBL_Name);
        Cursor c = qb.query(database,select,null,null,null,null,null);
        ArrayList<byte[]> result = new ArrayList<>();

        result.add(null);
        result.add(null);
        result.add(null);
        result.add(null);
        result.add(null);
        result.add(null);

        if (c.moveToFirst())
        {
            do {
                result.add(c.getPosition(), c.getBlob(c.getColumnIndex(COL_Data)));

            }while (c.moveToNext());

        }
        return result;
    }

    // delete query here
    public String DeleteImage(String imagename)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return String.valueOf(db.delete("CustomImages","Name = ?",new String[]{imagename}));
    }

}
