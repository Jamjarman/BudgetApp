package org.duckdns.jamiehsg.budgetapp.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.duckdns.jamiehsg.budgetapp.R;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jamie on 11/27/15.
 */
public class BarPercentViewer extends View {

    private Paint bgPaint;
    private Paint fillPaint;
    private Paint blackPaint;
    private double percent;


    public BarPercentViewer (Context context) {
        super(context);
        init();
    }
    public BarPercentViewer (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public BarPercentViewer (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        bgPaint=new Paint();
        bgPaint.setColor(getContext().getResources().getColor(R.color.chartBackground));
        bgPaint.setAntiAlias(true);
        bgPaint.setStyle(Paint.Style.FILL);
        fillPaint=new Paint();
        fillPaint.setColor(getContext().getResources().getColor(R.color.chartForefround));
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);
        blackPaint=new Paint();
        blackPaint.setColor(getContext().getResources().getColor(R.color.black));
        blackPaint.setStyle(Paint.Style.FILL);
        blackPaint.setAntiAlias(true);
        blackPaint.setTextSize(25);
    }

    public void setPercent(double per){
            this.percent=per;
    }

    public double getPercent(){
        return this.percent;
    }

    protected void onDraw(Canvas c){
        super.onDraw(c);
        int h=getHeight()-40;
        int w=getWidth();
        if(percent>1){
            c.drawRect(w/12, h*((float)(1-1/percent)), w/12*5, h, bgPaint);
            c.drawRect(w/12*7, 0, w/12*11, h, fillPaint);
        }
        else{
            c.drawRect(w/12, 0, w/12*5, h, bgPaint);
            c.drawRect(w/12*7, h*((float)(1-percent)), w/12*11, h, fillPaint);
        }
        c.drawLine(0, h, w, h, blackPaint);
        c.drawText("Income", w / 12, h + 20, blackPaint);
        c.drawText("Expenses", w/12*7, h+20, blackPaint);
    }

}
