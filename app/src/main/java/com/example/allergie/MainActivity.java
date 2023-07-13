package com.example.allergie;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    DBHelper helper;
    SQLiteDatabase db;

    Intent intent;
    BottomNavigationView bottomNavigationView;

    // 유저 등록 알러지 내역
    TextView allergy_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allergy_info = (TextView) findViewById(R.id.userAll);

        // 하단바 설정
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

                switch (menuItem.getItemId()){
                    case R.id.addBtn:
                        intent = new Intent(MainActivity.this, InputAllergy.class);
                        startActivity(intent);
                        return true;

                    case R.id.foodBtn:
                        intent = new Intent(MainActivity.this, AllergyFoodListInfo.class);
                        startActivity(intent);
                        return true;

                    case R.id.mapBtn:
                        intent = new Intent(MainActivity.this, Map.class);
                        startActivity(intent);
                        return true;

                    case R.id.userBtn:
                        intent = new Intent(MainActivity.this, UserInformation.class);
                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });


        helper = new DBHelper(this, null);
        try{
            db = helper.getWritableDatabase();
        } catch (SQLiteException ex){
            db = helper.getReadableDatabase();
        }


        allergy_info.setText(helper.getResult());


    }




}