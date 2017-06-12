package com.example.com.darckalram;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by com on 2017-05-09.
 */
//커스텀 리스트뷰 어답터 입니다.
public class ListViewAdapter extends BaseAdapter {

    //리스트 요소 배열 생성
    ArrayList<ListViewItem> listViewItems = new ArrayList<ListViewItem>();

    public ListViewAdapter(){

    }

    @Override
    public int getCount(){
        return listViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView sunText = (TextView) convertView.findViewById(R.id.sunText);
        TextView timeText = (TextView) convertView.findViewById(R.id.timeText);
        TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
        TextView daysText = (TextView) convertView.findViewById(R.id.daysText);

        Switch workSwitch = (Switch) convertView.findViewById(R.id.workSwitch);


        ListViewItem listViewItem = listViewItems.get(position);

        sunText.setText(listViewItem.getSun());
        timeText.setText(listViewItem.getTime());
        nameText.setText(listViewItem.getName());

        String days = listViewItem.getDays();
        daysText.setText(days);

        workSwitch.setChecked(listViewItem.getWork());

        return convertView;
    }

    //요소를 추가 합니다.
    public void addItem(String sun, String name, String time, String days, boolean isWork){
        ListViewItem item = new ListViewItem();

        item.setSun(sun);
        item.setName(name);
        item.setTime(time);
        item.setDays(days);
        item.setWork(isWork);

        listViewItems.add(item);
    }

    //요소를 삭제합니다.
    public boolean RemoveItem(int index){
        try {
            listViewItems.remove(index);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }


}
