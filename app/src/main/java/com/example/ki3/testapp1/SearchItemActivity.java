package com.example.ki3.testapp1;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ki3.testapp1.model.BuyAsyncTask;
import com.example.ki3.testapp1.model.DialogListener;
import com.example.ki3.testapp1.model.LoginPreferences;
import com.example.ki3.testapp1.model.OpStrings;
import com.example.ki3.testapp1.model.RecyclerAdapterTicket;
import com.example.ki3.testapp1.model.TicketData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class SearchItemActivity extends AppCompatActivity {
// 사용자가 가진 티켓을 보여주고 사용할 수 있는 액티비티. 항목을 터치하면 현재 장소에서 티켓을 사용가능한지 확인할 수 있다.

    TextView tvSearchShow;
    TextView tvNoItem;

    private BluetoothManager mBlutoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler;
    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000;

    HashSet<String> set;
    private ArrayList<TicketData> mArrayList;

    RecyclerAdapterTicket adapter;
    RecyclerView mRecyclerView;

    String mResult,uResult;

    String uuid;
    int major, minor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        Log.d("search", "search activity launch");

        //블루투스관련 권한 동적요청
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);


        //tvSearchShow = (TextView)findViewById(R.id.TxV_search_showitem);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.startScan(mScanCallback);

        final LoginPreferences pref = new LoginPreferences(this);
        final String get_email = pref.getValue("email", "");

        set = new HashSet<>();

        //mBluetoothLeScanner.startScan(mScanCallback);

        mArrayList = new ArrayList<>();

        BuyAsyncTask ticketAsyncTask = new BuyAsyncTask();

        try{
            mResult = ticketAsyncTask.execute(OpStrings.getSHOWTIKCET(), get_email).get();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if(mResult.equals("nodata")) {
            tvNoItem = (TextView) findViewById(R.id.TxV_show_noitem);
            tvNoItem.setText("구입한 이용권이 없습니다.");
            tvNoItem.setVisibility(View.VISIBLE);
        }else{
            try{
                JSONObject jsonObject = new JSONObject(mResult);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject ticket = jsonArray.getJSONObject(i);

                    String ticketName = ticket.getString("itemname");
                    String ticketNum = ticket.getString("itemnum");
                    String ticketImage = ticket.getString("itemimage");
                    String id = ticket.getString("id");
                    String ticketmajor = ticket.getString("major");
                    String ticketminor = ticket.getString("minor");
                    String date = ticket.getString("date");
                    String ticketuse = ticket.getString("ticketuse");
                    int resID = getResources().getIdentifier(ticketImage, "drawable", this.getPackageName());

                    TicketData ticketData = new TicketData();

                    ticketData.setTicketname(ticketName);
                    ticketData.setTicketnum(ticketNum);
                    ticketData.setTicketimage(ticketImage);
                    ticketData.setId(id);
                    ticketData.setMajor(ticketmajor);
                    ticketData.setMinor(ticketminor);
                    ticketData.setDate(date);
                    ticketData.setTicketRes(resID);
                    ticketData.setTicketuse(ticketuse);

                    mArrayList.add(ticketData);
                }

            }catch (JSONException e){
                Log.d("ShowticketActivity", "showResult(err) : ", e);
            }
            //tvSearchShow.setText("");

            mRecyclerView = (RecyclerView) findViewById(R.id.recylerview_showitem);
            adapter = new RecyclerAdapterTicket(this, mArrayList);
            mRecyclerView.setAdapter(adapter);
            adapter.setTicketClick(new RecyclerAdapterTicket.TicketClick() {
                @Override
                public void onClick(View view, final int position) {
                    //Toast.makeText(getApplicationContext(), position+" "+mArrayList.get(position).getMajor() + " "+view, Toast.LENGTH_SHORT).show();
                    String mjmn = ""+mArrayList.get(position).getMajor()+mArrayList.get(position).getMinor();
                    String equal = "no";
                    // 현재 읽은 비콘의 신호와 사용자 티켓의 major minor가 일치 하는지에 대한 여부 파악
                    Iterator<String> iterator = set.iterator();
                    while(iterator.hasNext()){
                        String str = iterator.next();
                        if(mjmn.equals(str)){
                            equal = "yes";
                            break;
                        }
                    }
                    if(equal.equals("yes")){
                        //Toast.makeText(getApplicationContext(), "major와 minor가 일치합니다.", Toast.LENGTH_SHORT).show();
                        TicketDialog dialog = new TicketDialog(SearchItemActivity.this, mArrayList.get(position).getTicketnum(), mArrayList.get(position).getId());
                        dialog.setDialogListener(new DialogListener() {
                            @Override
                            public void onPositiveClicked() {

                                if(mArrayList.get(position).getTicketuse().equals("yes")){
                                    Toast.makeText(getApplicationContext(), "이미 사용한 입장권입니다.", Toast.LENGTH_SHORT).show();
                                }else {
                                    BuyAsyncTask useAsyncTask = new BuyAsyncTask();
                                    try {
                                        uResult=useAsyncTask.execute(OpStrings.getUSETICKET(), mArrayList.get(position).getTicketnum(), mArrayList.get(position).getId()).get();
                                        Toast.makeText(getApplicationContext(), ""+mArrayList.get(position).getTicketname()+"입장권을 사용하였습니다.", Toast.LENGTH_LONG).show();


                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }catch (ExecutionException e){
                                        e.printStackTrace();
                                    }
                                }


                            }

                            @Override
                            public void onNegativeClicked() {

                            }
                        });
                        dialog.show();

                        equal = "no";
                    }
                }
            });
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    Log.e("hashset clear", "hash set 초기화");
                    set.clear();
                }
            };
            Timer timer = new Timer();
            timer.schedule(tt, 60000);



        }




    }
    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            try{
                ScanRecord scanRecord = result.getScanRecord();
                Log.d("TxPower", scanRecord.getTxPowerLevel()+"");
                Log.d("ScanRecord", scanRecord.toString());
                Log.d("scanByte", scanRecord.getBytes()+"");

                uuid = String.format("%02x%02x%02x%02x-%02x%02x-%02x%02x-%02x%02x-%02x%02x%02x%02x%02x%02x",
                        scanRecord.getBytes()[10], scanRecord.getBytes()[11], scanRecord.getBytes()[12],
                        scanRecord.getBytes()[13], scanRecord.getBytes()[14], scanRecord.getBytes()[15],
                        scanRecord.getBytes()[16], scanRecord.getBytes()[17], scanRecord.getBytes()[18],
                        scanRecord.getBytes()[19], scanRecord.getBytes()[20], scanRecord.getBytes()[21],
                        scanRecord.getBytes()[22], scanRecord.getBytes()[23], scanRecord.getBytes()[24],
                        scanRecord.getBytes()[25]);

                String sMajor, sMinor;
                sMajor = String.format("%02x%02x", scanRecord.getBytes()[26], scanRecord.getBytes()[27]);

                major = ((scanRecord.getBytes()[26])<<8) & 0x0000ff00 | (scanRecord.getBytes()[27]) & 0x000000ff;
                minor = ((scanRecord.getBytes()[28])<<8) & 0x0000ff00 | (scanRecord.getBytes()[29]) & 0x000000ff;

                set.add(""+major+""+minor);

                Log.d("UUID", uuid);
                Log.d("major-minor", "major="+ major + " & minor=" + minor +"");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };


}
