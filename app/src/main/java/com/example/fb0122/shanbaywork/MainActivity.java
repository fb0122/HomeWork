package com.example.fb0122.shanbaywork;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fb0122 on 2016/6/19.
 */

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private final static int GET_DATA = 1;
    private static Context context;

    private InputStream inputStream;
    long startTime = System.currentTimeMillis();
    static StringBuffer stringBuffer = new StringBuffer("");
    getDataHandler handler;
    HashMap<String,String> map = new HashMap<String, String>();
    ArrayList<String> unit_list = new ArrayList<String>();
    ArrayList<String> lesson_list = new ArrayList<String>();
    ListView lv_artical;
    String line1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        initView();
        inputStream = getResources().openRawResource(R.raw.forth);
        long endTime = System.currentTimeMillis();
        handler = new getDataHandler(Looper.myLooper(),inputStream);
        Message msg = handler.obtainMessage();
        msg.what = GET_DATA;
        handler.handleMessage(msg);
        MyAdapter adapter = new MyAdapter(context,unit_list);
        lv_artical.setAdapter(adapter);
        Log.e(TAG,"------use time-------" + (endTime - startTime));

    }

    private void initView(){
        lv_artical = (ListView)findViewById(R.id.lv_artical);
    }

    class getDataHandler extends Handler {
        InputStream inputStream;

        public getDataHandler(Looper looper, InputStream inputStream) {
            super(looper);
            this.inputStream = inputStream;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
                            line1 = line.replace(" ","");
                            Matcher m = r.matcher(line1);
                            if (!line.equals("") && (line1.length() < 15) && (m.find())) {
                                Matcher unit_m = unit_r.matcher(line);
                                Matcher lesson_m = lesson_r.matcher(line);
//                                while (unit_m.find() || lesson_m.find()) {
                                    if (unit_m.find()) {
                                        unit_list.add(unit_m.group(0));
                                        Log.e(TAG, "unit_list is: " + unit_list);

                                    }else if (lesson_m.find()){
                                        lesson_list.add(lesson_m.group(0));
                                        Log.e(TAG,"lesson_list is: " + lesson_list);
                                    }
//                                }
                            }
                            stringBuffer.append(line);
                            stringBuffer.append("\n");
                        }
                        for (int i = 0;i < unit_list.size(); i++){
                            if (i < unit_list.size()-1) {
                                map.put("Unit" + i, stringBuffer.toString().substring(stringBuffer.toString().indexOf(unit_list.get(i)), stringBuffer.toString().indexOf(unit_list.get(i + 1))));
                            }else {
                                map.put("Unit" + i,stringBuffer.toString().substring(stringBuffer.toString().indexOf(unit_list.get(i)), stringBuffer.toString().length()));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }
}
