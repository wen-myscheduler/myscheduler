package com.example.myscheduler;

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
    private Button btnLogout, btnAdd, btnView, btnSet;
    private App d ;
    private ListView list;
    private TextView tv,tv2;
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
        d = (App)getApplication();


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Log.d("id",id);
        ArrayList<String> as = d.search("name", "User", "id = \"" + id + "\"");
        System.out.println("size : " + as.size());
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
                startActivity(addintent);
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setintent = new Intent(getApplicationContext(), CalanderView.class);
                startActivity(setintent);
            }
        });
    }
    void view_data(String id){
        d.view_data("select * from " + id +"friend");
        ArrayAdapter<String> rr = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,d.arr);
        list.setAdapter(rr);
        int k  = d.arr.size();
        tv2.setText(" : "  +k+"명");

    }

}
