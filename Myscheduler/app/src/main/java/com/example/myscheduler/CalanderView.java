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

    CalendarView calanderView;
    DateTime startDateTime;
    DateTime endDateTime;
    boolean isStart;
    TextView date;
    TextView date2;
    TextView date3;
    App db1;
    Button btn2;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_view);
        calanderView = (CalendarView) findViewById(R.id.calendarView);
        date = (TextView) findViewById(R.id.textView3);
        date2 = (TextView) findViewById(R.id.textView1);
//        date3 = (TextView) findViewById(R.id.textView4);
        btn2 = (Button)findViewById(R.id.button2);
        db1 = (App)getApplication();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        isStart = true;

        calanderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                TimePickerDialog.OnTimeSetListener mTimeSetListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Toast.makeText(CalanderView.this, "hourOfDay : " + hourOfDay + " / minute : " + minute, Toast.LENGTH_SHORT).show();
                            }
                        };
                Calendar _cal = Calendar.getInstance();
                int _hour = _cal.get(Calendar.HOUR_OF_DAY);
                int _minute = _cal.get(Calendar.MINUTE);
                int _second = _cal.get(Calendar.SECOND);
                TimePickerDialog _alert = new TimePickerDialog(CalanderView.this, mTimeSetListener, _hour, _minute, false);
                _alert.show();

                if(isStart) {
                    startDateTime = new DateTime(year, month+1, dayOfMonth, _hour, _minute, _second);
                    String data = year + "년 " + (month+1) + "월 " + dayOfMonth + "일 " +_hour + "시 "+_minute+"분 "+_second+"초";
                    date.setText("시작 시간 : "+ data);
                    isStart = false;
                } else {
                    endDateTime = new DateTime(year, month+1, dayOfMonth, _hour, _minute, _second);
                    String data = year + "년 " + (month+1) + "월 " + dayOfMonth + "일 " +_hour + "시 "+_minute+"분 "+_second+"초";
                    date2.setText("종료 시간 : "+ data);
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
                        db1.InputSql("INSERT INTO scheduleTime VALUES('"+id+"','"+startDateTime.builder()+"','"+endDateTime.builder()+"')");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CalanderView.this);
                        dialog = builder.setMessage("종료날짜를 선택해주세요")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                    }
                }
            }
        });
    }
}
