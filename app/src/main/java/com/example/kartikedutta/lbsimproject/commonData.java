package com.example.kartikedutta.lbsimproject;

import android.content.Context;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class commonData {

    private static Socket socket = null;

    private static commonData instance = null;

    public synchronized static commonData getInstance() {
        if(instance==null){
            instance = new commonData();
        }
        return instance;
    }

    void setConnection(Socket socket){
        commonData.socket = socket;
    }

    Socket getConnection() {
        return commonData.socket;
    }

    //public void sendData(Context context, String msg){}

    boolean checkConnection(){
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    String getDateTime(Context context, Long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
        SimpleDateFormat syear = new SimpleDateFormat("yyyy", Locale.getDefault());

        if (Integer.parseInt(syear.format(System.currentTimeMillis())) > Integer.parseInt(syear.format(new Date(timestamp * 1000)))) {
            return sdf.format(new Date(timestamp * 1000));
        }

        SimpleDateFormat sday = new SimpleDateFormat("dd", Locale.getDefault());

        int difference = Integer.parseInt(sday.format(System.currentTimeMillis())) - Integer.parseInt(sday.format(new Date(timestamp * 1000)));
        switch(difference){
            case 1:return "Yesterday";

            case 0: return "Today";

            default:
                SimpleDateFormat sdfmonth = new SimpleDateFormat("dd MMM", Locale.getDefault());
                return sdfmonth.format(new Date(timestamp * 1000));

        }
    }
}
