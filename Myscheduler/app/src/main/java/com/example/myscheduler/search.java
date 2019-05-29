package com.example.myscheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class search extends AppCompatActivity {
    private ListView list;
    private App d;
    private TextView tv;
    private Dialog dialog;
    private Button bt_search,fulls;
    private EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bt_search = (Button)findViewById(R.id.bt_search);
        list = (ListView)findViewById(R.id.listview1);
        tv = (TextView)findViewById(R.id.text1);
        search = (EditText)findViewById(R.id.ed_search);
        fulls = (Button)findViewById(R.id.Bt_fulls);

        d = (App)getApplication();
        d.dbConnFuc();

        Intent intent = getIntent();
        String user_id = intent.getStringExtra("id");
        view_data(user_id);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Text = (String) parent.getItemAtPosition(position) ;
                AlertDialog.Builder builder = new AlertDialog.Builder(search.this);
                dialog = builder.setMessage(Text+"님을 친구추가 하시겠습까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                d.InputSql("Insert into "+user_id+"friend values(\""+Text+"\");");
                                Toast.makeText(getApplicationContext(), Text + " 님이 친구로 추가되었습니다!!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(search.this, MainActivity.class);
                                intent.putExtra("id",user_id);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("취소",null)
                        .create();
                dialog.show();
            }
        });
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to_sc = search.getText().toString();
                ArrayList<String> arr = d.view_data("select * from User where id LIKE \'%"+ to_sc + "%\' and id != \"" + user_id +"\"");
                ArrayAdapter<String> rr;
                rr = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arr);
                list.setAdapter(rr);
            }
        });
        fulls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_data(user_id);
            }
        });
    }

   void view_data(String userid){
        ArrayList<String> arr = new ArrayList<String>();
        arr = d.view_data("select * from User where id != \"" + userid+"\"");
        ArrayAdapter<String> rr;
        rr = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arr);
        list.setAdapter(rr);
    }


}
