package com.example.myscheduler;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class CalanderView extends AppCompatActivity {

    private  CalendarView calanderView;
    private DateTime startDateTime;
    private DateTime endDateTime;
    private boolean isStart;
    private TextView date;
    private TextView s_date;
    private TextView e_date;
    private App db1;
    private Button btn2,pick_bt;
    private AlertDialog dialog;
    private int _hour, _minute,_year,_month,_day;
    private DateTime date_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_view);
        calanderView = (CalendarView) findViewById(R.id.calendarView);
        date = (TextView) findViewById(R.id.cal_title);
        s_date = (TextView)findViewById(R.id.cal_start);
        e_date = (TextView)findViewById(R.id.cal_end);
        btn2 = (Button)findViewById(R.id.button2);
        pick_bt = (Button)findViewById(R.id.pick_bt);
        db1 = (App)getApplication();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String moim = intent.getStringExtra("moim");
        isStart = true;

        calanderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar _cal = Calendar.getInstance();
                TimePickerDialog _alert = new TimePickerDialog(CalanderView.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        String msg = String.format("%d 시 %d 분", hour, min);
                        _hour = hour;
                        _minute = min;
                        _year = year;
                        _month = month;
                        _day = dayOfMonth;
                        Toast.makeText(CalanderView.this, msg, Toast.LENGTH_SHORT).show();
                        date_container = new DateTime(year,month+1,dayOfMonth,_hour,_minute);
                    }
                }, _cal.get(Calendar.HOUR_OF_DAY), _cal.get(Calendar.MINUTE), false);
                _alert.show();


            }
        });
        pick_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStart) {
                    startDateTime = date_container;
                    String data = _year + "년 " + (_month+1) + "월 " + _day + "일 " +_hour + "시 "+_minute+"분";
                    s_date.setText("시작 시간 : "+ data);
                    isStart = false;
                } else {
                    endDateTime = date_container;
                    String data = _year + "년 " + (_month+1) + "월 " + _day + "일 " +_hour + "시 "+_minute+"분";
                    e_date.setText("종료 시간 : "+ data);
                    isStart = true;
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startDateTime == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalanderView.this);
                    dialog = builder.setMessage("시작날짜를 선택해주세요")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                } else {
                    if(isStart) {
                        db1.InputSql("INSERT INTO scheduleTime VALUES('"+id+"','"+startDateTime.builder()+"','"+endDateTime.builder()+"','"+moim+"')");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalanderView.this);
                        dialog = builder.setMessage("종료날짜를 선택해주세요")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        Intent setintent = new Intent(getApplicationContext(), CalanderView.class);
                        setintent.putExtra("id",id);
                        startActivity(setintent);
                    }
                }
            }
        });
    }
}
