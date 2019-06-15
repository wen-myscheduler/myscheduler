package com.example.myscheduler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.BufferedOutputStream;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String strNickname, strProfile;
    private Button btnLogout, btnAdd, btnView, btnSet,btnSche;
    private App d ;
    private ListView list;
    private ListViewAdapter2 adapter;
    private TextView tv,tv2;
    private String moim;
    private ImageView ivProfile;
    private static final int REQUEST_IMAGE_1 = 1;
    private String imagePath1 = "";
    Bitmap userImagesource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ListViewAdapter2();
        list = (ListView)findViewById(R.id.main_list);
        TextView tvNickname = findViewById(R.id.textNick2);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        tv = (TextView)findViewById(R.id.bt_friendlist);
        tv2 = (TextView)findViewById(R.id.textview6);
        btnLogout= findViewById(R.id.btnLogout);
        btnAdd = (Button)findViewById(R.id.btn_adfriend);
        btnView = (Button)findViewById(R.id.btn_scheduleview);
        btnSet = (Button)findViewById(R.id.btn_setsched);
        ivProfile = (ImageView)findViewById(R.id.ivProfile);

        ivProfile.setImageResource(R.drawable.default_image2);
        btnSche = (Button)findViewById(R.id.btn_team);
        d = (App)getApplication();


        Intent intent = getIntent();
        int login = intent.getIntExtra("login",0);
        if(login == 3)
        {
            naverlogin();
        }
        String id = intent.getStringExtra("id");
        ArrayList<String> as = d.search("name", "User", "id = \"" + id + "\"");
        for(String s : as) {
            tvNickname.setText(s);
        }
//        strNickname = intent.getStringExtra("name");
//        strProfile = intent.getStringExtra("profile");
//        tvNickname.setText(strNickname);
//        Glide.with(this).load(strProfile).into(ivProfile);

        d.dbConnFuc();
        view_data(id);


        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addintent = new Intent(getApplicationContext(), search.class);
                addintent.putExtra("id",id);
                startActivity(addintent);
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelectOption(id);
            }
        });
        btnSche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scheintent = new Intent(getApplicationContext(),MakeTeam.class);
                scheintent.putExtra("id",id);
                startActivity(scheintent);
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelectOption2(moim);
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Get Album"), REQUEST_IMAGE_1);
        }
        });
    }
    void view_data(String id){
        ArrayList<String> arr = new ArrayList<String>();
        arr = d.view_data("select * from " + id +"friend");
        //ArrayAdapter<String> rr = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arr);
        list.setAdapter(adapter);
        for(String s : arr)
        {
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.default_image2), s);
        }
        int k  = arr.size();
        tv2.setText(" : "  +k+"명");

    }

     void DialogSelectOption(String id) {
         ArrayList<String> items = d.search("NAME", "TEAM");
         int d = items.size();
         System.out.println(d);
         String[] items1 = new String[d];
         int i = 0;
         for (String s : items) {
             items1[i++] = s;
         }
         if (d == 0) {
             Toast.makeText(getApplicationContext(), " 가입된 팀이 없습니다.", Toast.LENGTH_LONG).show();
         } else {
             AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
             ab.setTitle("모임 선택");
             ab.setSingleChoiceItems(items1, 1,
                     new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int whichButton) {
                             // 각 리스트를 선택했을때
                             moim = items1[whichButton];
                             System.out.println("버튼 위치" + whichButton);
                             System.out.println(items1[whichButton]);

                         }
                     }).setPositiveButton("선택",
                     new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int whichButton) {
                             // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                            // System.out.println("moim값 :  " + moim);
                             Intent setintent = new Intent(getApplicationContext(), CalanderView.class);
                             setintent.putExtra("id",id);
                             setintent.putExtra("moim",moim);
                             startActivity(setintent);
                         }
                     }).setNegativeButton("취소", null);
             ab.show();
        }
     }
    void DialogSelectOption2(String id) {
        ArrayList<String> items = d.search("NAME", "TEAM");
        int d = items.size();
        System.out.println(d);
        String[] items1 = new String[d];
        int i = 0;
        for (String s : items) {
            items1[i++] = s;
        }
        if (d == 0) {
            Toast.makeText(getApplicationContext(), " 가입된 팀이 없습니다.", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle("모임 선택");
            ab.setSingleChoiceItems(items1, 1,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 각 리스트를 선택했을때
                            moim = items1[whichButton];
                            System.out.println("버튼 위치" + whichButton);
                            System.out.println(items1[whichButton]);

                        }
                    }).setPositiveButton("선택",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                            // System.out.println("moim값 :  " + moim);
                            Intent setintent = new Intent(getApplicationContext(), check_time.class);
                            setintent.putExtra("moim",moim);
                            startActivity(setintent);
                        }
                    }).setNegativeButton("취소", null);
            ab.show();
        }
    }
    void naverlogin() {
        Intent intent = getIntent();
        String userImage = intent.getStringExtra("Image");
        Thread imageThread = new Thread() {//프로필 이미지 다운로드
            public void run() {
                try {
                    //String userImage: 네이버 프로필 조회 API에서 받은 유저 프로필사진 주소
                    URL url = new URL(userImage);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    userImagesource = BitmapFactory.decodeStream(is);
                } catch (Exception e) {//프로필 이미지 다운로드 실패
                    //ImageView drawer_Ivprofile: 사용자 프로필 사진을 보여주는 이미지뷰
                    ivProfile.setImageResource(R.drawable.default_image2);
                    Toast.makeText(MainActivity.this, "로그인에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        imageThread.start();
        try {
            imageThread.join();
            //ImageView drawer_Ivprofile: 사용자 프로필 사진을 보여주는 이미지뷰
            ivProfile.setImageBitmap(userImagesource);
            //drawerstate = 1;
        } catch (Exception e) {//프로필 이미지 적용 실패
            //R.drawable.person: 로그인 실패 시 띄워두는 이미지파일. 그냥 사람 이모티콘이다.
            ivProfile.setImageResource(R.drawable.default_image2);
            Toast.makeText(MainActivity.this, "로그인에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
        }

    }







}
