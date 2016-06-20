package com.example.fb0122.shanbaywork;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fb0122 on 2016/6/19.
 */
public class AtyLesson extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final static String TAG = "AtyLesson";
    private static Context context;


    ArrayList<String> detail_list = new ArrayList<>();
    ListView lv_lesson;
    String[] content_str = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_lesson);

        context = getApplicationContext();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("unit").toString());
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (toolbar.getNavigationIcon() != null){
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        }

        initView();
        lv_lesson.setOnItemClickListener(this);
        detail_list = (ArrayList<String>) getIntent().getSerializableExtra("detail");
        content_str = getIntent().getStringArrayExtra("lesson");
        Log.e(TAG,"detail_list " + detail_list);
        LessonAdapter adapter = new LessonAdapter(context,detail_list);
        lv_lesson.setAdapter(adapter);

    }

    private void initView(){
        lv_lesson = (ListView)findViewById(R.id.lv_lesson);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.e(TAG,"click Itme = " + detail_list.get(position));
        Intent intent = new Intent(AtyLesson.this,ArticalDetail.class);
        intent.putExtra("content",content_str[position + 1]);
        intent.putExtra("lesson",detail_list.get(position));
        startActivity(intent);

    }

    class LessonAdapter extends BaseAdapter{

        private Context context;
        private ArrayList<String> list = new ArrayList<String>();

        public LessonAdapter(Context context,ArrayList<String> list){
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
                convertView = LayoutInflater.from(context).inflate(R.layout.lesson_item,null);
                holder.tv_lesson = (TextView)convertView.findViewById(R.id.tv_lesson);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.tv_lesson.setText(list.get(position).toString());

            return convertView;
        }

        class ViewHolder{
            TextView tv_lesson;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
