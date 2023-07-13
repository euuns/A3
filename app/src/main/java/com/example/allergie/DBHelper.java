package com.example.allergie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, "test.db", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE test (_id INTEGER PRIMARY KEY AUTOINCREMENT, triggerFood TEXT, water_Qy INTEGER," +
                "prot_Qy INTEGER, clci_Qy INTEGER, mg_Qy INTEGER, phph_Qy INTEGER, ptss_Qy INTEGER, na_Qy INTEGER, vtmn_C_Qy INTEGER);";

        // water_Qy 수분, prot_Qy 단백질, clci_Qy 칼슘, mg_Qy 마그네슘, phph_Qy 인, ptss_Qy 칼륨, na_Qy 나트륨, vtmn_C_Qy 비타민C

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS test";
        db.execSQL(sql);
        onCreate(db);
    }

    public void Delete(String name){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM test WHERE triggerFood = '" + name + "'";
        db.execSQL(sql);
        db.close();
    }

    public String getResult(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM test", null);
        while(cursor.moveToNext()){
            result += cursor.getString(1) + "\n";
        }

        return result;
    }

    public String getAll(){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM test", null);
        while(cursor.moveToNext()){
            result += cursor.getString(1) + "\n";
            result += getvt1(cursor.getString(1)) + "\n";
            result += getvt2(cursor.getString(1)) + "\n";
        }

        return result;
    }

    public int getFoodNum(String name){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM test WHERE triggerFood = " + '"' + name + '"', null);
        while(cursor.moveToNext()){
            result += cursor.getString(0);
        }

        return Integer.parseInt(result);
    }

    public String getFoodNumToFoodName(int num){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM test WHERE _id = " + '"' + num + '"', null);
        while(cursor.moveToNext()){
            result += cursor.getString(1);
        }

        return result;
    }

    public String getvt1(String name){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM test WHERE triggerFood = " + '"' + name + '"', null);
        while(cursor.moveToNext()){
            result += "수분: " + cursor.getString(2) + "% \n";
            result += "단백질: " + cursor.getString(3) + "g \n";
            result += "칼슘: " + cursor.getString(4) + "mg \n";
            result += "마그네슘: " + cursor.getString(5) + "mg \n";
        }

        return result;
    }

    public String getvt2(String name){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM test WHERE triggerFood = " + '"' + name + '"', null);
        while(cursor.moveToNext()){
            result += "인: " + cursor.getString(6) + "mg \n";
            result += "칼륨: " + cursor.getString(7) + "mg \n";
            result += "나트륨: " + cursor.getString(8) + "mg \n";
            result += "비타민C: " + cursor.getString(9) + "mg \n";
        }

        return result;
    }

    public String getNumtoVt1(int num){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM test WHERE _id = " + '"' + num + '"', null);
        while(cursor.moveToNext()){
            result += "수분: " + cursor.getString(2) + "% \n";
            result += "단백질: " + cursor.getString(3) + "g \n";
            result += "칼슘: " + cursor.getString(4) + "mg \n";
            result += "마그네슘: " + cursor.getString(5) + "mg \n";
        }

        return result;
    }

    public String getNumtoVt2(int num){
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM test WHERE _id = " + '"' + num + '"', null);
        while(cursor.moveToNext()){
            result += "인: " + cursor.getString(6) + "mg \n";
            result += "칼륨: " + cursor.getString(7) + "mg \n";
            result += "나트륨: " + cursor.getString(8) + "mg \n";
            result += "비타민C: " + cursor.getString(9) + "mg \n";
        }

        return result;
    }


    public Boolean dataCheck(String name){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM test WHERE triggerFood = " + '"' + name + '"', null);
        while(cursor.moveToNext()) {
            return cursor.isNull(1);
        }

        return true;
    }

    public int getCount(){
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM test";
        Cursor cursor = db.rawQuery(sql,null);

        return cursor.getCount();
    }


    public void dataDelete(int i){
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM test WHERE _id = " + i);
//        Cursor cursor = db.rawQuery("DELETE FROM test WHERE _id = " + '"' + i + '"', null);
    }
}
