/* MainActivity.java
 * -----------------
 * Author: Danial Fitri Ghazali (dfx) from dot cat
 * Contact: danialfitrighazali@gmail.com
 * Description: This class is the main entry point of the program.
 * It will initialize all parts of the program required for the
 * program at startup. It also read Z Values from a file at startup
 * and store it as a float array, using it when necessary.
 */

// Package declaration
package com.dotcat.graph;

// Imports
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import java.util.Scanner;

// Start of class
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    // Initialize every parts of the program as a private property
    private DrawerLayout drawer;
    private NormalDistributionFragment normalDistributionFragment = new NormalDistributionFragment();
    private PoissonDistributionFragment poissonDistributionFragment = new PoissonDistributionFragment();
    private BernoulliTrialFragment bernoulliTrialFragment = new BernoulliTrialFragment();
    
    private final int DELAY = 200; // Delay used for drawer
    
    // When the class was created, change the theme from splash to normal theme.
    // Load the Z val from text file as well as instantiating the drawer.
    // drawer will be set to point at Normal Distribution mode first.
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        loadData();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) // Set default only if the app do a cold start
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    normalDistributionFragment).commit();
            navigationView.setCheckedItem(R.id.normal);
        }
    }
    
    // Handle what to do when the drawer menu items was selected. Load different
    // fragments or activity if needed.
    // NOTE: Launching/loading fragments or activities while closing the drawer lags
    // some phones. We handle the loading/launch part using a timer to reduce
    // graphical lag. In this case, we launch after 200ms passed.
    // In the meantime, close the drawer.
    
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        new Handler().postDelayed(new Runnable() {
            
            // We use the instance we initialize earlier when the app starts, so the app
            // don't have to keep instantiating new objects for the fragments.
            
            @Override
            public void run() {
                switch (menuItem.getItemId()) {
                    case R.id.normal:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                normalDistributionFragment).commit();
                        break;
                    case R.id.poisson:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                poissonDistributionFragment).commit();
                        break;
                    case R.id.bernoulli:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                bernoulliTrialFragment).commit();
                        break;
                    case R.id.credits:
                        startCredits();
                        break;
                }
            }
        }, DELAY);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    
    // Launch the credit activity
    private void startCredits() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
    
    // We define additional actions for back button.
    // If the nav drawer is open, close it.
    // Else, use the default action for back button.
    
    @Override
    public void onBackPressed()
    {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
    
    // Load the Z val from the text file in assets.
    // Store them in a float array and save them in a bundle.
    // Other fragments/activity will have access to it through the
    // bundle.
    private void loadData()
    {
        float[] prob = new float[410];

        try
        {
            Scanner in = new Scanner(getAssets()
                    .open("prob_val.txt"));
            for (int i = 0; i != prob.length; i++)
                prob[i] = in.nextFloat();

            in.close();
        }
        catch (Exception err)
        {
            err.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putFloatArray("val", prob);
        normalDistributionFragment.setArguments(bundle);
    }
}
