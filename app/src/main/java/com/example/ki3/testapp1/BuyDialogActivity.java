package com.example.ki3.testapp1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ki3.testapp1.model.BuyAsyncTask;
import com.example.ki3.testapp1.model.LoginPreferences;
import com.example.ki3.testapp1.model.OpStrings;

import java.util.concurrent.ExecutionException;

public class BuyDialogActivity extends AppCompatActivity {
// 티켓을 구입하는 액티비티
    TextView tvDialogName, tvDialogDetail;
    ImageView imgDialog;
    Button btnOk, btnCancle;

    String mResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogbuy);

        String itemName = getIntent().getStringExtra("item-name");
        String itemDetail = getIntent().getStringExtra("item-detail");
        String itemImg = getIntent().getStringExtra("item-img");
        final String itemNum = getIntent().getStringExtra("item-num");
        final String itemMajor = getIntent().getStringExtra("item-major");
        final String itemMinor = getIntent().getStringExtra("item-minor");

        int resID = getResources().getIdentifier(itemImg,"drawable", this.getPackageName());

        tvDialogName = (TextView) findViewById(R.id.TxV_dialog_name);
        tvDialogDetail = (TextView) findViewById(R.id.TxV_dialog_detail);
        imgDialog = (ImageView) findViewById(R.id.Img_dialog_img);
        btnOk = (Button) findViewById(R.id.Btn_dialog_yes);
        btnCancle = (Button) findViewById(R.id.Btn_dialog_cancel);

        tvDialogName.setText(itemName);
        tvDialogDetail.setText(itemDetail);
        //imgDialog.setImageResource(R.drawable.ulsanpark);
        imgDialog.setImageResource(resID);

        final LoginPreferences pref = new LoginPreferences(this);
        final String get_email = pref.getValue("email", "");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyAsyncTask buyAsyncTask = new BuyAsyncTask();
                try{
                    mResult = buyAsyncTask.execute(OpStrings.getBUYTICKET(), itemNum, get_email, itemMajor, itemMinor).get();
                    Toast.makeText(getApplicationContext(), mResult, Toast.LENGTH_LONG).show();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }catch (ExecutionException e){
                    e.printStackTrace();
                }
            }
        });





    }
}
