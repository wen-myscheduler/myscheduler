package com.example.myscheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MakeTeam extends AppCompatActivity {
    private App d;
    private ListView teamlist;
    private Button btn1;
    private ListViewAdapter adapter;
    private EditText teamname;
    private Dialog dialog;
    private ArrayList<String> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_team);
        adapter = new ListViewAdapter();
        teamlist = (ListView)findViewById(R.id.teamlist);
        btn1 = (Button)findViewById(R.id.log);
        d = (App)getApplication();
        teamlist.setAdapter(adapter);
        teamname = (EditText)findViewById(R.id.teamnameinput);
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("id");
        view_data(user_id);


        //parent : ListView 자체에 대한 참조.
        //view : 클릭이 발생한 View에 대한 참조.
        //position : Adapter에서의 view의 position.
        //id : 클릭된 아이템의 row id.
        teamlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                String Str = item.getText() ;
                Drawable iconDrawable = item.getIcon() ;

                // TODO : use item data.
            }
        }) ;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = true;
                String naming =  teamname.getText().toString();
                ArrayList<String> select = new ArrayList<String>();
                SparseBooleanArray checkedItems = teamlist.getCheckedItemPositions();
                int count = adapter.getCount() ;

                //System.out.println("개수 : " + count);
                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        //System.out.println("번호 : " + i);
                        //System.out.println("CheckedItems" + checkedItems.toString());
                        ListViewItem ad1;
                        ad1 = (ListViewItem)adapter.getItem(i);
                       // System.out.println("item 가져오기 " + ad1.getText());
                        select.add(ad1.getText());
                    }
                }
                // ------------------------------------------------------------------------------------------------------------
                ArrayList<String> as1 = d.search("NAME", "TEAM", "NAME = \"" + naming + "\"");
                for (String s : as1) {
                        if (s.compareTo(naming) == 0) {
                            success = false;
                        }
                }
                if (naming.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MakeTeam.this);
                    dialog = builder.setMessage("모임이름을 입력해주세요!")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                } else if (success == true) {
                    d.InputSql("CREATE TABLE IF NOT EXISTS " + naming + "Team (id varchar(15), leader INT);");
                    d.InputSql("INSERT INTO TEAM VALUES('" + naming + "')");
                    d.InputSql("INSERT INTO " + naming + "Team VALUES('"+ user_id+"', 1)");
                    for(String s : select)
                    {
                        String ad = s;
                        d.InputSql("INSERT INTO " + naming + "Team VALUES('"+ ad +"', 0)");
                       // System.out.println("checked : " + s);
                    }

                    Toast.makeText(getApplicationContext(), naming + " 팀을 만드셨습니다..", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MakeTeam.this, MainActivity.class);

                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MakeTeam.this);
                    dialog = builder.setMessage("이미 있는 모임입니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                }
            }
        });
    }

    void view_data(String id) {
        ArrayList<String> arr = new ArrayList<String>();
        arr = d.view_data("select * from " + id + "friend");
        for (String s : arr) {
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.default_image2), s);
        }

    }
}
