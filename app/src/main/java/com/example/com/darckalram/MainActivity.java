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
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//Main 화면을 호출하는 클래스입니다.
public class MainActivity extends AppCompatActivity {

    //알람 매니저
    private AlarmManager alarmManager;

    private NotificationManager notificationManager;
    //플로팅 버튼
    private FloatingActionButton fab;

    //리스트 뷰
    private ListView listView;
    public static ListViewAdapter adapter = new ListViewAdapter();

    //초기 로드 되었는가?
    static boolean isFirst = false;

    //리스트 뷰 홀드시 메뉴 호출
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,v.getId(),0,"Delete");
    }

    //삭제 메뉴 선택
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle() == "Delete"){
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            DeleteItem(menuInfo.position);
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //초기 로드 상태인가?
        if(!isFirst) {
            getPreferences();
            isFirst = true;
        }
        //알림 등록
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //알람 등록
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //xml 로드
        listView = (ListView)findViewById(R.id.list);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        //리스너 등록
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddAlarm.class);
                startActivity(intent);
            }
        });
        //커스텀 리스트뷰 등록
        listView.setAdapter(adapter);
        //메뉴 등록
        registerForContextMenu(listView);

    }

    //리스트 요소를 삭제합니다.
    public void DeleteItem(int id){
        ListViewItem item = (ListViewItem) adapter.getItem(id);
        adapter.RemoveItem(id);
        savePreferences();
        //리스트뷰 업데이트
        listView.setAdapter(adapter);
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
                    //리스트 요소에 등록
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

        JSONArray jArray = new JSONArray();
        try {

            for (int i = 0; i < adapter.listViewItems.size(); i++)
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
