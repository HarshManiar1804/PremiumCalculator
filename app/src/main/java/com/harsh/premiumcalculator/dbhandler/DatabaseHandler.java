package com.harsh.premiumcalculator.dbhandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.harsh.premiumcalculator.activities.MainActivity;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHandler extends SQLiteAssetHelper {
    public static final String db_name = "cal.db";
    public static final int db_ver = 1;

    public DatabaseHandler(Context context) {
        super(context, db_name, null, db_ver);
    }

    @SuppressLint("Range")
    public String getAmount(String am, String ag, int x){
        String amount;
        String SQL;
        if(ag == "--")
        {
            return "0";
        }
        else{
            if (x == 1){

                SQL = "SELECT * FROM Ind WHERE AgeBand = '"+am+"'";
            }
            else{
                SQL = "SELECT * FROM HFF WHERE AgeBand = '"+am+"'";
            }

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(SQL, null);
            cursor.moveToFirst();

            amount = cursor.getString(cursor.getColumnIndex(ag));
            return amount;
        }

    }
}
