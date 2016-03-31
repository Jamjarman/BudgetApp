package org.duckdns.jamiehsg.budgetapp.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.duckdns.jamiehsg.budgetapp.activity.HomeFragment;
import org.duckdns.jamiehsg.budgetapp.R;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;



    @Override
    /**
     * set up main view and create nav bar
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerFragment=(FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
        displayView(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDrawerItemSelected(View view, int position){
        displayView(position);
    }

    /**
     * creates values and symbolic links in the nav drawer TODO adds seperating lines to nav drawer
     * @param position
     */
    public void displayView(int position){
        Fragment fragment=null;
        String title=getString(R.string.app_name);
        switch(position){
            case 0:
                fragment=new HomeFragment();
                title=getString(R.string.title_home);
                break;
            case 1:
                fragment=new MonthIncomeFragment();
                title=getString(R.string.title_month_income);
                break;
            case 2:
                fragment=new MonthExpensesFragment();
                title=getString(R.string.title_month_expense);
                break;
            case 3:
                fragment=new IncomeFragment();
                title=getString(R.string.title_income);
                break;
            case 4:
                fragment=new ExpenseFragment();
                title=getString(R.string.title_purchase);
                break;
            case 5:
                fragment=new DayFragment();
                title=getString(R.string.title_day);
                break;
            case 6:
                fragment=new WeekFragment();
                title=getString(R.string.title_week);
                break;
            case 7:
                fragment=new MonthFragment();
                title=getString(R.string.title_month);
                break;
            case 8:
                fragment=new AllTime();
                title=getString(R.string.title_all);
                break;
            case 9:
                fragment=new SettingsFragment();
                title=getString(R.string.title_settings);
            default:
                break;
        }
        if (fragment!=null){
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            getSupportActionBar().setTitle(title);
        }

    }
}
