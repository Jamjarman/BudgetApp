package org.duckdns.jamiehsg.budgetapp.activity;

/**
 * Created by jamie on 11/24/15.
 */
public class StringToURL {

    public StringToURL(){

    }

    public String toUrl(String in){
        String out="";
        in=in.trim();
        while (in.indexOf(' ')>-1){
            int nextSpace=in.indexOf(' ');
            String word=in.substring(0, nextSpace);
            out=out+word+"+";
            in=in.substring(nextSpace).trim();
        }
        out=out+in;
        return out;
    }

}
