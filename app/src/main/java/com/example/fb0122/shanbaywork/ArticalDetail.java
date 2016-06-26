package com.example.fb0122.shanbaywork;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
public class ArticalDetail extends AppCompatActivity{

    private final static String TAG = "ArticalDetail";
    private static Context context;
    private final static int LEVEL_WORDS = 2;
    private final static int ALIGN_TEXT = 3;

    CBAlignTextView tv_content;
    RangeBar rangeBar;
    private InputStream inputStream;
    HashMap<Integer,ArrayList<String>> map = new HashMap<>();
    ListFactory<String> listFactory = new ListFactory<>(6);
    String artical;
    SpannableStringBuilder style ;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_content);

        context = getApplicationContext();

        long startTime =  System.currentTimeMillis();
        initView();
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
        tv_content.setPunctuationConvert(true);
        tv_content.setText(artical);
        content = tv_content.getRealText().toString();
        style = new SpannableStringBuilder(content);
//
        inputStream = getResources().openRawResource(R.raw.nce4_words);
        LevelWords levelWords = new LevelWords(Looper.myLooper(),inputStream,0);
        Message msg = levelWords.obtainMessage();
        msg.what = LEVEL_WORDS;
        levelWords.handleMessage(msg);
        rangeBar.setSeekPinByIndex(0);
        //rangebar监听器
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                LevelWords levelWords1 = new LevelWords(Looper.myLooper(),null,Integer.parseInt(rightPinValue));
                Message msg1 = levelWords1.obtainMessage();
                msg1.what = ALIGN_TEXT;
                levelWords1.handleMessage(msg1);
            }
        });

        long endTime = System.currentTimeMillis();
//        Log.e(TAG,"use time: " + "-------" + (endTime - startTime) + "-------");

    }

    private void initView(){
        tv_content = (CBAlignTextView) findViewById(R.id.tv_content);
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
        private int right;

        public LevelWords(Looper looper,InputStream inputStream,int right){
            super(looper);
            this.inputStream = inputStream;
            this.right = right;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LEVEL_WORDS:
                    map = listFactory.getList();
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
                case ALIGN_TEXT:
                    highLight(right);
                    break;
            }
        }
    }

    //高亮
    private void highLight(int level){

        long startTime = System.currentTimeMillis();
        style.clearSpans();
        tv_content.invalidate();

        ArrayList<String> words = new ArrayList<>();
        ArrayList<String> hl_list = new ArrayList<>();
        String pattern = "[a-zA-Z]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(tv_content.getRealText().toString());
        while (m.find()){
            words.add(m.group(0));
        }
        HashMap<String,Integer> level_map = new HashMap<>();
        for (int i = 0;i <= level;i++) {
            ArrayList<String> list = map.get(i);
            for (String s : list) {
                level_map.put(s, 1);
            }
            for (String s1 : words) {
                if (level_map.get(s1) != null) {
                    hl_list.add(s1);
                }
            }
            tv_content.setChangeText(content,hl_list,TextView.BufferType.SPANNABLE);
            hl_list.clear();
        }
//        Log.e(TAG,"---------use time----------" + (startTime - System.currentTimeMillis()));
    }

}
