package com.example.com.darckalram;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Main 화면을 호출하는 클래스입니다.
public class MainActivity extends AppCompatActivity {

    public static MainActivity insatnce;

    //알람 매니저
    public static AlarmManager alarmManager;

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
        //홀드 메뉴 호출
        menu.add(0, v.getId(), 0, "Delete");
    }

    //삭제 메뉴 선택
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete") {
            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //아이템을 삭제합니다.
            DeleteItem(menuInfo.position);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //싱글 톤 지정
        insatnce = this;

        //초기 로드 상태인가?
        if (!isFirst) {
            getPreferences();
            isFirst = true;
        }
        //알림 등록
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //알람 등록
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //xml 로드
        listView = (ListView) findViewById(R.id.list);
        fab = (FloatingActionButton) findViewById(R.id.fab);
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
        //리스트뷰 스위치 이벤트 등록
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Switch workSwitch = (Switch) view.findViewById(R.id.workSwitch);
                workSwitch.setChecked(!workSwitch.isChecked());

                ListViewItem item = (ListViewItem) adapter.getItem(position);
                item.setWork(workSwitch.isChecked());
                if (workSwitch.isChecked() == false) {
                    //알람 해제를 설정할 경우
                    Log.i("dark","is false");
                    CancleAlarm(item.getID());
                } else {
                    Log.i("dark","is true");
                    AddAlarm(item);
                }
                //설정을 저장합니다.
                savePreferences();
                //리스트를 업데이트 합니다.
                updateListView();
            }
        });
        //메뉴 액션 등록
        registerForContextMenu(listView);

    }

    //알람을 해제 합니다.
    boolean CancleAlarm(int id) {
        Intent intent = new Intent(this, AlramReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (sender != null) {
            Log.i("dark", "is remove");
            alarmManager.cancel(sender);
            sender.cancel();
            return true;
        } else {
            Log.i("dark", "is not found");
            return false;
        }
    }

    //리스트 요소를 삭제합니다.
    public void DeleteItem(int id) {
        ListViewItem item = (ListViewItem) adapter.getItem(id);
        if(CancleAlarm(item.getID())) {
            //리스트에서 삭제
            adapter.RemoveItem(id);
        }
        //변경 사항을 저장
        savePreferences();
        //리스트뷰 업데이트
        updateListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("dark", "on start");
        savePreferences();
        listView.invalidateViews();
    }

    //리스트뷰를 업데이트 합니다.
    public void updateListView() {
        listView.invalidateViews();
    }

    //알람을 등록합니다.
    public void AddAlarm(ListViewItem item) {
        Intent intent = new Intent(this, AlramReceiver.class);
        intent.putExtra("weekday", item.getWeeks());


        int id = item.getID();

        intent.putExtra("id", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), item.getHour(), item.getMin(), calendar.get(Calendar.SECOND));

        long oneDay = 24 * 60 * 60 * 1000;

        alarmManager.setRepeating(alarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), oneDay, pendingIntent);

        startService(intent);

        //변경 사항을 저장
        savePreferences();
        //리스트뷰 업데이트
        updateListView();
    }


    // 값 불러오기
    public void getPreferences() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String list = pref.getString("list", "");
        if (list != "") {
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
                    int id = obj.getInt("id");
                    String sun = obj.getString("sun");
                    int hour = obj.getInt("hour");
                    int min = obj.getInt("min");
                    String name = obj.getString("name");
                    String days = obj.getString("days");
                    Boolean work = obj.getBoolean("work");
                    boolean[] weeks = new boolean[8];
                    weeks[0] = false;
                    weeks[1] = obj.getBoolean("bool_sun");
                    weeks[2] = obj.getBoolean("bool_mon");
                    weeks[3] = obj.getBoolean("bool_tue");
                    weeks[4] = obj.getBoolean("bool_wen");
                    weeks[5] = obj.getBoolean("bool_thu");
                    weeks[6] = obj.getBoolean("bool_fri");
                    weeks[7] = obj.getBoolean("bool_sat");
                    //리스트 요소에 등록
                    adapter.addItem(id, sun, name, hour, min, days, work, weeks);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("dark", "get error" + e);
            }
        }
    }

    // 값 저장하기
    public void savePreferences() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        JSONArray jArray = new JSONArray();
        try {

            if (adapter.listViewItems.size() == 0) {
                Log.i("dark", "save size is 0");
                return;
            }

            for (int i = 0; i < adapter.listViewItems.size(); i++) {
                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                sObject.put("id", adapter.listViewItems.get(i).getID());
                sObject.put("sun", adapter.listViewItems.get(i).getSun().toString());
                sObject.put("hour", adapter.listViewItems.get(i).getHour());
                sObject.put("min", adapter.listViewItems.get(i).getMin());
                sObject.put("name", adapter.listViewItems.get(i).getName().toString());
                sObject.put("days", adapter.listViewItems.get(i).getDays().toString());
                sObject.put("work", adapter.listViewItems.get(i).getWork());
                //요일 세팅을 저장합니다.
                sObject.put("bool_sun", adapter.listViewItems.get(i).getWeeks(0));
                sObject.put("bool_mon", adapter.listViewItems.get(i).getWeeks(1));
                sObject.put("bool_tue", adapter.listViewItems.get(i).getWeeks(2));
                sObject.put("bool_wen", adapter.listViewItems.get(i).getWeeks(3));
                sObject.put("bool_thu", adapter.listViewItems.get(i).getWeeks(4));
                sObject.put("bool_fri", adapter.listViewItems.get(i).getWeeks(5));
                sObject.put("bool_sat", adapter.listViewItems.get(i).getWeeks(6));
                jArray.put(sObject);
            }
            System.out.println(jArray.toString());

            editor.putString("list", jArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("dark", "save error" + e);
        }
        editor.commit();
    }

}
