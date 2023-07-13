package com.example.allergie;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class InputAllergy extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    Intent intent;
    Button main;

    // 데이터 베이스 관리
    SQLiteDatabase db;
    DBHelper helper;

    // 검색
    EditText insert_txt;
    Button search_btn;

    // 검색 기록
    TextView info_text;
    LinearLayout info_layout;
    Button insert_btn;          // 알러지 등록
    TextView food_name;         // 알러지 유발 식품


    // 음식 코드
    String food_code;

    // 데이터 베이스 속성
    String triggerFood, water_Qy, prot_Qy, clci_Qy, mg_Qy, phph_Qy, ptss_Qy, na_Qy, vtmn_C_Qy;


    String expression_age_xml, symptom_con_xml, symptom_xml, etc_xml;
    TextView symptom_con_layout, expression_age_layout, symptom_layout, etc_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_allergie);

        insert_txt = (EditText) findViewById(R.id.insertText);
        search_btn = (Button) findViewById(R.id.searchBtn);

        info_text = (TextView) findViewById(R.id.allergyInfoTxt);
        info_layout = (LinearLayout) findViewById(R.id.result);
        insert_btn = (Button) findViewById(R.id.insertFood);

        food_name = (TextView) findViewById(R.id.foodName);

        symptom_con_layout = (TextView) findViewById(R.id.symptom_con_xml);
        expression_age_layout = (TextView) findViewById(R.id.expression_age_xml);
        symptom_layout = (TextView) findViewById(R.id.symptom_xml);
        etc_layout = (TextView) findViewById(R.id.etc_xml);



        helper = new DBHelper(this, null);
        db = helper.getReadableDatabase();



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){

                switch (menuItem.getItemId()){
                    case R.id.foodBtn:
                        intent = new Intent(InputAllergy.this, AllergyFoodListInfo.class);
                        startActivity(intent);
                        return true;


                    case R.id.mapBtn:
                        intent = new Intent(InputAllergy.this, Map.class);
                        startActivity(intent);
                        return true;


                    case R.id.userBtn:
                        intent = new Intent(InputAllergy.this, UserInformation.class);
                        startActivity(intent);
                        return true;
                }

                return false;
            }
        });


        main = (Button) findViewById(R.id.gotoMain);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(InputAllergy.this, MainActivity.class);
                startActivity(intent);
            }
        });




        // 검색 버튼 클릭 리스너
        search_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                info_text.setText("");
                expression_age_layout.setText("");
                symptom_con_layout.setText("");
                symptom_layout.setText("");
                etc_layout.setText("");

                // 식품 명으로 식품 코드 반환
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        food_code = getXmlData();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                triggerFood = insert_txt.getText().toString();
                                insert_txt.setText("");

                                if(food_code.equals("NOT_DATA")){
                                    Toast.makeText(InputAllergy.this, "올바른 음식이 아닙니다.", Toast.LENGTH_SHORT).show();
                                    info_layout.setVisibility(View.GONE);
                                }

                                else{

                                    // 식품이 검색됐을 경우
                                    info_layout.setVisibility(View.VISIBLE);
                                    info_text.setText("알레르기 유발 식품");

                                    // 해당 식품의 코드를 이용해 영양소 반환
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String fName = getFoodCode(food_code);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    food_name.setText(triggerFood);

                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            getAllergyInfoXml(triggerFood);

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    expression_age_layout.setText(expression_age_xml);
                                                                    symptom_con_layout.setText(symptom_con_xml);
                                                                    symptom_layout.setText(symptom_xml);
                                                                    etc_layout.setText(etc_xml);
                                                                }
                                                            });
                                                        }
                                                    }).start();
                                                }
                                            });
                                        }
                                    }).start();
                                }
                            }
                        });
                    }
                }).start();
            }
        });


        // 추가 버튼 클릭 시 유저 알러지 정보 등록
        insert_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String sql = "INSERT INTO test VALUES ( null , '" + triggerFood + "', '" + water_Qy + "', '" + prot_Qy + "', '" + clci_Qy + "', '" + mg_Qy + "', '" + phph_Qy + "', '" + ptss_Qy + "', '" + na_Qy + "', '" + vtmn_C_Qy + "');";

                db.execSQL(sql);

                Toast.makeText(InputAllergy.this, "추가 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });


    }



    // 식품 명을 이용해 식품 코드를 알아내는 메서드
    String getXmlData() {
        String str = insert_txt.getText().toString();
        String key = "dPM9q%2FsCT%2FFehBWrvxw2L%2Ff2%2BOaR69GZ5ngTOqovDCobHfofCRZFGMXbu1qIia3Kh9ia%2BUmHv7PZj6NiXtgNrA%3D%3D";

        String queryUrl="https://apis.data.go.kr/1390802/AgriFood/MzenFoodCode/getKoreanFoodList?serviceKey="+key+"&Page_No=1&Page_Size=30&food_Name="+str;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;
            String f_name = "";

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {

                    tag = xpp.getName();

                    if (tag.equals("result_Msg")) {
                        xpp.next();

                        if(!xpp.getText().equals("OK")) return "NOT_DATA";

                    }

                    else if (tag.equals("food_Code")){
                        xpp.next();

                        f_name = xpp.getText();
                    }

                    else if(tag.equals("large_Name")){
                        xpp.next();

                        String a = xpp.getText();
                        if(a.equals("원재료") || a.equals("우유 및 유제품류"))
                            return f_name;
                    }

                    else if (tag.equals("middle_Name")){
                        xpp.next();

                        String a = xpp.getText();
                        if(a.equals("과일류"))
                            return f_name;
                    }

                }

                eventType = xpp.next();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "NOT_DATA";
    }


    // 식품 코드를 이용해 식품의 각 영양소를 알아내는 메서드
    String getFoodCode(String code){
        String result = "";
        String key = "dPM9q%2FsCT%2FFehBWrvxw2L%2Ff2%2BOaR69GZ5ngTOqovDCobHfofCRZFGMXbu1qIia3Kh9ia%2BUmHv7PZj6NiXtgNrA%3D%3D";
        
        String queryUrl="https://apis.data.go.kr/1390802/AgriFood/MzenFoodNutri/getKoreanFoodIdntList?serviceKey="+key+"&food_Code="+code;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {

                    tag = xpp.getName();
                    
                    if (tag.equals("main_Food_Name")){
                        xpp.next();

                        // 식품 이름은 result에 저장
                        result = xpp.getText();

                        // 식품 영양소는 태그로 구분하여 변수에 저장 후 DB 저장 시 사용
                    } else if (tag.equals("water_Qy")) {
                        xpp.next();
                        water_Qy = xpp.getText();
                    } else if (tag.equals("prot_Qy")) {
                        xpp.next();
                        prot_Qy = xpp.getText();
                    } else if (tag.equals("clci_Qy")) {
                        xpp.next();
                        clci_Qy = xpp.getText();
                    } else if (tag.equals("mg_Qy")) {
                        xpp.next();
                        mg_Qy = xpp.getText();
                    } else if (tag.equals("phph_Qy")) {
                        xpp.next();
                        phph_Qy = xpp.getText();
                    } else if (tag.equals("ptss_Qy")) {
                        xpp.next();
                        ptss_Qy = xpp.getText();
                    } else if (tag.equals("na_Qy")) {
                        xpp.next();
                        na_Qy = xpp.getText();
                    } else if (tag.equals("vtmn_C_Qy")) {
                        xpp.next();
                        vtmn_C_Qy = xpp.getText();
                    }
                }

                eventType = xpp.next();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }



    public void getAllergyInfoXml(String name){
        InputStream inputStream = getResources().openRawResource(R.raw.allergy_info_xml);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        XmlPullParserFactory factory = null;
        XmlPullParser xmlPullParser = null;



        try{
            factory = XmlPullParserFactory.newInstance();
            xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new InputStreamReader(inputStream, "UTF-8"));

            String tag;
            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {

                    tag = xmlPullParser.getName();

                    if(tag.equals("FOOD_NM")) {
                        xmlPullParser.next();
                        String nm = xmlPullParser.getText();

                        if(nm.equals(name)){

                            eventType = xmlPullParser.next();

                            while(eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_TAG) {

                                    tag = xmlPullParser.getName();

                                    if (tag.equals("EXPRESSION_AGE")) {
                                        xmlPullParser.next();
                                        expression_age_xml = xmlPullParser.getText();


                                    } else if (tag.equals("SYMPTOM_CON")) {
                                        xmlPullParser.next();
                                        symptom_con_xml = xmlPullParser.getText();
                                    } else if (tag.equals("SYMPTOM")) {
                                        xmlPullParser.next();
                                        symptom_xml = xmlPullParser.getText();
                                    } else if (tag.equals("ETC")) {
                                        xmlPullParser.next();
                                        etc_xml = xmlPullParser.getText();
                                        return;
                                    }
                                }

                                eventType = xmlPullParser.next();

                            }
                        }
                    }
                }

                eventType = xmlPullParser.next();

            }


            try{

            } catch (Exception e){
                e.printStackTrace();
            }

        } catch (XmlPullParserException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(reader != null)
                    reader.close();

                if(inputStreamReader != null)
                    inputStreamReader.close();

                if(inputStream != null)
                    inputStream.close();

            } catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }


}



