package com.example.fb0122.shanbaywork;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fb0122 on 2016/6/19.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final static String TAG = "MainActivity";
    private final static int GET_DATA = 1;
    private static Context context;

    private InputStream inputStream;
    long startTime = System.currentTimeMillis();

    getDataHandler handler;
    ArrayList<ArrayList<String>> le_list = new ArrayList<>();

    ArrayList<String> unit_list = new ArrayList<String>();
    ArrayList<String[]> lesson_list = new ArrayList<>();
    ListView lv_artical;
    String line1;
    String artical = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        initView();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("首页");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        inputStream = getResources().openRawResource(R.raw.forth);
        long endTime = System.currentTimeMillis();
        handler = new getDataHandler(Looper.myLooper(), inputStream);
        Message msg = handler.obtainMessage();
        msg.what = GET_DATA;
        if (artical.length() == 0) {
            handler.handleMessage(msg);
        }
        MyAdapter adapter = new MyAdapter(context, unit_list);
        lv_artical.setAdapter(adapter);
        lv_artical.setOnItemClickListener(this);
//        Log.e(TAG, "------use time-------" + (endTime - startTime));
    }

    private void initView() {
        lv_artical = (ListView) findViewById(R.id.lv_artical);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, AtyLesson.class);
        intent.putExtra("unit",unit_list.get(position));
        intent.putExtra("detail", le_list.get(position));
        intent.putExtra("lesson",lesson_list.get(position));
        startActivity(intent);
    }

    class getDataHandler extends Handler {
        InputStream inputStream;
        StringBuffer stringBuffer = new StringBuffer("");
        public getDataHandler(Looper looper, InputStream inputStream) {
            super(looper);
            this.inputStream = inputStream;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_DATA:
                    String unit_pattern = "\\bUnit(.*)\\d+";
                    String lesson_pattern = "\\bLesson(.*)\\d+";
                    String patten = "[A-Z]{0,}";
                    Pattern unit_r = Pattern.compile(unit_pattern);
                    Pattern lesson_r = Pattern.compile(lesson_pattern);
                    Pattern r = Pattern.compile(patten);

                    InputStreamReader reader = null;
                    try {
                        reader = new InputStreamReader(inputStream, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    BufferedReader bufRead = new BufferedReader(reader);

                    String line;
                    try {
                        while ((line = bufRead.readLine()) != null) {
                            line1 = line.replace(" ", "");
                            Matcher m = r.matcher(line1);
                            if (!line.equals("") && (line1.length() < 15) && (m.find())) {
                                Matcher unit_m = unit_r.matcher(line);

                                if (unit_m.find()) {
                                    unit_list.add(unit_m.group(0));
                                }
                            }
                            stringBuffer.append(line);
                            stringBuffer.append("\n");
                        }

                        artical = stringBuffer.toString();
                        String[] str = artical.split("\\s*\\bUnit(.*)\\d+\\s*");


                        Log.e(TAG,"str  = " + str.length);
                        for (int i = 0; i < str.length; i++) {
                            ArrayList<String> child_list = new ArrayList<>();
                            String[] lesson_str = new String[]{""};
                            child_list.clear();
                            if (!str[i].equals("")){
                                Matcher lesson_m = lesson_r.matcher(str[i]);
                                while (lesson_m.find()){
                                    child_list.add(lesson_m.group(0));
                                }
                                lesson_str = str[i].split("\\s*\\bLesson(.*)\\d+\\s*");
                                lesson_list.add(lesson_str);
                                le_list.add(child_list);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
