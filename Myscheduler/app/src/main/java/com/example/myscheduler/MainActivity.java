package com.example.myscheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String strNickname, strProfile;
    private Button btnLogout, btnAdd, btnView, btnSet,btnSche;
    private App d ;
    private ListView list;
    private TextView tv,tv2;
    private String moim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView)findViewById(R.id.main_list);
        TextView tvNickname = findViewById(R.id.textNick2);
        ImageView ivProfile = findViewById(R.id.ivProfile);
        tv = (TextView)findViewById(R.id.bt_friendlist);
        tv2 = (TextView)findViewById(R.id.textview6);
        btnLogout= findViewById(R.id.btnLogout);
        btnAdd = (Button)findViewById(R.id.btn_adfriend);
        btnView = (Button)findViewById(R.id.btn_scheduleview);
        btnSet = (Button)findViewById(R.id.btn_setsched);


        btnSche = (Button)findViewById(R.id.btn_team);
        d = (App)getApplication();


        Intent intent = getIntent();
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
                DialogSelectOption();
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
    }
    void view_data(String id){
        ArrayList<String> arr = new ArrayList<String>();
        arr = d.view_data("select * from " + id +"friend");
        ArrayAdapter<String> rr = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arr);
        list.setAdapter(rr);
        int k  = arr.size();
        tv2.setText(" : "  +k+"명");

    }

     void DialogSelectOption() {
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
                             setintent.putExtra("moim",moim);
                             startActivity(setintent);
                         }
                     }).setNegativeButton("취소", null);
             ab.show();
        }
     }



}
