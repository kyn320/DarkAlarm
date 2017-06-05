package com.example.com.darckalram;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;

    private NotificationManager notificationManager;
    private FloatingActionButton fab;

    private ListView listView;
    public static ListViewAdapter adapter = new ListViewAdapter();

    static boolean isFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isFirst) {
            getPreferences();
            isFirst = true;
        }
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        listView = (ListView)findViewById(R.id.list);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddAlarm.class);
                startActivity(intent);
            }
        });

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                ListViewItem item = (ListViewItem)parent.getItemAtPosition(position);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        savePreferences();
        listView.invalidateViews();
    }

    // 값 불러오기
    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String list = pref.getString("list","");
        if(list != "") {
            try {
                JSONArray json = new JSONArray(list);

                for (int i = 0; i < json.length(); i++) {
                /*
                sun
                time
                name
                days
                 */
                    JSONObject obj = json.getJSONObject(i);
                    String sun = obj.getString("sun");
                    String time = obj.getString("time");
                    String name = obj.getString("name");
                    String days = obj.getString("days");
                    Boolean work = obj.getBoolean("work");
                    adapter.addItem(sun, name, time, days, work);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 값 저장하기
    private void savePreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        JSONArray jArray = new JSONArray();//배열이 필요할때
        try {

            for (int i = 0; i < adapter.listViewItems.size(); i++)//배열
            {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("sun", adapter.listViewItems.get(i).getSun().toString());
                sObject.put("time", adapter.listViewItems.get(i).getTime().toString());
                sObject.put("name", adapter.listViewItems.get(i).getName().toString());
                sObject.put("days", adapter.listViewItems.get(i).getDays().toString());
                sObject.put("work", adapter.listViewItems.get(i).getWork());
                jArray.put(sObject);
            }
            System.out.println(jArray.toString());

            editor.putString("list",jArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

}
