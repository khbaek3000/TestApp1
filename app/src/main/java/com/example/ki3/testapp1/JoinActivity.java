package com.example.ki3.testapp1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class JoinActivity extends AppCompatActivity {
//회원가입 액티비티
    private static String IP_ADDRESS = "아이피입력";
    private static String TAG = "phpJoinTest";

    String mResult=null;

    EditText etJoinID;
    EditText etJoinPW;
    EditText etJoinPW_Check;

    Button btnJoinConfirm;
    Button btnJoinCancle;

    TextView txtJoinResult;
    TextView txtJoinTestMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        etJoinID = (EditText)findViewById(R.id.ET_Join_ID);
        etJoinPW = (EditText)findViewById(R.id.ET_Join_PW);
        etJoinPW_Check = (EditText)findViewById(R.id.ET_Join_PW_Chk);
        btnJoinConfirm = (Button)findViewById(R.id.Btn_Join_Confirm);
        btnJoinCancle = (Button)findViewById(R.id.Btn_Join_Cancle);
        //txtJoinResult = (TextView)findViewById(R.id.Txt_Join_Result);
        //txtJoinTestMsg = (TextView)findViewById(R.id.TxV_Join_testmsg);


        btnJoinConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uId = etJoinID.getText().toString();
                String uPw = etJoinPW.getText().toString();
                String uPwCh = etJoinPW_Check.getText().toString();

                if((!uId.equals(""))&&(!uPw.equals(""))&&uPw.equals(uPwCh)){

                    JoinAsynctask joinTask = new JoinAsynctask();
                    //joinTask.execute("http://" + IP_ADDRESS + "/join.php", uId, uPw);
                    try{
                        mResult =joinTask.execute("http://" + IP_ADDRESS + "/join.php", uId, uPw).get();


                    }catch (ExecutionException e){
                        e.printStackTrace();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    //txtJoinResult.setText(mResult);
                    //txtJoinTestMsg.setText(mResult);
                    finish();

                }
                else{
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    private class JoinAsynctask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //txtJoinResult.setText(result); // Join 결과 메시지
            //mResult = "메세지 테스트";
            //txtJoinTestMsg.setText(mResult);
            Log.d(TAG, "POST response - " + result);
        }

        @Override
        protected String doInBackground(String... strings) {

            String ip = (String)strings[0];
            String id = (String)strings[1];
            String pw = (String)strings[2];

            String postParameters = "u_id="+id+"&u_pw="+pw;

            try{

                URL url = new URL(ip);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //conn.setRequestProperty("Conetent-Type", "applicaton/x-www-form-urlencoded");
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

                return sb.toString();


            }catch(MalformedURLException e){
                e.printStackTrace();
                return new String("Error: " + e.getMessage());
            }catch (IOException e){
                e.printStackTrace();
                return new String("Error: " + e.getMessage());
            }


        }
    }
}
