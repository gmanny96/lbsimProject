package com.example.kartikedutta.lbsimproject;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class serviceSend extends IntentService {

    public serviceSend() {
        super("serviceSend");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            if(commonData.getInstance().checkConnection()) {
                PrintWriter pw = new PrintWriter(commonData.getInstance().getConnection().getOutputStream(), true);
                Log.e(intent.getStringExtra("msg"), "msg send");
                pw.println(intent.getStringExtra("msg"));
            }
            else Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
