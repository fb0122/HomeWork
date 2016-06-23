package com.example.fb0122.shanbaywork;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//实现两端对齐,未实现使用此方法实现高亮

public class WeDoridAlignTextView extends TextView {
    private final static String TAG = "WeDoridAlignTextView";

    private String content;
    private static int width;
    private Paint paint;
    private Context context;
    String[] texts;

    int textHeight;
    float yPadding;

    public WeDoridAlignTextView(Context context) {
        super(context);
        this.context = context;
        init();
        ViewTreeObserver vt = getViewTreeObserver();
        vt.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                width = getWidth();
                return true;
            }
        });
    }

    public WeDoridAlignTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        ViewTreeObserver vt = getViewTreeObserver();
        vt.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                width = getWidth();
                return true;
            }
        });
    }

    public WeDoridAlignTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        ViewTreeObserver vt = getViewTreeObserver();
        vt.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                width = getWidth();
                return true;
            }
        });
    }

    public void setText(String str) {
        Log.e(TAG,"width = " + width);
        if (width == 0){
            width = 1028;
        }
//        getPositions(str,width);
        //重新画控件
//        content = dealContent(str);
        content = dealContent(str);
        texts = autoSplit(content, paint, (width - 10));
        this.invalidate();
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }


    public void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.textColor));
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(getTextSize());

        Paint.FontMetrics fm = paint.getFontMetrics();
        textHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);
        yPadding = fm.leading;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = 5;
        float y = textHeight;
        String pattern = "\\n|\\r\\n";
        Pattern r = Pattern.compile(pattern);
        for(String text : texts) {
            Log.e(TAG,"text = " + text);
            if (text != null) {
                    canvas.drawText(text, x, y, paint);
                    y += textHeight + yPadding;//坐标以控件左上角为原点
                }
            }
    }

    private String[] autoSplit(String content, Paint p, float width) {
        int length = 0;
        float textWidth = 0;
        if (content != null) {
            length = content.length();
        }
        if (p != null && content != null) {
            textWidth = p.measureText(content);
        }

        if(textWidth <= width) {
            return new String[]{content};
        }

        int start = 0, end = 1, i = 0;
        int lines = (int) Math.ceil(textWidth / width); //计算行数
        String[] lineTexts = new String[lines];
        while(start < length) {
            if(p.measureText(content, start, end) > width) { //文本宽度超出控件宽度时
                lineTexts[i++] = content.substring(start, end);
                start = end;
            }
            if(end == length) { //不足一行的文本
                lineTexts[i] = (String) content.substring(start, end) + "\r\n";
                break;
            }
            end += 1;
        }
        this.setHeight((int) ((textHeight + yPadding) * lineTexts.length));
        return lineTexts;
    }

    public String dealContent(String str){
        String deal = "";
        String pattern = "\\s{3,}";
        Pattern r = Pattern.compile(pattern);
        deal = str.replaceAll("\\s{4,}$","\r\n");
//        Log.e(TAG,"deal_str = " + deal);
        return deal;
    }

    //工具类：判断是否是字母或者数字
    public boolean isNumOrLetters(String str)
    {
        String regEx="^[A-Za-z0-9_]+$";
        Pattern p= Pattern.compile(regEx);
        Matcher m=p.matcher(str);
        return m.matches();
    }
    // 工具类：在代码中使用dp的方法（因为代码中直接用数字表示的是像素）
    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

}
