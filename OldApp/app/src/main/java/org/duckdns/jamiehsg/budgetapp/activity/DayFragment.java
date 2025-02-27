package org.duckdns.jamiehsg.budgetapp.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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


public class DayFragment extends Fragment {

    private TextView outputText;
    private JSONObject monthlyIncomes;
    private JSONObject monthlyExpenses;
    private JSONObject incomes;
    private JSONObject expenses;
    private BarPercentViewer barChart;
    private DecimalFormat moneyForm;
    private PieChartDrawer pieChart;
    private TableLayout expenseTable;
    private TableLayout incomeTable;

        public DayFragment() {
            // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    /*
    Generate needed output widgets, set up table headers, make api calls
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day, container, false);
        outputText=(TextView)rootView.findViewById(R.id.day_performance_out);
        barChart=(BarPercentViewer)rootView.findViewById(R.id.day_bar_chart);
        pieChart=(PieChartDrawer)rootView.findViewById(R.id.day_pie_chart);
        expenseTable=(TableLayout)rootView.findViewById(R.id.day_expense_table);
        incomeTable=(TableLayout)rootView.findViewById(R.id.day_income_table);
        TableRow income_head=new TableRow(getActivity());
        int i=-9;
        income_head.setId(i + 10);
        income_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        TableRow expense_head=new TableRow(getActivity());
        expense_head.setId(i + 20);
        expense_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        TextView dateHead=new TextView(getActivity());
        dateHead.setText("Date");
        dateHead.setId(i + 11);
        dateHead.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        dateHead.setTextSize(20);
        TextView dateHead2=new TextView(getActivity());
        dateHead2.setText("Date");
        dateHead2.setId(i + 21);
        dateHead2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        dateHead2.setTextSize(20);
        income_head.addView(dateHead);
        expense_head.addView(dateHead2);
        TextView catHead=new TextView(getActivity());
        catHead.setText("Category");
        catHead.setId(i + 12);
        catHead.setTextSize(20);
        catHead.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        TextView catHead2=new TextView(getActivity());
        catHead2.setText("Category");
        catHead2.setId(i + 22);
        catHead2.setTextSize(20);
        catHead2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        income_head.addView(catHead);
        expense_head.addView(catHead2);
        TextView amountHead=new TextView(getActivity());
        amountHead.setText("Amount");
        amountHead.setTextSize(20);
        amountHead.setId(i + 14);
        amountHead.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        TextView amountHead2=new TextView(getActivity());
        amountHead2.setText("Amount");
        amountHead2.setId(i + 24);
        amountHead2.setTextSize(20);
        amountHead2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        income_head.addView(amountHead);
        expense_head.addView(amountHead2);
        TextView descHead=new TextView(getActivity());
        descHead.setText("Description");
        descHead.setId(i + 13);
        descHead.setTextSize(20);
        descHead.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        TextView descHead2=new TextView(getActivity());
        descHead2.setText("Description");
        descHead2.setId(i + 23);
        descHead2.setTextSize(20);
        descHead2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        income_head.addView(descHead);
        expense_head.addView(descHead2);
        expenseTable.addView(expense_head);
        incomeTable.addView(income_head);
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
                TableRow tr=new TableRow(getActivity());
                tr.setId(i+100);
                tr.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                TextView tdate=new TextView(getActivity());
                tdate.setId(i + 200);
                tdate.setTextSize(10);
                tdate.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                tdate.setText(temp.getString("date"));
                tr.addView(tdate);
                TextView tcat=new TextView(getActivity());
                tcat.setId(i + 300);
                tcat.setTextSize(10);
                tcat.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                tcat.setText(temp.getString("category"));
                tr.addView(tcat);
                TextView tamount=new TextView(getActivity());
                tamount.setId(i + 400);
                tamount.setTextSize(10);
                tamount.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                tamount.setText("$" + temp.getDouble("amount"));
                tr.addView(tamount);
                TextView tdesc=new TextView(getActivity());
                tdesc.setId(i + 500);
                tdesc.setTextSize(10);
                tdesc.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                tdesc.setText(temp.getString("description"));
                tr.addView(tdesc);
                incomeTable.addView(tr);
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
                TableRow tr=new TableRow(getActivity());
                tr.setId(k+600);
                tr.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                TextView tdate=new TextView(getActivity());
                tdate.setId(k + 700);
                tdate.setTextSize(10);
                tdate.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                tdate.setText(temp.getString("date"));
                tr.addView(tdate);
                TextView tcat=new TextView(getActivity());
                tcat.setId(k + 800);
                tcat.setTextSize(10);
                tcat.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                tcat.setText(temp.getString("category"));
                tr.addView(tcat);
                TextView tamount=new TextView(getActivity());
                tamount.setId(k + 900);
                tamount.setTextSize(10);
                tamount.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                tamount.setText("$" + temp.getDouble("amount"));
                tr.addView(tamount);
                TextView tdesc=new TextView(getActivity());
                tdesc.setId(k + 1000);
                tdesc.setTextSize(10);
                tdesc.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
                tdesc.setText(temp.getString("description"));
                tr.addView(tdesc);
                expenseTable.addView(tr);
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
            Log.d("displayDataPost", "Percent has been added to pieChart");
            String output="You have spent $"+moneyForm.format(expenseTotal)+" today out of $"+moneyForm.format(incomeTotal+monthIncomeTotal/30)+" which is "+moneyForm.format(percent*100)+"% of today's income";
            outputText.setText(output);


        }
        catch(JSONException error){

        }

    }

    /*
   Gets data from async call, gets the proper table based on the value of the "table" element in the JSON array and sets the proper JSON obj to the received JSON array,
    if all arrays are full then call display data
    */
    public void handleResponse(String data){
        try{
            JSONObject jsonObj=new JSONObject(data);
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
        catch (JSONException error){
            System.out.print(error);
        }
        if(incomes!=null&&expenses!=null&&monthlyExpenses!=null&&monthlyIncomes!=null){
            displayData();
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