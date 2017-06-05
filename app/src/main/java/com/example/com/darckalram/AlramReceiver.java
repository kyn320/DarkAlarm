package com.example.com.darckalram;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class AlramReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        boolean[] week = intent.getBooleanArrayExtra("weekday");

        Calendar calendar = Calendar.getInstance();

        Log.i("test","alram is work");

        if(!week[calendar.get(Calendar.DAY_OF_WEEK)]){
            return;
        }


        Log.i("test","alram is success");

        Toast.makeText(context,"ASD",Toast.LENGTH_LONG).show();

        Intent goIntent = new Intent(context, AlramWork.class);

        PendingIntent pi = PendingIntent.getActivity(context,0,goIntent,PendingIntent.FLAG_ONE_SHOT);

        try{
            Log.i("test","asdqwe");
            pi.send();
        }
        catch(PendingIntent.CanceledException e){
            Log.i("test","ya gi boun jo ta");
        }

    }
}
