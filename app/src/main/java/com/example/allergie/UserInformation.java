package com.example.allergie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserInformation extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    Intent intent;
    Button main;


    DBHelper dbHelper;
    SQLiteDatabase db;


    Button correction;


    Button symptom_save;


    private SharedPreferences appData;
    private boolean tach_saveData, hives_saveData, colic_saveData, etc_saveData, diarr_saveData,
            dysp_saveData, consci_saveData, edema_saveData, puke_saveData, whirl_saveData;
    CheckBox tach, hives, colic, etc, diarr, dysp, consci, edema, puke, whirl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);


        main = (Button) findViewById(R.id.gotoMain);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserInformation.this, MainActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

                switch (menuItem.getItemId()){
                    case R.id.addBtn:
                        intent = new Intent(UserInformation.this, InputAllergy.class);
                        startActivity(intent);
                        return true;

                    case R.id.foodBtn:
                        intent = new Intent(UserInformation.this, AllergyFoodListInfo.class);
                        startActivity(intent);
                        return true;


                    case R.id.mapBtn:
                        intent = new Intent(UserInformation.this, Map.class);
                        startActivity(intent);
                        return true;

                }

                return false;
            }
        });




        // 알레르기 증상 체크 박스
        tach = (CheckBox) findViewById(R.id.tachycardia);
        hives = (CheckBox) findViewById(R.id.hives);
        colic = (CheckBox) findViewById(R.id.colic);
        diarr = (CheckBox) findViewById(R.id.diarrhea);
        dysp = (CheckBox) findViewById(R.id.dyspnoea);
        consci = (CheckBox) findViewById(R.id.consciousness);
        edema = (CheckBox) findViewById(R.id.edema);
        puke = (CheckBox) findViewById(R.id.puke);
        whirl = (CheckBox) findViewById(R.id.whirl);
        etc = (CheckBox) findViewById(R.id.etc);

        // 저장 버튼 클릭
        symptom_save = (Button) findViewById(R.id.symSaveBtn);
        symptom_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });


        // 증상을 알리는 상태 내역 저장
        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        tach.setChecked(tach_saveData);
        hives.setChecked(hives_saveData);
        colic.setChecked(colic_saveData);
        diarr.setChecked(diarr_saveData);
        dysp.setChecked(dysp_saveData);
        consci.setChecked(consci_saveData);
        edema.setChecked(edema_saveData);
        puke.setChecked(puke_saveData);
        whirl.setChecked(whirl_saveData);
        etc.setChecked(etc_saveData);


        dbHelper = new DBHelper(UserInformation.this, null);
        db = dbHelper.getReadableDatabase();

        ListView listView = (ListView) findViewById(R.id.listView);
        String sql = "SELECT * FROM test;";
        Cursor c = db.rawQuery(sql, null);

        String[] strs = new String[]{"triggerFood", "water_Qy", "prot_Qy", "clci_Qy",
                "mg_Qy", "phph_Qy", "ptss_Qy", "na_Qy", "vtmn_C_Qy"};
        int[] ints = new int[]{R.id.name_list, R.id.water_list, R.id.prot_list, R.id.clci_list,
                R.id.mg_list, R.id.phph_list, R.id.ptss_list, R.id.na_list, R.id.vtmnC_list};

        CursorAdapter adapter = null;
        adapter = new CursorAdapter(listView.getContext(), R.layout.allergy_list_view, c, strs, ints);

        listView.setAdapter(adapter);



        // 알레르기 내역 수정
        correction = (Button) findViewById(R.id.correctionBtn);
        correction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(UserInformation.this, ModifyInformation.class);
                startActivity(intent);
            }
        });
    }




    // SharedPreferences 상태 저장
    private void save(){
        SharedPreferences.Editor editor = appData.edit();

        editor.putBoolean("tachycardia", tach.isChecked());
        editor.putBoolean("hives", hives.isChecked());
        editor.putBoolean("colic", colic.isChecked());
        editor.putBoolean("diarrhea", diarr.isChecked());
        editor.putBoolean("dyspnoea", dysp.isChecked());
        editor.putBoolean("consciousness", consci.isChecked());
        editor.putBoolean("edema", edema.isChecked());
        editor.putBoolean("puke", puke.isChecked());
        editor.putBoolean("whirl", whirl.isChecked());
        editor.putBoolean("etc", etc.isChecked());

        editor.apply();
    }

    // 저장된 SharedPreferences 데이터 불러오기
    private void load(){
        tach_saveData = appData.getBoolean("tachycardia", false);
        hives_saveData = appData.getBoolean("hives", false);
        colic_saveData = appData.getBoolean("colic", false);
        diarr_saveData = appData.getBoolean("diarrhea", false);
        dysp_saveData = appData.getBoolean("dyspnoea", false);
        consci_saveData = appData.getBoolean("consciousness", false);
        edema_saveData = appData.getBoolean("edema", false);
        puke_saveData = appData.getBoolean("puke", false);
        whirl_saveData = appData.getBoolean("whirl", false);
        etc_saveData = appData.getBoolean("etc", false);
    }


}