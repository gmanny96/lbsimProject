package com.example.kartikedutta.lbsimproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class serviceConnect extends Service {

    String email;
    SharedPreferences preferences;
    Socket socket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Log.d("net","worked");
            new Thread(new serverConnection()).start();
            Log.d("this","worked");
            return START_STICKY;
        }

        return START_NOT_STICKY;
    }

    class serverConnection implements Runnable {
        @Override
        public void run() {
            try {
                socket = new Socket("172.16.1.245", 5000);

                commonData.getInstance().setConnection(socket);

                startService(new Intent(getApplicationContext(), serviceRecieve.class));

                preferences = getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE);

                if(!preferences.getString("accountStatus", "none").equals("none")){
                    PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                    JSONArray arr = new JSONArray();
                    arr.put("logIn");
                    arr.put(preferences.getString("mail", ""));
                    pw.println(arr.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}