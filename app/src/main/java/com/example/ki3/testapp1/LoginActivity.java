package com.example.ki3.testapp1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ki3.testapp1.model.LoginPreferences;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
// 로그인 액티비티
    private static String IP_ADDRESS = "아이피입력";

    String uId=null, uPasswd=null;
    String mLoginResult = null;
    String TAG = "LoginTest";

    EditText etLoginID;
    EditText etLoginPW;

    Button btnLoginConfirm;
    Button btnLoginCancle;
    Button btnLoginJoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LoginPreferences pref = new LoginPreferences(this);

        etLoginID = (EditText) findViewById(R.id.ET_Login_ID);
        etLoginPW = (EditText) findViewById(R.id.ET_Login_PW);
        btnLoginConfirm = (Button)findViewById(R.id.Btn_Login_Confirm);
        btnLoginCancle = (Button)findViewById(R.id.Btn_Login_Cancle);
        btnLoginJoin = (Button)findViewById(R.id.Btn_Login_Join);

        //case click 'login button'
        btnLoginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uId = etLoginID.getText().toString();
                uPasswd = etLoginPW.getText().toString();
                if((uId.equals(""))||(uPasswd.equals(""))){

                    Toast.makeText(getApplicationContext(),"아이디와 패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();

                }else{

                    LoginAsynctask loginAsynctask = new LoginAsynctask();

                    try{
                        mLoginResult= loginAsynctask.execute("http://" + IP_ADDRESS + "/login.php", uId, uPasswd).get();
                        if(mLoginResult.equals("loginok")){
                            pref.put("email", uId);
                            pref.put("passwd", uPasswd);
                            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                        else if(mLoginResult.equals("fail")){
                            Toast.makeText(getApplicationContext(), "로그인 실패. 아이디와 패스워드를 확인하세요", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "서버의 응답이 없습니다.", Toast.LENGTH_LONG).show();
                        }

                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }catch(ExecutionException e){
                        e.printStackTrace();
                    }
                }


            }
        });

        //case click 'cancle button'
        btnLoginCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //case click 'join button'
        btnLoginJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent joinIntent = new Intent(getApplicationContext(),JoinActivity.class);
                startActivity(joinIntent);
            }
        });

    }

    private class LoginAsynctask extends AsyncTask<String, Void, String>{


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String check = "1";
            String logOk = "loginok";

            StringBuilder sbCheck = new StringBuilder();
            sbCheck.append("1");



        }

        @Override
        protected String doInBackground(String... strings) {

            String ip;
            String userID;
            String userPW;

            ip = (String)strings[0];
            userID = (String)strings[1];
            userPW = (String)strings[2];

            String postParameters = "u_id="+userID+"&u_pw="+ userPW;

            try{
                URL url = new URL(ip);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();
                OutputStream outs = conn.getOutputStream();
                outs.write(postParameters.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                int resposneStatusCode = conn.getResponseCode();
                Log.d(TAG, "POST response code:" + resposneStatusCode);

                InputStream inputStream = null;
                BufferedReader bufferedReader = null;

                if(resposneStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = conn.getInputStream();
                }
                else if(resposneStatusCode == HttpURLConnection.HTTP_GATEWAY_TIMEOUT){
                    return "timeout";
                }
                else{
                    inputStream = conn.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine())!=null){
                    sb.append(line);
                }

                bufferedReader.close();

                String data = sb.toString();


                return data;

            }catch(MalformedURLException e){
                e.printStackTrace();
                return "error:MalformedURLExepction";
            }catch(IOException e){
                e.printStackTrace();
                return "error:IOException";
            }

        }
    }

}
