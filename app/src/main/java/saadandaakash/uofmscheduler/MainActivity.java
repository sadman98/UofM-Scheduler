package saadandaakash.uofmscheduler;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import saadandaakash.uofmscheduler.Fragments.SavedSectionsFragment;
import saadandaakash.uofmscheduler.Fragments.SelectSubjectFragment;
import saadandaakash.uofmscheduler.Fragments.SettingsFragment;
import saadandaakash.uofmscheduler.Utitilies.CustomTypefaceSpan;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FrameLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // remove action bar text
        setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Make nav header text bold
        View headerLayout = navigationView.getHeaderView(0);

        // TODO: find a better font for menu/header stuff
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");
        //nav_header_text.setTypeface(tf);
        //nav_header_text.setTextSize(10 * getResources().getDisplayMetrics().density);

        // CHANGE MENU TEXT FONT
        Menu nav_menu = navigationView.getMenu();
        CustomTypefaceSpan custom_tf_span = new CustomTypefaceSpan(
                Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Regular.otf"));

        for (int i = 0; i < nav_menu.size(); i++) {
            MenuItem item = nav_menu.getItem(i);
            SpannableStringBuilder text = new SpannableStringBuilder(item.getTitle());
            text.setSpan(custom_tf_span, 0, text.length(), 0);
            text.setSpan(new RelativeSizeSpan(1.5f), 0, text.length(), 0);
            item.setTitle(text);

        }

        mainLayout = (FrameLayout) findViewById(R.id.container);
    }

    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return;
            } else {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    return;
                }
                super.onBackPressed();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.Select_Courses) {
            fragment = new SelectSubjectFragment();
        }
        else if (id == R.id.My_Courses) {
            // temporary to avoid crashing
            try {
                fragment = SavedSectionsFragment.newInstance();
            }
            catch (Exception e) {
                System.out.println("ERROR: FRAGMENT COULD NOT BE CREATED");
                e.printStackTrace();
            }

        }
        else if (id == R.id.Settings) {
            fragment = SettingsFragment.newInstance();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(Integer.toString(fragment.getId()))
                .commit();

        // hide the welcome text
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.greeting);
        layout.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
