package com.example.ki3.testapp1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ki3.testapp1.model.BuyAsyncTask;
import com.example.ki3.testapp1.model.ItemsData;
import com.example.ki3.testapp1.model.OpStrings;
import com.example.ki3.testapp1.model.LoginPreferences;
import com.example.ki3.testapp1.model.RecyclerAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class BuyActivity extends AppCompatActivity {
//구입할 수 있는 티켓 목록 보여주는 액티비티
    private ArrayList<ItemsData> mArrayList;
    RecyclerAdapter adapter;

    //TextView tvShowitem;
    RecyclerView mRecyclerView;
    String mResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        final LoginPreferences pref = new LoginPreferences(this);
        final String get_email = pref.getValue("email", "");

        mArrayList = new ArrayList<>();

        //tvShowitem =(TextView)findViewById(R.id.TxV_buy_showitem);

        BuyAsyncTask buyAsyncTask = new BuyAsyncTask();
        try{
            mResult = buyAsyncTask.execute(OpStrings.getSHOWITEMLIST(), get_email).get();

        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }
        try{
            JSONObject jsonObject = new JSONObject(mResult);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject item = jsonArray.getJSONObject(i);
                String num = item.getString("itemnum");
                String name = item.getString("itemname");
                String detail = item.getString("itemdetail");
                String major = item.getString("itemmajor");
                String minor = item.getString("itemminor");
                String category = item.getString("itemcategory");
                String area = item.getString("itemarea");
                String img = item.getString("itemimage");

                ItemsData itemsData = new ItemsData();

                itemsData.setItemnum(num);
                itemsData.setItemname(name);
                itemsData.setItemdetail(detail);
                itemsData.setItemmajor(major);
                itemsData.setItemminor(minor);
                itemsData.setItemcategory(category);
                itemsData.setItemarea(area);
                itemsData.setItemimg(img);

                mArrayList.add(itemsData);
            }


        }catch (JSONException e){
            Log.d("BuyActivity", "showResult(err) : ", e);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.recylerview_buy);
        adapter = new RecyclerAdapter(this, mArrayList);
        mRecyclerView.setAdapter(adapter);
        adapter.setItemClick(new RecyclerAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(getApplicationContext(), position+" "+mArrayList.get(position).getItemname() + " "+view, Toast.LENGTH_SHORT).show();

                Intent dialogIntent = new Intent(getApplicationContext(), BuyDialogActivity.class);
                dialogIntent.putExtra("item-name", mArrayList.get(position).getItemname());
                dialogIntent.putExtra("item-detail", mArrayList.get(position).getItemdetail());
                dialogIntent.putExtra("item-img", mArrayList.get(position).getItemimg());
                dialogIntent.putExtra("item-num", mArrayList.get(position).getItemnum());
                dialogIntent.putExtra("item-major", mArrayList.get(position).getItemmajor());
                dialogIntent.putExtra("item-minor", mArrayList.get(position).getItemminor());

                startActivity(dialogIntent);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        /*
        for(int s=0; s<mArrayList.size(); s++){

            tvShowitem.setText(""+mArrayList.get(s).getItemnum()+""+mArrayList.get(s).getItemname()+""
                    +mArrayList.get(s).getItemdetail() +""+mArrayList.get(s).getItemmajor()+""+mArrayList.get(s).getItemminor()
                    +""+mArrayList.get(s).getItemcategory()+""+mArrayList.get(s).getItemarea() + " " + mArrayList.get(s).getItemimg());
        }
        */


    }
}
