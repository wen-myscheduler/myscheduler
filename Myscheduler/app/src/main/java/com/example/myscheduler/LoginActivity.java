package com.example.myscheduler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.OAuthErrorCode;
import com.kakao.auth.Session;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private SessionCallback sessionCallback;
    private String OAUTH_CLIENT_ID = "RrhZvY4G1e0AzNlbR78V";
    private static String OAUTH_CLIENT_SECRET = "HqXu8pUwpT";
    private OAuthLoginButton btOAuthLoginButton;
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";
    private OAuthLogin mOAuthLoginModule;
    Context mContext = LoginActivity.this;
    private App db1;
    private Button btn1,btn2;
    private EditText edit1,edit2;
    private boolean validate = false;
    private AlertDialog dialog;
    private String accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db1 = (App)getApplication();
        btn1 = (Button)findViewById(R.id.button);
        btn2 = (Button)findViewById(R.id.button2);
        edit1 = (EditText)findViewById(R.id.editText);
        edit2 = (EditText)findViewById(R.id.editText2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_userID = edit1.getText().toString();
                String input_password = edit2.getText().toString();
                ArrayList<String> idcheck = new ArrayList<String>();
                idcheck = db1.search("id", "User", "id = \"" + input_userID + "\"" + " and pw = \"" + input_password +"\"");
                ArrayList<String> k = db1.view_data("select * from User");
                System.out.println("size: " +idcheck.size());
                System.out.println("size1 : " + k.size());
                for(String t : k)
                {

                }
                for(String s : idcheck)
                {

                    if(s.compareTo(input_userID)==0)
                    {
                        validate  = true;
                    }
                }
                if(input_userID.equals("") || input_password.equals("") ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("ID나 비밀번호를 입력해주세요!")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                }
                else {
                    if (validate) {
                        Intent regIntent = new Intent(getApplication(), MainActivity.class);
                        regIntent.putExtra("id",input_userID);
                        startActivity(regIntent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("id나 비밀번호가 일치하지 않습니다")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                    }
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent =  new Intent(getApplication(), registor.class);
                startActivity(regIntent);
            }
        });


        Button btnLoginKakao = findViewById(R.id.kakaoLoginButton2);
        btnLoginKakao.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });




        //------------------------------------------------------------------kakao-------------------------------------------
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        //Session.getCurrentSession().checkAndImplicitOpen();


        //네이버 로그인 인스턴스 초기화
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                LoginActivity.this,
                OAUTH_CLIENT_ID,
                OAUTH_CLIENT_SECRET,
                OAUTH_CLIENT_NAME
        );
        btOAuthLoginButton = (OAuthLoginButton)findViewById(R.id.btOAuthLoginImg);
        btOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        //btOAuthLoginButton.setBgResourceId(R.drawable.loginnaver);




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("name", result.getNickname());
                    intent.putExtra("profile", result.getProfileImagePath());
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    //--------------------------------------------------------------------
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean b) {
            if (b) {
                accessToken = mOAuthLoginModule.getAccessToken(mContext);
                Toast.makeText(LoginActivity.this,"success",Toast.LENGTH_SHORT).show();
                new NaverProfileGet().execute();
            } else {
                Toast.makeText(LoginActivity.this,"s",Toast.LENGTH_SHORT).show();
            }
        }
    };



    public class NaverProfileGet extends AsyncTask<String, Void, String> {
        //네이버 프로필 조회 API에 보낼 헤더. 그대로 쓰면 된다.
        String header = "Bearer " + accessToken;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            //네이버 프로필 조회 API에서 프로필을 jSON 형식으로 받아오는 부분.
            //이 부분도 그대로 사용하면 된다.
            StringBuffer response = new StringBuffer();
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", header);
                int responseCode = conn.getResponseCode();
                BufferedReader br;
                if(responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                String inputLine;
                while((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response.toString();
        }

        //네이버 프로필 조회 API에서 받은 jSON에서 원하는 데이터를 뽑아내는 부분
        //여기서는 닉네임, 프로필사진 주소, 이메일을 얻어오지만, 다른 값도 얻어올 수 있다.
        //이 부분을 원하는 대로 수정하면 된다.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                JSONObject jsonObject1 = new JSONObject(result);
                JSONObject jsonObject2 = (JSONObject)jsonObject1.get("response");
                String image = jsonObject2.getString("profile_image");
                String name = jsonObject2.getString("name");
                String email = jsonObject2.getString("email");
                intent.putExtra("Nickname",name);
                intent.putExtra("Image",image);
                intent.putExtra("Email",email);
                startActivity(intent);
                finish();
            } catch (Exception e) {}
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}


