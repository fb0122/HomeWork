package com.example.fb0122.shanbaywork;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.appyvet.rangebar.RangeBar;

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
 * Created by fb0122 on 2016/6/20.
 */
public class ArticalDetail extends AppCompatActivity {

    private final static String TAG = "ArticalDetail";
    private static Context context;
    private final static int LEVEL_WORDS = 2;

    TextView tv_content;
    RangeBar rangeBar;
    private InputStream inputStream;
    HashMap<Integer,ArrayList<String>> map = new HashMap<>();
    ListFactory<String> listFactory = new ListFactory<>(6);
    String artical;
    SpannableStringBuilder style ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_content);
        initView();
        context = getApplicationContext();

        long startTime =  System.currentTimeMillis();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("lesson").toString());
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (toolbar.getNavigationIcon() != null){
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        }
        artical = getIntent().getStringExtra("content").toString();
        tv_content.setText(artical);

        style = new SpannableStringBuilder(artical);
        inputStream = getResources().openRawResource(R.raw.nce4_words);
        LevelWords levelWords = new LevelWords(Looper.myLooper(),inputStream);
        Message msg = levelWords.obtainMessage();
        msg.what = LEVEL_WORDS;
        levelWords.handleMessage(msg);
        rangeBar.setSeekPinByIndex(0);
        //rangebar监听器
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                Log.e(TAG,"rightIndex: " + rightPinIndex + "; leftINdex: " + leftPinIndex);
                highLight(rightPinIndex);
            }
        });

        long endTime = System.currentTimeMillis();
        Log.e(TAG,"use time: " + "-------" + (endTime - startTime) + "-------");

    }

    private void initView(){
        tv_content = (TextView)findViewById(R.id.tv_content);
        rangeBar = (RangeBar)findViewById(R.id.rangbar);
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

    class LevelWords extends Handler{

        private InputStream inputStream;

        public LevelWords(Looper looper,InputStream inputStream){
            super(looper);
            this.inputStream = inputStream;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LEVEL_WORDS:
                    map = listFactory.getList();
                    String level_pattern = "\\d+";
                    Pattern level_r = Pattern.compile(level_pattern);
                    InputStreamReader reader = null;
                    try {
                        reader = new InputStreamReader(inputStream,"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line;
                    try {
                        while ((line = bufferedReader.readLine()) != null){
                            String[] words_str = line.split("\\s+");
                            if (words_str[1].length() < 2) {
                                switch (Integer.parseInt(words_str[1])) {
                                    case 0:
                                        map.get(0).add(words_str[0]);
                                        break;
                                    case 1:
                                        map.get(1).add(words_str[0]);
                                        break;
                                    case 2:
                                        map.get(2).add(words_str[0]);
                                        break;
                                    case 3:
                                        map.get(3).add(words_str[0]);
                                        break;
                                    case 4:
                                        map.get(4).add(words_str[0]);
                                        break;
                                    case 5:
                                        map.get(5).add(words_str[0]);
                                        break;
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    //高亮
    private void highLight(int level){
        long startTime = System.currentTimeMillis();
        style.clearSpans();
        ArrayList<String> words = new ArrayList<>();
        String pattern = "[a-zA-Z]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(artical);
        while (m.find()){
            words.add(m.group(0));
        }
        Log.e(TAG,"words" + words);
        HashMap<String,Integer> level_map = new HashMap<>();
        for (int i = 0;i <= level;i++) {
            ArrayList<String> list = map.get(i);
            for (String s : list) {
                level_map.put(s, 1);
            }
            for (String s1 : words) {
                if (level_map.get(s1) != null) {
                    Log.e(TAG,"" + s1 + "  start: " + artical.indexOf(s1) + "  end: " + artical.indexOf(s1.length()));
                    style.setSpan(new BackgroundColorSpan(Color.YELLOW),artical.indexOf(s1),artical.indexOf(s1) + s1.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    tv_content.setText(style);
                    tv_content.invalidate();
                }
            }
        }
        Log.e(TAG,"---------use time----------" + (startTime - System.currentTimeMillis()));
    }


}
