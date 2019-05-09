package com.example.myscheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class registor extends AppCompatActivity {
    EditText edit1;
    EditText edit2;
    EditText edit3;
    Button btn1;
    App a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);

        btn1 = (Button)findViewById(R.id.btn1);
        edit1 = (EditText)findViewById(R.id.editText1);
        edit2 = (EditText)findViewById(R.id.editText2);
        edit3 = (EditText)findViewById(R.id.editText3);
        a = (App)getApplication();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edit1.getText().toString();
                String pw = edit2.getText().toString();
                String nk = edit3.getText().toString();

                a.InputSql("INSERT INTO User VALUES('"+id+"','"+pw+"', '"+nk+"')");

                Toast.makeText(getApplicationContext(),id+" 님 회원가입이 완료 되었습니다.",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(registor.this, LoginActivity.class);

                startActivity(intent);
            }
        });
    }
}
