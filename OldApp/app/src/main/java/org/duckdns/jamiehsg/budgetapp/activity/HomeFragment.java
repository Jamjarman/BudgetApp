package org.duckdns.jamiehsg.budgetapp.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.duckdns.jamiehsg.budgetapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


public class HomeFragment extends Fragment {

    private EditText amountIncome;
    private EditText descIncome;
    private Spinner categoryIncome;
    private EditText amountExpense;
    private EditText descExpense;
    private Spinner categoryExpense;
    private JSONObject monthlyIncomes;
    private JSONObject monthlyExpenses;
    private JSONObject incomes;
    private JSONObject expenses;
    private BarPercentViewer barChart;
    private DecimalFormat moneyForm;
    private PieChartDrawer pieChart;
    private TextView outputText;

    private View rootView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    /**
     * set up needed objects for displaying data and make api calls.
     * TODO add swipe detection to change from daily view to weekly/monthly/all time
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        barChart=(BarPercentViewer)rootView.findViewById(R.id.bar_chart);
        pieChart=(PieChartDrawer)rootView.findViewById(R.id.pie_chart);
        amountExpense=(EditText)rootView.findViewById(R.id.expense_amount_entry);
        descExpense=(EditText)rootView.findViewById(R.id.expense_desc_entry);
        categoryExpense=(Spinner)rootView.findViewById(R.id.expense_spinner);
        outputText=(TextView)rootView.findViewById(R.id.performance_bar_chart);
        Button submitExpense=(Button)rootView.findViewById(R.id.submit_expense);
        submitExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitExpense(v);
            }
        });
        amountIncome=(EditText)rootView.findViewById(R.id.income_amount_entry);
        descIncome=(EditText)rootView.findViewById(R.id.income_desc_entry);
        categoryIncome=(Spinner)rootView.findViewById(R.id.income_spinner);
        Button submitIncome=(Button)rootView.findViewById(R.id.submit_income);
        submitIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitIncome(v);
            }
        });
        //make initial calls to the api to get data
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        moneyForm=new DecimalFormat("#.##");
        //outputText.setText("http://jamieg.duckdns.org/budgetAPI/getExpenses.php?username=jamie&pass=password&dateFrom="+dateFormat.format(date)+"&dateTo="+dateFormat.format(date));
        new APICall().execute("http://jamieg.duckdns.org/budgetAPI/getIncomes.php?username=jamie&pass=password&dateFrom="+dateFormat.format(date)+"&dateTo="+dateFormat.format(date));
        new APICall().execute("http://jamieg.duckdns.org/budgetAPI/getExpenses.php?username=jamie&pass=password&dateFrom="+dateFormat.format(date)+"&dateTo="+dateFormat.format(date));
        new APICall().execute("http://jamieg.duckdns.org/budgetAPI/getMonthlyExpenses.php?username=jamie&pass=password");
        new APICall().execute("http://jamieg.duckdns.org/budgetAPI/getMonthlyIncomes.php?username=jamie&pass=password");
        // Inflate the layout for this fragment
        return rootView;
    }

    /**
     * submit income to database and clear fields
     * @param view
     */
    public void submitIncome(View view){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String amountStr=amountIncome.getText().toString();
        String descStr=new StringToURL().toUrl(descIncome.getText().toString());
        String cat=new StringToURL().toUrl(categoryIncome.getSelectedItem().toString());
        String urlS="http://www.jamieg.duckdns.org/budgetAPI/addIncomes.php?username=jamie&pass=password&category="+cat+"&amount="+amountStr+"&desc="+descStr+"&date="+dateFormat.format(date);
        new APICall().execute(urlS);
        TextView text=(TextView)rootView.findViewById(R.id.instructions_plan);
        text.setText("Entry has been entered successfully");
        amountIncome.setText("");
        descIncome.setText("");
    }

    /**
     * submit expense to database and clear fields
     * @param view
     */
    public void submitExpense(View view){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String amountStr=amountExpense.getText().toString();
        String descStr=new StringToURL().toUrl(descExpense.getText().toString());
        String cat=new StringToURL().toUrl(categoryExpense.getSelectedItem().toString());
        String urlS="http://www.jamieg.duckdns.org/budgetAPI/addExpenses.php?username=jamie&pass=password&category="+cat+"&amount="+amountStr+"&desc="+descStr+"&date="+dateFormat.format(date);
        new APICall().execute(urlS);
        TextView text=(TextView)rootView.findViewById(R.id.instructions_plan_expense);
        text.setText("Entry has been entered successfully");
        amountExpense.setText("");
        descExpense.setText("");
    }

    /**
     * iterates through all received data, once it's been called and all api calls are processed. Converts this data to JSON objects and then does the following:
     * Create a map of expenses to be used to create a pie chart
     * Load data for each table into the display tables
     * Calculate total expenses/incomes
     */
    private void displayData(){
        try{
            JSONArray incomeArr= incomes.getJSONArray("rows");
            JSONArray expenseArr=expenses.getJSONArray("rows");
            JSONArray monthlyExpenseArr=monthlyExpenses.getJSONArray("rows");
            JSONArray monthlyIncomeArr=monthlyIncomes.getJSONArray("rows");
            JSONObject temp=null;
            double expenseTotal=0;
            double incomeTotal=0;
            double monthIncomeTotal=0;
            double monthExpenseTotal=0;
            Map<String, Double> expenseMap=new TreeMap<String, Double>();
            for(int i=0; i<incomeArr.length(); i++){
                temp=incomeArr.getJSONObject(i);
                incomeTotal+=temp.getDouble("amount");
            }
            for(int j=0; j<monthlyIncomeArr.length(); j++){
                temp=monthlyIncomeArr.getJSONObject(j);
                monthIncomeTotal+=temp.getDouble("amount");
            }
            for(int k=0; k<expenseArr.length(); k++){
                temp=expenseArr.getJSONObject(k);
                expenseTotal+=temp.getDouble("amount");
                if(expenseMap.get(temp.getString("category"))==null) {
                    expenseMap.put(temp.getString("category"), temp.getDouble("amount"));
                }
                else{
                    double curr=expenseMap.get(temp.getString("category"));
                    curr+=temp.getDouble("amount");
                    expenseMap.put(temp.getString("category"), curr);
                }
            }
            for(int l=0; l<monthlyExpenseArr.length(); l++){
                temp=monthlyExpenseArr.getJSONObject(l);
                monthIncomeTotal-=temp.getDouble("amount");
            }
            Log.d("displayDataPre", "About to calc percent and display data");
            double percent=expenseTotal/(incomeTotal+(monthIncomeTotal)/30);
            barChart.setPercent(percent);
            barChart.invalidate();
            pieChart.setValueMap(expenseMap);
            pieChart.invalidate();
            String output="You have spent $"+moneyForm.format(expenseTotal)+" today out of $"+moneyForm.format(incomeTotal+monthIncomeTotal/30)+" which is "+moneyForm.format(percent*100)+"% of today's income";
            outputText.setText(output);

        }
        catch(JSONException error){

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*
   Gets data from async call, gets the proper table based on the value of the "table" element in the JSON array and sets the proper JSON obj to the received JSON array,
    if all arrays are full then call display data
    */
    public void handleResponse(String data){
        try{
            JSONObject jsonObj=new JSONObject(data);
            if(jsonObj.getString("table")!=null){
                Log.d("Primary handler", "Adding data to jsonObj for processing");
                String tableName=jsonObj.getString("table");
                switch (tableName){
                    case "incomes":
                        incomes=jsonObj;
                        break;
                    case "expenses":
                        expenses=jsonObj;
                        break;
                    case "monthly_expenses":
                        monthlyExpenses=jsonObj;
                        break;
                    case "monthly_incomes":
                        monthlyIncomes=jsonObj;
                        break;
                }
            }
        }
        catch (JSONException error){
            System.out.print(error);
        }
        if(incomes!=null&&expenses!=null&&monthlyExpenses!=null&&monthlyIncomes!=null){
            displayData();
        }
    }

    /*
    Interior class extending AsyncTask, used for aysnchronus calls to the api
     */
    class APICall extends AsyncTask<String, String, String> {

        /**
         * call url and return response
         * @param urlToGo
         * @return
         */
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

        /**
         * pass respose from http get call to handleResponse in super class
         * @param response
         */
        protected void onPostExecute(String response){
            handleResponse(response);
        }
    }
}