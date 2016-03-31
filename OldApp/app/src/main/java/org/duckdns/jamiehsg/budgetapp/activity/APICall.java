package org.duckdns.jamiehsg.budgetapp.activity;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jamie on 11/23/15.
 */
public class APICall extends AsyncTask<String, String, String> {

    protected String doInBackground(String... urlToGo){
        String response=null;
        HttpURLConnection conn=null;
        try{
            URL url=new URL(urlToGo[0]);
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int status=conn.getResponseCode();
            if(status==200){
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line;
                while((line=br.readLine())!=null){
                    sb.append(line+"\n");
                }
                br.close();
                response=sb.toString();
            }
        }
        catch(MalformedURLException error){
            System.out.println("URL error: "+error);
        }
        catch(IOException error) {
            System.out.println("IOError: "+error);
        }
        finally{
            if(conn!=null){
                conn.disconnect();
            }
        }
        return response;
    }

}
