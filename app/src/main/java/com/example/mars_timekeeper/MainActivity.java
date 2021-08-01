package com.example.mars_timekeeper;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mars_timekeeper.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    TextView earthDate, earthTime, marsDate, marsTime;

    final double daySol = 1.02749;

    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        androidx.appcompat.widget.Toolbar myToolBar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.mainToolbar);

        setSupportActionBar(myToolBar);
        //TextView toolbarText = (TextView) myToolBar.findViewById(R.id.toolbarTextView);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragMars);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        earthTime = findViewById(R.id.earthTime3);
        earthDate = findViewById(R.id.earthDate3);
        marsTime = findViewById(R.id.marsTime);
        marsDate = findViewById(R.id.marsDate);

        Calendar marsOriginalTime = new GregorianCalendar(1955,3,11,0,0,0);

        String earthTimeFormatted, earthDateFormatted;

        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //getDateTime
                                Date currentTime = Calendar.getInstance().getTime();
                                Calendar earthNowTime = Calendar.getInstance();

                                //format calculated times
                                String earthTimeFormatted = timeFormat.format(currentTime);
                                String earthDateFormatted = dateFormat.format(currentTime);

                                //current julian date
                                int julianDate = Integer.parseInt((new SimpleDateFormat("'0'yyD").format(currentTime)));

                                //counts form epoch jan 1 1970 + time from origin to 1970
                                long secondsToday = (earthNowTime.getTimeInMillis() /1000 + 464745600)% 88775;
                                //mars sol: 24 hr 39 min 35 s = 88,619 s
                                int hrs = (int) (secondsToday / 3600);//3600 seconds in hour
                                int mins = (int) ((secondsToday-hrs*3600) / 60);
                                int secs = (int) ((secondsToday-hrs*3600-mins*60));

                                //feb 2 2021 is julian calendar day 21033
                                int marsSols = (int) ((julianDate-21033)/daySol);
                                int marsYears = 36;//mars year 36 started on feb 2 2021


                                String marsTimeFormatted = hrs+":"+mins+":"+ secs;
                                String marsDateFormatted = marsYears + " MY " + marsSols + " sols";


                                //set text of textview
                                earthTime.setText(earthTimeFormatted);
                                earthDate.setText(earthDateFormatted);

                                marsTime.setText(marsTimeFormatted);
                                marsDate.setText(marsDateFormatted);
                            }
                        });

                    }
                }catch (InterruptedException e){

                }
            }
        };
        t.start();


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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.navHostFragMars);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}