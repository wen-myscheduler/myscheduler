package com.example.myscheduler;

import android.app.TimePickerDialog;
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
    TextView date;
    TextView date2;
    TextView date3;
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_view);
        calanderView = (CalendarView) findViewById(R.id.calendarView);
        date = (TextView) findViewById(R.id.textView3);
//        date2 = (TextView) findViewById(R.id.textView1);
//        date3 = (TextView) findViewById(R.id.textView4);
        btn2 = (Button)findViewById(R.id.button2);


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
                TimePickerDialog _alert = new TimePickerDialog(CalanderView.this, mTimeSetListener, _hour, _minute, false);
                _alert.show();
                String data = year + "년 " + (month+1) + "월 " + dayOfMonth + "일 " +_hour + "시 "+_minute+"분 ";
                date.setText("시작 시간 : "+ data);
            }

        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
