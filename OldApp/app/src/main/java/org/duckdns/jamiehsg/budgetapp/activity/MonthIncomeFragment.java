package org.duckdns.jamiehsg.budgetapp.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.duckdns.jamiehsg.budgetapp.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MonthIncomeFragment extends Fragment {
    private EditText amount;
    private EditText desc;
    private Spinner category;
    private View rootView;

    public MonthIncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_month_income, container, false);
        amount=(EditText)rootView.findViewById(R.id.month_income_amount_entry);
        desc=(EditText)rootView.findViewById(R.id.month_income_desc_entry);
        category=(Spinner)rootView.findViewById(R.id.month_income_spinner);
        Button submit=(Button)rootView.findViewById(R.id.submit_income_month);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitIncome(v);
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void submitIncome(View view){
        String amountStr=amount.getText().toString();
        String descStr=new StringToURL().toUrl(desc.getText().toString());
        String cat=new StringToURL().toUrl(category.getSelectedItem().toString());
        String urlS="http://www.jamieg.duckdns.org/budgetAPI/addMonthlyIncomes.php?username=jamie&pass=password&category="+cat+"&amount="+amountStr+"&desc="+descStr;
        new APICall().execute(urlS);
    }

    public void handleResponse(String response){
        System.out.print(response);
        try{
            JSONObject jsonObj=new JSONObject(response);
            if(jsonObj.getString("success").equals("1")){
                TextView text=(TextView)rootView.findViewById(R.id.instructions_plan_income_month);
                text.setText(jsonObj.getString("message"));
                amount.setText("");
                desc.setText("");
            }
        }
        catch (JSONException error){
            System.out.print(error);
        }

    }


    class APICall extends AsyncTask<String, String, String> {

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
        protected void onPostExecute(String response){
            handleResponse(response);
        }
    }
}