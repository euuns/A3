package com.example.allergie;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;


public class Detailed extends AppCompatActivity {

    private int posi;
    private JSONArray obc;
    private String name;
    private String rcp;
    private String img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new  WarningFragment(); // Fragment 생성



        Intent intent = getIntent();
        try {
            obc = new JSONArray(intent.getExtras().getString("cc"));
            posi = Integer.parseInt(intent.getExtras().getString("positon"));
            System.out.println(obc.getJSONObject(posi).get("RCP_NM").toString());
            System.out.println(obc.getJSONObject(posi).get("ATT_FILE_NO_MAIN").toString());
            System.out.println(obc.getJSONObject(posi).get("RCP_PARTS_DTLS").toString());


            name = obc.getJSONObject(posi).get("RCP_NM").toString();
            img = obc.getJSONObject(posi).get("ATT_FILE_NO_MAIN").toString();
            rcp = obc.getJSONObject(posi).get("RCP_PARTS_DTLS").toString();

            transaction.replace(R.id.fragmentContainerView2,fragment);
            transaction.commit();
//            re(posi);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public void back(View v){
        finish();
    }

    public void plus(View v) throws JSONException {
        posi++;
        if(obc.length()-1 >= posi){
            System.out.println(obc.length() + "  "+posi);
            re(posi);
        }
        else{
            posi = obc.length()-1;
            Toast.makeText(getApplicationContext(),"마지막 목록입니다",Toast.LENGTH_SHORT).show();
            System.out.println("마지막 목록 입니다");

        }

    }

    public void minus(View v) throws JSONException {
        posi--;
        if(0<=posi){
            System.out.println(obc.length() + "  "+posi);
            re(posi);
        }
        else{
            posi = 0;
            System.out.println("첫번째 목록입니다");
            Toast.makeText(getApplicationContext(),"첫번째 목록입니다",Toast.LENGTH_SHORT).show();
        }

    }

    public void chck(View v) throws JSONException {
        Button button = findViewById(R.id.chck);
        button.setVisibility(View.GONE);
        re(posi);
    }

    public void re(int po) throws JSONException {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        FragmentTransaction ft = manager.beginTransaction();
        Bundle bundle = new Bundle();
        Fragment fragment = new  FoodImageFragment(); // Fragment 생성
        System.out.println("========================================================");
        System.out.println(obc.getJSONObject(posi).get("RCP_NM").toString());
        System.out.println(obc.getJSONObject(posi).get("ATT_FILE_NO_MAIN").toString());
        System.out.println(obc.getJSONObject(posi).get("RCP_PARTS_DTLS").toString());
        System.out.println("========================================================");
        bundle.putString("name", name); // Key, Value
        bundle.putString("img", img);
        bundle.putString("rcp", rcp);


        fragment.setArguments(bundle);
        transaction.replace(R.id.fragmentContainerView2,fragment);
        transaction.detach(fragment).attach(fragment).commit();
    }
}