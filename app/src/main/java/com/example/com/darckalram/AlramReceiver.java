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

//알람을 받는 리시버 입니다.
public class AlramReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //요일 세팅
        boolean[] week = intent.getBooleanArrayExtra("weekday");
        //아이디
        int id = intent.getIntExtra("id",0);
        String msg = intent.getStringExtra("msg");

        Calendar calendar = Calendar.getInstance();

        Log.i("test","alram is work");
        //요일이 맞지 않는 경우
        if(!week[calendar.get(Calendar.DAY_OF_WEEK)]){
            return;
        }


        Log.i("test","alram is success");

        Intent goIntent = new Intent(context, AlramWork.class);
        goIntent.putExtra("msg",msg);
        PendingIntent pi = PendingIntent.getActivity(context,id,goIntent,PendingIntent.FLAG_ONE_SHOT);

        try{
            Log.i("test","asdqwe");
            pi.send();
        }
        catch(PendingIntent.CanceledException e){
            Log.i("test","ya gi boun jo ta");
        }

    }
}
