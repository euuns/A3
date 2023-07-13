package com.example.allergie;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ModifyInformation extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;

    Button delete_btn;
    Button save_btn;

    SharedPreferences dbIDNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_information);


        dbHelper = new DBHelper(ModifyInformation.this, null);
        db = dbHelper.getReadableDatabase();

        ListView listView = (ListView) findViewById(R.id.allergy_list);
        String sql = "SELECT * FROM test;";
        Cursor c = db.rawQuery(sql, null);

        String[] strs = new String[]{"triggerFood"};
        int[] ints = new int[]{R.id.num};

        CursorAdapter adapter = null;
        adapter = new CursorAdapter(listView.getContext(), R.layout.allergy_modify_list, c, strs, ints);

        listView.setAdapter(adapter);


        delete_btn = (Button) findViewById(R.id.deleteBtn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checked = listView.getCheckedItemPosition();
                checked += 1;
                dbHelper.dataDelete(checked);
            }
        });

        save_btn = (Button) findViewById(R.id.okBtn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

