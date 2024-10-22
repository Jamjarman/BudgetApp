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

    //creates an array of paint objects of size valueMap for pie chart coloring
    private void init(){
        paintArr=new Paint[valueMap.size()];
        //sets color equal to next value in color array barChartColors
        int[] colorNums=getResources().getIntArray(R.array.barChartColors);
        for(int i=0; i<valueMap.size(); i++){
            paintArr[i]=new Paint();
            paintArr[i].setColor(colorNums[i%24]);
            paintArr[i].setStyle(Paint.Style.FILL);
            paintArr[i].setAntiAlias(true);
        }
        //creates a paint object text used for labeling
        text=new Paint();
        text.setColor(getResources().getColor(R.color.black));
        text.setStyle(Paint.Style.FILL);
        text.setTextSize(25);
        text.setAntiAlias(true);
    }

    //returns teh sum total of all amounts in given map
    private double sumAmounts(Map<String, Double> m){
        double ret=0;
        for(String key:m.keySet()){
            ret+=m.get(key);
            Log.d("value", m.get(key)+"");
            Log.d("sum", ret+"");
        }
        Log.d("sumAmounts", ret+"");
        return ret;
    }


    @Override
    /**
     * draws pie chart, based on percentage of total, and labels each section
     */
    protected void onDraw(Canvas c){
        //Log.d("onDraw", "in on draw");
        super.onDraw(c);
        int width=getWidth()/2;
        rect=new RectF();
        rect.set(getWidth()/4, 50, width+getWidth()/4, width+50);
        double total=sumAmounts(valueMap);
        float angleSum=0;
        int count=0;
        double textX;
        double textY;
        double perX;
        double perY;
        double theta;
        DecimalFormat moneyForm=new DecimalFormat("#.##");
        Rect tempRect;
        //if total>0 exists to protect from crash before a map has been input
        if (total>0){
            //iterate through all key's in valueMap
            for(String key:valueMap.keySet()){
                //draw arc using ith paint in the paint array, beginning at angle sum and continuing for 360*percent degrees
                c.drawArc(rect,angleSum, (float)(360*(valueMap.get(key)/total)), true, paintArr[count]);
                Log.d("valueMap(key)",""+valueMap.get(key));
                Log.d("percent", (valueMap.get(key) / total) + "");
                Log.d("angle", ""+(360*(valueMap.get(key)/total)));
                //gets theta of center of segment
                theta=(angleSum+(180*valueMap.get(key)/total))*Math.PI/180;
                //converts theta to x and y coordinates for label
                textX=width/2*Math.cos(theta)+width;
                textY=width/2*Math.sin(theta)+width/2+50;
                //creates string for percentage
                String per=moneyForm.format(valueMap.get(key)/total*100)+"%";
                //gets position for percentage within pie chart
                perX=width*.325*Math.cos(theta)+width- text.measureText(per)/2;
                perY=width*.325*Math.sin(theta)+width/2+50;
                String modKey=key;
                //if percent is less than 5% append percentage to the end of the label rather than drawing it in the pie chart itself
                if(valueMap.get(key)/total<.05){
                    modKey=modKey+" "+per;
                }
                //moves text position based on theta so that it does not intersect with the chart at all
                //adjust text y position up slightly
                if(theta>=0&&theta<=Math.PI/2){
                    textY+=25;
                }
                //adjust text position up and move text beginning so that end of text is near circle
                else if(theta>Math.PI/2&&theta<=Math.PI){
                    textY+=25;
                    textX-=text.measureText(modKey);
                }
                //adjust text position down and over so that end of text is near cirlce
                else if(theta>Math.PI&&theta<=Math.PI*3/2){
                    textY-=25;
                    textX-=text.measureText(modKey);
                }
                //adjust text down
                else{
                    textY-=25;
                }
                //draws percentage in chart if it's greater than 5%
                if(valueMap.get(key)/total>.05){
                    c.drawText(per, (float)perX, (float)perY, text);
                }
                //draw label text
                c.drawText(modKey, (float)textX, (float)textY, text);
                count++;
                angleSum+=360*(valueMap.get(key)/total);
            }
        }
    }
}
