package com.example.ki3.testapp1.model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BuyAsyncTask extends AsyncTask<String, Void, String> {

    String showItemPHP ="http://" + IpAddress.MY_IP_ADDRESS + "/showItemList.php";
    String buyticketPHP ="http://" + IpAddress.MY_IP_ADDRESS + "/buyticket.php";
    String showticketPHP ="http://" + IpAddress.MY_IP_ADDRESS + "/showticket.php";
    String useticketPHP ="http://" + IpAddress.MY_IP_ADDRESS + "/useticket.php";



    String TAG = "LoginTest";

    @Override
    protected String doInBackground(String... strings) {

        String ip = "";
        String postParameters ="";

        if(strings[0].equals(OpStrings.getSHOWITEMLIST())){
            Log.d("Buy Activity", "show item list!");

            ip = showItemPHP;
            postParameters = "u_id=" + strings[1];

        }
        else if(strings[0].equals(OpStrings.getBUYTICKET())){
            Log.d("BUY Dialog", "buy ticket!");

            ip = buyticketPHP;
            postParameters = "itemnum="+strings[1]+"&id="+strings[2]+"&major="+strings[3]+"&minor="+strings[4];

        }else if(strings[0].equals(OpStrings.getSHOWTIKCET())){
            Log.d("Show Tikcet", "show ticket!");

            ip = showticketPHP;
            postParameters = "id=" + strings[1];

        }else if(strings[0].equals(OpStrings.getUSETICKET())){
            Log.d("Use Ticket", "use ticket!");

            ip = useticketPHP;
            postParameters = "itemnum="+strings[1]+"&id="+strings[2];

        }

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
            Log.d("Buy Activity", data);

            return data;

        }catch (MalformedURLException e){
            e.printStackTrace();
            return "error:MalformedURLExepction";
        }catch (IOException e){
            e.printStackTrace();
            return "error:IOException";
        }


    }
}
