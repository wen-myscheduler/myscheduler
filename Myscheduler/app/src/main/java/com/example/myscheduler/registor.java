package com.example.myscheduler;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class registor extends AppCompatActivity {
    private EditText edit1,edit2,edit3;
    private Button btn1,btn2;
    private AlertDialog dialog;
    private App a;
    private boolean validate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.re_check);
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
                if(id.equals("") || pw.equals("") || nk.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(registor.this);
                    dialog = builder.setMessage("모든 칸을 입력해주세요!")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                }
                else {
                    if(validate) {
                        a.InputSql("INSERT INTO User VALUES('" + id + "','" + pw + "', '" + nk + "')");
                        a.InputSql("CREATE TABLE " +id+"friend (id varchar(15));");
                        Toast.makeText(getApplicationContext(), id + " 님 회원가입이 완료 되었습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(registor.this, LoginActivity.class);

                        startActivity(intent);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(registor.this);
                        dialog = builder.setMessage("ID중복을 확인해주세요!")
                                .setPositiveButton("확인",null)
                                .create();
                        dialog.show();
                    }
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edit1.getText().toString();
                //ArrayList<String> as = a.search("id","User","id = \"kim\" ");
                ArrayList<String> as = a.search("id", "User", "id = \"" + id + "\"");
                System.out.println("id string = " + id);

                if (validate) {
                    return;
                }
                if (id.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(registor.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                } else {

                    boolean success = true;

                    for (String s : as) {

                        if (s.compareTo(id) == 0) {
                            success = false;
                        }
                        //System.out.println("id = " + s + ", " + success);
                    }
                    //System.out.println("size : " + as.size());
                    if (success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(registor.this);
                        dialog = builder.setMessage("사용할 수 있는 ID입니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        validate = true;
                        // edit1.setEnabled(false);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(registor.this);
                        dialog = builder.setMessage("사용할 수 없는 ID입니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        validate = false;
                    }
                }
            }
        });
    }
}
