package com.example.fb0122.shanbaywork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fb0122 on 2016/6/18.
 */
public class MyAdapter extends BaseAdapter {

    private final static String TAG = "MyAdapter";

    private static Context context;
    private ArrayList<String> list;

    public MyAdapter(Context context,ArrayList<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout,null);
            holder.tv_artical = (TextView)convertView.findViewById(R.id.tv_artical);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tv_artical.setText(list.get(position).toString());
        return convertView;
    }

    class ViewHolder{
        TextView tv_artical;
    }
}
