package com.example.allergie;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoodImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodImageFragment newInstance(String param1, String param2) {
        FoodImageFragment fragment = new FoodImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            System.out.println("tlfgod");
            /*Bundle bundle = new Bundle();


            String nwname = getArguments().getString("name");
            String nwUrl = getArguments().getString("img");
            String nwRcp = getArguments().getString("rcp");
            System.out.println(nwname+"출력"+nwUrl+"cnffur"+nwRcp);*/

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        String urlaa = "https://velog.velcdn.com/images/jjunyjjuny/post/da903d71-44fc-4f6f-92bb-b1b2d0092020/error_img.png";
        View view = inflater.inflate(R.layout.fragment_food_image,container,false);
        String nwname = null;
        String nwRcp = null;
        URL nwUrl;
        Bundle bundle = getArguments();
        TextView name;
        TextView rcp;
        ImageView imag ;
        final Bitmap[] bitmap = new Bitmap[1];


        name = view.findViewById(R.id.textView2);
        rcp = view.findViewById(R.id.textView4);
        imag = view.findViewById(R.id.imageView3);

        if(bundle != null){
            nwname = bundle.getString("name");
            urlaa = bundle.getString("img");
            nwRcp = bundle.getString("rcp");

            System.out.println(nwname+"출력"+urlaa+"cnffur"+nwRcp);
        }


        String finalUrlaa = urlaa;
        Thread uThread = new Thread() {
            @Override
            public void run(){
                try{
                    // 이미지 URL 경로
                    URL url = new URL(finalUrlaa);

                    // web에서 이미지를 가져와 ImageView에 저장할 Bitmap을 만든다.
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true); // 서버로부터 응답 수신
                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)

                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                    bitmap[0] = BitmapFactory.decodeStream(is); // Bitmap으로 변환

                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };

        uThread.start(); // 작업 Thread 실행

        try{
            //메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야 한다.
            //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리도록 한다.
            //join() 메서드는 InterruptedException을 발생시킨다.
            uThread.join();

            //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
            imag.setImageBitmap(bitmap[0]);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        name.setText(nwname);

        rcp.setText(nwRcp);
        return view;
        /*return inflater.inflate(R.layout.fragment_blank, container, false);*/

    }





}