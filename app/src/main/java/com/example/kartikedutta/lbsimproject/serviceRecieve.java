package com.example.kartikedutta.lbsimproject;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class serviceRecieve extends IntentService {

    public serviceRecieve() {
        super("serviceRecieve");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(commonData.getInstance().getConnection().getInputStream()));
            String str;
            while ((str = br.readLine()) != null) {
                Log.e("New Message: ", str);

                try {
                    Intent i = null;
                    JSONArray arr = new JSONArray(str);
                    switch (arr.getString(0)) {
                        case "sendedOtp":
                        case "someProblemOccuredWhileSendingOtp":
                            i = new Intent("mail");
                            break;

                        case "correctOtp":
                        case "wrongOtp":
                            i = new Intent("otp");
                            break;
                        case "accountCreated":
                        case "someProblemOccuredWhileCreatingAccount":
                            i = new Intent("account");
                            break;
                        case "tagList":
                        case "addedTags":
                        case "someProblemOccuredWhileAddingTags":
                            i = new Intent("tags");
                            break;
                        case "jobsList":
                        case "noJobsFound":
                        case "someProblemOccuredWhileGettingJobs":
                            i = new Intent("jobs");
                            break;
                        case "addedJob":
                        case "someProblemOccuredWhileAddingJob":
                            i = new Intent("addJob");
                            break;
                        case "starredJobsList":
                        case "noStarredJobsFound":
                        case "someProblemOccuredWhileSearchingStarredJobs":
                            i = new Intent("starList");
                            break;
                        case "starredJob":
                        case "someProblemOccuredWhileStarredJob":
                        case "unStarredJob":
                        case "someProblemOccuredWhileUnStarredJobs":
                            i = new Intent("star");
                            break;
                        case "deletedJob":
                        case "someProblemOccuredWhileDeletingJob":
                            i = new Intent("deleteJob");
                            break;
                        case "deletedAccount":
                        case "someProblemOccuredWhileDeletingAccount":
                            i = new Intent("deleteAccount");
                            break;
                        case "searchJobsList":
                        case "noSearchJobsFound":
                        case "someProblemOccuredWhileSearchingJobs":
                            i = new Intent("search");
                            break;
                        case "addedJobsList":
                        case "noAddedJobsFound":
                        case "someProblemOccuredWhileGettingAddedJobs":
                            i = new Intent("addedJobs");
                            break;
                        case "updatedInfo":
                        case "someProblemOccuredWhileUpdatingInfo":
                            i = new Intent("deleteJob");
                            break;
                    }
                    if (i != null) {
                        i.putExtra("msg", str);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(commonData.getInstance().checkConnection())
            startService(new Intent(getApplicationContext(), serviceRecieve.class));
        else Toast.makeText(this, "Not Recieving Messages Anymore", Toast.LENGTH_SHORT).show();
    }
}
