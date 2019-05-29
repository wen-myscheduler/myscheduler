package com.example.myscheduler;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class search extends AppCompatActivity {
    private ListView list;
    private App d;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //bt_add_new = (Button)findViewById(R.id.bt_search);
        list = (ListView)findViewById(R.id.listview1);
        tv = (TextView)findViewById(R.id.text1);
        d = (App)getApplication();
        d.dbConnFuc();
        view_data();

//        bt_add_new.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

   void view_data(){
        d.view_data("select * from User");
        ArrayAdapter<String> rr;
        rr = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,d.arr);
        list.setAdapter(rr);
    }


}
