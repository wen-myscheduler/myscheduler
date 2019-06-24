package com.example.myscheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class check_time extends AppCompatActivity {
    private ListView list;
    private App d;
    private ArrayList<String> arr_time;
    private TextView time1;
    private Button rc;
    private String as = "";
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_time);
        list = (ListView)findViewById(R.id.listview_time);
        arr_time = new ArrayList<>();
        d = (App)getApplication();
        d.dbConnFuc();
        time1 = (TextView)findViewById(R.id.TIME1);
        rc = (Button)findViewById(R.id.recommend);
        select se = new select();
        Intent intent = getIntent();
        String moim = intent.getStringExtra("moim");
        arr_time = view_data(moim);
        String start_day = d.search("start_time","TEAM","NAME = '"+moim+"'").get(0);
        String end_day = d.search("end_time","TEAM","NAME = '"+moim+"'").get(0);
        String[] day = arr_time.toArray(new String[arr_time.size()]);
        int d = se.getDiffDay(start_day,end_day);
        int[] count = new int[d];
        for(int i=0; i<day.length; i++)
        {
            int c = se.getDiffDay2(start_day,day[i]);
            int k = se.getDiffDay2(day[i].substring(0,10),day[i].substring(17,27));
            for(int t=0;t<(k+1);t++) {
                if(c+t <d)
               {
                   if(c<0) {}
                   else count[c+t]++;
               }
            }
        }
        int chai = se.cha(count,day);
        String add_day = se.add(start_day,chai);
        int[] time = se.ms(start_day,end_day,add_day,day);
        int e = se.cha2(time,day);

        if(e<10) {
            as = ""+add_day +"일 0" + e+":00시를 \n모임 시간으로 추천합니다.";
        }
        else
            as = ""+add_day +"일 " + e+":00시를 \n모임 시간으로 추천합니다.";

        rc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(check_time.this);
                dialog = builder.setMessage(as)
                        .setPositiveButton("확인", null)
                        .create();
                dialog.show();

//                time1.setText(as);
            }
        });


}

    ArrayList<String> view_data(String moim){
        ArrayList<String> arr = new ArrayList<>();
        ArrayList<String> arr_time = new ArrayList<>();
        ArrayList<String> arr1,arr2,arr3;
        arr1 = d.search("id","scheduleTime","moim = '"+moim+"'");
        arr2 = d.search("start_time","scheduleTime","moim = '"+moim+"'");
        arr3 = d.search("end_time","scheduleTime","moim = '"+moim+"'");
        for(int i =0; i <arr1.size(); i++)
        {
            String s = arr1.get(i) +" : " + arr2.get(i).substring(0,16) + "~" +arr3.get(i).substring(0,16);
            arr.add(s);
        }
        System.out.println("arr : " + arr.size());
        ArrayAdapter<String> rr;
        rr = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arr);
        list.setAdapter(rr);
        for(int i = 0; i<arr1.size();i++)
        {
            String s = arr2.get(i).substring(0,16) + "~"+arr3.get(i).substring(0,16);
            arr_time.add(s);
        }
        return arr_time;
    }


}
