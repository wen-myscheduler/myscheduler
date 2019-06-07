package com.example.myscheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String strNickname, strProfile;
    private Button btnLogout, btnSel;
    private Spinner spinner;
    private App d ;
    private ListView list;
    private TextView tv,tv2;
    private int nextClass = 0;
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
        btnSel = (Button)findViewById(R.id.btn_sel);
        spinner = (Spinner)findViewById(R.id.spinner1);
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                nextClass = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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

        btnSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addintent= null;
                switch(nextClass) {
                    case 0: // 일정조회
                        //addintent = new Intent(getApplicationContext(), search.class);
                        break;
                    case 1: // 친구추가
                        addintent = new Intent(getApplicationContext(), search.class);
                        break;
                    case 2: // 일정등록
                        addintent = new Intent(getApplicationContext(), CalanderView.class);
                        break;
                    case 3: // 팀만들기
                        addintent = new Intent(getApplicationContext(), MakeTeam.class);
                }
                addintent.putExtra("id",id);
                startActivity(addintent);
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

}
