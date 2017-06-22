package com.example.com.darckalram;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Random;

//알람을 추가하는 클래스 입니다.
public class AddAlarm extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText nameSet;
    private ToggleButton toggleSun, toggleMon, toggleTue, toggleWed, toggleThu, toggleFri, toggleSat;
    private Button cancle, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        timePicker = (TimePicker)findViewById(R.id.add_timeSet);

        nameSet = (EditText)findViewById(R.id.add_nameSet);

        toggleSun = (ToggleButton) findViewById(R.id.add_Sun);
        toggleMon = (ToggleButton) findViewById(R.id.add_Mon);
        toggleTue = (ToggleButton) findViewById(R.id.add_Tue);
        toggleWed = (ToggleButton) findViewById(R.id.add_Wed);
        toggleThu = (ToggleButton) findViewById(R.id.add_Thu);
        toggleFri = (ToggleButton) findViewById(R.id.add_Fri);
        toggleSat = (ToggleButton) findViewById(R.id.add_Sat);

        cancle = (Button)findViewById(R.id.add_Cancle);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save = (Button)findViewById(R.id.add_Save);

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                onRegist(v);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void onRegist(View v){
        boolean[] week = {false, toggleSun.isChecked(), toggleMon.isChecked(), toggleTue.isChecked(), toggleWed.isChecked(), toggleThu.isChecked(), toggleFri.isChecked(), toggleSat.isChecked()};
        //알람 intent를 지정합니다.
        Intent intent = new Intent(this, AlramReceiver.class);
        intent.putExtra("weekday",week);
        intent.putExtra("msg",nameSet.getText().toString());

        Random random = new Random();

        int id = random.nextInt(10000) +1;


        intent.putExtra("id",id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),timePicker.getHour(),timePicker.getMinute(),calendar.get(Calendar.SECOND));

        long oneDay = 24 * 60 * 60 * 1000;

        //알람 세팅
        AlarmManager alarmManager = MainActivity.alarmManager;
        alarmManager.setRepeating(alarmManager.RTC_WAKEUP, calendar.getTimeInMillis() ,oneDay,pendingIntent);

        String am_pm = "";

        int hour = timePicker.getHour();

        if(timePicker.getHour() < 12){
            am_pm = "AM";
        }
        else {
            am_pm = "PM";
            hour -= 12;
            if(hour == 0)
                ++hour;
        }

        String days = "";

        for(int i = 1; i < week.length; i++){

            if(week[i]){
                switch (i)
                {
                    case 1:
                        days += "일 ";
                        break;
                    case 2:
                        days += "월 ";
                        break;
                    case 3:
                        days += "화 ";
                        break;
                    case 4:
                        days += "수 ";
                        break;
                    case 5:
                        days += "목 ";
                        break;
                    case 6:
                        days += "금 ";
                        break;
                    case 7 :
                        days += "토 ";
                        break;
                }
            }
        }

        Log.i("test","alram is work");
        //리스트에 등록
        MainActivity.adapter.addItem(id,am_pm,nameSet.getText().toString(),timePicker.getHour(),timePicker.getMinute(),days,true,week);
        //서비스 시작
        startService(intent);

        finish();
    }
}
