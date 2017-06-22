package com.example.com.darckalram;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by com on 2017-05-09.
 */

//알람을 해제하는 클래스입니다.
public class AlramWork extends AppCompatActivity {

    Button button;
    TextView textView;
    int touchCount = 0, maxCount = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        Random rand = new Random();

        maxCount = rand.nextInt(11) + 3;

        Log.i("dark","alarm is require + "+ (maxCount) +" locked");

        textView = (TextView)findViewById(R.id.workMessage);
        textView.setText(getIntent().getStringExtra("msg"));

        final Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);

        button = (Button)findViewById(R.id.dark);

        //버튼 애니메이션 처리
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++touchCount;
                if(touchCount >= maxCount){
                    Log.i("dark","alarm is unlocked");
                    finish();
                }
                else{

                    button.startAnimation(shake);
                    Log.i("dark","alarm is require + "+ (maxCount - touchCount) +" locked");
                    //Toast.makeText(getApplicationContext(),"알람 해제 까지 " + (maxCount - touchCount) + "번 남았습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


}
