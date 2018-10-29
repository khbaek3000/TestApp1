package com.example.ki3.testapp1;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ki3.testapp1.model.LoginPreferences;

public class MainActivity extends AppCompatActivity {
// 메인 액티비티
    TextView tvMainTestMsg, tvMainLoginOk, tvMainUUID;
    Button btnMainSearchItem, btnMainBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LoginPreferences pref = new LoginPreferences(this);
        final String get_email = pref.getValue("email", "");

        Button loginButton = (Button) findViewById(R.id.loginButton);

        //tvMainTestMsg = (TextView)findViewById(R.id.TxV_testmessage);
        tvMainLoginOk = (TextView)findViewById(R.id.TxV_main_loginTest);
        btnMainSearchItem = (Button)findViewById(R.id.Btn_main_searchItem);
        btnMainBuy = (Button)findViewById(R.id.Btn_main_buyticket);



       if(get_email.equals("")){



           loginButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                   startActivity(loginIntent);
                   finish();

               }
           });
           //tvMainLoginOk.setText("비로그인");

       }else{

           loginButton.setVisibility(View.GONE);

           tvMainLoginOk.setText("회원 : "+get_email);
           btnMainSearchItem.setVisibility(View.VISIBLE);
           btnMainSearchItem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent searchIntent = new Intent(getApplicationContext(), SearchItemActivity.class);
                   startActivity(searchIntent);

               }
           });

           btnMainBuy.setVisibility(View.VISIBLE);
           btnMainBuy.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent buyIntent = new Intent(getApplicationContext(), BuyActivity.class);
                   startActivity(buyIntent);

               }
           });



       }




    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        final LoginPreferences pref = new LoginPreferences(this);
        pref.put("email","");


    }


}