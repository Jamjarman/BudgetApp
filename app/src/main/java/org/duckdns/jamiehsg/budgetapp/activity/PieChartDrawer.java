package org.duckdns.jamiehsg.budgetapp.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.duckdns.jamiehsg.budgetapp.R;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jamie on 11/26/15.
 */
public class PieChartDrawer extends View {



    private Paint[] paintArr;
    private Map<String, Double> valueMap=new TreeMap<String, Double>();
    private Paint text;
    private RectF rect;

    public PieChartDrawer (Context context) {
        super(context);
        init();
    }
    public PieChartDrawer (Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PieChartDrawer (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setValueMap(Map inMap){
        this.valueMap=inMap;
        init();
    }

    public Map getValueMap(){
        return this.valueMap;
    }

    private void init(){
        paintArr=new Paint[valueMap.size()];
        int[] colorNums=getResources().getIntArray(R.array.barChartColors);
        for(int i=0; i<valueMap.size(); i++){
            paintArr[i]=new Paint();
            paintArr[i].setColor(colorNums[i%24]);
            paintArr[i].setStyle(Paint.Style.FILL);
            paintArr[i].setAntiAlias(true);
        }
        text=new Paint();
        text.setColor(getResources().getColor(R.color.black));
        text.setStyle(Paint.Style.FILL);
        text.setTextSize(25);
        text.setAntiAlias(true);
    }

    private int sumAmounts(Map<String, Double> m){
        int ret=0;
        for(String key:m.keySet()){
            ret+=m.get(key);
        }
        return ret;
    }


    @Override
    protected void onDraw(Canvas c){
        Log.d("onDraw", "in on draw");
        super.onDraw(c);
        int width=getWidth()/2;
        rect=new RectF();
        rect.set(getWidth()/4, 50, width+getWidth()/4, width+50);
        int total=sumAmounts(valueMap);
        float angleSum=0;
        int count=0;
        double textX;
        double textY;
        double perX;
        double perY;
        double theta;
        DecimalFormat moneyForm=new DecimalFormat("#.##");
        Rect tempRect;
        if (total>0){
            for(String key:valueMap.keySet()){
                c.drawArc(rect,angleSum, (float)(360*(valueMap.get(key)/total)), true, paintArr[count]);
                theta=(angleSum+(180*valueMap.get(key)/total))*Math.PI/180;
                textX=width/2*Math.cos(theta)+width;
                textY=width/2*Math.sin(theta)+width/2+50;
                String per=moneyForm.format(valueMap.get(key)/total*100)+"%";
                perX=width*.325*Math.cos(theta)+width- text.measureText(per)/2;
                perY=width*.325*Math.sin(theta)+width/2+50;
                String modKey=key;
                if(valueMap.get(key)/total<.05){
                    modKey=modKey+" "+per;
                }
                if(theta>=0&&theta<=Math.PI/2){
                    textY+=25;
                }
                else if(theta>Math.PI/2&&theta<=Math.PI){
                    textY+=25;
                    textX-=text.measureText(modKey);
                }
                else if(theta>Math.PI&&theta<=Math.PI*3/2){
                    textY-=25;
                    textX-=text.measureText(modKey);
                }
                else{
                    textY-=25;
                }
                if(valueMap.get(key)/total>.05){
                    c.drawText(per, (float)perX, (float)perY, text);
                }
                c.drawText(modKey, (float)textX, (float)textY, text);
                count++;
                angleSum+=360*(valueMap.get(key)/total);
            }
        }
    }
}
