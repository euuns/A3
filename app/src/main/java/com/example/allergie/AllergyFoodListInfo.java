package com.example.allergie;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AllergyFoodListInfo extends AppCompatActivity {

    private final String BASEURL = "https://openapi.foodsafetykorea.go.kr/api/6ed93bc795f34c379d0b/COOKRCP01/json/1/20/";
    EditText et;


    private ArrayList<ContactsContract.RawContacts.Data> mArrayList;
    private RecipeApiService jsonPlaceHolderApi;

    private ArrayList<NameForAdapterList> dataList = new ArrayList<>();
    private RecyclerView recyclerView;


    LinearLayoutManager manager;
    private JSONArray cc; // row 데이터를 받아옴


    ShowNameAdapter adapter = new ShowNameAdapter();


    Intent intent;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy_food_list_info);
        et = findViewById(R.id.et);
        recyclerView = findViewById(R.id.RecyclerView);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(RecipeApiService.class);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ShowNameAdapter.ViewHolder viewHolder, View view, int position) throws JSONException {
                NameForAdapterList item = adapter.getItem(position);
                System.out.println("=============="+position);
//                Toast.makeText(getApplicationContext(),"position  =  "+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Detailed.class);
                intent.putExtra("cc", String.valueOf(cc)); /*송신*/
                intent.putExtra("positon", String.valueOf(position));
                startActivity(intent);
            }
        });


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

                switch (menuItem.getItemId()){
                    case R.id.addBtn:
                        intent = new Intent(AllergyFoodListInfo.this, InputAllergy.class);
                        startActivity(intent);
                        return true;

                    case R.id.mapBtn:
                        intent = new Intent(AllergyFoodListInfo.this, Map.class);
                        startActivity(intent);
                        return true;

                    case R.id.userBtn:
                        intent = new Intent(AllergyFoodListInfo.this, UserInformation.class);
                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });

    }


    private void createPost(String kind, int i) {
        //아이템 전체 삭제

        DBHelper dbHelper = new DBHelper(this, null);
        SQLiteDatabase db;
        db = dbHelper.getReadableDatabase();

        String testName = dbHelper.getFoodNumToFoodName(i);

        Call<String> call = jsonPlaceHolderApi.createGET(testName,kind); // 새우에 다른 부분에서 불러온 값을 입력(새우는 임시값)
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                String post = response.body();
                System.out.println("..............>>>>>>"+post);
                try {
                    JSONObject ab = new JSONObject(post);
                    JSONObject bb = ab.getJSONObject("COOKRCP01");
                    String count = bb.getString("total_count");
                    if(count.equals("0")){
                        adapter.addItem(new NameForAdapterList("일치하는 정보가 없습니다", testName));
                    }
                    else{
                        cc = bb.getJSONArray("row");
                        for(int i = 0; i< cc.length(); i++){
                            String st =  cc.getJSONObject(i).get("RCP_NM").toString();
                            adapter.addItem(new NameForAdapterList(st, testName));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {

                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("onFailure    :    "+t.getMessage());
            }
        });
    }
    public void put(View view){

        DBHelper dbHelper = new DBHelper(this, null);

        String kind = String.valueOf(et.getText());
        System.out.println(kind);

        if(kind.length()>0){
            for (int i = 1; i < dbHelper.getCount(); i++) {
                createPost(kind, i);
                et.setText("");
            }

        }
        else{
            Toast.makeText(getApplicationContext(),"값을 추가해 주세요",Toast.LENGTH_SHORT).show();
        }

    }



}